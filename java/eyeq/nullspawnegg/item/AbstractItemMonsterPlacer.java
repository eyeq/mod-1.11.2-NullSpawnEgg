package eyeq.nullspawnegg.item;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class AbstractItemMonsterPlacer extends ItemMonsterPlacer {

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        ItemStack itemStack = player.getHeldItem(hand);
        if(!player.canPlayerEdit(pos.offset(facing), facing, itemStack)) {
            return EnumActionResult.FAIL;
        }

        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() == Blocks.MOB_SPAWNER) {
            TileEntity tileentity = world.getTileEntity(pos);
            if(!(tileentity instanceof TileEntityMobSpawner)) {
                return EnumActionResult.SUCCESS;
            }
            MobSpawnerBaseLogic logic = ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic();
            logic.setEntityId(getNamedIdFrom(itemStack));
            tileentity.markDirty();
            world.notifyBlockUpdate(pos, state, state, 3);
        } else {
            pos = pos.offset(facing);
            double d0 = 0.0;
            if(facing == EnumFacing.UP && state.getBlock() instanceof BlockFence) {
                d0 = 0.5;
            }
            // Changed
            Entity entity = this.spawnEntity(world, getNamedIdFrom(itemStack), pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D);
            if(entity == null) {
                return EnumActionResult.SUCCESS;
            }
            if(entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
                entity.setCustomNameTag(itemStack.getDisplayName());
            }
            applyItemEntityDataToEntity(world, player, itemStack, entity);
        }
        if(!player.isCreative()) {
            itemStack.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if(world.isRemote) {
            return new ActionResult<>(EnumActionResult.PASS, itemStack);
        }
        RayTraceResult moving = this.rayTrace(world, player, true);
        if(moving == null || moving.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(EnumActionResult.PASS, itemStack);
        }

        BlockPos pos = moving.getBlockPos();
        if(!(world.getBlockState(pos).getBlock() instanceof BlockLiquid)) {
            return new ActionResult<>(EnumActionResult.PASS, itemStack);
        }
        if(!world.isBlockModifiable(player, pos) || !player.canPlayerEdit(pos, moving.sideHit, itemStack)) {
            return new ActionResult<>(EnumActionResult.FAIL, itemStack);
        }

        // Changed
        Entity entity = this.spawnEntity(world, getNamedIdFrom(itemStack), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        if(entity == null) {
            return new ActionResult<>(EnumActionResult.PASS, itemStack);
        }
        if(entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
            entity.setCustomNameTag(itemStack.getDisplayName());
        }
        applyItemEntityDataToEntity(world, player, itemStack, entity);

        if(!player.isCreative()) {
            itemStack.shrink(1);
        }
        player.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    public abstract Entity spawnEntity(World world, ResourceLocation id, double x, double y, double z);
}
