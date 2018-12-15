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

package net.mcreator.minecraft.link.devices.arduino;

import com.fazecast.jSerialComm.SerialPort;
import net.mcreator.minecraft.link.LinkProtocol;
import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.IDeviceDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ArduinoDetector implements IDeviceDetector {

	/**
	 * If detector needs any initialization, it can be done in this method
	 */
	@Override public void initDetector() {
	}

	/**
	 * @param currentDevices List of current devices, can be used to skip already connected devices
	 * @return List of detected Arduino Minecraft Link devices
	 */
	@Override public List<AbstractDevice> getDeviceList(Set<AbstractDevice> currentDevices) {
		List<AbstractDevice> arduinos = new ArrayList<>();

		Map<String, Arduino> connectedPorts = currentDevices.stream().filter(e -> e instanceof Arduino)
				.map(e -> (Arduino) e).collect(Collectors.toMap(e -> e.getPort().getDescriptivePortName(), e -> e));

		/*
		 * To make Serical comm to work on Unix devices, some need these commands to be executed:
		 * sudo usermod -a -G uucp username
		 * sudo usermod -a -G dialout username
		 * sudo usermod -a -G lock username
		 * sudo usermod -a -G tty username
		 */
		SerialPort[] serialPorts = SerialPort.getCommPorts();

		for (SerialPort port : serialPorts) {
			if (connectedPorts.get(port.getDescriptivePortName())
					!= null) { // directly add existing Arduinos if still detected
				arduinos.add(connectedPorts.get(port.getDescriptivePortName()));
				continue;
			}

			try {
				String linkData = null;

				port.clearDTR(); // workaround for some Arduinos to work

				port.openPort();
				port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 500, 500);
				port.setComPortParameters(115200, 8, 1, SerialPort.NO_PARITY);
				if (port.isOpen()) {
					Thread.sleep(1000);
					port.writeBytes(LinkProtocol.IDENT_REQUEST, LinkProtocol.IDENT_REQUEST.length);
					byte[] readBuffer = new byte[128];
					int numRead = port.readBytes(readBuffer, readBuffer.length);
					linkData = new String(readBuffer, 0, numRead);

				}
				port.closePort();
				
				if (linkData == null || !linkData.startsWith("tnedi:Minecraft Link (") || !linkData.contains(";")
						|| (linkData.length() - linkData.replace(";", "").length()) < 3)
					continue;

				String[] dataSplit = linkData.split(";");

				String description = port.getDescriptivePortName() + ", Version: " + dataSplit[0]
						.replace("tnedi:Minecraft Link (", "").replace(")", "");
				arduinos.add(new Arduino(dataSplit[1].trim(), description, Integer.parseInt(dataSplit[2].trim()),
						Integer.parseInt(dataSplit[3].trim()), port));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return arduinos;
	}

}
