package de.alaoli.games.minecraft.mods.yadm.data;

import java.util.Random;

import com.google.gson.annotations.Expose;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class Dimension extends DimensionPattern
{
	/**********************************************************************
	 * Attributes 
	 **********************************************************************/

	@Expose
	protected int id;
	
	@Expose
	protected String patternName;
	
	private DimensionPattern pattern;
	
	private WorldType type;
	
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
		this.setPatternName( patternName );
		
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

	public DimensionPattern getPattern()
	{
		//Get reference
		if( this.pattern == null )
		{
			this.pattern = (DimensionPattern) YADM.proxy.getPatternManager().get( this.patternName );
		}
		return this.pattern;
	}
	
	public WorldType getType()
	{
		//Get reference
		if( this.type == null )
		{
			this.type = YADM.proxy.getPatternManager().getWorldType( this.typeName );
		}
		return this.type;
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
		
		//Copy provider, type, generator options and seed if seed null -> random
		this.providerName = this.getPattern().getProviderName();
		this.typeName = this.getPattern().getTypeName();
		this.generatorOptions = this.getPattern().getGeneratorOptions();
		this.seed = (this.getPattern().getSeed() != null ) ? this.getPattern().getSeed() : (new Random()).nextLong();
	}

	public void setRegistered( boolean isRegistered )
	{
		this.isRegistered = isRegistered;
	}
}
