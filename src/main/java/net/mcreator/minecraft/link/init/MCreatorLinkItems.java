package net.mcreator.minecraft.link.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD) public class MCreatorLinkItems {

	@ObjectHolder("mcreator_link:link") public static final Item LINK_BLOCK = null;

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(
				new BlockItem(MCreatorLinkBlocks.LINK_BLOCK, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS))
						.setRegistryName(MCreatorLinkBlocks.LINK_BLOCK.getRegistryName()));
	}

}
