package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.List;

import com.google.gson.annotations.Expose;

import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.yadm.interceptor.Injectable;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;

public class GeneratorOptionsSetting implements Setting, Injectable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	@Expose
	private String value;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public String getValue()
	{
		return this.value;
	}
	
	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.GENERATOROPTIONS;
	}

	@Override
	public boolean isRequired() 
	{
		return false;
	}
	
	/********************************************************************************
	 * Methods - Implements Injectable
	 ********************************************************************************/

	@Override
	public void injectInto( Object target )
	{
		if( target instanceof WorldProvider )
		{
			WorldProvider worldProvider = (WorldProvider)target;
			worldProvider.field_82913_c = this.value;
		}
		else if( target instanceof WorldInfo )
		{
			WorldInfo worldInfo = (WorldInfo)target;
			
			ReflectionHelper.setPrivateValue( 
				WorldInfo.class, worldInfo, 
				this.value,
				new String[] { "field_82576_c", "generatorOptions" } 
			);				
		}
	}		
	
	@Override
	public void injectInto( List targets )
	{
		for( Object target : targets )
		{
			this.injectInto( target );
		}
	}		
}
