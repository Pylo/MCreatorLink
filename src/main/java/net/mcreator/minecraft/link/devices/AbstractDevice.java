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

import net.mcreator.minecraft.link.LinkProtocol;
import net.mcreator.minecraft.link.event.LinkCustomMessageReceivedEvent;
import net.mcreator.minecraft.link.event.LinkDigitalPinChangedEvent;
import net.minecraftforge.common.MinecraftForge;

public abstract class AbstractDevice {

	private String name;
	private String description;

	private byte[] digitalInputs;
	private byte[] digitalInputsPrevious;

	private short[] analogInputs;

	/**
	 * Constructor for the AbstractDevice class
	 *
	 * @param name             Device name as sent via Link
	 * @param description      Device description as set by device detector
	 * @param digitalInputsNum Number of digital inputs the device has
	 * @param analogInputsNum  Number of digital outputs the device has
	 */
	public AbstractDevice(String name, String description, int digitalInputsNum, int analogInputsNum) {
		this.name = name;
		this.description = description;

		this.analogInputs = new short[analogInputsNum];

		this.digitalInputs = new byte[digitalInputsNum];
		this.digitalInputsPrevious = new byte[digitalInputsNum];
		for (int i = 0; i < digitalInputsNum; i++)
			digitalInputsPrevious[i] = -1;
	}

	public final String getName() {
		return name;
	}

	public final String getDescription() {
		return description;
	}

	public final int getDigitalPinsCount() {
		return digitalInputs.length;
	}

	public final int getAnalogPinsCount() {
		return analogInputs.length;
	}

	/**
	 * Call this method to enable or disable digital input events for the given pin.
	 * Digital input event is called each time a logic value of the given pin changes and the events are enabled.
	 * <p>
	 * The event is registered in Minecraft Forge bus as LinkDigitalPinChangedEvent.
	 *
	 * @param pin    The pin to set events on
	 * @param enable true to enable events, false to disable them
	 * @see net.mcreator.minecraft.link.event.LinkDigitalPinChangedEvent
	 */
	public final void enableDigitalInputEvents(int pin, boolean enable) {
		if (pin < digitalInputs.length)
			if (enable)
				digitalInputsPrevious[pin] = digitalInputs[pin];
			else
				digitalInputsPrevious[pin] = -1;
	}

	/**
	 * Call this method to check the value of digital pin
	 *
	 * @param pin Pin to check
	 * @return Pin value
	 */
	public final byte digitalInputs(int pin) {
		if (pin < digitalInputs.length)
			return digitalInputs[pin];
		else
			return -1;
	}

	/**
	 * Internal method to set the digital pin value and invoke events if enabled for the given pin
	 * <p>
	 * Called by pin polling system
	 *
	 * @param pin Pin to set value for
	 * @param val The pin value to set
	 */
	private void digitalInputs(int pin, byte val) {
		if (pin < digitalInputs.length) {
			digitalInputs[pin] = val;
			if (digitalInputsPrevious[pin] != -1) { // check if events on this pin are enabled
				int prev_val = digitalInputsPrevious[pin];
				if (prev_val != val) { //check if value of pin has changed
					if (val > prev_val) { //rising edge event
						MinecraftForge.EVENT_BUS.post(new LinkDigitalPinChangedEvent(this, pin, val, true));
					} else { // falling edge event
						MinecraftForge.EVENT_BUS.post(new LinkDigitalPinChangedEvent(this, pin, val, false));
					}
				}
			}
		}
	}

	/**
	 * Call this method to check the value of analog pin
	 *
	 * @param pin Pin to check
	 * @return Pin analog value
	 */
	public final short analogInputs(int pin) {
		if (pin < analogInputs.length)
			return analogInputs[pin];
		else
			return -1;
	}

	/**
	 * Internal method to set the analog pin value
	 * <p>
	 * Called by pin polling system
	 *
	 * @param pin Pin to set value for
	 * @param val The pin value to set
	 */
	private void analogInputs(int pin, short val) {
		if (pin < analogInputs.length)
			analogInputs[pin] = val;
	}

	/**
	 * Method that should be called in each device definition after the inbound message is received.
	 * <p>
	 * This method parses inbound pin state and other messages.
	 *
	 * @param message The message that was received from the connected device.
	 */
	protected final void processInboundMessage(byte[] message) {
		if (LinkProtocol.isDigitalInputsStatusMessage(message)) {
			byte[] pins = LinkProtocol.parseDigitalInputsMessage(message);
			for (int i = 0; i < pins.length; i++)
				digitalInputs(i, pins[i]);
		} else if (LinkProtocol.isAnalogInputsStatusMessage(message)) {
			short[] pins = LinkProtocol.parseAnalogInputsMessage(message);
			for (int i = 0; i < pins.length; i++)
				analogInputs(i, pins[i]);
		} else {
			MinecraftForge.EVENT_BUS.post(new LinkCustomMessageReceivedEvent(this, message));
		}
	}

	@Override public boolean equals(Object o) {
		if (o instanceof AbstractDevice)
			return ((AbstractDevice) o).name.equals(this.name) && ((AbstractDevice) o).description
					.equals(this.description);
		return false;
	}

	@Override public int hashCode() {
		return (this.name + this.description).hashCode();
	}

	/**
	 * This method checks if the current device is connected with the Link
	 *
	 * @return true if the connection is established
	 */
	public abstract boolean isConnected();

	/**
	 * This method provides a wrapper for validation of the connection.
	 * Connection is validated based on the device communication implementation.
	 *
	 * @return true if the connection is still active
	 */
	public abstract boolean validateConnection();

	/**
	 * Call this method to initiate connection with the Link device that was detected
	 */
	public abstract void connect();

	/**
	 * Call this method to abort the connection
	 */
	public abstract void disconnect();

	/**
	 * This is a wrapper for sending data to the device.
	 * Implementation of this method is specific to the device.
	 *
	 * @param dataPacket Byte array representation of the data to be sent
	 * @param force	Forces sending of data even if current device has send interval limit set
	 */
	public abstract void sendData(byte[] dataPacket, boolean force);

	/**
	 * This is a wrapper for sending data to the device.
	 * Implementation of this method is specific to the device.
	 *
	 * This method will send the data only if the sending interval is not exceeded
	 *
	 * @param dataPacket Byte array representation of the data to be sent
	 */
	public final void sendData(byte[] dataPacket) {
		this.sendData(dataPacket, false);
	}

}
