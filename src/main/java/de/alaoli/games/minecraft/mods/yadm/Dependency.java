package de.alaoli.games.minecraft.mods.yadm;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.common.versioning.ComparableVersion;

public class Dependency 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	protected static String mavenurl;
	protected static File downloadFolder;
	
	public final String group;
	public final String name;
	public final ComparableVersion version;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Dependency( String dependency )
	{
		String[] splited = dependency.split( ";" );
		
		this.group = splited[ 0 ];
		this.name = splited[ 1 ];
		this.version = new ComparableVersion( splited[ 2 ] );
	}
	
	public static void setMavenUrl( String url )
	{
		mavenurl = url;
	}
	
	public static void setDownloadFolder( File folder )
	{
		downloadFolder = folder;
	}
	
	public String getFilename()
	{
		return this.name + "-" + this.version + ".jar";
	}
	
	public String getDownloadUrl()
	{
		StringJoiner url = new StringJoiner( "/" )
			.add( mavenurl )
			.add( this.group.replace( ".", "/" ) )
			.add( this.name )
			.add( this.version.toString() )
			.add( this.getFilename() );
		return url.toString();
	}
	
	public boolean isDownloadNewer()
	{
		String filename;
		String version;
		
		for( File file : downloadFolder.listFiles() )
		{
			filename = file.getName();
			
			if( filename.contains( this.name ) )
			{
				version = filename.replace( this.name + "-", "" ).replace( ".json", "" );
				
				if( this.version.compareTo( new ComparableVersion( version ) ) > 0 )
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public void delete()
	{
		for( File file :downloadFolder.listFiles() )
		{
			if( file.getName().contains( this.name ) )
			{
				file.delete();
			}
		}		
	}
	
	public void download() throws MalformedURLException, IOException
	{
		String filename = this.getFilename();
		String url = this.getDownloadUrl();
		
		Log.info( "Downloading '" + filename + "' from '" + url + "'.");
		FileUtils.copyURLToFile( new URL( url ), new File( downloadFolder, filename ) );
	}
}
