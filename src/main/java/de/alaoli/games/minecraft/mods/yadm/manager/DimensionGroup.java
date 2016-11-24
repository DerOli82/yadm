package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringJoiner;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.WriterConfig;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;
import net.minecraftforge.common.DimensionManager;

public class DimensionGroup extends ManageableGroup implements JsonFileAdapter
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/

	private boolean dirty;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public DimensionGroup( String name )
	{
		super( name );
		this.dirty = false;
	}
		
	/********************************************************************************
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/	

	@Override
	public Manageable create() 
	{
		Manageable manageable = new Dimension();
		manageable.setManageableGroupName( this.getManageableGroupName() );
		
		return manageable;
	}
	
	/********************************************************************************
	 * Methods - Implement JsonFileAdapter
	 ********************************************************************************/
	
	@Override
	public void setSavePath( String savePath ) {}

	@Override
	public String getSavePath() 
	{
		StringJoiner joiner = new StringJoiner( File.separator )
			.add( DimensionManager.getCurrentSaveRootDirectory().toString() )
			.add( "data" )
			.add( YADM.MODID )
			.add( "dimensions" );
		return joiner.toString();
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
		Manageable data;
		String fileName = this.getSavePath() + File.separator + this.getManageableGroupName() + ".json";
		
		//Delete file if empty
		if( this.isEmpty() )
		{
			File file = new File( fileName );
			
			if( file.isFile() )
			{
				file.delete();
				return;
			}
		}
			
		try 
		{
			OutputStreamWriter writer = new OutputStreamWriter( new FileOutputStream( fileName ), "UTF-8" );
			
			this.serialize().writeTo( writer, WriterConfig.PRETTY_PRINT );
			this.setDirty( false );
			
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
		Manageable data;
		String fileName = this.getSavePath() + File.separator + this.getManageableGroupName() + ".json";		
		
		InputStreamReader reader = new InputStreamReader( new FileInputStream( fileName ), "UTF-8" );
		
		this.deserialize( Json.parse( reader ) );
		
		reader.close();
	}
	
	@Override
	public void cleanup()
	{
		this.clear();
	}	
}
