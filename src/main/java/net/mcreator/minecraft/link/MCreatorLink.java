/*
 * Copyright 2018 Pylo
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Main mod class for the MCreator Link Minecraft mod
 */
@Mod(modid = MCreatorLink.MODID, name = MCreatorLink.NAME, version = MCreatorLink.VERSION)
public class MCreatorLink {

	public static final String MODID = "mcreator_link";
	public static final String NAME = "MCreator Link";
	public static final String VERSION = "1.0";

	public static DeviceManager LINK = new DeviceManager();

	@SideOnly(Side.CLIENT) @EventHandler public void preInit(FMLPreInitializationEvent event) {
		LINK.registerDeviceDetector(new ArduinoDetector());
		LINK.registerDeviceDetector(new RaspberryPiDetector());
	}

	@EventHandler public void start(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandLink());
	}

}
