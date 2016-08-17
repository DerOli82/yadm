package de.alaoli.games.minecraft.mods.yadm.data.settings;

public enum SettingType 
{
	WORLDPROVIDER( "worldProvider" ),
	WORLDTYPE( "worldType" ),
	SEED( "seed" ),
	GENERATOROPTIONS( "generatorOptions" ),
	SPAWN( "spawn" );
	
	private String name;
	
	private SettingType( String name )
	{
		this.name = name;
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
		return null;
	}
}
