package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.command.manage.WhitelistCommand;
import de.alaoli.games.minecraft.mods.yadm.command.manage.OwnerCommand;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class ManageCommandGroup extends CommandGroup
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public ManageCommandGroup( Command parent )
	{
		super( parent );
		
		this.add( new OwnerCommand( this ) );
		this.add( new WhitelistCommand( this ) );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public boolean canCommandSenderUseCommand( ICommandSender sender )
	{
		//Check dimension owner
		if( sender instanceof EntityPlayer )
		{
			EntityPlayer player = (EntityPlayer) sender;
			
			if( YADimensionManager.INSTANCE.exists( player.dimension ) ) 
			{
				Dimension dimension = YADimensionManager.INSTANCE.get( player.dimension );
				
				if( dimension.isOwner( player) )
				{
					return true;
				}
				else
				{
					return super.canCommandSenderUseCommand( sender );
				}
			}
		}
		return false;
	}	
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 1;
	}
	
	@Override
	public String getCommandName()
	{
		return "manage";
	}
	
	@Override
	public void processCommand( CommandParser command )
	{
		super.processCommand( command );
	}	
}
