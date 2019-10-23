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
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.mcreator.minecraft.link.CurrentDevice;
import net.mcreator.minecraft.link.MCreatorLink;
import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.PinMode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
				c.getSource().sendErrorMessage(new TranslationTextComponent("link.command.nodevice"));
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
				}))))

		.then(Commands.literal("digitalwrite")
			.then(Commands.argument("pin", IntegerArgumentType.integer())
				.then(Commands.argument("value", IntegerArgumentType.integer()).executes(c -> {
					try {
						CurrentDevice.digitalWrite(c.getArgument("pin", Integer.class),
								c.getArgument("value", Integer.class).byteValue());
					} catch (Exception e) {
						c.getSource().sendErrorMessage(new TranslationTextComponent("link.command.wrongusage"));
					}
					return Command.SINGLE_SUCCESS;
				}))))

		.then(Commands.literal("analogwrite")
			.then(Commands.argument("pin", IntegerArgumentType.integer())
				.then(Commands.argument("value", IntegerArgumentType.integer()).executes(c -> {
					try {
						CurrentDevice.analogWrite(c.getArgument("pin", Integer.class),
								c.getArgument("value", Integer.class).byteValue());
					} catch (Exception e) {
						c.getSource().sendErrorMessage(new TranslationTextComponent("link.command.wrongusage"));
					}
					return Command.SINGLE_SUCCESS;
				}))))

		.then(Commands.literal("sendmessage")
			.then(Commands.argument("message", StringArgumentType.greedyString()).executes(c -> {
				try {
					CurrentDevice.sendMessage(c.getArgument("message", String.class));
				} catch (Exception e) {
					c.getSource().sendErrorMessage(new TranslationTextComponent("link.command.wrongusage"));
				}
				return Command.SINGLE_SUCCESS;
			})))

		.then(Commands.literal("digitalread")
			.then(Commands.argument("pin", IntegerArgumentType.integer()).executes(c -> {
				try {
				byte val = CurrentDevice.digitalRead(c.getArgument("pin", Integer.class));
				c.getSource().sendFeedback(new StringTextComponent(Byte.toString(val)), true);
			} catch (Exception e) {
				c.getSource().sendErrorMessage(new TranslationTextComponent("link.command.wrongusage"));
			}
				return Command.SINGLE_SUCCESS;
			})))

		.then(Commands.literal("analogread")
			.then(Commands.argument("pin", IntegerArgumentType.integer()).executes(c -> {
				try {
				short val = CurrentDevice.analogRead(c.getArgument("pin", Integer.class));
				c.getSource().sendFeedback(new StringTextComponent(Short.toString(val)), true);
			} catch (Exception e) {
				c.getSource().sendErrorMessage(new TranslationTextComponent("link.command.wrongusage"));
			}
				return Command.SINGLE_SUCCESS;
			})))

		;
		// @formatter:on
	}

}
