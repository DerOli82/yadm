package de.alaoli.games.minecraft.mods.yadm.command;

import java.io.IOException;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;
import net.minecraft.util.ChatComponentText;

public class ReloadCommand extends Command 
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public ReloadCommand( Command parent ) 
	{
		super(parent);
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public String getCommandName() 
	{
		return "reload";
	}

	@Override
	public void processCommand( Arguments command ) 
	{
		try 
		{
			TemplateManager.INSTANCE.load();
			
			command.sender.addChatMessage( new ChatComponentText( "Templates reloaded." ) );
		}
		catch ( DataException | IOException e )
		{
			e.printStackTrace();
			
			throw new CommandException( "Can't reload templates." );
		}
	}

}
