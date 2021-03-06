package crafttweaker.api.world;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.*;
import crafttweaker.api.data.IData;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityArrow;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.entity.IEntityThrowable;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.util.Position3f;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("crafttweaker.world.IWorld")
@ZenRegister
public interface IWorld extends IBlockAccess {
    
    @ZenMethod
    int getBrightness(IBlockPos position);
    
    @ZenMethod
    int getBrightness(int x, int y, int z);
    
    @ZenMethod
    IBiome getBiome(Position3f position);
    
    @ZenMethod
    IBiome getBiome(IBlockPos position);
    
    @ZenGetter("worldInfo")
    @ZenMethod
    IWorldInfo getWorldInfo();
    
    @ZenGetter("remote")
    @ZenMethod
    boolean isRemote();
    
    @ZenGetter("raining")
    @ZenMethod
    boolean isRaining();
    
    @ZenGetter("dayTime")
    @ZenMethod
    boolean isDayTime();
    
    @ZenGetter("time")
    @ZenMethod
    long getWorldTime();
    
    @ZenGetter("surfaceWorld")
    @ZenMethod
    boolean isSurfaceWorld();
    
    @ZenGetter("moonPhase")
    @ZenMethod
    int getMoonPhase();
    
    @ZenGetter("dimension")
    @ZenMethod
    int getDimension();
    
    @ZenGetter("dimensionType")
    @ZenMethod
    String getDimensionType();
    
    @ZenMethod
    IBlock getBlock(int x, int y, int z);
    
    @ZenMethod
    IBlock getBlock(IBlockPos pos);
    
    @ZenGetter("worldType")
    @ZenMethod
    String getWorldType();
    
    @ZenMethod
    boolean setBlockState(IBlockState state, IBlockPos pos);

    @ZenMethod
    boolean setBlockState(IBlockState state, IData tileEntityData, IBlockPos pos);
    
    @ZenGetter("provider")
    @ZenMethod
    IWorldProvider getProvider();
    
    Object getInternal();
    
    @ZenMethod
    boolean spawnEntity(IEntity entity);
    
    @ZenMethod
    default IRayTraceResult rayTraceBlocks(IVector3d begin, IVector3d ray, @Optional boolean stopOnLiquid, @Optional boolean ignoreBlockWithoutBoundingBox, @Optional(valueBoolean = true) boolean returnLastUncollidableBlock) {
        CraftTweakerAPI.logError(this.getClass().getName() + " does not override IWorld.getRayTrace, tell the author to fix that.");
        return null;
    }
    
    @ZenMethod
    default List<IEntity> getEntitiesInArea(Position3f start, @Optional Position3f end) {
        CraftTweakerAPI.logError(this.getClass().getName() + " does not override IWorld.getEntitiesInArea, tell the author to fix that.");
        return new ArrayList<>();
    }

    @ZenMethod
    default IItemStack getPickedBlock(IBlockPos pos, IRayTraceResult rayTraceResult, IPlayer player) {
        CraftTweakerAPI.logError(this.getClass().getName() + " does not override IWorld.getPickedBlock!");
        return null;
    }
    
    @ZenMethod
    boolean extinguishFire(IPlayer player, IBlockPos pos, String side);
    
    @ZenMethod
    boolean isSpawnChunk(int x, int z);
    
    @ZenGetter("seaLevel")
    @ZenMethod
    int getSeaLevel();
    
    @ZenMethod
    boolean addWeatherEffect(IEntity entity);

    @ZenMethod
	IEntity createLightningBolt(double x, double y, double z, @Optional boolean effectOnly);
}
