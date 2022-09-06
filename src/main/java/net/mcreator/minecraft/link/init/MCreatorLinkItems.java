package net.mcreator.minecraft.link.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MCreatorLinkItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, "mcreator_link");

    public static final RegistryObject<Item> LINK_BLOCK = REGISTRY.register("link",
            () -> new BlockItem(MCreatorLinkBlocks.LINK_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

}
