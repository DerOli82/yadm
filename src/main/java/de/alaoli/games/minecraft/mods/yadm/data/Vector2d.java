package de.alaoli.games.minecraft.mods.yadm.data;

public class Vector2d 
{
	public float x;
	public float y;
	
	public Vector2d( float x, float y )
	{
		this.x = x;
		this.y = y;
	}
	
	public float length()
	{
		return (float) Math.sqrt( ( this.x * this.x ) + ( this.y * this.y ) );
	}
	
	public void scale( float factor )
	{
		this.x = this.x * factor;
		this.y = this.y * factor;
	}
}
