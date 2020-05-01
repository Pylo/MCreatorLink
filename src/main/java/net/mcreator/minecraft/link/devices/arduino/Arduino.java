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

package net.mcreator.minecraft.link.devices.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import net.mcreator.minecraft.link.LinkProtocol;
import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.event.LinkDeviceConnectedEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Arduino extends AbstractDevice {

	private static final int SEND_INTERVAL = 50;

	private SerialPort port;
	private boolean connected;

	private ExecutorService deviceCommunicationThread = Executors.newSingleThreadExecutor();

	private long lastSendInterval;

	/**
	 * Arduino device constructor
	 *
	 * @param name             Device name as sent via Link
	 * @param description      Device description as set by device detector
	 * @param digitalInputsNum Number of digital inputs the device has
	 * @param analogInputsNum  Number of digital outputs the device has
	 * @param port             SerialPort object of the serial port on which the device communicates
	 */
	Arduino(String name, String description, int digitalInputsNum, int analogInputsNum, SerialPort port) {
		super(name, description, digitalInputsNum, analogInputsNum);
		this.port = port;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public void connect() {
		if (!connected) {
			deviceCommunicationThread.submit(() -> {
				this.port.openPort();
				this.connected = true;

				this.port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 20, 20);
				this.port.setComPortParameters(115200, 8, 1, SerialPort.NO_PARITY);
				this.port.addDataListener(new SerialPortDataListener() {

					String currentData = "";

					@Override public int getListeningEvents() {
						return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
					}

					@Override public void serialEvent(SerialPortEvent event) {
						if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
							return;

						byte[] data = new byte[Arduino.this.port.bytesAvailable()];
						Arduino.this.port.readBytes(data, data.length);

						currentData += new String(data);
						if (currentData.contains("\n")) {
							String[] lines = currentData.split("\n");
							if (lines.length == 1) {
								processInboundMessage(lines[0].getBytes());
								currentData = "";
							} else {
								for (int i = 0; i < lines.length - 1; i++)
									processInboundMessage(lines[i].getBytes());
								currentData = lines[lines.length - 1];
							}
						}
					}
				});

				try {
					Thread.sleep(250);
				} catch (InterruptedException ignored) {
				}

				sendData(LinkProtocol.START_POLLING_INPUTS, true);
				sendData(LinkProtocol.createInpulPollRate(100), true);

				MinecraftForge.EVENT_BUS.post(new LinkDeviceConnectedEvent(this));
			});
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public void disconnect() {
		if (connected) {
			deviceCommunicationThread.submit(() -> {
				try {
					sendData(LinkProtocol.STOP_POLLING_INPUTS, true);
					this.port.removeDataListener();
					this.port.closePort();
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.connected = false;
			});
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public void sendData(byte[] data, boolean forced) {
		if (connected) {
			deviceCommunicationThread.submit(() -> {
				// limit sending interval if not forced
				if (forced || (System.currentTimeMillis() - lastSendInterval) > SEND_INTERVAL)
					try {
						this.port.writeBytes(data, data.length);
						if (!forced)
							lastSendInterval = System.currentTimeMillis();
					} catch (Exception e) {
						e.printStackTrace();
					}
			});
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public boolean isConnected() {
		return connected;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public boolean validateConnection() {
		return port.isOpen();
	}

	/**
	 * Returns the serial port used by this connection.
	 *
	 * @return SerialPort object of the port used by this device.
	 */
	SerialPort getPort() {
		return port;
	}

}
