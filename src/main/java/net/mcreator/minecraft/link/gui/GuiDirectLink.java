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

package net.mcreator.minecraft.link.gui;

import net.mcreator.minecraft.link.MCreatorLink;
import net.mcreator.minecraft.link.devices.raspberrypi.RaspberryPi;
import net.mcreator.minecraft.link.devices.raspberrypi.RaspberryPiDetector;
import net.minecraft.client.gui.Button;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@OnlyIn(Dist.CLIENT) public class GuiDirectLink extends Screen {

	private final Screen lastScreen;
	private GuiTextField ipTextField;

	GuiDirectLink(Screen lastScreenIn) {
		this.lastScreen = lastScreenIn;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.ipTextField.updateCursorCounter();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, I18n.format("link.direct.title"), this.width / 2, 20, 16777215);
		this.drawString(this.fontRenderer, I18n.format("link.direct.field"), this.width / 2 - 100, 100, 10526880);
		this.ipTextField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new Button(0, this.width / 2 - 100, this.height / 4 + 96 + 12,
				I18n.format("link.direct.connect")));
		this.buttonList
				.add(new Button(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
		this.ipTextField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, 116, 200, 20);
		this.ipTextField.setMaxStringLength(128);
		this.ipTextField.setFocused(true);
		(this.buttonList.get(0)).enabled =
				!this.ipTextField.getText().isEmpty() && this.ipTextField.getText().split(":").length > 0;
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	protected void actionPerformed(Button button) {
		if (button.enabled) {
			if (button.id == 1) {
				this.mc.displayScreen(this.lastScreen);
			} else if (button.id == 0) {
				String device = this.ipTextField.getText();
				RaspberryPi raspberryPi = RaspberryPiDetector.getRaspberryPiForIP(device);
				if (raspberryPi != null) {
					MCreatorLink.LINK.setConnectedDevice(raspberryPi);
					this.mc.displayScreen(this.lastScreen);
				} else {
					this.ipTextField.setTextColor(0xff5d4d);
				}
			}
		}
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) {
		if (this.ipTextField.textboxKeyTyped(typedChar, keyCode)) {
			this.ipTextField.setTextColor(0xffffff);
			(this.buttonList.get(0)).enabled =
					!this.ipTextField.getText().isEmpty() && this.ipTextField.getText().split(":").length > 0;
		} else if (keyCode == 28 || keyCode == 156) {
			this.actionPerformed(this.buttonList.get(0));
		}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.ipTextField.mouseClicked(mouseX, mouseY, mouseButton);
	}

}
