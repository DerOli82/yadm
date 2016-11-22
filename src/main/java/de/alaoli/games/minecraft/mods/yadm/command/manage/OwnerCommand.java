package de.alaoli.games.minecraft.mods.yadm.command.manage;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandException;
import de.alaoli.games.minecraft.mods.yadm.command.Permission;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class OwnerCommand extends Command
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public OwnerCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/

	@Override
	public String getCommandName() 
	{
		return "owner";
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
						
		String cmd = args.next();
		
		switch( cmd )
		{
			case "set" :
				if( args.senderIsEntityPlayer )
				{
					EntityPlayer sender = (EntityPlayer)args.sender;
					
					if( YADimensionManager.INSTANCE.exists( sender.dimension ) )
					{
						Player owner = args.parsePlayer();
						Dimension dimension = YADimensionManager.INSTANCE.get( sender.dimension );
						
						dimension.setOwner( owner );
						
						sender.addChatComponentMessage( 
							new ChatComponentText( owner + " now owns dimension " + dimension )
						);
					}
				}
				break;
				
			default:
				break;
		}
	}
}