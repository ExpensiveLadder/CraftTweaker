package crafttweaker.mc1120.world;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.block.*;
import crafttweaker.api.data.IData;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityArrow;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.entity.IEntityThrowable;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.util.Position3f;
import crafttweaker.api.world.*;
import crafttweaker.mc1120.data.NBTConverter;
import crafttweaker.mc1120.entity.MCEntity;
import crafttweaker.mc1120.entity.MCEntityThrowable;
import crafttweaker.mc1120.util.MCPosition3f;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import stanhebben.zenscript.annotations.Optional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Stan
 */
public class MCWorld extends MCBlockAccess implements IWorld {

    private final World world;

    public MCWorld(World world) {
        super(world);
        this.world = world;
    }

    @Override
    public int getBrightness(int x, int y, int z) {
        return world.getLight(new BlockPos(x, y, z));
    }
    
    @Override
    public int getBrightness(IBlockPos pos) {
        return world.getLight((BlockPos)pos.getInternal());
    }

    @Override
    public IBlock getBlock(int x, int y, int z) {
        return CraftTweakerMC.getBlock(world, x, y, z);
    }
    
    @Override
    public IBlock getBlock(IBlockPos pos) {
        return CraftTweakerMC.getBlock(world, pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public IBiome getBiome(Position3f position) {
    	return getBiome(position.asBlockPos());
    }
    
    @Override
    public IBiome getBiome(IBlockPos position) {
    	return new MCBiome(world.getBiome((BlockPos) position.getInternal()));
    }

	@Override
	public IWorldInfo getWorldInfo() {
		return new MCWorldInfo(world.getWorldInfo());
	}

	@Override
	public boolean isRemote() {
		return world.isRemote;
	}

	@Override
	public boolean isRaining() {
		return world.isRaining();
	}

	@Override
	public boolean isDayTime() {
		return world.isDaytime();
	}

	@Override
	public boolean isSurfaceWorld() {
		return world.provider.isSurfaceWorld();
	}

	@Override
	public long getWorldTime() {
		return world.getTotalWorldTime();
	}

	@Override
	public int getMoonPhase() {
		return world.getMoonPhase();
	}

	@Override
	public int getDimension() {
		return world.provider.getDimension();
	}

	@Override
	public String getDimensionType() {
		return world.provider.getDimensionType().getName();
	}

	@Override
	public String getWorldType() {
		return world.getWorldType().getName();
	}
	
	@Override
	public Object getInternal() {
		return world;
	}
    
    @Override
    public boolean spawnEntity(IEntity entity) {
        return world.spawnEntity(CraftTweakerMC.getEntity(entity));
    }
    
    @Override
    public IRayTraceResult rayTraceBlocks(IVector3d begin, IVector3d ray, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        return CraftTweakerMC.getIRayTraceResult(this.world.rayTraceBlocks(CraftTweakerMC.getVec3d(begin), CraftTweakerMC.getVec3d(ray), stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock));
    }
    
    @Override
    public List<IEntity> getEntitiesInArea(Position3f start, @Optional Position3f end) {
        if(end == null) {
            end = new MCPosition3f(start.getX() + 1, start.getY() + 1, start.getZ() + 1);
        }
        return this.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(((BlockPos) start.asBlockPos().getInternal()), ((BlockPos) end.asBlockPos().getInternal()))).stream().map(MCEntity::new).collect(Collectors.toList());
    }
    
    @Override
	public boolean setBlockState(IBlockState state, IBlockPos pos) {
		return world.setBlockState((BlockPos)pos.getInternal(), (net.minecraft.block.state.IBlockState)state.getInternal());
	}

	@Override
	public boolean setBlockState(IBlockState state, IData tileEntityData, IBlockPos pos) {
		boolean placed = world.setBlockState((BlockPos)pos.getInternal(), (net.minecraft.block.state.IBlockState)state.getInternal());
		if (tileEntityData != null) {
			NBTTagCompound tileEntityNBT = (NBTTagCompound) NBTConverter.from(tileEntityData);
			if (!tileEntityNBT.hasNoTags()) {
				TileEntity tileEntity = world.getTileEntity((BlockPos) pos.getInternal());
				if (tileEntity != null) {
					NBTTagCompound currentNBT = tileEntity.writeToNBT(new NBTTagCompound());
					NBTTagCompound originalNBT = currentNBT.copy();
					currentNBT.merge(tileEntityNBT.copy());
					currentNBT.setInteger("x", pos.getX());
					currentNBT.setInteger("y", pos.getY());
					currentNBT.setInteger("z", pos.getZ());
					if (!currentNBT.equals(originalNBT)) {
						tileEntity.readFromNBT(currentNBT);
						tileEntity.markDirty();
					}
				} else {
					CraftTweakerAPI.logInfo("No tile entity found when placing block.");
				}
			}
		}

		return placed;
	}

	@Override
	public IWorldProvider getProvider() {
		return new MCWorldProvider(world.provider);
	}

	@Override
	public IItemStack getPickedBlock(IBlockPos pos, IRayTraceResult rayTraceResult, IPlayer player) {
		BlockPos blockPos = CraftTweakerMC.getBlockPos(pos);
		net.minecraft.block.state.IBlockState state = world.getBlockState(blockPos);
		ItemStack stack = state.getBlock().getPickBlock(state, CraftTweakerMC.getRayTraceResult(rayTraceResult), world, blockPos, CraftTweakerMC.getPlayer(player));
		return CraftTweakerMC.getIItemStack(stack);
	}

	@Override
	public boolean extinguishFire(IPlayer player, IBlockPos pos, String side) {
		return world.extinguishFire(CraftTweakerMC.getPlayer(player), CraftTweakerMC.getBlockPos(pos), EnumFacing.valueOf("side"));
	}

	@Override
	public boolean isSpawnChunk(int x, int z) {
		return world.isSpawnChunk(x, z) ;
	}


	@Override
	public int getSeaLevel() {
		return world.getSeaLevel();
	}
	
	@Override
	public IEntity createLightningBolt(double x, double y, double z, boolean effectOnly) {
		EntityLightningBolt bolt = new EntityLightningBolt(world, x, y, z, effectOnly);
		return CraftTweakerMC.getIEntity(bolt);
	}
	
	@Override
	public boolean addWeatherEffect(IEntity entity) {
		return world.addWeatherEffect(CraftTweakerMC.getEntity(entity));
	}
}