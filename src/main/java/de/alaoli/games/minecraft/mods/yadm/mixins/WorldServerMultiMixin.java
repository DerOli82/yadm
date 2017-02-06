package de.alaoli.games.minecraft.mods.yadm.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

@Mixin( WorldServerMulti.class )
public abstract class WorldServerMultiMixin extends World 
{
	public WorldServerMultiMixin(ISaveHandler p_i45368_1_, String p_i45368_2_, WorldProvider p_i45368_3_,
			WorldSettings p_i45368_4_, Profiler p_i45368_5_) {
		super(p_i45368_1_, p_i45368_2_, p_i45368_3_, p_i45368_4_, p_i45368_5_);
		// TODO Auto-generated constructor stub
	}

	@Inject( method = "<init>", at = @At( "RETURN" ) )
	private void onConstruct( MinecraftServer p_i45283_1_, ISaveHandler p_i45283_2_, String p_i45283_3_, int p_i45283_4_, WorldSettings p_i45283_5_, WorldServer p_i45283_6_, Profiler p_i45283_7_, CallbackInfo ci )
	{		
		Log.debug( "Mixins: WorldServerMulti.<init>" );
		
		//Is YADM dimension?
		if( YADimensionManager.INSTANCE.existsDimension( p_i45283_4_ ) )
		{
			Log.debug( "Restore WorldServer WorldInfo" );
						
			//Get theWorldInfo back
			if( this.worldInfo instanceof DerivedWorldInfo )
			{
				this.worldInfo = (WorldInfo) ReflectionHelper.getPrivateValue( 
					DerivedWorldInfo.class, 
					(DerivedWorldInfo)worldInfo, 
					new String[] { "field_76115_a", "theWorldInfo" } 
				);
			}
		}

	}
}
