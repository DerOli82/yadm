package de.alaoli.games.minecraft.mods.yadm.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class YADimensionManager extends WorldSavedData 
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	public final static String ID = "YADimensionManager";

	/********************************************************************************
	 * Attribute
	 ********************************************************************************/

	/**
	 * YADimensionManagerHolder is loaded on the first execution of YADimensionManager.getInstance() 
	 * or the first access to YADimensionManagerHolder.INSTANCE, not before.
	 */
	private final static class YADimensionManagerHolder
	{
		private static final YADimensionManager INSTANCE = new YADimensionManager();
	}
	
	/**
	 * List all possible WorldTypes
	 */
	private Map<String, WorldType> worldTypes;

	private Map<String, Dimension> dimByName;
	
	/********************************************************************************
	 * Methods - Constructor
	 ********************************************************************************/
	
	private YADimensionManager() 
	{
		super( YADimensionManager.ID );
		
		this.worldTypes = new HashMap<String, WorldType>();
		this.dimByName = new HashMap<String, Dimension>();
		
		this.initTypes();
		
		
	}

	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/

	public static YADimensionManager getInstance()
	{
		return YADimensionManagerHolder.INSTANCE;
	}	
	
	public Set<String> getWorldTypes()
	{
		return this.worldTypes.keySet();
	}
	
	public Dimension getDimensionByName( String name )
	{
		return this.dimByName.get( name );
	}
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
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
	
	public int nextDimensionId()
	{
		int nextId = Config.Dimensions.beginsWithId + this.dimByName.size();
		
		//Check if id isn't registered
		while( DimensionManager.isDimensionRegistered( nextId ) )
		{
			nextId++;
		}
		return nextId;
	}
	
	public boolean existsWorldType( String type )
	{
		return this.worldTypes.containsKey( type );
	}
	
	public boolean existsDimension( String name )
	{
		return this.dimByName.containsKey( name );
	}

	public Dimension createDimension( String name, String worldType ) throws RuntimeException
	{
		Dimension dim = new Dimension( this.nextDimensionId(), name );
		int worldTypeId = this.worldTypes.get( worldType ).getWorldTypeID();
				
		DimensionManager.registerDimension( dim.getId(), worldTypeId );
		
		if( !DimensionManager.isDimensionRegistered(1000) )
		{
			throw new RuntimeException();
		}
		
		this.dimByName.put( name, dim );
		
		return dim;
	}
		
	/********************************************************************************
	 * Abstract - WorldSavedData
	 ********************************************************************************/
	
	@Override
	public void readFromNBT( NBTTagCompound comp ) 
	{

	}

	@Override
	public void writeToNBT( NBTTagCompound comp )
	{
		
	}
}
