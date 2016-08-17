package de.alaoli.games.minecraft.mods.yadm.world;

import java.lang.reflect.Field;
import java.util.Hashtable;
import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider.DimensionFieldAccessor;
import de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider.GetDimensionNameInterceptor;
import de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider.GetSeedInterceptor;
import de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider.RegisterWorldChunkManagerPostInterceptor;
import de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider.RegisterWorldChunkManagerPreInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class WorldBuilder 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
		
	public static final WorldBuilder instance = new WorldBuilder();
	
	private static int nextId = Config.Provider.beginsWithId;
	
	private Hashtable<Integer, Class<? extends WorldProvider>> worldProviders;

	private Hashtable<String, WorldType> worldTypes;	
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private WorldBuilder() 
	{
		this.initProvider();
		this.initTypes();
	}
	
	@SuppressWarnings( "unchecked" )
	private void initProvider()
	{
		this.worldProviders = new Hashtable<Integer, Class<? extends WorldProvider>>();
		
		try 
		{
			Hashtable<Integer, Class<? extends WorldProvider>> providers;
			Field providersField = DimensionManager.class.getDeclaredField( "providers" );
			
			providersField.setAccessible( true );
			
			this.worldProviders = (Hashtable<Integer, Class<? extends WorldProvider>>) providersField.get( null );
		}
		catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e )
		{
			Log.error( e.getMessage() );
		}
		
	}
	
	private void initTypes()
	{
		WorldType type;
		String name;
		
		this.worldTypes = new Hashtable<String, WorldType>();
		
		for( int i = 0; i < WorldType.worldTypes.length; i++ )
		{
			type = WorldType.worldTypes[ i ];
			
			if( type != null )
			{
				name = type.getWorldTypeName();
			
				//Minecraft doesn't allow to create DEFAULT_1_1 ( see net.minecraft.world.WorldType )
				if ( !name.equals( WorldType.DEFAULT_1_1.getWorldTypeName() ) )
				{
					this.worldTypes.put( name, type );
				}
			}
		}
	}	
	
	public boolean existsWorldProvider( int provider )
	{
		return this.worldProviders.containsKey( provider );
	}
	
	public boolean existsWorldType( String type )
	{
		return this.worldTypes.containsKey( type );
	}
	
	@SuppressWarnings( "unchecked" )
	public WorldProvider createProvider( Dimension dimension ) throws ClassNotFoundException, InstantiationException, IllegalAccessException  
	{
		Class<? extends WorldProvider> providerClass;
		
		switch( ((WorldProviderSetting)dimension.get( SettingType.WORLDPROVIDER )).getName() )
		{
			case WorldProviderSetting.NETHER :
				providerClass = this.worldProviders.get( -1 );
				break;
				
			case WorldProviderSetting.OVERWORLD :
				providerClass = this.worldProviders.get( 0 );
				break;
				
			case WorldProviderSetting.END :
				providerClass = this.worldProviders.get( 1 );
				break;				
				
			default :
				//TODO Custom Provider
				providerClass = this.worldProviders.get( 0 );
				break;
		}		
		String registerWorldChunkManagerMethodName = ReflectionHelper.findMethod( 
			WorldProvider.class, (WorldProvider)providerClass.newInstance(), 
			new String[]{ "func_76572_b", "registerWorldChunkManager" } 
		).getName();
		String getDimensionNameMethodName = ReflectionHelper.findMethod( 
			WorldProvider.class, (WorldProvider)providerClass.newInstance(), 
			new String[]{ "func_80007_l", "getDimensionName" } 
		).getName();
		String getSeedMethodName = ReflectionHelper.findMethod( 
			WorldProvider.class, (WorldProvider)providerClass.newInstance(), 
			new String[]{ "func_72905_C", "getSeed" } 
		).getName();	
		
		Class<?> dynamicType = new ByteBuddy()
			.with( new NamingStrategy.SuffixingRandom( "YADM" ) )
			.subclass( providerClass )
			.defineField( "dimensionYADM", Dimension.class, Visibility.PROTECTED )
			.implement( DimensionFieldAccessor.class )
			.intercept( FieldAccessor.ofBeanProperty() )
			.method( ElementMatchers.named( registerWorldChunkManagerMethodName ) )
			.intercept( 
				MethodDelegation.to( RegisterWorldChunkManagerPreInterceptor.class )
				.andThen( SuperMethodCall.INSTANCE
				.andThen( MethodDelegation.to( RegisterWorldChunkManagerPostInterceptor.class ) ) ) 
			)
			.method( ElementMatchers.named( getDimensionNameMethodName ) )
			.intercept( MethodDelegation.to( GetDimensionNameInterceptor.class ) )
			.method( ElementMatchers.named( getSeedMethodName ) )
			.intercept( MethodDelegation.to( GetSeedInterceptor.class ) )
			.make()
			.load( getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER )
			.getLoaded();
		
		return (WorldProvider) dynamicType.newInstance();
	}	
	
	public int registerProvider( WorldProvider provider, boolean keepLoaded )
	{		
		while( !DimensionManager.registerProviderType( nextId, provider.getClass(), keepLoaded))
		{
			nextId++;
		}
		
		return nextId;
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	
	public WorldType getWorldType( String name )
	{
		return this.worldTypes.get( name );
	}
	
	public Hashtable<String, WorldType> getWorldTypes()
	{
		return this.worldTypes;
	}
	
	public Hashtable<Integer, Class<? extends WorldProvider>> getWorldProviders()
	{
		return this.worldProviders;
	}	
}
