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

package net.mcreator.minecraft.link.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.minecraft.link.MCreatorLink;
import net.mcreator.minecraft.link.devices.raspberrypi.RaspberryPi;
import net.mcreator.minecraft.link.devices.raspberrypi.RaspberryPiDetector;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT) public class GuiDirectLink extends Screen {

	private final Screen lastScreen;
	private EditBox ipTextField;

	private Button connect;

	GuiDirectLink(Screen lastScreenIn) {
		super(new TextComponent("Minecraft Link direct connect"));
		this.lastScreen = lastScreenIn;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(PoseStack);
		super.render(PoseStack, mouseX, mouseY, partialTicks);

		drawCenteredString(PoseStack, this.font, new TranslatableComponent("link.direct.title"), this.width / 2, 20,
				16777215);
		drawString(PoseStack, this.font, new TranslatableComponent("link.direct.field"), this.width / 2 - 100, 100,
				10526880);

		this.ipTextField.render(PoseStack, mouseX, mouseY, partialTicks);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override public void init() {
		super.init();

		if (this.minecraft != null)
			minecraft.keyboardHandler.setSendRepeatsToGui(true);

		this.addRenderableWidget(connect = new Button(this.width / 2 - 100, this.height / 4 + 96 + 12, 200, 20,
				new TranslatableComponent("link.direct.connect"), e -> {
			String device = this.ipTextField.getValue();
			RaspberryPi raspberryPi = RaspberryPiDetector.getRaspberryPiForIP(device);
			if (raspberryPi != null) {
				MCreatorLink.LINK.setConnectedDevice(raspberryPi);
				if (this.minecraft != null)
					this.minecraft.setScreen(this.lastScreen);
			} else {
				this.ipTextField.setTextColor(0xff5d4d);
			}
		}));
		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20,
				new TranslatableComponent("gui.cancel"), e -> {
			if (this.minecraft != null) {
				this.minecraft.setScreen(this.lastScreen);
			}
		}));

		this.ipTextField = new EditBox(this.font, this.width / 2 - 100, 116, 200, 20, new TextComponent(""));

		connect.active = !this.ipTextField.getValue().isEmpty() && this.ipTextField.getValue().split(":").length > 0;

		this.ipTextField.setMaxLength(128);
		this.addWidget(this.ipTextField);
		this.ipTextField.setFocus(true);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override public void onClose() {
		super.onClose();
		if (minecraft != null) {
			minecraft.keyboardHandler.setSendRepeatsToGui(false);
		}
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	@Override public boolean keyPressed(int typedChar, int keyCode, int par) {
		if (keyCode == 28 || keyCode == 156) {
			connect.onPress();
		} else if (this.ipTextField.keyPressed(typedChar, keyCode, par)) {
			this.ipTextField.setTextColor(0xffffff);
			connect.active = !this.ipTextField.getValue().isEmpty() && this.ipTextField.getValue().split(":").length > 0;
			return true;
		}

		connect.active = !this.ipTextField.getValue().isEmpty() && this.ipTextField.getValue().split(":").length > 0;
		return super.keyPressed(typedChar, keyCode, par);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override public void tick() {
		this.ipTextField.tick();
	}

}
