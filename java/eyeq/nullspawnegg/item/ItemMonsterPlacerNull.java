package eyeq.nullspawnegg.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class ItemMonsterPlacerNull extends AbstractItemMonsterPlacer {
    @Override
    public Entity spawnEntity(World world, ResourceLocation id, double x, double y, double z) {
        Entity entity = EntityList.createEntityByIDFromName(id, world);
        if(entity == null) {
            return null;
        }
        entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
        if(entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            entityLivingBase.rotationYawHead = entityLivingBase.rotationYaw;
            entityLivingBase.renderYawOffset = entityLivingBase.rotationYaw;
            if(entity instanceof EntityLiving) {
                EntityLiving entityLiving = (EntityLiving) entity;
                entityLiving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityLiving)), null);
                entityLiving.playLivingSound();
            }
        }
        world.spawnEntity(entity);
        return entity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        Set<ResourceLocation> registered = EntityList.ENTITY_EGGS.keySet();
        EntityList.getEntityNameList().stream().filter(name -> !registered.contains(name)).forEach(id -> {
            ItemStack itemStack = new ItemStack(item, 1);
            applyEntityIdToItemStack(itemStack, id);
            subItems.add(itemStack);
        });
    }
}
