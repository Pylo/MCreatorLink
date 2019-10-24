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

import net.mcreator.minecraft.link.MCreatorLink;
import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT) public class GuiListDevices extends ExtendedList<GuiListDevicesEntry> {

	final GuiMCreatorLink guiMCreatorLink;

	private final GuiListDevicesEntryScan devicesEntryScan;

	GuiListDevices(GuiMCreatorLink guiMCreatorLink, Minecraft clientIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
		super(clientIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.guiMCreatorLink = guiMCreatorLink;
		this.devicesEntryScan = new GuiListDevicesEntryScan(this, null);

		this.refreshList(); // initial refresh
	}

	void refreshList() {
		GuiListDevicesEntry entry = this.getSelected();

		this.clearEntries();

		for (AbstractDevice device : MCreatorLink.LINK.getAllDevices()) {
			GuiListDevicesEntry tmp;
			this.addEntry(tmp = new GuiListDevicesEntry(this, device));
			if (entry != null && device.equals(entry.getDevice()))
				entry = tmp;
		}

		this.addEntry(devicesEntryScan);

		if (entry != null)
			super.setSelected(entry);
	}

	@Override protected void moveSelection(int selectionIdx) {
		int i = this.children().indexOf(this.getSelected());
		int j = MathHelper.clamp(i + selectionIdx, 0, this.getItemCount() - 1);
		GuiListDevicesEntry selection = this.children().get(j);
		super.setSelected(selection);
		if (selection instanceof GuiListDevicesEntryScan) {
			if (selectionIdx <= 0 || j != this.getItemCount() - 1) {
				if (selectionIdx >= 0 || j != 0) {
					this.moveSelection(selectionIdx);
				}
			}
		} else {
			this.ensureVisible(selection);
		}
	}

	@Override public void setSelected(@Nullable GuiListDevicesEntry guiListDevicesEntry) {
		super.setSelected(guiListDevicesEntry);
		this.guiMCreatorLink.setSelectedDevice(this.getSelectedDevice());
	}

	/**
	 * Gets the width of the list
	 */
	@Override public int getRowWidth() {
		return super.getRowWidth() + 85;
	}

	@Override protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 32;
	}

	/**
	 * Returns true if the element passed in is currently selected
	 */
	@Override protected boolean isSelectedItem(int slotIndex) {
		if (slotIndex == super.getItemCount()) { // GuiListDevicesEntryScan can't be selected
			return false;
		}
		return super.isSelectedItem(slotIndex);
	}

	@Nullable GuiListDevicesEntry getSelectedDevice() {
		return this.getSelected() instanceof GuiListDevicesEntryScan ? null : this.getSelected();
	}

	@Override public int getRowLeft() {
		return super.getRowLeft();
	}
}
