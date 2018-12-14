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

package net.mcreator.minecraft.link.event;

import net.mcreator.minecraft.link.devices.AbstractDevice;

/**
 * This event is registered when a digital pin logic level changes and if the pin events for the given pin are enabled.
 */
public class LinkDigitalPinChangedEvent extends MinecraftLinkEvent {

	private int pin;
	private byte value;
	private boolean risingEdge;

	/**
	 * Constructor of LinkDigitalPinChangedEvent
	 *
	 * @param device     The device to which the event is associated to
	 * @param pin        Custom message data
	 * @param value      Current pin value
	 * @param risingEdge true if the value changed from low to high
	 */
	public LinkDigitalPinChangedEvent(AbstractDevice device, int pin, byte value, boolean risingEdge) {
		super(device);
		this.pin = pin;
		this.value = value;
		this.risingEdge = risingEdge;
	}

	/**
	 * Returns the pin on which the event happened
	 *
	 * @return Pin number
	 */
	public int getPin() {
		return pin;
	}

	/**
	 * Returns current pin logic level value
	 *
	 * @return Current pin value
	 */
	public byte getValue() {
		return value;
	}

	/**
	 * Returns the type of pin value change
	 *
	 * @return true if the value changed from low to high
	 */
	public boolean isRisingEdge() {
		return risingEdge;
	}
}
