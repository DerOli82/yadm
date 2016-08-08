package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

public class InfoSubCommand implements SubCommand
{
	public static final InfoSubCommand instance = new InfoSubCommand();
	
	private InfoSubCommand() {}
	
	@Override
	public String getCommandName()
	{
		return "info";
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 0;
	}
	
	@Override
	public String getCommandUsage( ICommandSender sender )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		World world = DimensionManager.getWorld( sender.getEntityWorld().provider.dimensionId );
		WorldInfo worldInfo = world.getWorldInfo();
		
		sender.addChatMessage( new ChatComponentText( "Current dimension info:" ) );
		sender.addChatMessage( new ChatComponentText( "Name: " + world.provider.getDimensionName() ) );
		sender.addChatMessage( new ChatComponentText( "ID: " + world.provider.dimensionId ) );
		sender.addChatMessage( new ChatComponentText( "Seed: " + world.getSeed() ) );
		sender.addChatMessage( new ChatComponentText( "GenOpt: " + world.provider.field_82913_c ) );
	}

}
