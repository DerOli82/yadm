package de.alaoli.games.minecraft.mods.yadm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion( value = "1.7.10" )
@IFMLLoadingPlugin.Name( value = "YADMLoadingPlugin" )
public class YADMLoadingPlugin implements IFMLLoadingPlugin  
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Properties getProperties() throws IOException
	{
		Properties properties = new Properties();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream( "dependencies.properties" );
		
		if( input == null ) {
			throw new FileNotFoundException( "Property file 'dependencies.properties' not found in the classpath."  );
		}
		properties.load( input );
		input.close();
		
		return properties;
	}
	
	public static boolean dependencyExists( File folder, String[] dependency )
	{
		for( File file :folder.listFiles() )
		{
			if( file.getName().contains( dependency[1] ) )
			{
				return true;
			}
			
		}
		return false;
	}

	public static void downloadDependency( String url, File folder, String[] dependency ) throws MalformedURLException, IOException
	{
		Log.info( "Downloading dependencies..." );
		
		StringBuilder filename = new StringBuilder()
			.append( dependency[1] )
			.append( "-" )
			.append( dependency[2] )
			.append( ".jar" );
		
		StringBuilder download = new StringBuilder()
			.append(url)
			.append( dependency[0].replace( ".", "/" ) )
			.append( "/" )
			.append( dependency[1] )
			.append( "/" )
			.append( dependency[2] )
			.append( "/" )
			.append( filename );
		Log.info( "Downloading '" + filename.toString() + "' from '" +download.toString() + "'.");
		FileUtils.copyURLToFile( new URL( download.toString() ), new File( folder, filename.toString() ) );
	}
	
	public static void checkAndDownloadDependency( String url, File folder, String[] dependency ) throws MalformedURLException, IOException
	{
		if( !dependencyExists( folder, dependency ) )
		{
			downloadDependency( url, folder, dependency );
		}
	}
	
	/********************************************************************************
	 * Methods - Implement IFMLLoadingPlugin
	 ********************************************************************************/
	
	@Override
	public String[] getASMTransformerClass() 
	{
		return new String[]{};
	}

	@Override
	public String getModContainerClass() 
	{
		return null;
	}

	@Override
	public String getSetupClass() 
	{
		return null;
	}

	@Override
	public String getAccessTransformerClass()  
	{
		return null;
	}
	
	@Override
	public void injectData( Map<String, Object> data )
	{
		//Don't download in development environment
		if( !(Boolean)data.get( "runtimeDeobfuscationEnabled" ) ) { return; }
		
		Log.info( "Checking dependencies..." );

        File folder = new File( (File) data.get( "mcLocation" ), "mods" + File.separator + Loader.MC_VERSION );
        
        if( !folder.exists() )
        {
        	folder.mkdir();
        }
        
		try 
		{
			Properties properties = this.getProperties();
			String mavenurl = properties.getProperty( "mavenurl" );
			
			properties.remove( "mavenurl" );
			
			for( Object property : properties.values() )
			{
				checkAndDownloadDependency( mavenurl, folder, ((String)property).split( ";" ) );
			}
		}
		catch ( IOException e )
		{
			Log.error( "Something went wrong. Can't download dependencies!");
			e.printStackTrace();
		}
	}
}
