package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.ArrayList;
import java.util.List;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public abstract class Command implements ICommand
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private Command parent;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Command( Command parent )
	{
		this.parent = parent;
	}
	
	public boolean hasParent()
	{
		if( this.parent == null )
		{
			return false;
		}
		return true;
	}
	
	public Command getParent()
	{
		return this.parent;
	}

	public void sendUsage( ICommandSender sender )
	{
		sender.addChatMessage( new ChatComponentText( this.getCommandUsage( sender ) ) );
	}
	
	public boolean canCommandSenderUseCommand( Arguments args )
	{
		if( args.senderIsOP ) { return true; }
		
		switch( this.requiredPermission() )
		{				
			case OWNER:
				if( !args.senderIsEntityPlayer ) { return false; }
				
				EntityPlayer owner = args.getSenderAsEntityPlayer();
				
				if( !YADimensionManager.INSTANCE.exists( owner.dimension ) ) { return false; }
				
				Dimension dimension = YADimensionManager.INSTANCE.get( owner.dimension );
				
				return dimension.isOwner( owner );
				
			case PLAYER:
				return args.senderIsEntityPlayer;
				
			default:
				return false;
		}
	}
	
	public abstract Permission requiredPermission();
	
	public abstract void processCommand( Arguments args );
	
	/********************************************************************************
	 * Interface - ICommand
	 ********************************************************************************/
	
	@Override
	public int compareTo( Object obj ) 
	{
		return  this.getCommandName().compareTo( ((ICommand)obj).getCommandName() );
	}

	@Override
	public boolean canCommandSenderUseCommand( ICommandSender sender )
	{
		return true;		
	}
	
	@Override
	public boolean isUsernameIndex( String[] p_82358_1_, int p_82358_2_ )
	{
		return false;
	}
    
	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		String usage = this.getCommandName();
		
		if( this.hasParent() )
		{
			usage = this.getParent().getCommandUsage( sender ) + " " + usage;
		}
		return usage;
	}
	
	@Override
	public List getCommandAliases() 
	{
		List<String> list = new ArrayList<String>();
		
		list.add( this.getCommandName() );
		
		return list;
	}	
	
	@Override
	public List addTabCompletionOptions( ICommandSender sender, String[] args )
	{
		List<String> list = new ArrayList<String>();
		list.add( this.getCommandName() );
		
		return list;
	}
	
	@Override
	public void processCommand( ICommandSender sender, String[] args )
	{
		try
		{
			this.processCommand( new Arguments( sender, args ) );
		}
		catch( CommandException e )
		{
			sender.addChatMessage( new ChatComponentText( e.getMessage() ) );
		}
	}
}
