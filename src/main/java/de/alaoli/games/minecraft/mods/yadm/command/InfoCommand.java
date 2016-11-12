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
	public void processCommand( Arguments command )
	{
		World world;
		
		int dimensionId = command.sender.getEntityWorld().provider.dimensionId;
		
		command.sender.addChatMessage( new ChatComponentText( "Current dimension info:" ) );
		
		if( YADimensionManager.INSTANCE.exists( dimensionId ) )
		{
			Dimension dimension = YADimensionManager.INSTANCE.get( dimensionId ); 
			world = YADimensionManager.INSTANCE.getWorldServerForDimension( dimension );
			
			if( dimension.hasOwner() )
			{	
				command.sender.addChatMessage( new ChatComponentText( "Owner: " + dimension.getOwner().toString() ) );
			}
		}
		else
		{
			world = DimensionManager.getWorld( dimensionId );
		}
		WorldInfo worldInfo = world.getWorldInfo();
		
		command.sender.addChatMessage( new ChatComponentText( "Name: " + world.provider.getDimensionName() ) );
		command.sender.addChatMessage( new ChatComponentText( "ID: " + world.provider.dimensionId ) );
		command.sender.addChatMessage( new ChatComponentText( "Seed: " + world.getSeed() ) );
		command.sender.addChatMessage( new ChatComponentText( "GenOpt: " + world.provider.field_82913_c ) );
	}
}
