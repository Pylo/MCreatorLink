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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.mcreator.minecraft.link.CurrentDevice;
import net.mcreator.minecraft.link.MCreatorLink;
import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.PinMode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.command.TextComponentHelper;

import java.util.Arrays;

/**
 * Class that defines the behaviour of the /link command in Minecraft.
 */
public class CommandLink {

	public static LiteralArgumentBuilder<CommandSource> build() {
		// @formatter:off
		return LiteralArgumentBuilder.<CommandSource>literal("link")

		.then(Commands.literal("device").executes(c -> {
			AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
			if (device != null) {
				c.getSource().sendFeedback(new StringTextComponent(device.getName() + " - " + device.getDescription()),
						true);
				c.getSource().sendFeedback(new StringTextComponent(
						"Digital pins: " + device.getDigitalPinsCount() + ", Analog pins: " + device
								.getAnalogPinsCount()), true);
			} else {
				c.getSource().sendFeedback(new TranslationTextComponent("link.command.nodevice"), true);
			}
			return Command.SINGLE_SUCCESS;
		}))

		.then(Commands.literal("pinmode")
			.then(Commands.argument("pin", IntegerArgumentType.integer())
				.then(Commands.literal("output").executes(c -> {
					CurrentDevice.pinMode(c.getArgument("pin", Integer.class), PinMode.OUT);
					return Command.SINGLE_SUCCESS;
				})).then(Commands.literal("input").executes(c -> {
					CurrentDevice.pinMode(c.getArgument("pin", Integer.class), PinMode.IN);
					return Command.SINGLE_SUCCESS;
				})).then(Commands.literal("input_pullup").executes(c -> {
					CurrentDevice.pinMode(c.getArgument("pin", Integer.class), PinMode.IN_P);
					return Command.SINGLE_SUCCESS;
				}))));
		// @formatter:on
	}

	/*@Override public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		switch (args[0]) {
		case "digitalwrite":
		case "dw":
			try {
				int pin = Integer.parseInt(args[1]);
				byte val = Byte.parseByte(args[2]);
				CurrentDevice.digitalWrite(pin, val);
			} catch (Exception e) {
				sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
			}
			return;
		case "analogwrite":
		case "aw":
			try {
				int pin = Integer.parseInt(args[1]);
				short val = Short.parseShort(args[2]);
				CurrentDevice.analogWrite(pin, val);
			} catch (Exception e) {
				sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
			}
			return;
		case "sendmessage":
		case "sm":
			try {

				String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
				CurrentDevice.sendMessage(message);
			} catch (Exception e) {
				sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
			}
			return;
		case "digitalread":
		case "dr":
			try {
				int pin = Integer.parseInt(args[1]);
				byte val = CurrentDevice.digitalRead(pin);
				sender.sendMessage(new TextComponentString(Byte.toString(val)));
			} catch (Exception e) {
				sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
			}
			return;
		case "analogread":
		case "ar":
			try {
				int pin = Integer.parseInt(args[1]);
				short val = CurrentDevice.analogRead(pin);
				sender.sendMessage(new TextComponentString(Short.toString(val)));
			} catch (Exception e) {
				sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "link.command.wrongusage"));
			}
			return;
		}
	}*/

}
