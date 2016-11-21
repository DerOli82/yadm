package de.alaoli.games.minecraft.mods.yadm.util;

import de.alaoli.games.minecraft.mods.yadm.data.DimensionDummy;
import de.alaoli.games.minecraft.mods.yadm.teleport.DimensionTeleport;
import de.alaoli.games.minecraft.mods.yadm.teleport.TeleportException;
import de.alaoli.games.minecraft.mods.yadm.teleport.TeleportSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

public class TeleportUtil 
{
	public static void emergencyTeleport( EntityPlayer player )
	{
		teleport( new TeleportSettings( new DimensionDummy( 0 ), player ) );
	}

	public static void teleport( TeleportSettings settings)
	{
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		
		settings.prepare();
		
		if( settings.dimension == null ) { throw new TeleportException( "Teleport 'dimension' is missing." ); }
		if( settings.target == null ) { throw new TeleportException( "Teleport 'target' is missing." ); }
		if( settings.player == null ) { throw new TeleportException( "Teleport 'player' is missing." ); }
		if( settings.coordinate == null ) { throw new TeleportException( "Teleport 'coordinate' is missing." ); }
		
		scm.transferPlayerToDimension(
			(EntityPlayerMP)settings.player, 
			settings.dimension.getId(), 
			new DimensionTeleport( settings.target, settings.coordinate ) 
		);

	}
}
