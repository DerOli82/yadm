package de.alaoli.games.minecraft.mods.yadm.data;

import com.google.gson.annotations.Expose;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class Dimension extends DataObject
{
	/**********************************************************************
	 * Attributes 
	 **********************************************************************/

	@Expose
	private int id;
	
	@Expose
	private String patternName;
	
	@Expose
	private String providerName;
	
	@Expose
	private String typeName;
	
	private DimensionPattern pattern;
	
	private WorldServer worldServer;
	
	private boolean isRegistered;
	
	/**********************************************************************
	 * Methods 
	 **********************************************************************/

	public Dimension( String name )
	{
		super( name );
	}
	
	public Dimension( int id, String name )
	{
		super( name );
		
		this.id = id;
	}
	
	public Dimension( int id, String name, String patternName )
	{
		super( name );
		
		this.id = id;
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

	public String getPatternName()
	{
		return this.patternName;
	}
	
	public String getProviderName() 
	{
		return this.providerName;
	}

	public String getTypeName() 
	{
		return this.typeName;
	}

	public DimensionPattern getPattern()
	{
		//Get reference
		if( this.pattern == null )
		{
			this.pattern = (DimensionPattern) YADM.proxy.getPatternManager().get( this.patternName );
		}
		return this.pattern;
	}
	
	public WorldServer getWorldServer() 
	{
		//Get reference		
		if( this.worldServer == null ) 
		{
			this.worldServer = DimensionManager.getWorld( this.id );
		}
		return this.worldServer;
	}

	public void setPatternName( String patternName )
	{
		this.patternName = patternName;
		
		//Copy Provider & Type
		this.providerName = this.getPattern().getProvider();
		this.typeName = this.getPattern().getType();
	}
	
	public void setProviderName( String providerName )
	{
		this.providerName = providerName;
	}
	
	public void setTypeName( String typeName )
	{
		this.typeName = typeName;
	}
	
	public void setRegistered( boolean isRegistered )
	{
		this.isRegistered = isRegistered;
	}
}
