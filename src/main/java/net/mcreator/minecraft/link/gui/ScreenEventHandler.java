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
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(value = { Side.CLIENT }) public class ScreenEventHandler {

	/**
	 * This method subsribes to screen draw events so the Link notice can be rendered on some of the screens.
	 *
	 * @param drawScreenEvent GuiScreenEvent.DrawScreenEvent event instance
	 */
	@SideOnly(Side.CLIENT) @SubscribeEvent public static void drawScreenEvent(
			GuiScreenEvent.DrawScreenEvent drawScreenEvent) {
		if (drawScreenEvent.getGui() instanceof GuiMainMenu || drawScreenEvent.getGui() instanceof GuiIngameMenu) {
			int color = drawScreenEvent.getGui() instanceof GuiMainMenu ? 0x000000 : 0xffffff;
			Minecraft.getMinecraft().fontRenderer
					.drawString("Minecraft Link " + MinecraftLink.VERSION, 3, 3, color, false);
			Minecraft.getMinecraft().fontRenderer.drawString(I18n.format("link.menu.settingskey"), 3, 14, color, false);

			if (Keyboard.isKeyDown(Keyboard.KEY_L))
				Minecraft.getMinecraft().displayGuiScreen(new GuiMinecraftLink(drawScreenEvent.getGui()));
		}
	}

}
