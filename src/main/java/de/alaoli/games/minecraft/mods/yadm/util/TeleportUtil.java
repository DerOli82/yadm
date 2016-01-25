package de.alaoli.games.minecraft.mods.yadm.util;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
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
		/**
		 * @TODO Check if teleport target is blocked		
		 */
		DimensionManager.initDimension( coordinate.getDimId() );
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		WorldServer world = DimensionManager.getWorld( coordinate.getDimId() );
		float pitch = player.rotationPitch;
		float yaw = player.rotationYaw;
		
		scm.transferPlayerToDimension( player, coordinate.getDimId(), new DimensionTeleport( world ) );
		
		player.playerNetServerHandler.setPlayerLocation( 
			coordinate.getX(), 
			coordinate.getY(), 
			coordinate.getZ(), 
			yaw,
			pitch
		);
	}
}
