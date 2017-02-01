package de.alaoli.games.minecraft.mods.yadm.command.manage;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandException;
import de.alaoli.games.minecraft.mods.yadm.command.Permission;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.DimensionException;
import de.alaoli.games.minecraft.mods.yadm.manager.player.PlayerException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class OwnerCommand extends Command
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
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
	public String getCommandUsage( ICommandSender sender ) 
	{
		return super.getCommandUsage( sender ) + " set <player>|unset";
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
					
		String cmd = "";
		
		if( !args.isEmpty() )
		{
			cmd = args.next();
		}
		
		switch( cmd )
		{
			case "set" :
				try
				{
					Dimension dimension = dimensions.findDimension( args.sender.getEntityWorld().provider.dimensionId );
					Player player = args.parsePlayer();
					
					dimension.setOwner( player );
					dimensions.setDirty( true );
					args.sender.addChatMessage( new ChatComponentText( player + " now owns dimension '" + dimension + "'." ) );
				}
				catch( DimensionException|PlayerException e )
				{
					throw new CommandException( e.getMessage(), e );
				}
				break;
				
			case "unset" :
				try
				{
					Dimension dimension = dimensions.findDimension( args.sender.getEntityWorld().provider.dimensionId );
					
					if( dimension.hasOwner() )
					{
						dimension.setOwner( null );
						dimensions.setDirty( true );
					}
					args.sender.addChatMessage( new ChatComponentText( "Owner has removed from dimension '" + dimension + "'." ) );
				}
				catch( DimensionException e )
				{
					throw new CommandException( e.getMessage(), e );
				}				
				break;
				
			default:
				this.sendUsage( args.sender );
				break;
		}
	}
}