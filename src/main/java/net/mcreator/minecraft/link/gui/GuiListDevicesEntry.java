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

import com.mojang.blaze3d.platform.GlStateManager;
import net.mcreator.minecraft.link.MCreatorLink;
import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.arduino.Arduino;
import net.mcreator.minecraft.link.devices.raspberrypi.RaspberryPi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT) public class GuiListDevicesEntry extends ExtendedList.AbstractListEntry<GuiListDevicesEntry> {

	private static final ResourceLocation DEVICE_ARDUINO = new ResourceLocation("mcreator_link" ,
			"textures/arduino.png" );
	private static final ResourceLocation DEVICE_RASPBERRYPI = new ResourceLocation("mcreator_link" ,
			"textures/raspberrypi.png" );

	protected final Minecraft client;
	private final GuiListDevices containingListSel;
	private long lastClickTime;

	private AbstractDevice device;

	GuiListDevicesEntry(GuiListDevices listWorldSelIn, AbstractDevice device) {
		this.containingListSel = listWorldSelIn;
		this.client = listWorldSelIn.guiMCreatorLink.getMinecraft();
		this.device = device;
	}

	@Override
	public void render(int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
		String s2 = "Status: ";

		if (device.isConnected())
			s2 += TextFormatting.GREEN + "CONNECTED" + TextFormatting.RESET;
		else
			s2 += TextFormatting.GRAY + "AVAILABLE" + TextFormatting.RESET;

		this.client.fontRenderer.drawString(device.getName(), x + 32 + 8, y + 1, 16777215);
		this.client.fontRenderer.drawString(device.getDescription(), x + 32 + 8, y + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
		this.client.fontRenderer.drawString(s2, x + 32 + 8,
				y + this.client.fontRenderer.FONT_HEIGHT + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (device instanceof Arduino) {
			this.client.getTextureManager().bindTexture(DEVICE_ARDUINO);
			GlStateManager.enableBlend();
			Screen.blit(x, y, 0, 0, 32, 32, 32, 32);
			GlStateManager.disableBlend();
		} else if (device instanceof RaspberryPi) {
			this.client.getTextureManager().bindTexture(DEVICE_RASPBERRYPI);
			GlStateManager.enableBlend();
			Screen.blit(x, y, 0, 0, 32, 32, 32, 32);
			GlStateManager.disableBlend();
		}

		if (this.client.gameSettings.touchscreen || isSelected) {
			Screen.fill(x, y, x + 32, y + 32, -1601138544);
		}
	}

	/**
	 * Called when the mouse is clicked within this entry. Returning true means that something within this entry was
	 * clicked and the list should not be dragged.
	 */
	@Override public boolean mouseClicked(double x, double y, int par) {
		this.containingListSel.setSelected(this);

		if (x - (double) containingListSel.getRowLeft() < 32) { // clicked on icon
			if (!device.isConnected())
				MCreatorLink.LINK.setConnectedDevice(device);
			else
				MCreatorLink.LINK.disconnectDevice(device);
			this.containingListSel.refreshList();
			return true;
		} else if (Util.milliTime() - this.lastClickTime < 250L) { // double clicked
			this.lastClickTime = Util.milliTime();

			if (!device.isConnected())
				MCreatorLink.LINK.setConnectedDevice(device);
			else
				MCreatorLink.LINK.disconnectDevice(device);
			this.containingListSel.refreshList();
			return true;
		} else {
			this.lastClickTime = Util.milliTime();
			return false;
		}
	}

	AbstractDevice getDevice() {
		return device;
	}

}
