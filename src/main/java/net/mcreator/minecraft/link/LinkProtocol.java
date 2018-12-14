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

import net.mcreator.minecraft.link.devices.PinMode;

/**
 * Class that contains helper methods to convert data to and from Minecraft Link messages.
 */
public final class LinkProtocol {

	public static final byte[] IDENT_REQUEST = "ident?\n".getBytes();

	public static final byte[] START_POLLING_INPUTS = "pstrt?\n".getBytes();
	public static final byte[] STOP_POLLING_INPUTS = "pstop?\n".getBytes();

	public static byte[] createDigitalWriteMessage(int port, byte value) {
		return ("diwrt?" + port + ":" + value + "\n").getBytes();
	}

	public static byte[] createAnalogWriteMessage(int port, short value) {
		return ("anwrt?" + port + ":" + value + "\n").getBytes();
	}

	public static byte[] createPinmodeMessage(int port, PinMode pinMode) {
		return ("pinmd?" + port + ":" + pinMode.name().toLowerCase() + "\n").getBytes();
	}

	public static boolean isDigitalInputsStatusMessage(byte[] message) {
		return new String(message).startsWith("digrd:") && new String(message).split(":").length == 2;
	}

	public static boolean isAnalogInputsStatusMessage(byte[] message) {
		return new String(message).startsWith("anlrd:") && new String(message).split(":").length == 2;
	}

	public static byte[] parseDigitalInputsMessage(byte[] message) {
		char[] data = new String(message).split(":")[1].trim().toCharArray();
		byte[] retval = new byte[data.length];
		for (int i = 0; i < data.length; i++)
			retval[i] = (byte) (data[i] == '0' ? 0 : 1);
		return retval;
	}

	public static short[] parseAnalogInputsMessage(byte[] message) {
		String[] data = new String(message).split(":")[1].trim().split("#");
		short[] retval = new short[data.length];
		for (int i = 0; i < data.length; i++)
			retval[i] = Short.parseShort(data[i].trim());
		return retval;
	}

}
