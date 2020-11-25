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

import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.lwjgl.glfw.GLFW;

public class ScreenEventHandler {

	private final ArtifactVersion linkVersion;

	public ScreenEventHandler() {
		linkVersion = ModList.get().getModFileById("mcreator_link").getMods().get(0).getVersion();
	}

	/**
	 * This method subscribes to screen draw events so the Link notice can be rendered on some of the screens.
	 *
	 * @param drawScreenEvent ScreenEvent.DrawScreenEvent event instance
	 */
	@OnlyIn(Dist.CLIENT) @SubscribeEvent public void drawScreenEvent(GuiScreenEvent.DrawScreenEvent drawScreenEvent) {
		if (drawScreenEvent.getGui() instanceof MainMenuScreen || drawScreenEvent
				.getGui() instanceof IngameMenuScreen) {
			drawScreenEvent.getGui().getMinecraft().fontRenderer
					.drawString(drawScreenEvent.getMatrixStack(), "MCreator Link " + linkVersion, 3, 3, 0xffffff);
			drawScreenEvent.getGui().getMinecraft().fontRenderer
					.drawString(drawScreenEvent.getMatrixStack(), I18n.format("link.menu.settingskey"), 3, 14,
							0xffffff);

			if (GLFW.glfwGetKey(drawScreenEvent.getGui().getMinecraft().getMainWindow().getHandle(), GLFW.GLFW_KEY_L)
					== GLFW.GLFW_PRESS)
				drawScreenEvent.getGui().getMinecraft().displayGuiScreen(new GuiMCreatorLink(drawScreenEvent.getGui()));
		}
	}

}
