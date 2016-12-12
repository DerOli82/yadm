package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.event.TeleportEvent;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class TPToCommand extends Command
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final TeleportPlayer players = PlayerManager.INSTANCE;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public TPToCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public String getCommandName() 
	{
		return "tpto";
	}

	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		return super.getCommandUsage( sender ) + " <dimensionOwner>";
	}
	
	@Override
	public Permission requiredPermission()
	{
		return Permission.PLAYER;
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
		Player player = args.parsePlayer();
		
		if( !player.ownsDimension() ) { throw new CommandException( player.getManageableName() + "doesn't owns any dimension." );}
		if( !args.senderIsEntityPlayer ) { throw new CommandException( "Command sender must be an player." );}
		
		MinecraftForge.EVENT_BUS.post( new TeleportEvent( player.getDimension(), (EntityPlayerMP)args.sender ) );
	}
}
