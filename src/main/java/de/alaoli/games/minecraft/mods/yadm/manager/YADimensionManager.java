package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.DimensionException;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.ListDimensions;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.ManageDimensions;
import de.alaoli.games.minecraft.mods.yadm.world.ManageWorlds;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraftforge.common.DimensionManager;

public class YADimensionManager extends ManageableGroup implements ManageDimensions, ListDimensions, JsonFileAdapter 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final YADimensionManager INSTANCE = new YADimensionManager();
	
	private static final ManageWorlds worldBuilder = WorldBuilder.INSTANCE;
	
	private int nextId;
	
	private boolean dirty;
	
	private Map<Integer, Dimension> mappingId;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private YADimensionManager() 
	{
		super( null );
		
		this.nextId = Config.Dimension.beginsWithId;
		this.mappingId = new HashMap<Integer, Dimension>();
	}
	
	/**
	 * Returns next free dimension id
	 * 
	 * @return int
	 */
	public int nextDimensionId()
	{
		while( DimensionManager.isDimensionRegistered( this.nextId ) )
		{
			this.nextId++;
		}
		return this.nextId;
	}
		
	public Manageable get( int id )
	{
		//Mapping
		if( this.mappingId.containsKey( id ) ) { return this.mappingId.get( id ); }
		
		//Searching
		for( Entry<String, Manageable> groupEntry : this.getAll() )		
		{			
			for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getAll() )		
			{		
				if( ( dimensionEntry.getValue() instanceof Dimension ) &&
					( ((Dimension)dimensionEntry).getId() == id ) )
				{
					this.mappingId.put( ((Dimension)dimensionEntry).getId(), (Dimension)dimensionEntry );
					
					return dimensionEntry.getValue();
				}
			}		
		}
		return null;
	}
	
	public Manageable get( String group, String name )
	{
		if( this.exists( group ) )
		{
			Manageable manageable = this.get( group );
			
			if( ( manageable instanceof ManageableGroup ) &&
			( ((ManageableGroup)manageable).exists( name ) ) )
			{
				return ((ManageableGroup)manageable).get( name );
			}
		}
		return null;
	}	
	
	public void add( Dimension dimension )
	{
		if( !this.exists( dimension.getManageableGroupName() ) )
		{
			this.add( new DimensionGroup( dimension.getManageableGroupName() ) );
		}
		ManageableGroup group = (ManageableGroup)this.get( dimension.getManageableGroupName() );
		
		group.add( dimension );
	}
	
	
	public void remove( Dimension dimension )
	{
		if( this.exists( dimension.getManageableGroupName() ) )
		{
			ManageableGroup group = (ManageableGroup)this.get( dimension.getManageableGroupName() );
			
			group.remove( dimension );
		}
	}

	/********************************************************************************
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/	

	@Override
	public Manageable create() 
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
		
 		for( Entry<String, Manageable> groupEntry : this.getAll() )	
 		{			
 			for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getAll() )		
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
		WorldProviderSetting providerSetting = (WorldProviderSetting) dimension.get( SettingType.WORLDPROVIDER );
		
		try
		{
			worldBuilder.registerWorldProviderForDimension( dimension );
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
		
		for( Entry<String, Manageable> groupEntry : this.getAll() )
		{	
			for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getAll() )
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
			
			worldBuilder.unregisterWorldProviderForDimension( dimension );
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
	public boolean existsDimension( int id )
	{
		return this.get( id ) != null;
	}
	
	@Override
	public boolean existsDimension( String name )
	{
		return this.existsDimension( "default", name );
	}
	
	@Override
	public boolean existsDimension( String group, String name )
	{
		if( this.exists( group ) )
		{
			return ((ManageableGroup)this.get( group )).exists( name );
		}
		return false;
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
		
		dimension.add( template.getAll() );
		this.registerDimension( dimension );
		
		this.add( dimension );
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
		this.remove( dimension );
		worldBuilder.markWorldForDeletion( dimension );
		
		DimensionManager.unloadWorld( dimension.getId() );
	}

	/********************************************************************************
	 * Methods - Implement ListDimensions
	 ********************************************************************************/
	
	@Override
	public Set<Entry<String, Manageable>> listDimensions()
	{
		return this.getAll();
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
		this.dirty = flag;
	}

	@Override
	public boolean isDirty() 
	{
		if( this.dirty ) { return true; }
		
		Manageable data;
		
		for( Entry<String, Manageable> entry : this.getAll() )
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
		
		for( Entry<String, Manageable> entry : this.getAll() )
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
				if( this.exists( groupName ) )
				{
					data = this.get( groupName );
				}
				else
				{
					data = new DimensionGroup( groupName ); 
					this.add( data );
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
		this.clear();
	}	
}
