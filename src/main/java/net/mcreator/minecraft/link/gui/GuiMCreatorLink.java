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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.minecraft.link.MCreatorLink;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT) public class GuiMCreatorLink extends Screen {

	private Screen prevScreen;
	private Button connectButton;
	private Button disconnectButton;

	private GuiListDevices selectionList;

	private int ticks = 0;

	public GuiMCreatorLink(Screen screenIn) {
		super(new TextComponent("MCreator Link"));
		this.prevScreen = screenIn;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override public void init() {
		super.init();

		this.selectionList = new GuiListDevices(this, this.minecraft, this.width, this.height, 32, this.height - 42,
				36);
		//this.children.add(this.selectionList);

		this.connectButton = this.addRenderableWidget(new Button(this.width / 2 - 154, this.height - 32, 72, 20,
				new TranslatableComponent("link.menu.connect"), e -> {
			GuiListDevicesEntry selected = this.selectionList.getSelectedDevice();
			if (selected != null) {
				MCreatorLink.LINK.setConnectedDevice(selected.getDevice());
				this.selectionList.refreshList();
			}
		}));
		this.disconnectButton = this.addRenderableWidget(new Button(this.width / 2 - 76, this.height - 32, 72, 20,
				new TranslatableComponent("link.menu.disconnect"), e -> {
			GuiListDevicesEntry selected = this.selectionList.getSelectedDevice();
			if (selected != null) {
				MCreatorLink.LINK.disconnectDevice(selected.getDevice());
				this.selectionList.refreshList();
			}
		}));

		this.addRenderableWidget(
				new Button(this.width / 2 + 2, this.height - 32, 72, 20, new TranslatableComponent("link.menu.direct"),
						e -> {
							assert this.minecraft != null;
							this.minecraft.setScreen(new GuiDirectLink(this));
						}));
		this.addRenderableWidget(
				new Button(this.width / 2 + 82, this.height - 32, 72, 20, new TranslatableComponent("gui.done"), e -> {
					if (this.minecraft != null) {
						this.minecraft.setScreen(this.prevScreen);
					}
				}));

		this.addRenderableWidget(new Button(this.width / 2 + 82 + 55, 6, 20, 20, new TextComponent("?"),
				e -> Util.getPlatform().openUri("https://mcreator.net/link")));

		this.disconnectButton.active = false;
		this.connectButton.active = false;
	}

	private static final ResourceLocation LOGO = new ResourceLocation("mcreator_link", "textures/logo_small.png");

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
		this.selectionList.render(PoseStack, mouseX, mouseY, partialTicks);

		super.render(PoseStack, mouseX, mouseY, partialTicks);

		RenderSystem.setShaderTexture(0, LOGO);

		RenderSystem.enableBlend();
		blit(PoseStack, this.width / 2 - 50, 8, 0.0F, 0.0F, 100, 16, 100, 16);
		RenderSystem.disableBlend();
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		this.selectionList.mouseClicked(mouseX, mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Called when a mouse button is released.
	 */
	@Override public boolean mouseReleased(double mouseX, double mouseY, int state) {
		this.selectionList.mouseReleased(mouseX, mouseY, state);
		return super.mouseReleased(mouseX, mouseY, state);
	}

	void setSelectedDevice(@Nullable GuiListDevicesEntry entry) {
		if (this.connectButton != null) {
			if (entry != null) {
				if (entry.getDevice().isConnected()) {
					this.disconnectButton.active = true;
					this.connectButton.active = false;
				} else {
					this.disconnectButton.active = false;
					this.connectButton.active = true;
				}
			} else {
				this.disconnectButton.active = false;
				this.connectButton.active = false;
			}
			if (MCreatorLink.LINK.getConnectedDevice() != null)
				this.connectButton.active = false;
		}
	}

	@Override public void tick() {
		super.tick();

		ticks++;

		if (ticks % 50 == 0)
			this.selectionList.refreshList();
	}
}
