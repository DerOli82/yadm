package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.ArrayList;
import java.util.List;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.ManageDimensions;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;

public class DeleteCommand extends Command
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final ManageDimensions dimensions = YADimensionManager.INSTANCE;
	protected static final TeleportPlayer players = PlayerManager.INSTANCE;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public DeleteCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public String getCommandName() 
	{
		return "delete";
	}

	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		return super.getCommandUsage( sender ) + " <dimensionName>";
	}
	
	@Override
	public Permission requiredPermission()
	{
		return Permission.OPERATOR;
	}
	
	@Override
	public void processCommand( Arguments args ) 
	{
		//Check permission
		if( !this.canCommandSenderUseCommand( args ) ) { throw new CommandException( "You're not allowed to perform this command."); }
		
		//Usage
		if( args.isEmpty() )
		{
			this.sendUsage( args.sender );
			return;
		}
		Dimension dimension = args.parseDimension();
				
		WorldServer world = MinecraftServer.getServer().worldServerForDimension( dimension.getId() );
		List<EntityPlayerMP> playersInDimension = new ArrayList<EntityPlayerMP>(world.playerEntities);
		
		//Teleport all players out
		for( EntityPlayerMP player : playersInDimension )
		{
			players.emergencyTeleport( player );
		}
		dimensions.deleteDimension( dimension );
		args.sender.addChatMessage( new ChatComponentText( "Dimension '" + dimension + "' removed." ) );
	}
}
