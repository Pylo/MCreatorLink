package net.mcreator.minecraft.link.block;

import net.mcreator.minecraft.link.gui.GuiMCreatorLink;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class LinkBlock extends Block {

	public LinkBlock() {
		super(Block.Properties.create(Material.IRON));
		setRegistryName("link");
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult p_225533_6_) {

		if (worldIn.isRemote) {
			Minecraft.getInstance().displayGuiScreen(new GuiMCreatorLink(Minecraft.getInstance().currentScreen));
		}

		return super.onBlockActivated(state, worldIn, pos, player, handIn, p_225533_6_);
	}
}
