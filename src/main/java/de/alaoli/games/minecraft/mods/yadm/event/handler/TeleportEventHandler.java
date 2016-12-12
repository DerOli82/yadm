package de.alaoli.games.minecraft.mods.yadm.event.handler;

import java.util.ArrayList;
import java.util.List;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import de.alaoli.games.minecraft.mods.yadm.event.TeleportEvent;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportException;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;

public class TeleportEventHandler 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final TeleportEventHandler INSTANCE = new TeleportEventHandler(); 
	protected static final TeleportPlayer players = PlayerManager.INSTANCE;
	
	private List<TeleportEvent> teleports = new ArrayList<TeleportEvent>();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private TeleportEventHandler() {}
	
	public static void register()
	{
		FMLCommonHandler.instance().bus().register( INSTANCE );
		MinecraftForge.EVENT_BUS.register( INSTANCE );
	}
	
	/********************************************************************************
	 * Methods - Events
	 ********************************************************************************/	
	
	@SubscribeEvent
	public void triggerTeleport( TeleportEvent event )
	{
		if( !event.target.isRemote ) 
		{
			this.teleports.add( event );
		}
		
	}
	
	@SubscribeEvent
	public void executeTeleport( TickEvent.WorldTickEvent event )
	{
		if( ( !event.world.isRemote ) && 
			( event.phase == TickEvent.Phase.START && event.type == TickEvent.Type.WORLD ) )
		{
			List<TeleportEvent> remove = new ArrayList<TeleportEvent>();
			
			for( TeleportEvent teleport : this.teleports )
			{
				if( teleport.target == event.world )
				{
					try
					{
						players.teleport( teleport );
					}
					catch( TeleportException e )
					{
						teleport.player.addChatComponentMessage( new ChatComponentText( e.getMessage() ) );
					}
					remove.add( teleport );
				}
			}
			this.teleports.removeAll( remove );
		}
	}   	
}
