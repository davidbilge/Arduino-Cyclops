package de.davidbilge.cyclopse_controller;

import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.davidbilge.cyclopse_controller.serial.SerialPortFactory;

/**
 * Hello world!
 * 
 */
public class App {
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws IOException {
		LOGGER.info("Startup ...");
		LOGGER.info("Available serial ports are " + SerialPortFactory.listSerialPortIdentifiers() + ".");

		SerialPortFactory.listSerialPortIdentifiers();

		SerialPort serialPort = SerialPortFactory.openPort("COM3");
		OutputStream os = serialPort.getOutputStream();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(os));
		InputStream is = serialPort.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));

		try {
			Thread.sleep(2000); // Wait for device to boot ... kinda uncool
		} catch (InterruptedException e) {
			LOGGER.error("Unable to wait", e);
		}

		LOGGER.info("Blinking LED ...");
		out.println("[blink_led(3)]");
		out.flush();

		while (is.available() < 1) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				LOGGER.error("Unable to wait", e);
			}
		}

		String answer = in.readLine();
		if (answer.equalsIgnoreCase("S")) {
			LOGGER.info("Blinked LED.");
		}

		out.close();
		is.close();
		serialPort.close();

		// BufferedWriter writer = new BufferedWriter()

		// BufferedImage img = ImageIO.read(new File(""));
		// Color c = new Color(img.getRGB(100, 100));
		//
		// System.out.println(c.getRed() + "/" + c.getGreen() + "/" +
		// c.getBlue());
	}
}
