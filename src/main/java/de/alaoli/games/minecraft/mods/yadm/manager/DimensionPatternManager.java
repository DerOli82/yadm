package de.alaoli.games.minecraft.mods.yadm.manager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.alaoli.games.minecraft.mods.yadm.data.DimensionPattern;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class DimensionPatternManager 
{
	
	
	private static DimensionPatternManager instance;
	
	/**
	 * List all possible WorldProviders
	 */
	private Map<Integer, String> worldProviders;
	
	/**
	 * List all possible WorldTypes
	 */
	private Map<String, WorldType> worldTypes;
	
	private Map<String, DimensionPattern> patterns;
	
	private boolean isDirty;
	
	private DimensionPatternManager()
	{
		this.patterns = new HashMap<String, DimensionPattern>();
		
		this.initProvider();
		this.initTypes();
		
	}
	
	public void markDirty()
	{
		this.isDirty = true;
	}
	
	public boolean isDirty()
	{
		return this.isDirty;
	}
	
	private void initProvider()
	{
		this.worldProviders = new HashMap<Integer, String>();
		
		try 
		{
			Hashtable<Integer, Class<? extends WorldProvider>> providers;
			Field providersField = DimensionManager.class.getDeclaredField( "providers" );
			
			providersField.setAccessible( true );
			
			providers = (Hashtable<Integer, Class<? extends WorldProvider>>) providersField.get( null );
			
			for( Entry<Integer, Class<? extends WorldProvider>> provider : providers.entrySet() )
			{
				this.worldProviders.put( provider.getKey(), provider.getValue().getName() );
			}
		}
		catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e )
		{
			/*
			 * @TODO Log
			 */
			e.printStackTrace();
		}
		
	}
	
	private void initTypes()
	{
		WorldType type;
		String name;
		
		this.worldTypes = new HashMap<String, WorldType>();
		
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
	
	public boolean existsPattern( String name )
	{
		return this.patterns.containsKey( name );
	}
	
	public void addPattern( DimensionPattern pattern )
	{
		if( !this.patterns.containsKey( pattern.getName() ))
		{
			this.patterns.put( pattern.getName(), pattern );
		}
	}
	
	
	public static DimensionPatternManager getInstance()
	{
		if( instance == null )
		{
			instance = new DimensionPatternManager();
		}
		return instance;
	}

	public DimensionPattern getPattern( String name )
	{
		return this.patterns.get( name );
	}
	
	public Set<Entry<Integer, String>> getWorldProviderEntries()
	{
		return this.worldProviders.entrySet();
	}
	
	public Set<Entry<String, WorldType>> getWorldTypeEntries()
	{
		return this.worldTypes.entrySet();
	}
	
	public WorldType getWorldTypeByName( String name )
	{
		return this.worldTypes.get( name );
	}	
	public Set<Entry<String, DimensionPattern>> getPatterns()
	{
		return this.patterns.entrySet();
	}
}
