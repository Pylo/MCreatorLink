package net.mcreator.minecraft.link.block;

import net.mcreator.minecraft.link.gui.GuiMCreatorLink;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class LinkBlock extends Block {

	public LinkBlock() {
		super(BlockBehaviour.Properties.of());
	}

	@SuppressWarnings("deprecation") @Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
			BlockHitResult blockHitResult) {

		if (worldIn.isClientSide()) {
			Minecraft.getInstance().setScreen(new GuiMCreatorLink(Minecraft.getInstance().screen));
		}

		return super.use(state, worldIn, pos, player, handIn, blockHitResult);
	}
}
