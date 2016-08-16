package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.List;

import com.google.gson.annotations.Expose;

import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.yadm.interceptor.Injectable;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;

public class WorldTypeSetting implements Setting, Injectable
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	public static final String DEFAULT = "default";
	
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	@Expose
	private String name;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public WorldTypeSetting() {}
	
	public WorldTypeSetting( String name )
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	/********************************************************************************
	 * Methods - Implements Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.WORLDTYPE;
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
			worldProvider.terrainType = WorldBuilder.instance.getWorldType( this.name );
		}
		else if( target instanceof WorldInfo )
		{
			WorldInfo worldInfo = (WorldInfo)target;
			
			ReflectionHelper.setPrivateValue( 
				WorldInfo.class, worldInfo, 
				WorldBuilder.instance.getWorldType( this.name ), 
				new String[] { "field_76098_b", "terrainType" } 
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
