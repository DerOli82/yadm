package de.alaoli.games.minecraft.mods.yadm.data.settings;

public enum TeleportMode 
{
	PLATFORM( "platform" );
	
	private String name;
	
	private TeleportMode( String name )
	{
		this.name = name;
	}
		
	@Override
	public String toString() 
	{
		return this.name;
	}

	public static TeleportMode get( String name )
	{
		for( TeleportMode mode : TeleportMode.values() )
		{
			if( mode.toString().equals( name ) )
			{
				return mode;
			}
		}
		return null;
	}	
}
