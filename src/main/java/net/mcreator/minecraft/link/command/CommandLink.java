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

package net.mcreator.minecraft.link.command;

import net.mcreator.minecraft.link.CurrentDevice;
import net.mcreator.minecraft.link.MCreatorLink;
import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.PinMode;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.TextComponentHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class that defines the behaviour of the /link command in Minecraft.
 */
public class CommandLink extends CommandBase {

	@Override public String getName() {
		return "link";
	}

	@Override public List<String> getAliases() {
		return Collections.singletonList("l");
	}

	@Override public String getUsage(ICommandSender sender) {
		return "link.command.usage";
	}

	@Override public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if (server.isDedicatedServer()) {
			sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "link.command.server"));
			return;
		} else if (args.length > 0) {
			switch (args[0]) {
			case "device":
			case "d":
				AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
				if (device != null) {
					sender.sendMessage(new TextComponentString(device.getName() + " - " + device.getDescription()));
					sender.sendMessage(new TextComponentString(
							"Digital pins: " + device.getDigitalPinsCount() + ", Analog pins: " + device
									.getAnalogPinsCount()));
				} else {
					sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "link.command.nodevice"));
				}
				return;
			case "pinmode":
			case "pm":
				try {
					int pin = Integer.parseInt(args[1]);
					PinMode pinMode = PinMode.fromString(args[2]);
					CurrentDevice.pinMode(pin, pinMode);
				} catch (Exception e) {
					sender.sendMessage(
							TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
				}
				return;
			case "digitalwrite":
			case "dw":
				try {
					int pin = Integer.parseInt(args[1]);
					byte val = Byte.parseByte(args[2]);
					CurrentDevice.digitalWrite(pin, val);
				} catch (Exception e) {
					sender.sendMessage(
							TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
				}
				return;
			case "analogwrite":
			case "aw":
				try {
					int pin = Integer.parseInt(args[1]);
					short val = Short.parseShort(args[2]);
					CurrentDevice.analogWrite(pin, val);
				} catch (Exception e) {
					sender.sendMessage(
							TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
				}
				return;
			case "sendmessage":
			case "sm":
				try {

					String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
					CurrentDevice.sendMessage(message);
				} catch (Exception e) {
					sender.sendMessage(
							TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
				}
				return;
			case "digitalread":
			case "dr":
				try {
					int pin = Integer.parseInt(args[1]);
					byte val = CurrentDevice.digitalRead(pin);
					sender.sendMessage(new TextComponentString(Byte.toString(val)));
				} catch (Exception e) {
					sender.sendMessage(
							TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
				}
				return;
			case "analogread":
			case "ar":
				try {
					int pin = Integer.parseInt(args[1]);
					short val = CurrentDevice.analogRead(pin);
					sender.sendMessage(new TextComponentString(Short.toString(val)));
				} catch (Exception e) {
					sender.sendMessage(
							TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
				}
				return;
			}
		}
		sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, getUsage(sender)));
	}

	@Override public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		return Collections.emptyList();
	}

}
