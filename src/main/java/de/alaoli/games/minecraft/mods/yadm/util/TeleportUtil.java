package de.alaoli.games.minecraft.mods.yadm.util;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.teleport.DimensionTeleport;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class TeleportUtil 
{
	public static void teleport( EntityPlayerMP player, Coordinate coordinate )
	{
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		
		Dimension dimension = YADM.proxy.getDimensionManager().getById( coordinate.dimId ); 
		WorldServer target = DimensionManager.getWorld( dimension.getId() );
				
		if( target != null )
		{
			if( player.isRiding() )
			{
				player.mountEntity( null );
			}
			if( player.isSneaking() )
			{
				player.setSneaking( false );
			}
			scm.transferPlayerToDimension(player, coordinate.dimId, new DimensionTeleport( target, coordinate ) );
			//player.playerNetServerHandler.sendPacket( new S2BPacketChangeGameState( 1, 0.0F ) );
		}
	}
}
