package net.mcreator.minecraft.link.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MCreatorLinkItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, "mcreator_link");

    public static final RegistryObject<Item> LINK_BLOCK = REGISTRY.register("link",
            () -> new BlockItem(MCreatorLinkBlocks.LINK_BLOCK.get(), new Item.Properties()));

    @SubscribeEvent
    public static void buildContentsVanilla(CreativeModeTabEvent.BuildContents tabData) {
        if (tabData.getTab() == CreativeModeTabs.REDSTONE_BLOCKS)
            tabData.accept(MCreatorLinkBlocks.LINK_BLOCK.get());
    }

}
