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

import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT) public class GuiListDevicesEntryScan extends GuiListDevicesEntry {

	GuiListDevicesEntryScan(GuiListDevices listWorldSelIn, AbstractDevice device) {
		super(listWorldSelIn, device);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX,
					   int mouseY, boolean isSelected, float partialTicks) {
		if (this.client.screen != null) {
			int i = y + slotHeight / 2 - this.client.font.lineHeight / 2;

			guiGraphics.drawString(this.client.font, "Scanning for link compatible devices",
					(int) (this.client.screen.width / 2f - this.client.font.width("Scanning for link compatible devices") / 2f),
					i, 16777215, false);

			String s = switch ((int) (Util.getMillis() / 300L % 4L)) {
				default -> "O o o";
				case 1, 3 -> "o O o";
				case 2 -> "o o O";
			};

			if (Minecraft.getInstance().screen != null) {
				guiGraphics.drawString(this.client.font, s,
						(int) (Minecraft.getInstance().screen.width / 2f - this.client.font.width(s) / 2f),
						i + this.client.font.lineHeight, 8421504, false);
			}
		}
	}

	@Override public boolean mouseClicked(double x, double y, int par) {
		return false;
	}

}