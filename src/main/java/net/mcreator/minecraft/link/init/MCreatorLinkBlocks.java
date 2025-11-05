package net.mcreator.minecraft.link.init;

import net.mcreator.minecraft.link.block.LinkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MCreatorLinkBlocks {

    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.Blocks.createBlocks("mcreator_link");

    public static final DeferredBlock<Block> LINK_BLOCK = REGISTRY.registerBlock("link", LinkBlock::new, BlockBehaviour.Properties::of);

}
