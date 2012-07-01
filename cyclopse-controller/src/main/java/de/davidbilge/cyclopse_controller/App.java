package de.davidbilge.cyclopse_controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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

		BufferedImage img = ImageIO.read(new File(""));
		Color c = new Color(img.getRGB(100, 100));

		System.out.println(c.getRed() + "/" + c.getGreen() + "/" + c.getBlue());
	}
}
