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

package net.mcreator.minecraft.link.event;

import net.mcreator.minecraft.link.devices.AbstractDevice;

/**
 * This event is registered when a custom message is received by a device.
 */
public class LinkCustomMessageReceivedEvent extends MCreatorLinkEvent {

	private byte[] data;

	/**
	 * Constructor of LinkCustomMessageReceivedEvent
	 *
	 * @param device The device to which the event is associated to
	 * @param data   Custom message data
	 */
	public LinkCustomMessageReceivedEvent(AbstractDevice device, byte[] data) {
		super(device);
		this.data = data;
	}

	/**
	 * Returns the custom message data. Can be used this way:
	 * <p>
	 * new String(event.getData());
	 *
	 * @return Data of the custom message.
	 */
	public byte[] getData() {
		return data;
	}

}
