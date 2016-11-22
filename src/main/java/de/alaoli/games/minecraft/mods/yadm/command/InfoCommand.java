package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

public class InfoCommand extends Command
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public InfoCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public String getCommandName() 
	{
		return "info";
	}

	@Override
	public Permission requiredPermission()
	{
		return Permission.PLAYER;
	}
	
	@Override
	public void processCommand( Arguments args )
	{
		//Check permission
		if( !this.canCommandSenderUseCommand( args ) ) { throw new CommandException( "You're not allowed to perform this command."); }
				
		World world;
		
		int dimensionId = args.sender.getEntityWorld().provider.dimensionId;
		
		args.sender.addChatMessage( new ChatComponentText( "Current dimension info:" ) );
		
		if( YADimensionManager.INSTANCE.exists( dimensionId ) )
		{
			Dimension dimension = YADimensionManager.INSTANCE.get( dimensionId ); 
			world = YADimensionManager.INSTANCE.getWorldServerForDimension( dimension );
			
			if( dimension.hasOwner() )
			{	
				args.sender.addChatMessage( new ChatComponentText( "Owner: " + dimension.getOwner().toString() ) );
			}
		}
		else
		{
			world = DimensionManager.getWorld( dimensionId );
		}
		WorldInfo worldInfo = world.getWorldInfo();
		
		args.sender.addChatMessage( new ChatComponentText( "Name: " + world.provider.getDimensionName() ) );
		args.sender.addChatMessage( new ChatComponentText( "ID: " + world.provider.dimensionId ) );
		args.sender.addChatMessage( new ChatComponentText( "Seed: " + world.getSeed() ) );
		args.sender.addChatMessage( new ChatComponentText( "GenOpt: " + world.provider.field_82913_c ) );
	}
}
