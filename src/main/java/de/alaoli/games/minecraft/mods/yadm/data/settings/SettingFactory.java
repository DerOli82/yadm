package de.alaoli.games.minecraft.mods.yadm.data.settings;

public class SettingFactory 
{
	public static Setting createNewInstance( String name )
	{
		return createNewInstance( SettingType.get( name ) );
	}
	
	public static Setting createNewInstance( SettingType type )
	{
		switch( type )
		{
			case WORLDPROVIDER :
				return new WorldProviderSetting();
						
			case WORLDTYPE :
				return new WorldTypeSetting();
				
			case SEED :
				return new SeedSetting();
				
			case GENERATOROPTIONS :
				return new GeneratorOptionsSetting();
				
			case SPAWN :
				return new SpawnSetting();
				
			case WORLDBORDER :
				return new WorldBorderSetting();
				
			default:
				return null;
		}
	}
}
