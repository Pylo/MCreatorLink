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

package net.mcreator.minecraft.link;

import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.IDeviceDetector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class DeviceManager {

	private final List<IDeviceDetector> deviceDetectorList = new ArrayList<>();
	private final Set<AbstractDevice> currentDevices = new HashSet<>();

	private volatile boolean refreshRunning = false;

	/**
	 * Call this method to register device detector for a new device type.
	 *
	 * @param deviceDetector Object implementing IDeviceDetector that detects a specific device type
	 */
	void registerDeviceDetector(IDeviceDetector deviceDetector) {
		this.deviceDetectorList.add(deviceDetector);
		deviceDetector.initDetector();
	}

	/**
	 * This method returns the list of currently detected devices
	 *
	 * @return List of AbstractDevice objects containing devices that were detected (can be connected or not)
	 */
	public Set<AbstractDevice> getAllDevices() {
		new Thread(() -> {
			if (!refreshRunning) {
				refreshRunning = true;

				List<AbstractDevice> newDevices = this.deviceDetectorList.stream()
						.flatMap(deviceDetector -> deviceDetector.getDeviceList(currentDevices).stream())
						.collect(Collectors.toList());

				// add new devices in device set, existing devices are not added again
				currentDevices.addAll(newDevices);

				List<AbstractDevice> obsoleteDevices = new ArrayList<>();

				// remove devices that don't exist anymore
				for (AbstractDevice device : currentDevices) {
					if (!newDevices.contains(device))
						obsoleteDevices.add(device);
				}

				currentDevices.removeAll(obsoleteDevices);

				refreshRunning = false;
			}
		}).start();
		return currentDevices;
	}

	/**
	 * Call this method to select the device that should be connected. Can only be called if there is not already a device connected.
	 * Right now only one device can be connected at a time.
	 *
	 * @param toConnect AbstractDevice returned by getAllDevices() to which we want to connect
	 */
	public void setConnectedDevice(AbstractDevice toConnect) {
		if (getConnectedDevice() != null)
			return;

		if (currentDevices.contains(toConnect)) {
			List<AbstractDevice> _currentDevices = new ArrayList<>(currentDevices);
			AbstractDevice device = _currentDevices.get(_currentDevices.indexOf(toConnect));
			device.connect();
		}
	}

	/**
	 * Call this method to disconnect from the device.
	 *
	 * @param toDisconnect AbstractDevice returned by getAllDevices() from which we want to disconnect
	 */
	public void disconnectDevice(AbstractDevice toDisconnect) {
		if (currentDevices.contains(toDisconnect)) {
			List<AbstractDevice> _currentDevices = new ArrayList<>(currentDevices);
			AbstractDevice device = _currentDevices.get(_currentDevices.indexOf(toDisconnect));
			device.disconnect();
		}
	}

	/**
	 * This method returns the currently connected device.
	 *
	 * @return AbstractDevice object of the currently connected device or null if no device is connected or the connection was dropped.
	 */
	public AbstractDevice getConnectedDevice() {
		for (AbstractDevice device : currentDevices)
			if (device.isConnected()) {
				if (device.validateConnection())
					return device;
				else
					device.disconnect();
			}
		return null;
	}

}
