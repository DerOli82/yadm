package de.alaoli.games.minecraft.mods.yadm.teleport;

import java.util.HashSet;
import java.util.Set;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnSetting;
import de.alaoli.games.minecraft.mods.yadm.teleport.modifier.CoordinateTopDownModifier;

public class TeleportModifierFactory 
{
	public static Set<TeleportModifier> factory( Dimension dimension )
	{
		Set<TeleportModifier> modifiers = new HashSet<TeleportModifier>();
		
		//Spawn mode
		if( dimension.hasSetting( SettingType.SPAWN ) )
		{
			SpawnSetting spawn = (SpawnSetting)dimension.get( SettingType.SPAWN );
			
			switch( spawn.getMode() )
			{
				case EXACT:
					//Nothing to do
					break;
					
				case TOPDOWN :
				default :
					modifiers.add( new CoordinateTopDownModifier() );
					break;
			}
		}
		else
		{
			//Default safe spawn
			modifiers.add( new CoordinateTopDownModifier() );
		}
		return modifiers;
	}
}
