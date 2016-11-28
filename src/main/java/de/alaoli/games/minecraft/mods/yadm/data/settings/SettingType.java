package de.alaoli.games.minecraft.mods.yadm.data.settings;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;

public enum SettingType 
{
	WORLDPROVIDER( "worldProvider", true ),
	WORLDTYPE( "worldType" ),
	SEED( "seed" ),
	GENERATOROPTIONS( "generatorOptions" ),
	SPAWN( "spawn" ),
	WHITELIST( "whitelist" ),
	WORLDGUARD( "worldGuard" ),
	WORLDBORDER( "border" ),
	WORLDBORDER_MESSAGE( "border:message" ),
	WORLDBORDER_KNOCKBACK( "border:knockback" ),
	WORLDBORDER_TRAVEL( "border:travel" );
	
	public final String name;
	public final boolean required;
	
	private SettingType( String name )
	{
		this.name = name;
		this.required = false;
	}

	private SettingType( String name, boolean required )
	{
		this.name = name;
		this.required = required;
	}
	
	@Override
	public String toString() 
	{
		return this.name;
	}

	public static SettingType get( String name )
	{
		for( SettingType type : SettingType.values() )
		{
			if( type.toString().equals( name ) )
			{
				return type;
			}
		}
		throw new DataException( "Unknown SettingType." );
	}
}
