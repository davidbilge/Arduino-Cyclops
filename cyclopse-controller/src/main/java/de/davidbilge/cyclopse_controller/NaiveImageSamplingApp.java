package de.davidbilge.cyclopse_controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NaiveImageSamplingApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(NaiveImageSamplingApp.class);

	private static final int MAX_X = 200;
	private static final int MAX_Y = 200;

	public static void main(String[] args) throws IOException {
		BufferedImage img = ImageIO.read(new File(args[0]));

		final BufferedImage scaledImg;
		if (img.getWidth() > img.getHeight()) {
			scaledImg = Scalr.resize(img, MAX_X);
		} else {
			scaledImg = Scalr.resize(img, MAX_Y);
		}

		drawImage(scaledImg);

	}

	private static void drawImage(BufferedImage scaledImg) throws IOException {
		try (ArduinoCommandInterface arduinoCommandInterface = new ArduinoCommandInterface("COM3")) {
			LedWriter ledWriter = new LedWriter(arduinoCommandInterface);

			for (int x = 0; x < scaledImg.getWidth(); ++x) {
				for (int y = 0; y < scaledImg.getHeight(); ++y) {
					Color color = getColor(scaledImg, x, y);
					ledWriter.write(color, x, y);
					LOGGER.debug("Wrote " + color + " to (" + x + ", " + y + ")");
				}
			}
		}
	}

	private static final Color getColor(BufferedImage img, int x, int y) {
		int rgb = img.getRGB(x, y);
		return new Color(rgb);

	}
}
