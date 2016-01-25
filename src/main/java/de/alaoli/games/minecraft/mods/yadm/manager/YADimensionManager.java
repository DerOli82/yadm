package de.alaoli.games.minecraft.mods.yadm.manager;

import java.util.HashMap;
import java.util.Map;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.network.DimensionSyncMessage;
import de.alaoli.games.minecraft.mods.yadm.util.NBTUtil;
import de.alaoli.games.minecraft.mods.yadm.world.WorldProviderGeneric;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

public class YADimensionManager extends WorldSavedData 
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	public final static String ID = "YADimensionManager";

	/********************************************************************************
	 * Attribute
	 ********************************************************************************/

	private static YADimensionManager instance;
	
	private Map<String, Dimension> dimByName;
	
	/********************************************************************************
	 * Methods - Constructor
	 ********************************************************************************/
	
	private YADimensionManager() 
	{
		super( YADimensionManager.ID );
	
		this.dimByName = new HashMap<String, Dimension>();
	}

	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/

	public static YADimensionManager getInstance()
	{
		if( YADimensionManager.instance == null )
		{
			throw new RuntimeException( "YADimensionManager is not initialized yet." );
		}
		return YADimensionManager.instance;
	}
	
	public static YADimensionManager getInstance( World world )
	{
		if( world.mapStorage == null )
		{
			throw new RuntimeException( "world.mapStorage is not initialized yet." );
		}
		
		//Initialize
		if( instance == null )
		{
			//Load WorldSavedData
			instance = (YADimensionManager) world.mapStorage.loadData( YADimensionManager.class, ID );
			
			//No data yet?
			if( instance == null )
			{
				instance = new YADimensionManager();
				instance.markDirty();
				world.mapStorage.setData( ID, instance );
			}
		}
		return YADimensionManager.instance;
	}	
	
	public Dimension getDimensionByName( String name )
	{
		return this.dimByName.get( name );
	}
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public static Dimension createDimension( String name, String pattern ) throws RuntimeException
	{
		YADimensionManager dimensionManager = YADimensionManager.getInstance();
		DimensionPatternManager patternManager = DimensionPatternManager.getInstance();
		
		if( dimensionManager.existsDimension( name ) )
		{
			throw new RuntimeException( "Dimension name already exists." );
		}
		
		if( !patternManager.existsPattern( pattern ) )
		{
			throw new RuntimeException( "Pattern doesn't exists." );
		}
		Dimension dimension = new Dimension( dimensionManager.nextDimensionId(), name, pattern );
		
		dimensionManager.registerDimension( dimension );
		dimensionManager.initDimension( dimension );
		
		dimensionManager.addDimension( dimension );
		dimensionManager.markDirty();
		
		return dimension;
	}
	
	public int nextDimensionId()
	{
		int nextId = Config.Dimensions.beginsWithId;
		
		//Check for next ID
		while( DimensionManager.isDimensionRegistered( nextId ) )
		{
			nextId++;
		}
		return nextId;
	}
	
	public boolean existsDimension( String name )
	{
		return this.dimByName.containsKey( name );
	}

	public void addDimension( Dimension dimension )
	{
		this.dimByName.put( dimension.getName(), dimension );
	}
	
	public void registerDimensions()
	{
		for( Dimension dimension : this.dimByName.values() )
		{
			if( !dimension.isRegistered() )
			{
				this.registerDimension( dimension );
			}
		}
	}
	
	public void unregisterDimensions()
	{
		for( Dimension dimension : this.dimByName.values() )
		{
			if( dimension.isRegistered() )
			{
				DimensionManager.unregisterDimension( dimension.getId() );
				dimension.setRegistered( false );
			}
		}
	}
	
	
	/**
	 * Register dimension and check if registered
	 * 
	 * @param Dimension dimension
	 * @throws RuntimeException
	 */
	private void registerDimension( Dimension dimension ) throws RuntimeException
	{
		if( dimension.isRegistered() )
		{
			return;
		}
		DimensionManager.registerProviderType( dimension.getId(), WorldProviderGeneric.class, false );
		DimensionManager.registerDimension( dimension.getId(), dimension.getId() );

		YADM.network.sendToServer( new DimensionSyncMessage( dimension.getId() ) );
		YADM.network.sendToAll( new DimensionSyncMessage( dimension.getId() ));
		
		dimension.setRegistered( true );
	}
	
	/**
	 * Initialize Dimension
	 * 
	 * @param Dimension dimension
	 * @throws RuntimeException
	 */
	public void initDimension( Dimension dimension ) throws RuntimeException
	{
		DimensionManager.initDimension( dimension.getId() );
		/*
        WorldServer overworld = DimensionManager.getWorld( 0 );
        
        if (overworld == null)
        {
            throw new RuntimeException( "Cannot Hotload Dim: Overworld is not Loaded!" );
        }
        
        try
        {
            DimensionManager.getProviderType( dimension.getId() );
        }
        catch ( Exception e )
        {
        	// If a provider hasn't been registered then we can't hotload the dim
        	StringBuilder msg = new StringBuilder();
        	
        	msg.append( "Cannot Hotload Dimension \"" );
			msg.append( dimension.getId() );
			msg.append( ":" );
			msg.append( dimension.getName() );
			msg.append( "\" : " );
			msg.append( e.getMessage() );
			
            throw new RuntimeException( msg.toString() );
        }
        //MinecraftServer mcServer = overworld.func_73046_m();
        MinecraftServer mcServer = MinecraftServer.getServer();
        ISaveHandler savehandler = overworld.getSaveHandler();
  /*      WorldSettings worldSettings = new WorldSettings(
    		dimension.getSeed(),
    		GameType.SURVIVAL,
    		true,
    		false,
    		this.getWorldTypeByName( dimension.getWorldType() )
		);        
        WorldServer world = new WorldServerMulti(
    		mcServer, 
    		savehandler, 
    		overworld.getWorldInfo().getWorldName(), 
    		dimension.getId(), 
    		worldSettings, 
    		overworld, 
    		mcServer.theProfiler
		);
        world.provider.dimensionId = dimension.getId();
        world.addWorldAccess(new WorldManager( mcServer, world ) );

        if ( !mcServer.isSinglePlayer() )
        {
            world.getWorldInfo().setGameType( mcServer.getGameType() );
        }
        mcServer.func_147139_a( mcServer.func_147135_j() );		
        MinecraftForge.EVENT_BUS.post( new WorldEvent.Load( world ) );*/
	}
	
	/*
	public void createDimension( Dimension dimension )
	{
		this.registerDimension( dimension );
		this.initDimension( dimension );
		
		YADM.network.sendToAll( new DimensionSyncMessage( dimension.getId() ));
		/*
        FMLEmbeddedChannel channel = NetworkRegistry.INSTANCE.getChannel( "FORGE", Side.SERVER );
        DimensionRegisterMessage msg = new DimensionRegisterMessage( dimension.getId(), dimension.getWorldProviderId() );
        channel.attr( FMLOutboundHandler.FML_MESSAGETARGET ).set( FMLOutboundHandler.OutboundTarget.ALL );
        channel.writeOutbound( msg );
        */ /*
        this.dimByName.put( dimension.getName(), dimension );
	}*/
		
	
	
	/********************************************************************************
	 * Abstract - WorldSavedData
	 ********************************************************************************/
	
	@Override
	public void readFromNBT( NBTTagCompound comp ) 
	{
		try 
		{
			this.dimByName.clear();
			this.dimByName = NBTUtil.toDimensionMap( (NBTTagList)comp.getTag( ID ) );
		}
		catch ( NBTException e )
		{
			/**
			 * Log
			 */
			e.printStackTrace();
		}
	}

	@Override
	public void writeToNBT( NBTTagCompound comp )
	{
		comp.setTag( ID, NBTUtil.fromDimensionMap(dimByName ) );
	}
}
