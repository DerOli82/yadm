package de.alaoli.games.minecraft.mods.yadm.teleport;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class DimensionTeleport extends Teleporter
{
    public DimensionTeleport( WorldServer world )
    {
        super( world );
    }

    @Override
    public boolean placeInExistingPortal( Entity entity, double x, double y, double z, float rotationYaw )
    {
        return false;
    }

    @Override
    public void placeInPortal( Entity entity, double x, double y, double z, float rotationYaw ) {}
    

    @Override
    public void removeStalePortalLocations( long totalWorldTime ) {}    
}
