package de.alaoli.games.minecraft.mods.yadm.util;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.teleport.DimensionTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class TeleportUtil 
{
	public static void emergencyTeleport( EntityPlayerMP player)
	{
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		WorldServer target = DimensionManager.getWorld( 0 );
		
		
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
			
			Coordinate coordinate = new Coordinate(
				target.getSpawnPoint().posX,
				target.getSpawnPoint().posY+5,
				target.getSpawnPoint().posZ
			);
			scm.transferPlayerToDimension( player, 0, new DimensionTeleport( target, coordinate ) );
		}
		
	}
	
	public static boolean teleport( EntityPlayerMP player, Dimension dimension, Coordinate coordinate )
	{
		if( !dimension.canTeleport() || ( dimension == null ) ) { return false; }
		
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager(); 
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
			scm.transferPlayerToDimension(player, dimension.getId(), new DimensionTeleport( target, coordinate ) );
			//player.playerNetServerHandler.sendPacket( new S2BPacketChangeGameState( 1, 0.0F ) );
			return true;
		}
		return false;
	}
}
