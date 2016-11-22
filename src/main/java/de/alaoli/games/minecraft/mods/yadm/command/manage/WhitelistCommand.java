package de.alaoli.games.minecraft.mods.yadm.command.manage;

import java.util.Map.Entry;
import java.util.UUID;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandException;
import de.alaoli.games.minecraft.mods.yadm.command.Permission;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WhitelistSetting;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class WhitelistCommand extends Command
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public WhitelistCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/

	@Override
	public String getCommandName() 
	{
		return "whitelist";
	}

	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		return super.getCommandUsage( sender ) + " add|remove <player> or list";
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
				
		String action = args.next();
		
		EntityPlayer owner = (EntityPlayer)args.sender;
		
		if( !YADimensionManager.INSTANCE.exists( owner.dimension ) ) { return; }
		
		Dimension dimension = YADimensionManager.INSTANCE.get( owner.dimension );
		
		if( !dimension.hasSetting( SettingType.WHITELIST ) )
		{
			dimension.add( new WhitelistSetting() );
		}
		WhitelistSetting setting = (WhitelistSetting)dimension.get( SettingType.WHITELIST );
		Player player = args.parsePlayer();
		
		switch( action )
		{
			case "add":
				setting.add( player );
				break;
				
			case "remove":
				setting.remove( player );
				break;
				
			case "list" :
			default :
				args.sender.addChatMessage( new ChatComponentText( "Whitelist:" ) );
				
				for( Entry<UUID, Player> entry : setting.getUsers() )
				{
					args.sender.addChatMessage( new ChatComponentText( "    - '" + entry.getValue().toString() ) );
				}
				break;
		}
	}
}