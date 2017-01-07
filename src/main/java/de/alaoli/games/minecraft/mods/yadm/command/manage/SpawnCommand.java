package de.alaoli.games.minecraft.mods.yadm.command.manage;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandException;
import de.alaoli.games.minecraft.mods.yadm.command.Permission;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnMode;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnSetting;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class SpawnCommand extends Command
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public SpawnCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/

	@Override
	public String getCommandName() 
	{
		return "spawn";
	}

	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		return super.getCommandUsage( sender ) + "here [exact|topdown] | set <x> <y> <z> [exact|topdown]";
	}

	@Override
	public Permission requiredPermission()
	{
		return Permission.OWNER;
	}	
	
	@Override
	public void processCommand( Arguments args )
	{		
		//Check permission
		if( !this.canCommandSenderUseCommand( args ) ) { throw new CommandException( "You're not allowed to perform this command."); }
		
		Coordinate coordinate;
		SpawnMode mode;
		
		
		Dimension dimension = dimensions.findDimension( args.sender.getEntityWorld().provider.dimensionId );
		
		
		switch( args.next().toLowerCase() )
		{
			case "here" :
				if( !args.senderIsEntityPlayer ) { throw new CommandException( "Sender isn't a player" ); }
				
				EntityPlayer player = (EntityPlayer)args.sender;
				coordinate = new Coordinate( (int)player.posX, (int)player.posY, (int)player.posZ );
				
				try
				{
					mode = args.parseSpawnMode();
				}
				catch( CommandException | DataException e )
				{
					//Ignore because optional
					mode = SpawnMode.EXACT;
				}
				dimension.update( new SpawnSetting( coordinate, mode ) );
				YADM.proxy.updateDimension( dimension );
				
				args.sender.addChatMessage( new ChatComponentText( "Change spawnpoint to " + coordinate + " (" + mode + ")" ) );
				break;
				
			case "set" :
				coordinate = args.parseCoordinate();
				
				try
				{
					mode = args.parseSpawnMode();
				}
				catch( CommandException | DataException e )
				{
					//Ignore because optional
					mode = SpawnMode.EXACT;
				}
				dimension.update( new SpawnSetting( coordinate, mode ) );
				YADM.proxy.updateDimension( dimension );
				
				args.sender.addChatMessage( new ChatComponentText( "Change spawnpoint to " + coordinate + " (" + mode + ")" ) );
				break;
				
			default :
				this.sendUsage( args.sender );
				break;
		}
		

		

		
		
	}
}