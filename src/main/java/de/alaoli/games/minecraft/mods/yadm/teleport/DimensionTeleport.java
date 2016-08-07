package de.alaoli.games.minecraft.mods.yadm.teleport;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class DimensionTeleport extends Teleporter
{
	protected Coordinate coordinate;
	protected WorldServer world;
	
    public DimensionTeleport( WorldServer world, Coordinate coordinate )
    {
        super( world );
        this.coordinate = coordinate;
        this.world = world;
    }

    @Override
    public void placeInPortal( Entity entity, double x, double y, double z, float rotationYaw ) 
    {
    	this.world.getBlock( 
			this.coordinate.x, 
			this.coordinate.y, 
			this.coordinate.z
		);
    	entity.setPosition( 
			this.coordinate.x, 
			this.coordinate.y, 
			this.coordinate.z
		);
    	entity.motionX = 0.0F;
    	entity.motionY = 0.0F;
    	entity.motionZ = 0.0F;
    }

    @Override
    public boolean placeInExistingPortal( Entity entity, double x, double y, double z, float rotationYaw ) { return false; }

    @Override
	public boolean makePortal(Entity p_85188_1_) { return false; }

	@Override
    public void removeStalePortalLocations( long totalWorldTime ) {}    
}
