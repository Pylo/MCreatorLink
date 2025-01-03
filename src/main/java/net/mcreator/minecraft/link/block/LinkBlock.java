package net.mcreator.minecraft.link.block;

import net.mcreator.minecraft.link.gui.GuiMCreatorLink;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class LinkBlock extends Block {

    public LinkBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        if (worldIn.isClientSide()) {
            Minecraft.getInstance().setScreen(new GuiMCreatorLink(Minecraft.getInstance().screen));
        }

        return super.useWithoutItem(state, worldIn, pos, player, blockHitResult);
    }
}
