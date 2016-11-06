package de.alaoli.games.minecraft.mods.yadm.data.settings;

public enum BorderSide 
{
	NORTH( "north" ),
	EAST( "east" ),
	SOUTH( "south" ),
	WEST( "west" );
	
	private String name;
	
	private BorderSide( String name )
	{
		this.name = name;
	}
		
	@Override
	public String toString() 
	{
		return this.name;
	}

	public static BorderSide get( String name )
	{
		for( BorderSide side : BorderSide.values() )
		{
			if( side.toString().equals( name ) )
			{
				return side;
			}
		}
		return null;
	}	
}
