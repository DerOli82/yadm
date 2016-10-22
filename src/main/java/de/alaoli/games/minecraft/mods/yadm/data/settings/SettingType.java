package de.alaoli.games.minecraft.mods.yadm.data.settings;

public enum SettingType 
{
	WORLDPROVIDER( "worldProvider", true ),
	WORLDTYPE( "worldType" ),
	SEED( "seed" ),
	GENERATOROPTIONS( "generatorOptions" ),
	SPAWN( "spawn" ),
	WORLDBORDER( "border" );
	
	private String name;
	
	private boolean required;
	
	private SettingType( String name )
	{
		this.name = name;
		this.required = false;
	}

	private SettingType( String name, boolean required )
	{
		this.name = name;
		this.required = true;
	}
	
	@Override
	public String toString() 
	{
		return this.name;
	}

	public boolean isRequired()
	{
		return this.required;
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
		return null;
	}
}
