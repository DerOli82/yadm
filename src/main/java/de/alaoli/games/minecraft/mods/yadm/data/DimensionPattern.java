package de.alaoli.games.minecraft.mods.yadm.data;

import com.google.gson.annotations.Expose;

import net.minecraft.nbt.NBTTagCompound;

public class DimensionPattern implements DataObject
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
	protected String name;
	
	@Expose
	protected String providerName;
	
	@Expose
	protected String typeName;
	
	@Expose
	protected String generatorOptions;
	
	@Expose
	protected Long seed;
	
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public DimensionPattern()
	{
		
	}
	
	public DimensionPattern( String name )
	{
		this.name = name;
	}
	
	public DimensionPattern( String name, String providerName, String typeName, String generatorOptions, Long seed )
	{
		this.name = name;
		this.providerName = providerName;
		this.typeName = typeName;
		this.generatorOptions = generatorOptions;
		this.seed = seed;
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/

	public String getProviderName() 
	{
		return this.providerName;
	}

	public String getTypeName() 
	{
		return this.typeName;
	}

	public String getGeneratorOptions()
	{
		return this.generatorOptions;
	}
	
	public Long getSeed() 
	{
		return this.seed;
	}

	public void setProviderName( String providerName )
	{
		this.providerName = providerName;
	}

	public void setTypeName( String typeName )
	{
		this.typeName = typeName;
	}

	public void setGeneratorOptions( String generatorOptions ) 
	{
		this.generatorOptions = generatorOptions;
	}

	public void setSeed( Long seed ) 
	{
		this.seed = seed;
	}
	
	/********************************************************************************
	 * Methods - Implements DataObject
	 ********************************************************************************/	
	
	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void writeToNBT( NBTTagCompound tagCompound )
	{
		tagCompound.setString( "name", this.name );
		tagCompound.setString( "providerName", this.providerName );
		tagCompound.setString( "typeName", this.typeName );
		
		//Optional
		if( this.generatorOptions != null )
		{
			tagCompound.setString( "generatorOptions", this.generatorOptions );
		}
		if( this.seed != null )
		{
			tagCompound.setLong( "seed", this.seed );
		}
	}

	@Override
	public void readFromNBT( NBTTagCompound tagCompound ) 
	{
		this.name = 			tagCompound.getString( "name" );
		this.providerName = 	tagCompound.getString( "providerName" );
		this.typeName = 		tagCompound.getString( "typeName" );
		
		//Optional
		if( tagCompound.hasKey("generatorOptions" ) )
		{
			this.generatorOptions = tagCompound.getString( "generatorOptions" );
		}
		if( tagCompound.hasKey("seed" ) )
		{
			this.seed = tagCompound.getLong( "seed" );
		}
	}
}
