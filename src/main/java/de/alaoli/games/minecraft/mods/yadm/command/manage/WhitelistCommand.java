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
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.DimensionException;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.FindDimension;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class WhitelistCommand extends Command
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
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
		
		Dimension dimension;
		
		try
		{
			dimension = dimensions.findDimension( args.sender.getEntityWorld().provider.dimensionId );
		}
		catch( DimensionException e )
		{
			throw new CommandException( e.getMessage(), e );
		}
		String action = "";
		
		if( !args.isEmpty() )
		{
			action = args.next();
		}
		
		if( !dimension.hasSetting( SettingType.WHITELIST ) )
		{
			dimension.add( new WhitelistSetting() );
		}
		WhitelistSetting setting = (WhitelistSetting)dimension.get( SettingType.WHITELIST );
		
		if( !setting.isEditable() ) { throw new CommandException( "Whitelist isn't editable."); }
		
		Player player;
		
		switch( action )
		{
			case "add":
				player = args.parsePlayer();
				setting.add( player );
				dimensions.setDirty( true );
				break;
				
			case "remove":
				player = args.parsePlayer();
				setting.remove( player );
				dimensions.setDirty( true );
				break;
				
			case "list" :
			default :
				this.sendUsage( args.sender );
				args.sender.addChatMessage( new ChatComponentText( "Whitelist:" ) );
				
				for( Entry<UUID, Player> entry : setting.getUsers() )
				{
					args.sender.addChatMessage( new ChatComponentText( "    - '" + entry.getValue().toString() ) );
				}
				break;
		}
	}
}