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

package net.mcreator.minecraft.link;

import net.mcreator.minecraft.link.devices.AbstractDevice;
import net.mcreator.minecraft.link.devices.PinMode;

/**
 * This is a helper class that provides static methods for interacting with the currently connected device.
 * <p>
 * The device is selected in the Minecraft GUI and once it is selected, it can be controlled using commands
 * (that call methods of this class in the background), by using Link API objects
 * or in most cases by using this helper class.
 * <p>
 * Example usage of this class:
 * <p>
 * CurrentDevice.digitalWrite(13, 1);
 * <p>
 * In this example, the pin 13 of the currently connected device is set to logic level "high".
 */
public final class CurrentDevice {

	/**
	 * This method sets the pin mode of the selected pin on the current device.
	 * <p>
	 * Pin mode is the mode of the pin on the device. It can be input or output. There is a special mode input_pullup too, in which case
	 * the internal pullup resitor of the connected device is enabled if it is supported by the current device.
	 * <p>
	 * The output mode is used when one want to controll the logic level of the pin and input mode is used when one wants to check the input logic level
	 * of a pin on the device.
	 * <p>
	 * If there is no device connected, nothing happens.
	 *
	 * @param port    Port of which to set the pin mode
	 * @param pinMode PinMode of the pin. Can be PinMode.OUT, PinMode.IN or PinMode.IN_P
	 */
	public static void pinMode(int port, PinMode pinMode) {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		if (device != null)
			device.sendData(LinkProtocol.createPinmodeMessage(port, pinMode), true);
	}

	/**
	 * This methods sets the logic level of the selected pin on the connected to the given level.
	 * The pin needs to be set to output for this method to work.
	 * <p>
	 * If there is no device connected, nothing happens.
	 *
	 * @param port Port on the device of which the logic level should be set
	 * @param val  Logic level value - should be 0 for low or 1 for high
	 */
	public static void digitalWrite(int port, byte val) {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		if (device != null)
			device.sendData(LinkProtocol.createDigitalWriteMessage(port, val));
	}

	/**
	 * This methods sets the analog level of the selected pin on the connected device to the given level.
	 * The pin needs to be set to output for this method to work.
	 * <p>
	 * If there is no device connected, nothing happens.
	 *
	 * @param port Port on the device of which the analog level should be set
	 * @param val  Value depends on the DAC resolution. For most devices, the interval goes from
	 *             0 for the lowest voltage level and 1024 for the highest voltage level
	 */
	public static void analogWrite(int port, short val) {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		if (device != null)
			device.sendData(LinkProtocol.createAnalogWriteMessage(port, val));
	}

	/**
	 * This methods reads the digital logic level of the selected pin on the selected device.
	 *
	 * @param port Port on the device from which to read from
	 * @return Value of the pin which is 0 if the logic level is low, 1 if the level is high or -1 if there is no device connected
	 */
	public static byte digitalRead(int port) {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		if (device != null)
			return device.digitalInputs(port);
		else
			return -1;
	}

	/**
	 * This methods reads the analog level of the selected pin on the selected device.
	 *
	 * @param port Port on the device from which to read from
	 * @return Analog level of the selected port - usually in range from 0 to 1024, -1 if there is no device connected
	 */
	public static short analogRead(int port) {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		if (device != null)
			return device.analogInputs(port);
		else
			return -1;
	}

	/**
	 * Call this method to enable digital input events on the selected port.
	 * Digital input event is called each time a logic value of the given pin changes and the events are enabled.
	 * <p>
	 * If there is no device connected, nothing happens.
	 *
	 * @param port Port on which to enable digital input events
	 * @see net.mcreator.minecraft.link.event.LinkDigitalPinChangedEvent
	 */
	public static void enableDigitalInputEvents(int port) {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		if (device != null)
			device.enableDigitalInputEvents(port, true);
	}

	/**
	 * Call this method to disable digital input events on the selected port.
	 * <p>
	 * If there is no device connected, nothing happens.
	 *
	 * @param port Port on which to enable digital input events
	 * @see net.mcreator.minecraft.link.event.LinkDigitalPinChangedEvent
	 */
	public static void disableDigitalInputEvents(int port) {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		if (device != null)
			device.enableDigitalInputEvents(port, false);
	}

	/**
	 * Call this method to send a custom message to the current device.
	 * <p>
	 * If there is no device connected, nothing happens.
	 *
	 * @param message The message to be sent
	 */
	public static void sendMessage(String message) {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		String command = message.split(" ")[0];
		try {
			String data = message.split(" ")[1];
			if (device != null)
				device.sendData(("msg?" + command + ":" + data + "\n").getBytes());
		}
		catch (ArrayIndexOutOfBoundsException e){
			System.out.println(command);
			if (device != null)
				device.sendData(("msg?" + command + ":" + " " + "\n").getBytes());
		}

	}

	/**
	 * Call this method to get the number of digital inputs the current device has
	 *
	 * @return Number of digital inputs the device has or -1 if no device is connected
	 */
	public static int getDigitalPinsCount() {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		if (device != null)
			return device.getDigitalPinsCount();
		return -1;
	}

	/**
	 * Call this method to get the number of analog inputs the current device has
	 *
	 * @return Number of analog inputs the device has or -1 if no device is connected
	 */
	public static int getAnalogPinsCount() {
		AbstractDevice device = MCreatorLink.LINK.getConnectedDevice();
		if (device != null)
			return device.getAnalogPinsCount();
		return -1;
	}

}