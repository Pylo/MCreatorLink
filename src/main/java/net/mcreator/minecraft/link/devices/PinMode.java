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

import java.text.ParseException;

/**
 * Class that defines pin modes
 */
public enum PinMode {

	IN, OUT, IN_P;

	/**
	 * Used by /link command to parse input string to PinMode object
	 *
	 * @param s PinMode string representation
	 * @return PinMode object
	 * @throws ParseException If the pinmode parsing failed (unknown pinmode string)
	 */
	public static PinMode fromString(String s) throws ParseException {
		switch (s) {
		case "out":
		case "output":
			return OUT;
		case "in_p":
		case "input_pullup":
			return IN_P;
		case "in":
		case "input":
			return IN;
		default:
			throw new ParseException("Invalid pin mode: " + s, 0);
		}
	}

}
