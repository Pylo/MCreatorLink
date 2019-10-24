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
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT) public class GuiListDevicesEntryScan extends GuiListDevicesEntry {

	GuiListDevicesEntryScan(GuiListDevices listWorldSelIn, AbstractDevice device) {
		super(listWorldSelIn, device);
	}

	@Override
	public void render(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
			boolean isSelected, float partialTicks) {
		if (this.client.currentScreen != null) {
			int i = y + slotHeight / 2 - this.client.fontRenderer.FONT_HEIGHT / 2;

			this.client.fontRenderer.drawString("Scanning for link compatible devices",
					this.client.currentScreen.width / 2f
							- this.client.fontRenderer.getStringWidth("Scanning for link compatible devices") / 2f, i,
					16777215);

			String s;
			switch ((int) (Util.milliTime() / 300L % 4L)) {
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

			this.client.fontRenderer.drawString(s,
					this.client.currentScreen.width / 2f - this.client.fontRenderer.getStringWidth(s) / 2f,
					i + this.client.fontRenderer.FONT_HEIGHT, 8421504);
		}
	}

	@Override public boolean mouseClicked(double x, double y, int par) {
		return false;
	}

}