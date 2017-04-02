package eyeq.nullspawnegg;

import eyeq.nullspawnegg.item.ItemMonsterPlacerNull;
import eyeq.nullspawnegg.item.ItemMonsterPlacerRandom;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.creativetab.UCreativeTab;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

import static eyeq.nullspawnegg.NullSpawnEgg.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class NullSpawnEgg {
    public static final String MOD_ID = "eyeq_nullspawnegg";

    @Mod.Instance(MOD_ID)
    public static NullSpawnEgg instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static final CreativeTabs TAB_NULL_SPAWN_EGG = new UCreativeTab("NullSpawnEgg", () -> new ItemStack(Items.SPAWN_EGG));

    public static Item spawnEggRandom;
    public static Item spawnEggNull;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        createFiles();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if(event.getSide().isServer()) {
            return;
        }
        registerItemColors();
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        spawnEggRandom = new ItemMonsterPlacerRandom().setUnlocalizedName("spawnEggRandom");
        spawnEggNull = new ItemMonsterPlacerNull().setUnlocalizedName("spawnEgg").setCreativeTab(TAB_NULL_SPAWN_EGG);

        GameRegistry.register(spawnEggRandom, resource.createResourceLocation("random_spawn_egg"));
        GameRegistry.register(spawnEggNull, resource.createResourceLocation("null_spawn_egg"));
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        ModelLoader.setCustomModelResourceLocation(spawnEggRandom, 0, ResourceLocationFactory.createModelResourceLocation(Items.SPAWN_EGG));
        ModelLoader.setCustomModelResourceLocation(spawnEggNull, 0, ResourceLocationFactory.createModelResourceLocation(Items.SPAWN_EGG));
    }

    public static void createFiles() {
        File project = new File("../1.11.2-NullSpawnEgg");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, TAB_NULL_SPAWN_EGG, "Null SpawnEgg");

        language.register(LanguageResourceManager.EN_US, spawnEggRandom, "Spawn Random");
        language.register(LanguageResourceManager.JA_JP, spawnEggRandom, "スポーン ランダム");

        ULanguageCreator.createLanguage(project, MOD_ID, language);
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemColors() {
        ItemColors itemColors = FMLClientHandler.instance().getClient().getItemColors();
        itemColors.registerItemColorHandler((stack, tintIndex) -> (tintIndex == 0 ? 0xFF6A00 : 0xF3DF73), spawnEggRandom);
    }
}
