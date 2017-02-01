package de.alaoli.games.minecraft.mods.yadm.command;

import java.io.IOException;

import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
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
	public Permission requiredPermission()
	{
		return Permission.OPERATOR;
	}
	
	@Override
	public void processCommand( Arguments args ) 
	{
		//Check permission
		if( !this.canCommandSenderUseCommand( args ) ) { throw new CommandException( "You're not allowed to perform this command."); }
		
		try 
		{
			TemplateManager.INSTANCE.load();
			
			args.sender.addChatMessage( new ChatComponentText( "Templates reloaded." ) );
		}
		catch ( DataException | IOException e )
		{
			e.printStackTrace();
			
			throw new CommandException( "Can't reload templates." );
		}
	}

}
