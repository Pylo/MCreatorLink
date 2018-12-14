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

import net.mcreator.minecraft.link.MinecraftLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.IOException;

@SideOnly(Side.CLIENT) public class GuiMinecraftLink extends GuiScreen {

	private GuiScreen prevScreen;
	private GuiButton connectButton;
	private GuiButton disconnectButton;

	private GuiListDevices selectionList;

	private int ticks = 0;

	GuiListDevicesEntry entry;

	GuiMinecraftLink(GuiScreen screenIn) {
		this.prevScreen = screenIn;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override public void initGui() {
		this.selectionList = new GuiListDevices(this, this.mc, this.width, this.height, 32, this.height - 42, 36);

		this.connectButton = this.addButton(
				new GuiButton(1, this.width / 2 - 154, this.height - 32, 72, 20, I18n.format("link.menu.connect")));
		this.disconnectButton = this.addButton(
				new GuiButton(2, this.width / 2 - 76, this.height - 32, 72, 20, I18n.format("link.menu.disconnect")));

		this.addButton(new GuiButton(0, this.width / 2 + 82, this.height - 32, 72, 20, I18n.format("gui.done")));

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
	@Override protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			GuiListDevicesEntry selected = this.selectionList.getSelectedDevice();
			if (button.id == 1) {
				if (selected != null) {
					MinecraftLink.LINK.setConnectedDevice(selected.getDevice());
					this.selectionList.refreshList();
				}
			} else if (button.id == 2) {
				if (selected != null) {
					MinecraftLink.LINK.disconnectDevice(selected.getDevice());
					this.selectionList.refreshList();
				}
			} else if (button.id == 0) {
				this.mc.displayGuiScreen(this.prevScreen);
			}
		}
	}

	private static final ResourceLocation LOGO = new ResourceLocation(MinecraftLink.MODID, "textures/logo_small.png");

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ticks++;

		if (ticks % 50 == 0)
			this.selectionList.refreshList();

		this.selectionList.drawScreen(mouseX, mouseY, partialTicks);

		Minecraft.getMinecraft().getTextureManager().bindTexture(LOGO);
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
			if (MinecraftLink.LINK.getConnectedDevice() != null)
				this.connectButton.enabled = false;
		}
	}
}
