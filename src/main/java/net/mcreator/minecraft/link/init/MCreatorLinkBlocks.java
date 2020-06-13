package net.mcreator.minecraft.link.init;

import net.mcreator.minecraft.link.block.LinkBlock;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD) public class MCreatorLinkBlocks {

	@ObjectHolder("mcreator_link:link") public static final Block LINK_BLOCK = null;

	@SubscribeEvent public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new LinkBlock());
	}

}
