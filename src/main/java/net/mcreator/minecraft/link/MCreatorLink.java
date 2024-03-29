/*
 * Copyright 2019 Pylo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.mcreator.minecraft.link;

import net.mcreator.minecraft.link.command.CommandLink;
import net.mcreator.minecraft.link.command.LinkDeviceArgumentType;
import net.mcreator.minecraft.link.devices.arduino.ArduinoDetector;
import net.mcreator.minecraft.link.devices.raspberrypi.RaspberryPiDetector;
import net.mcreator.minecraft.link.init.MCreatorLinkArgumentTypes;
import net.mcreator.minecraft.link.init.MCreatorLinkBlocks;
import net.mcreator.minecraft.link.init.MCreatorLinkItems;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.apache.maven.artifact.versioning.ArtifactVersion;

/**
 * Main mod class for the MCreator Link Minecraft mod
 */
@Mod("mcreator_link") @Mod.EventBusSubscriber public class MCreatorLink {

	public static final DeviceManager LINK = new DeviceManager();

	public static ArtifactVersion VERSION;

    public MCreatorLink(IEventBus modEventBus) {
        LINK.registerDeviceDetector(new ArduinoDetector());
        LINK.registerDeviceDetector(new RaspberryPiDetector());

        VERSION = ModList.get().getModFileById("mcreator_link").getMods().get(0).getVersion();

        MCreatorLinkBlocks.REGISTRY.register(modEventBus);
        MCreatorLinkItems.REGISTRY.register(modEventBus);
        MCreatorLinkArgumentTypes.REGISTRY.register(modEventBus);
    }

    @SubscribeEvent
    public static void serverLoad(RegisterCommandsEvent event) {
        ArgumentTypeInfos.registerByClass(LinkDeviceArgumentType.class, MCreatorLinkArgumentTypes.LINK_DEVICE_ARGUMENT_INFO.get());

        event.getDispatcher().register(CommandLink.build());
    }

}
