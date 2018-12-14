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

import com.google.common.collect.Lists;
import net.mcreator.minecraft.link.MinecraftLink;
import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT) public class GuiListDevices extends GuiListExtended {
	private final GuiMinecraftLink guiMinecraftLink;
	private final List<GuiListDevicesEntry> entries = Lists.newArrayList();
	private int selectedIdx = -1;

	private final GuiListDevicesEntryScan devicesEntryScan = new GuiListDevicesEntryScan();

	GuiListDevices(GuiMinecraftLink guiMinecraftLink, Minecraft clientIn, int widthIn, int heightIn, int topIn,
			int bottomIn, int slotHeightIn) {
		super(clientIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.guiMinecraftLink = guiMinecraftLink;

		this.refreshList(); // initial refresh
	}

	void refreshList() {
		this.entries.clear();

		for (AbstractDevice device : MinecraftLink.LINK.getAllDevices())
			this.entries.add(new GuiListDevicesEntry(this, device));

		guiMinecraftLink.setSelectDevice(guiMinecraftLink.entry);
	}

	void selectDevice(int idx) {
		this.selectedIdx = idx;
		this.guiMinecraftLink.setSelectDevice(this.getSelectedDevice());
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	@Override public GuiListExtended.IGuiListEntry getListEntry(int index) {
		if (index < this.entries.size()) {
			return this.entries.get(index);
		} else {
			index = index - this.entries.size();

			if (index == 0) {
				return this.devicesEntryScan;
			} else {
				--index;
				return this.entries.get(index);
			}
		}
	}

	@Override protected int getSize() {
		return this.entries.size() + 1;
	}

	@Override protected int getScrollBarX() {
		return super.getScrollBarX() + 20;
	}

	/**
	 * Gets the width of the list
	 */
	@Override public int getListWidth() {
		return super.getListWidth() + 50;
	}

	/**
	 * Returns true if the element passed in is currently selected
	 */
	@Override protected boolean isSelected(int slotIndex) {
		if (slotIndex == this.entries.size()) { // GuiListDevicesEntryScan can't be selected
			return false;
		}
		return slotIndex == this.selectedIdx;
	}

	@Nullable GuiListDevicesEntry getSelectedDevice() {
		GuiListExtended.IGuiListEntry selected =
				this.selectedIdx >= 0 && this.selectedIdx < this.getSize() ? this.getListEntry(this.selectedIdx) : null;
		if (selected instanceof GuiListDevicesEntry)
			return (GuiListDevicesEntry) selected;
		return null;
	}

}
