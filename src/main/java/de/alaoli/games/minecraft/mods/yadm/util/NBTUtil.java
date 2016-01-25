package de.alaoli.games.minecraft.mods.yadm.util;

import java.util.HashMap;
import java.util.Map;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTUtil 
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	final public static String NBT_DIMENSION_ID			= "id";
	final public static String NBT_DIMENSION_NAME		= "name";
	final public static String NBT_DIMENSION_PATTERN	= "pattern";

	
	/********************************************************************************
	 * Methods - Data to NBT
	 ********************************************************************************/
	
	/**
	 * From Dimension to NBTTagCompound
	 * 
	 * @param Dimension
	 * @return NBTTagCompound
	 */
	public static NBTTagCompound fromDimension( Dimension dimension )
	{
		NBTTagCompound comp = new NBTTagCompound();
		
		comp.setInteger( NBT_DIMENSION_ID, dimension.getId() );
		comp.setString( NBT_DIMENSION_NAME, dimension.getName() );
		comp.setString( NBT_DIMENSION_PATTERN, dimension.getPatternName() );
		
		return comp;
	}
	
	/**
	 * From Dimension Map to NBTTagList
	 * 
	 * @param Map<String, Dimension>
	 * @return NBTTagList
	 */
	public static NBTTagList fromDimensionMap( Map<String, Dimension> dimensions )
	{
		NBTTagList list = new NBTTagList();
		
		for( Dimension dimension : dimensions.values() )
		{
			list.appendTag( NBTUtil.fromDimension( dimension ) );
		}
		return list;
		
	}
	
	/********************************************************************************
	 * Methods - NBT to Data
	 ********************************************************************************/
	
	/**
	 * From NBTTagCompound to Dimension
	 * 
	 * @param NBTTagCompound
	 * @return Dimension
	 * @throws NBTException
	 */
	public static Dimension toDimension( NBTTagCompound comp ) throws NBTException
	{
		//All keys are required
		if( ( !comp.hasKey( NBT_DIMENSION_ID ) ) ||
			( !comp.hasKey( NBT_DIMENSION_NAME ) ) ||
			( !comp.hasKey( NBT_DIMENSION_PATTERN ) ) )
		{
			throw new NBTException( "NBTTagCompound has no dimension data." );
		}
		return new Dimension(
			comp.getInteger( NBT_DIMENSION_ID ),
			comp.getString( NBT_DIMENSION_NAME ),
			comp.getString( NBT_DIMENSION_PATTERN )
		);
	}
	
	/**
	 * From NBTTagList to Dimension Map
	 * 
	 * @param NBTTagList
	 * @return Map<String, Dimension>
	 * @throws NBTException
	 */
	public static Map<String, Dimension> toDimensionMap( NBTTagList list ) throws NBTException
	{
		Dimension dimension;
		Map<String, Dimension> dimensions = new HashMap<String, Dimension>();
		
		for( int i = 0; i < list.tagCount(); i++ )
		{
			dimension = NBTUtil.toDimension( list.getCompoundTagAt( i ) );
			dimensions.put( dimension.getName(), dimension );
		}
		return dimensions;
	}
}
