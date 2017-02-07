package de.alaoli.games.minecraft.mods.yadm.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

@Mixin( WorldProvider.class )
public abstract class WorldProviderMixin
{
	private Dimension dimension; 
	
	@Inject( method = "registerWorld", at = @At( "HEAD" ) )
	private void preRegisterWorld( World world, CallbackInfo ci )
    {
		Log.debug( "Mixins: pre WorldProviderMixin.registerWorld" );
		
		if( YADimensionManager.INSTANCE.existsDimension( world.provider.dimensionId) )
		{
			this.dimension = YADimensionManager.INSTANCE.findDimension( world.provider.dimensionId );

    		try 
    		{
    			dimension.injectInto( world );
    		}
    		catch ( Exception e ) 
    		{
    			e.printStackTrace();
    		}
		}  		
    }
	
	@Inject( method = "registerWorld", at = @At( "RETURN" ) )
	private void postRegisterWorld( World world, CallbackInfo ci )
    {
		Log.debug( "Mixins: post WorldProviderMixin.registerWorld" );
		
		if( ( this.dimension != null ) && 
			( this.dimension.getId() != world.provider.dimensionId ) )
		{
			Log.debug( "Mixins: Correcting Id " + world.provider.dimensionId + " => " + this.dimension.getId() );
			
			world.provider.dimensionId = this.dimension.getId();
		}  		
    }
}
