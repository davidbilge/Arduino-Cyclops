package de.davidbilge.cyclopse_controller;

import static java.lang.Math.acos;
import static java.lang.Math.atan;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.davidbilge.cyclopse_controller.serial.SerialPortFactory;

/**
 * Hello world!
 * 
 */
public class App {
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	private static final double len_arm_2 = 180;
	private static final double len_arm_1 = 180;
	private static final int steps_per_revolution = 1600;

	public static void main(String[] args) throws IOException {
		LOGGER.info("Startup ...");
		LOGGER.info("Available serial ports are " + SerialPortFactory.listSerialPortIdentifiers() + ".");

		ArduinoCommandInterface w = new ArduinoCommandInterface("COM3");
		w.executeCommand("[blink_led(3)]");
		w.close();

		// BufferedWriter writer = new BufferedWriter()

		// BufferedImage img = ImageIO.read(new File(""));
		// Color c = new Color(img.getRGB(100, 100));
		//
		// System.out.println(c.getRed() + "/" + c.getGreen() + "/" +
		// c.getBlue());
	}

	// ALPHA
	static double getAngleAlpha(int x, int y) {
		double d = Math.sqrt(x * x + y * y);

		return acos(-((len_arm_2 * len_arm_2 - len_arm_1 * len_arm_1 - d * d) / (2 * len_arm_1 * d)));
	}

	// BETA
	static double getAngleBeta(int x, int y) {
		double d = Math.sqrt(x * x + y * y);

		return acos((len_arm_2 * len_arm_2 + d * d - len_arm_1 * len_arm_1) / (2 * len_arm_2 * d));
	}

	static double getAngleM1(int x, int y) {
		return (Math.PI / 2) - getAngleAlpha(x, y) - atan(y / x);
	}

	static double getAngleM2(int x, int y) {
		return getAngleAlpha(x, y) + getAngleBeta(x, y) - getAngleM1(x, y);
	}

	static int angleToMC(double angle) {
		// angle %= Math.PI * 2;
		return (int) ((angle / Math.PI * 2) * steps_per_revolution);
	}
}
