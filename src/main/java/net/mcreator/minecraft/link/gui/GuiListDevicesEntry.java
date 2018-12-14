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
import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.arduino.Arduino;
import net.mcreator.minecraft.link.devices.raspberrypi.RaspberryPi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT) public class GuiListDevicesEntry implements GuiListExtended.IGuiListEntry {

	private static final ResourceLocation DEVICE_ARDUINO = new ResourceLocation(MinecraftLink.MODID,
			"textures/arduino.png");
	private static final ResourceLocation DEVICE_RASPBERRYPI = new ResourceLocation(MinecraftLink.MODID,
			"textures/raspberrypi.png");

	private final Minecraft client;
	private final GuiListDevices containingListSel;
	private long lastClickTime;

	private AbstractDevice device;

	GuiListDevicesEntry(GuiListDevices listWorldSelIn, AbstractDevice device) {
		this.containingListSel = listWorldSelIn;
		this.client = Minecraft.getMinecraft();
		this.device = device;
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
			boolean isSelected, float partialTicks) {
		String s2 = "Status: ";

		if (device.isConnected())
			s2 += TextFormatting.GREEN + "CONNECTED" + TextFormatting.RESET;
		else
			s2 += TextFormatting.GRAY + "AVAILABLE" + TextFormatting.RESET;

		this.client.fontRenderer.drawString(device.getName(), x + 32 + 8, y + 1, 16777215);
		this.client.fontRenderer
				.drawString(device.getDescription(), x + 32 + 8, y + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
		this.client.fontRenderer.drawString(s2, x + 32 + 8,
				y + this.client.fontRenderer.FONT_HEIGHT + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (device instanceof Arduino) {
			this.client.getTextureManager().bindTexture(DEVICE_ARDUINO);
			GlStateManager.enableBlend();
			Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
			GlStateManager.disableBlend();
		} else if (device instanceof RaspberryPi) {
			this.client.getTextureManager().bindTexture(DEVICE_RASPBERRYPI);
			GlStateManager.enableBlend();
			Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
			GlStateManager.disableBlend();
		}

		if (this.client.gameSettings.touchscreen || isSelected) {
			Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
		}
	}

	/**
	 * Called when the mouse is clicked within this entry. Returning true means that something within this entry was
	 * clicked and the list should not be dragged.
	 */
	@Override public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX,
			int relativeY) {
		this.containingListSel.selectDevice(slotIndex);
		if (relativeX <= 32 && relativeX < 32) { // clicked on icon
			if (!device.isConnected())
				MinecraftLink.LINK.setConnectedDevice(device);
			else
				MinecraftLink.LINK.disconnectDevice(device);
			this.containingListSel.refreshList();
			return true;
		} else if (Minecraft.getSystemTime() - this.lastClickTime < 250L) { // double clicked
			if (!device.isConnected())
				MinecraftLink.LINK.setConnectedDevice(device);
			else
				MinecraftLink.LINK.disconnectDevice(device);
			this.containingListSel.refreshList();
			return true;
		} else {
			this.lastClickTime = Minecraft.getSystemTime();
			return false;
		}
	}

	AbstractDevice getDevice() {
		return device;
	}

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	@Override public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
	}

	@Override public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
	}
}
