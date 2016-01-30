package de.alaoli.games.minecraft.mods.yadm.data;

import com.google.gson.annotations.Expose;

public class DimensionPattern extends DataObject
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	public static final String PROVIDER_OVERWORLD	= "overworld";
	public static final String PROVIDER_NETHER		= "nether";
	public static final String PROVIDER_END			= "end";
	
	public static final String TYPE_DEFAULT			= "default";
	
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	@Expose
	private String provider;
	
	@Expose
	private String type;
	
	@Expose
	private Long seed;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public DimensionPattern( String name ) 
	{
		super( name );
	}
	
	public DimensionPattern( String name, String provider, String type, Long seed )
	{
		super( name );
		this.provider = provider;
		this.type = type;
		this.seed = seed;
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/

	public String getProvider() 
	{
		return this.provider;
	}

	public String getType() 
	{
		return this.type;
	}

	public Long getSeed() 
	{
		return this.seed;
	}

	public void setProvider( String provider )
	{
		this.provider = provider;
	}

	public void setType( String type )
	{
		this.type = type;
	}

	public void setSeed( Long seed ) 
	{
		this.seed = seed;
	}
	
	
}
