package de.alaoli.games.minecraft.mods.yadm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import de.alaoli.games.minecraft.mods.lib.common.Dependency;

@IFMLLoadingPlugin.MCVersion( value = "1.7.10" )
@IFMLLoadingPlugin.Name( value = "YADMLoadingPlugin" )
public class YADMLoadingPlugin implements IFMLLoadingPlugin 
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
		
	public YADMLoadingPlugin()
	{
		MixinBootstrap.init();
		MixinEnvironment.setCompatibilityLevel( MixinEnvironment.CompatibilityLevel.JAVA_8 );
		Mixins.addConfiguration( "mixins.yadm.json" );
	}
	
	public Properties getProperties() throws IOException
	{
		Properties properties = new Properties();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream( "dependencies.properties" );
		
		if( input == null ) { throw new FileNotFoundException( "Property file 'dependencies.properties' not found in the classpath."  ); }
		properties.load( input );
		input.close();
		
		return properties;
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
			Dependency dependency;
			Properties properties = this.getProperties();
			
			Dependency.setMavenUrl( properties.getProperty( "mavenurl" ) );
			Dependency.setDownloadFolder( folder );
			properties.remove( "mavenurl" );
			
			for( Object property : properties.values() )
			{
				dependency = new Dependency( (String)property );
				
				if( dependency.isDownloadNewer() )
				{
					dependency.delete();
					dependency.download();
				}
			}
		}
		catch ( IOException e )
		{
			Log.error( "Something went wrong. Can't download dependencies!");
			e.printStackTrace();
		}
	}
}
