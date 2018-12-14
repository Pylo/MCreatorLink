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

package net.mcreator.minecraft.link.devices;

import java.util.List;
import java.util.Set;

public interface IDeviceDetector {

	/**
	 * If detector needs any initialization, it can be done in this method
	 */
	void initDetector();

	/**
	 * @param currentDevices List of current devices, can be used to skip already connected devices
	 * @return List of devices for this specific device detector that were detected.
	 */
	List<AbstractDevice> getDeviceList(Set<AbstractDevice> currentDevices);

}
