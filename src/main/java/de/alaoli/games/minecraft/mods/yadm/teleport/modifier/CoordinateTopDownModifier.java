package de.alaoli.games.minecraft.mods.yadm.teleport.modifier;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.teleport.TeleportModifier;
import de.alaoli.games.minecraft.mods.yadm.teleport.TeleportSettings;
import net.minecraft.block.Block;

/**
 * Default (safe) spawn modifier
 */
public class CoordinateTopDownModifier implements TeleportModifier 
{
	@Override
	public void applyModifier( TeleportSettings settings )
	{
		Block block;
		
		int y = 255;
		
		while( y > 0 )
		{
			block = settings.target.getBlock( settings.coordinate.x, y, settings.coordinate.z );
			
			if( ( block != null ) &&
				( !block.isAir( settings.target, settings.coordinate.x, y, settings.coordinate.z ) ) )
			{
				settings.coordinate =  new Coordinate( settings.coordinate.x, y + settings.OFFSETY, settings.coordinate.z );
				break;
			}
			y--;
		}
	}

}
