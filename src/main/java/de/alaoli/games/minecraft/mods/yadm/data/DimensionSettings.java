package de.alaoli.games.minecraft.mods.yadm.data;

import java.util.Random;

import com.google.gson.annotations.Expose;

import net.minecraft.nbt.NBTTagCompound;

public class DimensionSettings implements NBTData
{
	@Expose
	protected int providerId;
	
	@Expose
	protected String providerName;
	
	@Expose
	protected String typeName;
	
	@Expose
	protected String generatorOptions;
	
	@Expose
	protected Long seed;
	
	public DimensionSettings( NBTTagCompound tagCompound )
	{
		this.readFromNBT( tagCompound );
	}
	
	public DimensionSettings( DimensionTemplate template )
	{
		this.providerName = template.providerName;
		this.typeName = template.getTypeName();
		this.generatorOptions = ( template.getGeneratorOptions() != null ) ? template.getGeneratorOptions() : "";
		this.seed = ( template.getSeed() != null ) ? template.getSeed() : (new Random()).nextLong();
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/


	public int getProviderId() 
	{
		return this.providerId;
	}
	
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
	
	public void setProviderId( int providerId )
	{
		this.providerId = providerId;
	}
	
	/********************************************************************************
	 * Methods - Implements NBTData
	 ********************************************************************************/	
	
	@Override
	public void writeToNBT( NBTTagCompound tagCompound ) 
	{
		tagCompound.setInteger( "providerId", this.providerId );
		tagCompound.setString( "providerName", this.providerName );
		tagCompound.setString( "typeName", this.typeName );
		tagCompound.setString( "generatorOptions", this.generatorOptions );
		tagCompound.setLong( "seed", this.seed );
	}

	@Override
	public void readFromNBT( NBTTagCompound tagCompound ) 
	{
		this.providerId 	= tagCompound.getInteger( "providerId" );
		this.providerName 	= tagCompound.getString( "providerName" );
		this.typeName 		= tagCompound.getString( "typeName" );
		this.generatorOptions = tagCompound.getString( "generatorOptions" );
		this.seed = tagCompound.getLong( "seed" );
	}	
}
