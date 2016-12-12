package de.alaoli.games.minecraft.mods.yadm.command.manage;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandException;
import de.alaoli.games.minecraft.mods.yadm.command.Permission;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.BorderSide;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldBorderSetting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.worldborder.TravelSetting;
import de.alaoli.games.minecraft.mods.yadm.YADMException;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.DimensionException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class TravelCommand extends Command
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public TravelCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/

	@Override
	public String getCommandName() 
	{
		return "travel";
	}


	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		return super.getCommandUsage( sender ) + " set <sourceBorderSide> <targetDimensionName|Id> [<targetBorderSide>]|unset <sourceBorderSide>|list";
	}
	
	@Override
	public Permission requiredPermission()
	{
		return Permission.OWNER;
	}	
	
	@Override
	public void processCommand( Arguments args ) throws CommandException
	{
		//Check permission
		if( !this.canCommandSenderUseCommand( args ) ) { throw new CommandException( "You're not allowed to perform this command."); }
					
		String cmd = "";
		
		if( !args.isEmpty() )
		{
			cmd = args.next();
		}
		TravelSetting action;
		BorderSide targetSide, sourceSide;
		Dimension target, source;
		
		try
		{
			source = dimensions.findDimension( ((EntityPlayer)args.sender).dimension );
		}
		catch( DimensionException e )
		{
			throw new CommandException( e.getMessage(), e );
		}
		
		if( !source.hasSetting( SettingType.WORLDBORDER ) ) { throw new CommandException( "Travel option requires a WorldBorder." ); }
		
		WorldBorderSetting border = (WorldBorderSetting)source.get( SettingType.WORLDBORDER );
		
		switch( cmd )
		{
			case "set" :
				sourceSide = args.parseBorderSide();
				target = args.parseDimension( true );
				targetSide = null;
				
				//Non YADM Dimension Target only OP
				if( ( !dimensions.existsDimension( target.getId() ) ) && 
					( !args.senderIsOP ) ) 
				{ 
					throw new CommandException("Target must be a YADM Dimension.");
				}
				action = (TravelSetting)border.getAction(sourceSide, TravelSetting.class );
				
				//Check if editable
				if( ( action != null ) &&
					( !action.isEditable() ) )
				{
					throw new CommandException( "Border '" + sourceSide + "' isn't editable." );
				}
				
				try
				{
					targetSide = args.parseBorderSide();
				}
				catch( CommandException e )
				{
					//Ignore because optional
				}
				
				if( !border.allowedBorderSides().contains( sourceSide ) ) { throw new CommandException( "Source '" + sourceSide + "' border not allowed." );}
				if( ( targetSide != null ) && 
					( !border.allowedBorderSides().contains( targetSide ) ) )
				{
					throw new CommandException( "Source '" + targetSide + "' border not allowed." );
				}
				
				//Remove old
				if( action != null )
				{
					border.removeAction( sourceSide, action );
				}
				action = new TravelSetting( target.getId(), targetSide );
				border.addAction( sourceSide, action );
				
				dimensions.setDirty( true );
				
				args.sender.addChatMessage( 
					new ChatComponentText( "Established connection from dimension '" + source + "' to '" + target + "'." ) 
				);
				break;
				
			case "unset" :
				sourceSide = args.parseBorderSide();
				action = (TravelSetting) border.getAction( sourceSide, TravelSetting.class );
				
				if( action != null )
				{
					if( !action.isEditable() ) { throw new CommandException( "Border '" + sourceSide + "' isn't editable." ); }
					
					border.removeAction( sourceSide, action );
					dimensions.setDirty( true );
					args.sender.addChatMessage( new ChatComponentText( "Travel setting on '" + sourceSide + "' border removed." ) );
				}
				else
				{
					args.sender.addChatMessage( new ChatComponentText( "No travel setting on '" + sourceSide + "' border." ) );
				}
				break;
				
			case "list" :
			default:
				this.sendUsage( args.sender );
				args.sender.addChatMessage( new ChatComponentText( "Border travel connections:" ) );
				
				for( BorderSide side : BorderSide.values() )
				{
					action = (TravelSetting)border.getAction( side, TravelSetting.class );
					
					if( action != null )
					{
						int targetId = action.getTargetId();
						
						try
						{
							target = dimensions.findDimension( targetId );
							args.sender.addChatMessage( new ChatComponentText( "  - '" + side + "' border to '" + target + "'" ) );
						}
						catch( YADMException e )
						{
							args.sender.addChatMessage( new ChatComponentText( "  - '" + side + "' border to '" + targetId + "'" ) );
						}
					}
				}
				break;
		}
	}
}