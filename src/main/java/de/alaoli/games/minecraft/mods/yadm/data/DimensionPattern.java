package de.alaoli.games.minecraft.mods.yadm.data;

public class DimensionPattern 
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
	
	private String name;
	
	private String provider;
	
	private String type;
	
	private Long seed;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public DimensionPattern( String name, String provider, String type, Long seed )
	{
		this.name = name;
		this.provider = provider;
		this.type = type;
		this.seed = seed;
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	
	public String getName() 
	{
		return this.name;
	}

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
}
