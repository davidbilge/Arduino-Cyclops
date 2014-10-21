package de.davidbilge.cyclopse_controller;

import java.awt.Color;
import java.io.IOException;

public class LedWriter {
	private final ArduinoCommandInterface ci;

	public LedWriter(ArduinoCommandInterface ci) {
		this.ci = ci;
	}

	/**
	 * Move the LED arm to the specified coordinates and set the specified color
	 * to the led. Note that this method will block until the whole operation is
	 * completed.
	 * 
	 * @param c
	 *            The color to set
	 * @param x
	 *            The x part of the coordinate in millimeters
	 * @param y
	 *            The y part of the coordinate in millimeters
	 */
	public void write(Color c, int x, int y) throws IOException {
		ci.executeCommand("[moveToCoord(" + x + "," + y + ")]");
		ci.executeCommand("[setColor(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")]");
	}
}
