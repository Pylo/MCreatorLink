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
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber({ Dist.CLIENT }) public class ScreenEventHandler {

	/**
     * This method subscribes to screen draw events so the Link notice can be rendered on some screens.
     *
     * @param drawScreenEvent ScreenEvent.DrawScreenEvent event instance
     */
    @SubscribeEvent
    public static void drawScreenEvent(ScreenEvent.Render.Post drawScreenEvent) {
        if (drawScreenEvent.getScreen() instanceof TitleScreen || drawScreenEvent.getScreen() instanceof PauseScreen) {
            drawScreenEvent.getScreen().getMinecraft().font.draw(drawScreenEvent.getPoseStack(),
                    "MCreator Link " + MCreatorLink.VERSION, 3, 3, 0xffffff);
            drawScreenEvent.getScreen().getMinecraft().font.draw(drawScreenEvent.getPoseStack(),
                    I18n.get("link.menu.settingskey"), 3, 14, 0xffffff);

            if (GLFW.glfwGetKey(drawScreenEvent.getScreen().getMinecraft().getWindow().getWindow(), GLFW.GLFW_KEY_L)
                    == GLFW.GLFW_PRESS)
                drawScreenEvent.getScreen().getMinecraft().setScreen(new GuiMCreatorLink(drawScreenEvent.getScreen()));
        }
	}

}
