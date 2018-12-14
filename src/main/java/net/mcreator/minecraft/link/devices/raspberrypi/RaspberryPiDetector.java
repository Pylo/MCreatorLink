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

package net.mcreator.minecraft.link.devices.raspberrypi;

import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.IDeviceDetector;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class RaspberryPiDetector implements IDeviceDetector {

	private boolean shouldScan = false;

	private Set<AbstractDevice> raspberripies = new HashSet<>();

	/**
	 * If detector needs any initialization, it can be done in this method.
	 * <p>
	 * For this case, the detectors network detection thread is made and started.
	 */
	@Override public void initDetector() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override public void run() {
				if (shouldScan) {
					raspberripies.clear();
					try (DatagramSocket datagramSocket = new DatagramSocket()) {
						datagramSocket.setSoTimeout(1000);
						byte[] buf_send = "ident?\n".getBytes();
						DatagramPacket packet = new DatagramPacket(buf_send, buf_send.length,
								InetAddress.getByName("224.0.2.63"), RaspberryPi.REMOTE_PORT);
						datagramSocket.send(packet);

						byte[] buf_rec = new byte[1024];
						DatagramPacket packet_back = new DatagramPacket(buf_rec, buf_rec.length);
						datagramSocket.receive(packet_back);
						String dataBack = new String(buf_rec).split("\n")[0].trim();

						if (dataBack.startsWith("tnedi:Minecraft Link (") && dataBack.contains(";")
								&& dataBack.length() - dataBack.replace(";", "").length() >= 3) {
							String[] dataSplit = dataBack.split(";");

							String description =
									packet_back.getAddress().getHostAddress() + ", Version: " + dataSplit[0]
											.replace("tnedi:Minecraft Link (", "").replace(")", "");

							raspberripies.add(new RaspberryPi(dataSplit[1].trim(), description,
									Integer.parseInt(dataSplit[2].trim()), Integer.parseInt(dataSplit[3].trim()),
									packet_back.getAddress(), (InetSocketAddress) packet_back.getSocketAddress()));
						}
					} catch (SocketTimeoutException ignored) {
						// timeouts can occur as not all devices on multicast group will always respond
					} catch (IOException e) {
						e.printStackTrace();
					}
					shouldScan = false;
				}
			}
		}, 0, 2000);
	}

	/**
	 * This method returns the current list of devices and requests a new scan
	 *
	 * @param currentDevices List of current devices, can be used to skip already connected devices
	 * @return List of Raspberry Pis detected
	 */
	@Override public List<AbstractDevice> getDeviceList(Set<AbstractDevice> currentDevices) {
		shouldScan = true;
		return new ArrayList<>(raspberripies);
	}

}
