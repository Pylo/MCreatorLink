package net.mcreator.minecraft.link.init;

import net.mcreator.minecraft.link.command.LinkDeviceArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MCreatorLinkArgumentTypes {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> REGISTRY = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, "mcreator_link");

    public static final RegistryObject<ArgumentTypeInfo<LinkDeviceArgumentType, SingletonArgumentInfo<LinkDeviceArgumentType>.Template>> LINK_DEVICE_ARGUMENT_INFO =
            REGISTRY.register("link_device", () -> SingletonArgumentInfo.contextFree(LinkDeviceArgumentType::new));

}
