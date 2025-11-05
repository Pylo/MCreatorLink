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
import net.mcreator.minecraft.link.devices.arduino.Arduino;
import net.mcreator.minecraft.link.devices.raspberrypi.RaspberryPi;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

public class GuiListDevicesEntry extends ObjectSelectionList.Entry<GuiListDevicesEntry> {

    private static final ResourceLocation DEVICE_ARDUINO = ResourceLocation.fromNamespaceAndPath("mcreator_link",
            "textures/arduino.png");
    private static final ResourceLocation DEVICE_RASPBERRYPI = ResourceLocation.fromNamespaceAndPath("mcreator_link",
            "textures/raspberrypi.png");

    protected final Minecraft client;
    protected final GuiListDevices containingListSel;
    private long lastClickTime;

    private final AbstractDevice device;

    GuiListDevicesEntry(GuiListDevices listWorldSelIn, AbstractDevice device) {
        this.containingListSel = listWorldSelIn;
        this.client = listWorldSelIn.guiMCreatorLink.getMinecraft();
        this.device = device;
    }

    @Override
    public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovering, float partialTicks) {
        String s2 = "Status: ";

        if (device.isConnected())
            s2 += ChatFormatting.GREEN + "CONNECTED" + ChatFormatting.RESET;
        else
            s2 += ChatFormatting.GRAY + "AVAILABLE" + ChatFormatting.RESET;

        guiGraphics.drawString(this.client.font, device.getName(), this.getContentX() + 32 + 8, this.getContentY() + 1, ARGB.opaque(16777215), false);
        guiGraphics.drawString(this.client.font, device.getDescription(), this.getContentX() + 32 + 8, this.getContentY() + this.client.font.lineHeight + 3,
                ARGB.opaque(8421504), false);
        guiGraphics.drawString(this.client.font, s2, this.getContentX() + 32 + 8,
                this.getContentY() + this.client.font.lineHeight + this.client.font.lineHeight + 3, ARGB.opaque(8421504), false);

        if (device instanceof Arduino) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, DEVICE_ARDUINO, this.getContentX(), this.getContentY(), 0, 0, 32, 32, 32, 32);
        } else if (device instanceof RaspberryPi) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, DEVICE_RASPBERRYPI, this.getContentX(), this.getContentY(), 0, 0, 32, 32, 32, 32);
        }

        if (this.client.options.touchscreen().get() || containingListSel.getSelected() == this) {
            guiGraphics.fill(this.getContentX(), this.getContentY(), this.getContentX() + 32, this.getContentY() + 32, -1601138544);
        }
    }

    /**
     * Called when the mouse is clicked within this entry. Returning true means that something within this entry was
     * clicked and the list should not be dragged.
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean flag) {
        this.containingListSel.setSelected(this);

        if (event.x() - (double) containingListSel.getRowLeft() < 32) { // clicked on icon
            if (!device.isConnected())
                MCreatorLink.LINK.setConnectedDevice(device);
            else
                MCreatorLink.LINK.disconnectDevice(device);
            this.containingListSel.refreshList();
            return true;
        } else if (Util.getMillis() - this.lastClickTime < 250L) { // double clicked
            this.lastClickTime = Util.getMillis();

            if (!device.isConnected())
                MCreatorLink.LINK.setConnectedDevice(device);
            else
                MCreatorLink.LINK.disconnectDevice(device);
            this.containingListSel.refreshList();
            return true;
        } else {
            this.lastClickTime = Util.getMillis();
            return false;
        }
    }

    AbstractDevice getDevice() {
        return device;
    }

    @Override
    public Component getNarration() {
        return Component.translatable("link.menu.selectlist");
    }
}
