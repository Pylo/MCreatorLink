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
import net.mcreator.minecraft.link.devices.arduino.ArduinoDetector;
import net.mcreator.minecraft.link.devices.raspberrypi.RaspberryPiDetector;
import net.mcreator.minecraft.link.gui.ScreenEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Main mod class for the MCreator Link Minecraft mod
 */
@Mod("mcreator_link") public class MCreatorLink {

	public static DeviceManager LINK = new DeviceManager();

	public MCreatorLink() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new ScreenEventHandler());
	}

	private void init(FMLCommonSetupEvent event) {
		LINK.registerDeviceDetector(new ArduinoDetector());
		LINK.registerDeviceDetector(new RaspberryPiDetector());
	}

	@SubscribeEvent public void serverLoad(RegisterCommandsEvent event) {
		event.getDispatcher().register(CommandLink.build());
	}

}
