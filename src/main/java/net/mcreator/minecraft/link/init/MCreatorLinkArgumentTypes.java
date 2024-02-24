package net.mcreator.minecraft.link.init;

import net.mcreator.minecraft.link.command.LinkDeviceArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MCreatorLinkArgumentTypes {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, "mcreator_link");

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<LinkDeviceArgumentType, SingletonArgumentInfo<LinkDeviceArgumentType>.Template>> LINK_DEVICE_ARGUMENT_INFO =
            REGISTRY.register("link_device", () -> SingletonArgumentInfo.contextFree(LinkDeviceArgumentType::new));

}
