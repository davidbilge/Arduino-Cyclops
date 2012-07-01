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

public class ArduinoCommandInterface {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArduinoCommandInterface.class);

	private final boolean closed = false;
	private final PrintWriter out;
	private final BufferedReader in;
	private final InputStream is;
	private final SerialPort serialPort;

	public ArduinoCommandInterface(String portName) throws IOException {
		serialPort = SerialPortFactory.openPort(portName);
		OutputStream os = serialPort.getOutputStream();
		out = new PrintWriter(new OutputStreamWriter(os));
		is = serialPort.getInputStream();
		in = new BufferedReader(new InputStreamReader(is));

		try {
			Thread.sleep(2000); // Wait for arduino to establish connection ...
								// apparently
		} catch (InterruptedException e) {
			LOGGER.error("Unable to sleep", e);
		}
	}

	public void close() {
		if (!closed) {
			out.close();
			try {
				in.close();
			} catch (IOException e) {
				LOGGER.warn("Unable to close input stream", e);
			}
			serialPort.close();
		}
	}

	public void executeCommand(String command) throws IOException {
		out.println(command);
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
	}

}
