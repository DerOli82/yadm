package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;

import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.lib.common.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.lib.common.manager.Manageable;
import de.alaoli.games.minecraft.mods.lib.common.manager.ManageableGroup;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.config.ConfigDimensionSection;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.DimensionException;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.FindDimension;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.ListDimensions;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.ManageDimensions;
import de.alaoli.games.minecraft.mods.yadm.world.ManageWorlds;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

public class YADimensionManager extends ManageableGroup implements ManageDimensions, FindDimension, ListDimensions, JsonFileAdapter 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final YADimensionManager INSTANCE = new YADimensionManager();
	private static final ManageWorlds worlds = WorldBuilder.INSTANCE;
	
	private int nextId = ConfigDimensionSection.beginsWithId;
	private boolean dirty = false;
	
	private Map<Integer, Dimension> mappingId = new HashMap<>();
	private Set<NBTTagCompound> nbtDimensions = new HashSet<>();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private YADimensionManager() 
	{
		super( "dimensions" );
	}
	
	/**
	 * Returns next free dimension id
	 * 
	 * @return int
	 */
	protected int nextDimensionId()
	{
		while( DimensionManager.isDimensionRegistered( this.nextId ) )
		{
			this.nextId++;
		}
		return this.nextId;
	}
		
	protected Manageable get( int id )
	{
		//Mapping
		if( this.mappingId.containsKey( id ) ) { return this.mappingId.get( id ); }
		
		//Searching
		for( Entry<String, Manageable> groupEntry : this.getManageable() )		
		{			
			for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getManageable() )		
			{		
				if( ( dimensionEntry.getValue() instanceof Dimension ) &&
					( ((Dimension)dimensionEntry.getValue()).getId() == id ) )
				{
					this.mappingId.put( ((Dimension)dimensionEntry.getValue()).getId(), (Dimension)dimensionEntry.getValue() );
					
					return dimensionEntry.getValue();
				}
			}		
		}
		return null;
	}
	
	protected Manageable get( String group, String name )
	{
		if( this.existsManageable( group ) )
		{
			Manageable manageable = this.getManageable( group );
			
			if( ( manageable instanceof ManageableGroup ) &&
			( ((ManageableGroup)manageable).existsManageable( name ) ) )
			{
				return ((ManageableGroup)manageable).getManageable( name );
			}
		}
		return null;
	}	

	public Set<NBTTagCompound> getAsNBT()
	{
		if( ( !this.isManageableEmpty() ) && 
			( this.nbtDimensions.isEmpty() ) )
		{
			StringBuilder msg;
			Dimension dimension;
			NBTTagCompound compound;
			
			for( Entry<String, Manageable> groupEntry : this.getManageable() )
			{	
				for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getManageable() )
				{
		    		dimension = (Dimension) dimensionEntry.getValue();
					compound = new NBTTagCompound();
					
					((Dimension)dimensionEntry.getValue()).writeToNBT( compound );
					this.nbtDimensions.add( compound );
				}
			}
		}
		return this.nbtDimensions;
	}
	
	/********************************************************************************
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/	

	@Override
	public Manageable createManageable() 
	{
		return null;
	}	

	/********************************************************************************
	 * Methods - Implement ManageDimensions
	 ********************************************************************************/	
	
	@Override
	public void registerDimensions() throws DimensionException
	{
		Dimension dimension;
		
 		for( Entry<String, Manageable> groupEntry : this.getManageable() )	
 		{			
 			for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getManageable() )		
  			{	
 				dimension = (Dimension) dimensionEntry.getValue();	
 					 
 				if( !dimension.isRegistered() )					
 				{	
 					this.registerDimension( dimension );
 				}		
 			}		
 		}		
	}
	
	@Override
	public void registerDimension( Dimension dimension ) throws DimensionException
	{
		if( dimension.isRegistered() ) { return; }
		
		if( DimensionManager.isDimensionRegistered( dimension.getId() ) )
		{
			dimension.setRegistered( true );
			return;
		}
		StringBuilder msg;
		WorldProviderSetting providerSetting = (WorldProviderSetting) dimension.getSetting( SettingType.WORLDPROVIDER );
		
		try
		{
			worlds.registerWorldProviderForDimension( dimension );
			DimensionManager.registerDimension( dimension.getId(), providerSetting.getId() );
			dimension.setRegistered( true );
			
			msg = new StringBuilder()
				.append( "Register Dimension '" )
				.append( dimension.toString() )
				.append( "' with Id '" )
				.append( dimension.getId() )
				.append( "'." );
			Log.info( msg.toString() );			
		}
		catch( Exception e )
		{
			msg = new StringBuilder()
				.append( "Couldn't register Dimension '" )
				.append( dimension.toString() )
				.append( "' with Id '" )
				.append( dimension.getId() )
				.append( "'." );
			throw new DimensionException( msg.toString(), e );
		}
	}
	
	@Override
	public void unregisterDimensions() throws DimensionException
	{
		Dimension dimension;
		
		for( Entry<String, Manageable> groupEntry : this.getManageable() )
		{	
			for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getManageable() )
			{
				dimension = (Dimension) dimensionEntry.getValue();
			
				if( dimension.isRegistered() )
				{
					this.unregisterDimension( dimension );
				}
			}
		}			
	}
	
	@Override
	public void unregisterDimension( Dimension dimension ) throws DimensionException
	{
		if( ( !DimensionManager.isDimensionRegistered( dimension.getId() ) ) || ( !dimension.isRegistered()) ) { return; }
		
		StringBuilder msg;
		
		try
		{
			DimensionManager.unregisterDimension( dimension.getId() );
			
			msg = new StringBuilder()
				.append( "Unregister Dimension '" )
				.append( dimension.toString() )
				.append( "' with Id '" )
				.append( dimension.getId() )
				.append( "'." );
			Log.info( msg.toString() );
			
			worlds.unregisterWorldProviderForDimension( dimension );
			dimension.setRegistered( false );
			YADM.proxy.unregisterDimension( dimension );
		}
		catch( Exception e )
		{
			msg = new StringBuilder()
				.append( "Couldn't unregister Dimension '" )
				.append( dimension.toString() )
				.append( "' with Id '" )
				.append( dimension.getId() )
				.append( "'." );
			throw new DimensionException( msg.toString(), e );
		}
	}
	
	@Override
	public boolean existsDimension( int dimensionId )
	{
		return this.get( dimensionId ) != null;
	}
	
	@Override
	public boolean existsDimension( String name )
	{
		return this.existsDimension( "default", name );
	}
	
	@Override
	public boolean existsDimension( String group, String name )
	{
		if( this.existsManageable( group ) )
		{
			return ((ManageableGroup)this.getManageable( group )).existsManageable( name );
		}
		return false;
	}
	
	@Override
	public void addDimension( Dimension dimension )
	{
		if( !this.existsManageable( dimension.getManageableGroupName() ) )
		{
			this.addManageable( new DimensionGroup( dimension.getManageableGroupName() ) );
		}
		ManageableGroup group = (ManageableGroup)this.getManageable( dimension.getManageableGroupName() );
		
		group.addManageable( dimension );		
	}
	
	@Override
	public void removeDimension( Dimension dimension )
	{
		if( this.existsManageable( dimension.getManageableGroupName() ) )
		{
			ManageableGroup group = (ManageableGroup)this.getManageable( dimension.getManageableGroupName() );
			
			group.removeManageable( dimension );
		}		
	}
	
	@Override
	public Dimension createDimension( String name, Template template ) throws DimensionException
	{
		return this.createDimension( "default", name, template );
	}
	
	@Override
	public Dimension createDimension( String group, String name, Template template ) throws DimensionException
	{
		Dimension dimension = new Dimension( this.nextDimensionId(), group, name );
		
		dimension.addSettings( template.getSettings() );
		YADM.proxy.registerDimension( dimension );
		
		this.addDimension( dimension );
		this.setDirty( true );

		return dimension;
	}
	
	@Override
	public void deleteDimension( int id ) throws DimensionException
	{
		this.deleteDimension( (Dimension)this.get(id) );
	}
	
	@Override
	public void deleteDimension( String name ) throws DimensionException
	{
		this.deleteDimension( "default", name ); 
	}
	
	@Override
	public void deleteDimension( String group, String name ) throws DimensionException
	{
		this.deleteDimension( (Dimension)this.get( group, name ) );
	}
	
	@Override
	public void deleteDimension( Dimension dimension ) throws DimensionException
	{
		this.removeDimension( dimension );
		worlds.markWorldForDeletion( dimension );
		
		YADM.proxy.unregisterDimension( dimension );
		DimensionManager.unloadWorld( dimension.getId() );
	}

	/********************************************************************************
	 * Methods - Implement FindDimensions
	 ********************************************************************************/	
	
	@Override
	public Dimension findDimension( int dimensionId ) throws DimensionException
	{
		if( !this.existsDimension( dimensionId ) ) { throw new DimensionException( "Can't find dimension with Id '" + dimensionId + "'."); }
		
		return (Dimension)this.get( dimensionId );
	}
	
	@Override
	public Dimension findDimension( String name ) throws DimensionException
	{
		return this.findDimension( "default", name );
	}
	
	@Override
	public Dimension findDimension( String group, String name ) throws DimensionException
	{
		if( !this.existsDimension( group, name ) ) { throw new DimensionException( "Can't find dimension  '" + group + ":" + name + "'."); }
		
		return (Dimension) this.get( group, name );
	}
	
	/********************************************************************************
	 * Methods - Implement ListDimensions
	 ********************************************************************************/
	
	@Override
	public Set<Entry<String, Manageable>> listDimensions()
	{
		return this.getManageable();
	}
	
	/********************************************************************************
	 * Methods - Implement JsonFileAdapter
	 ********************************************************************************/
	
	@Override
	public void setSavePath( String savePath ) {}

	@Override
	public String getSavePath() 
	{
		return DimensionManager.getCurrentSaveRootDirectory().toString();
	}

	@Override
	public void setDirty( boolean flag )
	{
		if( flag == true )
		{
			this.mappingId.clear();
			this.nbtDimensions.clear();
		}
		this.dirty = flag;
	}

	@Override
	public boolean isDirty() 
	{
		if( this.dirty ) { return true; }
		
		Manageable data;
		
		for( Entry<String, Manageable> entry : this.getManageable() )
		{
			data = entry.getValue();
			
			if( data instanceof JsonFileAdapter )
			{
				if( ((JsonFileAdapter)data).isDirty() ) { return true; }
			}
		}
		return false;
	}

	@Override
	public void save() throws IOException, DataException
	{
		if( !this.isDirty() ) { return; }
		
		Manageable data;
		
		for( Entry<String, Manageable> entry : this.getManageable() )
		{
			data = entry.getValue();
			
			if( data instanceof JsonFileAdapter )
			{
				((JsonFileAdapter)data).save();
			}		
		}
		this.setDirty( false );
	}

	@Override
	public void load() throws IOException, DataException
	{
		StringJoiner path = new StringJoiner( File.separator )
			.add( this.getSavePath() )
			.add( "data" )
			.add( YADM.MODID )
			.add( "dimensions" );
		File folder	= new File( path.toString() );
		
		if( !folder.exists() ) { FileUtils.forceMkdir( folder ); }
		
		
		Manageable data;
		String groupName;
		File[] files = folder.listFiles();
		
		for( File file : files ) 
		{
			if( ( file.isFile() ) && 
				( file.getName().endsWith(".json") ) )
			{
				groupName = file.getName().replace( ".json", "" );
				
				//Initialize group 
				if( this.existsManageable( groupName ) )
				{
					data = this.getManageable( groupName );
				}
				else
				{
					data = new DimensionGroup( groupName ); 
					this.addManageable( data );
				}
				
				if( data instanceof JsonFileAdapter )
				{
					((JsonFileAdapter)data).load();
				}
			}
		}
	}	

	@Override
	public void cleanup()
	{
		this.mappingId.clear();
		this.clearManageable();
	}	
}
