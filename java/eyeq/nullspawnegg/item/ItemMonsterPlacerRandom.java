package eyeq.nullspawnegg.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMonsterPlacerRandom extends AbstractItemMonsterPlacer {
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return ("" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name")).trim();
    }

    @Override
    public Entity spawnEntity(World world, ResourceLocation id, double x, double y, double z) {
        ResourceLocation[] keys = EntityList.ENTITY_EGGS.keySet().toArray(new ResourceLocation[EntityList.ENTITY_EGGS.size()]);
        ResourceLocation key = keys[itemRand.nextInt(keys.length)];
        return spawnCreature(world, key, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList subItems) {
        subItems.add(new ItemStack(item));
    }
}
