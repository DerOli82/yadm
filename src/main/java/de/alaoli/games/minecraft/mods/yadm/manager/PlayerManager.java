package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;
import java.util.UUID;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.WriterConfig;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;

public class PlayerManager extends ManageableGroup implements JsonFileAdapter 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final PlayerManager INSTANCE = new PlayerManager();
	
	private boolean dirty;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private PlayerManager() 
	{
		super( "players" );
		
		this.setDirty( false );
	}
	
	public boolean exists( EntityPlayer player )
	{
		return this.exists( player.getUniqueID() );
	}
	
	public boolean exists( UUID id )
	{
		return this.get( id ) != null;
	}
	
	public Manageable get( UUID id )
	{
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			if( ( entry.getValue() instanceof Player ) &&
				( ((Player)entry.getValue()).getId().equals( id ) ) )
			{
				return entry.getValue();
			}
		}
		return null;
	}
	
	public void cleanup()
	{
		this.clear();
	}
	
	/********************************************************************************
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/	

	@Override
	public Manageable create() 
	{
		return new Player();
	}
	
	/********************************************************************************
	 * Methods - Implement JsonFileAdapter
	 ********************************************************************************/
	
	@Override
	public void setSavePath( String savePath ) {}

	@Override
	public String getSavePath() 
	{
		return DimensionManager.getCurrentSaveRootDirectory() + File.separator + "data" + File.separator + YADM.MODID;
	}

	@Override
	public void setDirty( boolean flag )
	{
		this.dirty = flag;
	}

	@Override
	public boolean isDirty() 
	{
		return this.dirty;
	}

	@Override
	public void save() throws IOException, DataException
	{
		if( !this.isDirty() ) { return; }
		
		Manageable data;
		String fileName = this.getSavePath() + File.separator + this.getManageableGroupName() + ".json";
		
		try 
		{
			OutputStreamWriter writer = new OutputStreamWriter( new FileOutputStream( fileName ), "UTF-8" );
			
			this.serialize().writeTo( writer, WriterConfig.PRETTY_PRINT );
			
			writer.close();
		} 
		catch ( IOException | DataException e )
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void load() throws IOException, DataException
	{
		File folder	= new File( this.getSavePath() );
		
		if( !folder.exists() ) { folder.mkdir(); }
		
		Manageable data;
		File file = new File( this.getSavePath() + File.separator + this.getManageableGroupName() + ".json" );
		
		if( ( file.exists() ) && 
			( file.isFile() ) )
		{
			InputStreamReader reader = new InputStreamReader( new FileInputStream( file ), "UTF-8" );
			
			this.deserialize( Json.parse( reader ) );
			
			reader.close();
		}
	}		
}
