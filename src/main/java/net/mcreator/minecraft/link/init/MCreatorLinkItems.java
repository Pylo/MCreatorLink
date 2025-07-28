package net.mcreator.minecraft.link.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class MCreatorLinkItems {

    public static final DeferredRegister.Items REGISTRY = DeferredRegister.Items.createItems("mcreator_link");

    public static final DeferredItem<BlockItem> LINK_BLOCK = REGISTRY.registerSimpleBlockItem("link", MCreatorLinkBlocks.LINK_BLOCK);

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
            event.accept(MCreatorLinkBlocks.LINK_BLOCK.get());
    }

}
