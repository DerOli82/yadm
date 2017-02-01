package de.alaoli.games.minecraft.mods.yadm.data.settings;

import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.settings.worldborder.KnockbackSetting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.worldborder.MessageSetting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.worldborder.TravelSetting;

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
				
			case WHITELIST :
				return new WhitelistSetting();
				
			case WORLDGUARD :
				return new WorldGuardSetting();				
				
			case WORLDBORDER :
				return new WorldBorderSetting();
				
			case ENTITYSPAWN :
				return new EntitySpawnSetting();
				
			case WORLDBORDER_MESSAGE :
				return new MessageSetting();
				
			case WORLDBORDER_KNOCKBACK :
				return new KnockbackSetting();
				
			case WORLDBORDER_TRAVEL :
				return new TravelSetting();
				
			default:
				throw new DataException( "Unknown setting type." );
		}
	}
}
