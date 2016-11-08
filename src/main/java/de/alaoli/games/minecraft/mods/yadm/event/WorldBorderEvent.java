package de.alaoli.games.minecraft.mods.yadm.event;

import de.alaoli.games.minecraft.mods.yadm.data.ChunkCoordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.BorderSide;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldBorderSetting;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;

public class WorldBorderEvent
{
	final public EnteringChunk chunkEvent;
	final public Dimension dimension;
	final public WorldBorderSetting setting;
	final public ChunkCoordinate currentChunk;
	final public BorderSide side;
	
	private boolean isCanceled = false;
	
	public WorldBorderEvent( EnteringChunk chunkEvent, Dimension dimension )
	{
		this.chunkEvent = chunkEvent;
		this.dimension = dimension;
		this.setting = (WorldBorderSetting)dimension.get( SettingType.WORLDBORDER );;
		this.currentChunk = new ChunkCoordinate( chunkEvent.newChunkX, chunkEvent.newChunkZ );
		this.side = setting.intersectsBorder( this.currentChunk );
	}
	
    public boolean isCanceled()
    {
        return this.isCanceled;
    }
    
    public void setCanceled( boolean cancel )
    {
    	this.isCanceled = cancel;
    }
}
