package net.mcreator.minecraft.link.init;

import net.mcreator.minecraft.link.block.LinkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MCreatorLinkBlocks {

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, "mcreator_link");

    public static final RegistryObject<Block> LINK_BLOCK = REGISTRY.register("link", LinkBlock::new);

}
