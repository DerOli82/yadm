package de.alaoli.games.minecraft.mods.yadm.data;

import de.alaoli.games.minecraft.mods.yadm.manager.DimensionPatternManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class Dimension 
{
	/**********************************************************************
	 * Attributes 
	 **********************************************************************/

	private int id;

	private String name;
	
	private String patternName;
	
	private DimensionPattern pattern;
	
	private WorldServer worldServer;
	
	private boolean isRegistered;
	
	/**********************************************************************
	 * Methods 
	 **********************************************************************/

	public Dimension( int id, String name, String patternName )
	{
		this.id = id;
		this.name = name;
		this.patternName = patternName;
		
		this.isRegistered = false;
	}	
	
	public boolean isRegistered()
	{
		return this.isRegistered;
	}
	/**********************************************************************
	 * Methods - Getter/Setter
	 **********************************************************************/
		
	public int getId() 
	{
		return this.id;
	}

	public String getName() 
	{
		return this.name;
	}

	public String getPatternName()
	{
		return this.patternName;
	}
	
	public DimensionPattern getPattern()
	{
		if( this.pattern == null )
		{
			this.pattern = DimensionPatternManager.getInstance().getPattern( this.patternName );
		}
		return this.pattern;
	}
	
	public WorldServer getWorldServer() 
	{
		if( this.worldServer == null ) 
		{
			this.worldServer = DimensionManager.getWorld( this.id );
		}
		return this.worldServer;
	}

	public void setRegistered( boolean isRegistered )
	{
		this.isRegistered = isRegistered;
	}
}
