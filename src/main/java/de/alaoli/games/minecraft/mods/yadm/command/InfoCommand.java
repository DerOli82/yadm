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
	public int getRequiredPermissionLevel() 
	{
		return -1;
	}
	
	@Override
	public String getCommandName() 
	{
		return "info";
	}

	@Override
	public void processCommand( CommandParser command )
	{
		World world;
		int dimensionId = command.getSender().getEntityWorld().provider.dimensionId;
		
		command.getSender().addChatMessage( new ChatComponentText( "Current dimension info:" ) );
		
		if( YADimensionManager.INSTANCE.exists( dimensionId ) )
		{
			Dimension dimension = YADimensionManager.INSTANCE.get( dimensionId ); 
			world = YADimensionManager.INSTANCE.getWorldServerForDimension( dimension );
			
			if( dimension.hasOwner() )
			{	
				command.getSender().addChatMessage( new ChatComponentText( "Owner: " + dimension.getOwner().toString() ) );
			}
		}
		else
		{
			world = DimensionManager.getWorld( dimensionId );
		}
		WorldInfo worldInfo = world.getWorldInfo();
		
		
		
		command.getSender().addChatMessage( new ChatComponentText( "Name: " + world.provider.getDimensionName() ) );
		command.getSender().addChatMessage( new ChatComponentText( "ID: " + world.provider.dimensionId ) );
		command.getSender().addChatMessage( new ChatComponentText( "Seed: " + world.getSeed() ) );
		command.getSender().addChatMessage( new ChatComponentText( "GenOpt: " + world.provider.field_82913_c ) );
	}
}
