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

package net.mcreator.minecraft.link.devices.raspberrypi;

import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.IDeviceDetector;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.*;

public class RaspberryPiDetector implements IDeviceDetector {

	private boolean shouldScan = false;

    private final Set<AbstractDevice> raspberripies = new HashSet<>();

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
					RaspberryPi pi = getRaspberryPiForIP("224.0.2.63");
					if (pi != null)
						raspberripies.add(pi);
					shouldScan = false;
				}
			}
		}, 0, 2000);
	}

	/**
	 * Call this method to try to connect to the given IP.
	 *
	 * @param ip IP can be a multicast group or a device IP
	 * @return RaspberryPi instance or null if not found
	 */
	public static RaspberryPi getRaspberryPiForIP(String ip) {
		try (DatagramSocket datagramSocket = new DatagramSocket()) {
			datagramSocket.setSoTimeout(500);
			byte[] buf_send = "ident?\n".getBytes();
			DatagramPacket packet = new DatagramPacket(buf_send, buf_send.length, InetAddress.getByName(ip),
					RaspberryPi.REMOTE_PORT);
			datagramSocket.send(packet);

			byte[] buf_rec = new byte[1024];
			DatagramPacket packet_back = new DatagramPacket(buf_rec, buf_rec.length);
			datagramSocket.receive(packet_back);
			String dataBack = new String(buf_rec).split("\n")[0].trim();

			if (dataBack.startsWith("tnedi:MCreator Link (") && dataBack.contains(";")
					&& dataBack.length() - dataBack.replace(";", "").length() >= 3) {
				String[] dataSplit = dataBack.split(";");

				String description = packet_back.getAddress().getHostAddress() + ", Version: " + dataSplit[0]
						.replace("tnedi:MCreator Link (", "").replace(")", "");

				// get local ip which was used to connect to the remote device, so device can send data back
				datagramSocket.connect(packet_back.getSocketAddress());
				InetAddress localAddress = datagramSocket.getLocalAddress();
				datagramSocket.disconnect();

				return new RaspberryPi(dataSplit[1].trim(), description, Integer.parseInt(dataSplit[2].trim()),
						Integer.parseInt(dataSplit[3].trim()), packet_back.getAddress(), localAddress);
			}
		} catch (SocketTimeoutException ignored) {
			// timeouts can occur as not all devices on multicast group will always respond
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
