package de.alaoli.games.minecraft.mods.yadm.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraftforge.common.DimensionManager;

@Mixin( DimensionManager.class )
public abstract class DimensionManagerMixin 
{
	@Inject( method = "initDimension", at = @At( "HEAD" ), remap = false, cancellable = true )
	private static void initDimension( int dim, CallbackInfo ci )
	{		
		//Is YADM dimension?
		if( YADimensionManager.INSTANCE.existsDimension( dim ) )
		{
			Log.debug( "Mixins: DimensionManager.initDimension" );
			
	    	Dimension dimension = YADimensionManager.INSTANCE.findDimension( dim );
	    	
			WorldBuilder.INSTANCE.initWorldServer( dimension );
			ci.cancel();
		}
	}
}
