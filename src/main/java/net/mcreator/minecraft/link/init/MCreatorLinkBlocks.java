package net.mcreator.minecraft.link.init;

import net.mcreator.minecraft.link.block.LinkBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MCreatorLinkBlocks {

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK, "mcreator_link");

    public static final DeferredHolder<Block, Block> LINK_BLOCK = REGISTRY.register("link", LinkBlock::new);

}
