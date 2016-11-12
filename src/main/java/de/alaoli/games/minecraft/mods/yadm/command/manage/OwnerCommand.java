package de.alaoli.games.minecraft.mods.yadm.command.manage;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
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
	public void processCommand( Arguments command )
	{
		String cmd = command.next();
		
		switch( cmd )
		{
			case "set" :
				if( command.senderIsEntityPlayer )
				{
					EntityPlayer sender = (EntityPlayer)command.sender;
					
					if( YADimensionManager.INSTANCE.exists( sender.dimension ) )
					{
						Player owner = command.parsePlayer();
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