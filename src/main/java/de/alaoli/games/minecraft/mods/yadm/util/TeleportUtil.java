package de.alaoli.games.minecraft.mods.yadm.util;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnSetting;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.teleport.DimensionTeleport;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;

public class TeleportUtil 
{
	private static final int OFFSETY = 4;
	
	private static void preparePlayer( EntityPlayer player )
	{
		if( player.isRiding() )
		{
			player.mountEntity( null );
		}
		if( player.isSneaking() )
		{
			player.setSneaking( false );
		}
	}
	
	private static Coordinate safeSpawn( EntityPlayer player, Dimension dimension, Coordinate coordinate, WorldServer target )
	{
		Block block;
		int y = 255;
		
		while( y > 0 )
		{
			block = target.getBlock( coordinate.x, y, coordinate.z );
			
			if( ( block != null ) &&
				( !block.isAir(target, coordinate.x, y, coordinate.z ) ) )
			{
				return new Coordinate( coordinate.x, y + OFFSETY, coordinate.z );
			}
			y--;
		}
		return coordinate;
	}
	
	public static void emergencyTeleport( EntityPlayer player )
	{
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		WorldServer target = MinecraftServer.getServer().worldServerForDimension( 0 );
		
		
		if( target != null )
		{
			preparePlayer( player );
			
			Coordinate coordinate = new Coordinate(
				target.getSpawnPoint().posX,
				target.getSpawnPoint().posY + OFFSETY,
				target.getSpawnPoint().posZ
			);
			scm.transferPlayerToDimension( (EntityPlayerMP)player, 0, new DimensionTeleport( target, coordinate ) );
		}
		
	}
	
	public static boolean teleport( EntityPlayer player, Dimension dimension )
	{
		if( ( dimension == null ) || !dimension.isRegistered() || ( player == null ) ) { return false; }
		
		WorldServer target = YADimensionManager.INSTANCE.getWorldServerForDimension( dimension );
		if( target == null ) { return false; }
		
		Coordinate coordinate;
		
		if( dimension.hasSetting( SettingType.SPAWN ) )
		{
			SpawnSetting spawn = (SpawnSetting)dimension.get( SettingType.SPAWN );
			coordinate = spawn.getCoordinate();
		}
		else
		{
			coordinate = new Coordinate(
				target.getSpawnPoint().posX,
				target.getSpawnPoint().posY + OFFSETY,
				target.getSpawnPoint().posZ
			);
		}
		return teleport( player, dimension, coordinate, target );
	}
	
	public static boolean teleport( EntityPlayer player, Dimension dimension, Coordinate coordinate )
	{
		if( ( dimension == null ) || !dimension.isRegistered() || ( player == null ) ) { return false; }
		 
		WorldServer target = YADimensionManager.INSTANCE.getWorldServerForDimension( dimension );
				
		if( target == null ) { return false; }
			
		return teleport( player, dimension, coordinate, target );
	}
	
	public static boolean teleport( EntityPlayer player, Dimension dimension, Coordinate coordinate, WorldServer target )
	{
		if( ( dimension == null ) || !dimension.isRegistered() || ( player == null ) || ( target == null ) ) { return false; }
		
		coordinate = safeSpawn( player, dimension, coordinate, target );
		
		preparePlayer( player );
		
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		scm.transferPlayerToDimension((EntityPlayerMP)player, dimension.getId(), new DimensionTeleport( target, coordinate ) );
		
		return true;
	}
}
