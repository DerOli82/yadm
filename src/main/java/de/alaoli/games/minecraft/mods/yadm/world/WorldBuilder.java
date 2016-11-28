package de.alaoli.games.minecraft.mods.yadm.world;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.ManageDimensions;
import de.alaoli.games.minecraft.mods.yadm.world.interceptor.provider.DimensionFieldAccessor;
import de.alaoli.games.minecraft.mods.yadm.world.interceptor.provider.GetDimensionNameInterceptor;
import de.alaoli.games.minecraft.mods.yadm.world.interceptor.provider.RegisterWorldChunkManagerPostInterceptor;
import de.alaoli.games.minecraft.mods.yadm.world.interceptor.provider.RegisterWorldChunkManagerPreInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

public class WorldBuilder implements ManageWorlds, FindWorldType, ListOptions
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
		
	public static final WorldBuilder INSTANCE = new WorldBuilder();
	
	private static final JsonFileAdapter dimensionFiles = YADimensionManager.INSTANCE;
	private static final ManageDimensions dimensionManager = YADimensionManager.INSTANCE;
	
	private int nextId;
	
	private Map<Integer, Class<? extends WorldProvider>> worldProviders;
	private Map<String, WorldType> worldTypes;	
	
	private List<Integer> registeredWorldIds;
	private Map<Integer, Dimension> worldsForDeletion; 
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private WorldBuilder() 
	{
		this.nextId = Config.Provider.beginsWithId;
		this.worldProviders = new Hashtable<Integer, Class<? extends WorldProvider>>();
		this.worldTypes = new Hashtable<String, WorldType>();
		this.worldsForDeletion = new HashMap<Integer, Dimension>();
		
		this.initProvider();
		this.initTypes();
	}
	
	@SuppressWarnings( "unchecked" )
	private void initProvider()
	{
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

	@SuppressWarnings( "unchecked" )
	private WorldProvider createProvider( Dimension dimension ) throws ClassNotFoundException, InstantiationException, IllegalAccessException  
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
			.make()
			.load( getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER )
			.getLoaded();
		
		return (WorldProvider) dynamicType.newInstance();
	}	
	
	private int registerProvider( WorldProvider provider, boolean keepLoaded )
	{		
		while( !DimensionManager.registerProviderType( nextId, provider.getClass(), keepLoaded))
		{
			nextId++;
		}
		return nextId;
	}
	
	private void initWorldServer( Dimension dimension )
	{	
        WorldServer overworld = DimensionManager.getWorld(0);
        if (overworld == null)
        {
            throw new RuntimeException("Cannot Hotload Dim: Overworld is not Loaded!");
        }
        try
        {
            DimensionManager.getProviderType(dimension.getId());
        }
        catch (Exception e)
        {
            System.err.println("Cannot Hotload Dim: " + e.getMessage());
            return; // If a provider hasn't been registered then we can't hotload the dim
        }
        MinecraftServer mcServer = overworld.func_73046_m();
        ISaveHandler savehandler = overworld.getSaveHandler();
        WorldSettings worldSettings = new WorldSettings( overworld.getWorldInfo() );

        WorldServer world = new WorldServerGeneric( mcServer, savehandler, overworld.getWorldInfo().getWorldName(), dimension, worldSettings, mcServer.theProfiler );
        world.addWorldAccess( new WorldManager( mcServer, world ) );
        
        MinecraftForge.EVENT_BUS.post( new WorldEvent.Load( world ) );
        if (!mcServer.isSinglePlayer())
        {
            world.getWorldInfo().setGameType( mcServer.getGameType() );
        }
        mcServer.setDifficultyForAllWorlds( mcServer.getDifficulty() );		
	}
	
	/********************************************************************************
	 * Methods - Implement ManageWorlds
	 ********************************************************************************/
	
	@Override
	public void registerWorldProviderForDimension( Dimension dimension ) throws WorldException
	{
		StringBuilder msg;
		WorldProviderSetting providerSetting = (WorldProviderSetting)dimension.get( SettingType.WORLDPROVIDER );
		
		try 
		{
			WorldProvider provider = this.createProvider( dimension );
			int providerId = this.registerProvider( provider, false );
			
			providerSetting.setId( providerId );
			
			msg = new StringBuilder()
				.append( "Register WorldProvider '" )
				.append( providerSetting.getName() )
				.append( "' with Id '" )
				.append( providerSetting.getId() )
				.append( "'." );
			Log.info( msg.toString() );
		} 
		catch ( ClassNotFoundException | InstantiationException | IllegalAccessException e )
		{
			msg = new StringBuilder()
				.append( "Couldn't register WorldProvider '" )
				.append( providerSetting.getName() )
				.append( "' with Id '" )
				.append( providerSetting.getId() )
				.append( "'." );			
			throw new WorldException( msg.toString(), e );
		}		
	}
	
	@Override
	public void unregisterWorldProviderForDimension( Dimension dimension )
	{
		StringBuilder msg;
		WorldProviderSetting providerSetting = (WorldProviderSetting)dimension.get( SettingType.WORLDPROVIDER );
		
		try
		{
			DimensionManager.unregisterProviderType( providerSetting.getId() );
			
			msg = new StringBuilder()
				.append( "Unregister WorldProvider '" )
				.append( providerSetting.getName() )
				.append( "' with Id '" )
				.append( providerSetting.getId() )
				.append( "'." );
			Log.info( msg.toString() );
		}
		catch( Exception e )
		{
			msg = new StringBuilder()
				.append( "Couldn't unregister WorldProvider '" )
				.append( providerSetting.getName() )
				.append( "' with Id '" )
				.append( providerSetting )
				.append( "'.");
			throw new WorldException( msg.toString(), e );
		}		
	}
	
	@Override
	public void markWorldForDeletion( Dimension dimension )
	{
		this.worldsForDeletion.put( dimension.getId(), dimension );
	}
	
	@Override
	public void deleteWorld( World world ) throws WorldException
	{
		if( !this.worldsForDeletion.containsKey( world.provider.dimensionId ) ) { return; }
		
		Dimension dimension = this.worldsForDeletion.get( world.provider.dimensionId );
		
		((WorldServer)world).flush();
		dimensionManager.unregisterDimension( dimension );
		
		
		//Delete dimension folder
		StringJoiner path = new StringJoiner( File.separator )
			.add( DimensionManager.getCurrentSaveRootDirectory().toString() )
			.add( "DIM" + dimension.getId() );
		File folder = new File( path.toString() );
		
		if( ( folder.exists() ) && ( folder.isDirectory() ) )
		{
			StringBuilder msg;
			
			try
			{
				FileUtils.deleteDirectory( folder );
				dimensionFiles.setDirty( true );
				
				msg = new StringBuilder()
					.append( "Delete folder for Dimension '" )
					.append( dimension.toString() )
					.append( "' with Id '" )
					.append( dimension.getId() )
					.append( "'." );
				Log.info( msg.toString() );
			}
			catch ( IOException e ) 
			{
				msg = new StringBuilder()
					.append( "Couldn't delete folder for Dimension '" )
					.append( dimension.toString() )
					.append( "' with Id '" )
					.append( dimension.getId() )
					.append( "'." );
				throw new WorldException( msg.toString(), e );
			}
		}
	}

	@Override
	public WorldServer getWorldServerForDimension( Dimension dimension ) throws WorldException
	{
		WorldServer worldServer = null;
		
		//Is YADM dimension?
		if( dimensionManager.existsDimension( dimension.getId() ) )
		{
			worldServer = DimensionManager.getWorld( dimension.getId() );
			
			//worldServer initialized?
			if( worldServer == null )
			{
				this.initWorldServer( dimension );
			}
			worldServer = DimensionManager.getWorld( dimension.getId() );
		}
		else
		{
			worldServer = MinecraftServer.getServer().worldServerForDimension( dimension.getId() );
		}
		//Still not initialized?
		if( worldServer == null ) { throw new WorldException( "Can't initialize WorldServer." ); }
		
		return worldServer;		
	}

	
	/********************************************************************************
	 * Methods - Implement FindWorldType
	 ********************************************************************************/
	
	@Override
	public WorldType findWorldType( String name ) throws WorldException
	{
		if( !this.worldTypes.containsKey( name ) ) { throw new WorldException( "Can't find WorldType '" + name + "'."); }
		
		return this.worldTypes.get( name );
	}
	
	/********************************************************************************
	 * Methods - Implement ListOptions
	 ********************************************************************************/
	
	@Override
	public Set<Entry<Integer, Class<? extends WorldProvider>>> listWorldProvider()
	{
		return this.worldProviders.entrySet();
	}
	
	@Override
	public Set<Entry<String, WorldType>>  listWorldType()
	{
		return this.worldTypes.entrySet();
	}
}
