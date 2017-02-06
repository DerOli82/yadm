package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionDummy;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.DimensionException;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.FindDimension;
import de.alaoli.games.minecraft.mods.yadm.world.ManageWorlds;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class InfoCommand extends Command
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final ManageWorlds worlds = WorldBuilder.INSTANCE;
	protected static final FindDimension dimensions = YADimensionManager.INSTANCE;
	
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
		
		Dimension dimension;
		
		args.sender.addChatMessage( new ChatComponentText( "Current dimension info:" ) );
		
		try
		{
			dimension = dimensions.findDimension( args.sender.getEntityWorld().provider.dimensionId );
			
			if( dimension.hasOwner() )
			{
				args.sender.addChatMessage( new ChatComponentText( "Owner: " + dimension.getOwner().toString() ) );
			}
		}
		catch( DimensionException e )
		{
			dimension = new DimensionDummy( args.sender.getEntityWorld().provider.dimensionId );
		}
		World world = MinecraftServer.getServer().worldServerForDimension( dimension.getId() );
		WorldInfo worldInfo = world.getWorldInfo();
		
		args.sender.addChatMessage( new ChatComponentText( "Name: " + world.provider.getDimensionName() ) );
		args.sender.addChatMessage( new ChatComponentText( "ID: " + world.provider.dimensionId ) );
		args.sender.addChatMessage( new ChatComponentText( "Seed: " + world.getSeed() ) );
		args.sender.addChatMessage( new ChatComponentText( "GenOpt: " + world.provider.field_82913_c ) );
	}
}
