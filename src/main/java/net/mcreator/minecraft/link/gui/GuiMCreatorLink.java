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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;

@OnlyIn(Dist.CLIENT) public class GuiMCreatorLink extends Screen {

	private Screen prevScreen;
	private Button connectButton;
	private Button disconnectButton;

	private GuiListDevices selectionList;

	private int ticks = 0;

	GuiListDevicesEntry entry;

	GuiMCreatorLink(Screen screenIn) {
		this.prevScreen = screenIn;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override public void initGui() {
		this.selectionList = new GuiListDevices(this, this.mc, this.width, this.height, 32, this.height - 42, 36);

		this.connectButton = this.addButton(
				new Button(1, this.width / 2 - 154, this.height - 32, 72, 20, I18n.format("link.menu.connect")));
		this.disconnectButton = this.addButton(
				new Button(2, this.width / 2 - 76, this.height - 32, 72, 20, I18n.format("link.menu.disconnect")));

		this.addButton(new Button(3, this.width / 2 + 2, this.height - 32, 72, 20, I18n.format("link.menu.direct")));
		this.addButton(new Button(0, this.width / 2 + 82, this.height - 32, 72, 20, I18n.format("gui.done")));

		this.addButton(new Button(4, this.width / 2 + 82 + 55, 6, 20, 20, "?"));

		this.disconnectButton.enabled = false;
		this.connectButton.enabled = false;
	}

	/**
	 * Handles mouse input.
	 */
	@Override public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.selectionList.handleMouseInput();
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override protected void actionPerformed(Button button) {
		if (button.enabled) {
			GuiListDevicesEntry selected = this.selectionList.getSelectedDevice();
			if (button.id == 1) {
				if (selected != null) {
					MCreatorLink.LINK.setConnectedDevice(selected.getDevice());
					this.selectionList.refreshList();
				}
			} else if (button.id == 2) {
				if (selected != null) {
					MCreatorLink.LINK.disconnectDevice(selected.getDevice());
					this.selectionList.refreshList();
				}
			} else if (button.id == 0) {
				this.mc.displayScreen(this.prevScreen);
			} else if (button.id == 3) {
				this.mc.displayScreen(new GuiDirectLink(this));
			} else if (button.id == 4) {
				try {
					Class<?> oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop").invoke(null);
					oclass.getMethod("browse", URI.class).invoke(object, new URI("https://mcreator.net/link"));
				} catch (Throwable ignored) {
				}
			}
		}
	}

	private static final ResourceLocation LOGO = new ResourceLocation(MCreatorLink.MODID, "textures/logo_small.png");

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ticks++;

		if (ticks % 50 == 0)
			this.selectionList.refreshList();

		this.selectionList.drawScreen(mouseX, mouseY, partialTicks);

		Minecraft.getInstance().getTextureManager().bindTexture(LOGO);
		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 50, 8, 0.0F, 0.0F, 100, 16, 100.0F, 16);
		GlStateManager.disableBlend();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.selectionList.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Called when a mouse button is released.
	 */
	@Override protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		this.selectionList.mouseReleased(mouseX, mouseY, state);
	}

	void setSelectDevice(@Nullable GuiListDevicesEntry entry) {
		this.entry = entry;
		if (this.connectButton != null) {
			if (entry != null) {
				if (entry.getDevice().isConnected()) {
					this.disconnectButton.enabled = true;
					this.connectButton.enabled = false;
				} else {
					this.disconnectButton.enabled = false;
					this.connectButton.enabled = true;
				}
			} else {
				this.disconnectButton.enabled = false;
				this.connectButton.enabled = false;
			}
			if (MCreatorLink.LINK.getConnectedDevice() != null)
				this.connectButton.enabled = false;
		}
	}
}
