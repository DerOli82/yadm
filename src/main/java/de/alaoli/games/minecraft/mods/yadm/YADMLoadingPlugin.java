package de.alaoli.games.minecraft.mods.yadm;

import java.util.Map;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

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

	/********************************************************************************
	 * Methods - Implement IFMLLoadingPlugin
	 ********************************************************************************/
	
	@Override
	public String[] getASMTransformerClass() { return new String[]{}; }

	@Override
	public String getModContainerClass() { return null; }

	@Override
	public String getSetupClass() { return null; }

	@Override
	public String getAccessTransformerClass() { return null; }
	
	@Override
	public void injectData( Map<String, Object> data ) {}
}
