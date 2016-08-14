package de.alaoli.games.minecraft.mods.yadm.data.settings;

public enum SettingType 
{
	WORLDPROVIDER( "worldProvider", WorldProviderSetting.class ),
	WORLDTYPE( "worldType", WorldTypeSetting.class ),
	SEED( "seed", SeedSetting.class ),
	GENERATOROPTIONS( "generatorOptions", GeneratorOptionsSetting.class ),
	SPAWN( "spawn", SpawnSetting.class );
	
	private String name;
	private Class clazz;
	
	private SettingType( String name, Class clazz )
	{
		this.name = name;
		this.clazz = clazz;
	}
		
	@Override
	public String toString() 
	{
		return this.name;
	}

	public Class getClazz()
	{
		return this.clazz;
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
