package de.alaoli.games.minecraft.mods.yadm.world;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public interface ManageWorlds 
{
	public void registerWorldProviderForDimension( Dimension dimension ) throws WorldException;
	public void unregisterWorldProviderForDimension( Dimension dimension ) throws WorldException;
	
	public void markWorldForDeletion( Dimension dimension );
	public void deleteWorld( World world ) throws WorldException;
	
	public WorldServer getWorldServerForDimension( Dimension dimension ) throws WorldException;
}
