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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT) public class GuiListDevicesEntryScan implements GuiListExtended.IGuiListEntry {
	private final Minecraft mc = Minecraft.getInstance();

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
			boolean isSelected, float partialTicks) {
		if (this.mc.currentScreen != null) {
			int i = y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2;

			this.mc.fontRenderer.drawString("Scanning for link compatible devices", this.mc.currentScreen.width / 2
					- this.mc.fontRenderer.getStringWidth("Scanning for link compatible devices") / 2, i, 16777215);
			String s;

			switch ((int) (Minecraft.getSystemTime() / 300L % 4L)) {
			case 0:
			default:
				s = "O o o";
				break;
			case 1:
			case 3:
				s = "o O o";
				break;
			case 2:
				s = "o o O";
			}

			this.mc.fontRenderer
					.drawString(s, this.mc.currentScreen.width / 2 - this.mc.fontRenderer.getStringWidth(s) / 2,
							i + this.mc.fontRenderer.FONT_HEIGHT, 8421504);
		}
	}

	@Override public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
	}

	/**
	 * Called when the mouse is clicked within this entry. Returning true means that something within this entry was
	 * clicked and the list should not be dragged.
	 */
	@Override public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX,
			int relativeY) {
		return false;
	}

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	@Override public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
	}

}