package de.davidbilge.cyclopse_controller;

import java.io.IOException;

import jssc.SerialPort;
import jssc.SerialPortException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.davidbilge.cyclopse_controller.serial.SerialCommunicationException;
import de.davidbilge.cyclopse_controller.serial.SerialPortFactory;

public class ArduinoCommandInterface implements AutoCloseable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArduinoCommandInterface.class);

	private static final String LINESEP = System.lineSeparator();

	private final boolean closed = false;
	private final SerialPort serialPort;

	public ArduinoCommandInterface(String portName) throws IOException {
		serialPort = SerialPortFactory.openPort(portName);

		try {
			Thread.sleep(2000); // Wait for arduino to establish connection ...
								// apparently
		} catch (InterruptedException e) {
			LOGGER.error("Unable to sleep", e);
		}
	}

	@Override
	public void close() {
		if (!closed) {
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				throw new SerialCommunicationException("Unable to close serial port", e);
			}
		}
	}

	public void executeCommand(String command) throws IOException {
		LOGGER.info("-> " + command);

		try {
			serialPort.writeString(command);

		} catch (SerialPortException e) {
			throw new IOException(e);
		}

		String answer;
		try {
			answer = readLine();
		} catch (SerialPortException e) {
			throw new IOException(e);
		}

		if (answer.equalsIgnoreCase("S")) {
			LOGGER.info("Received 'success' answer.");
		} else {
			throw new SerialCommunicationException("No success received, answer was " + answer);
		}
	}

	private String readLine() throws SerialPortException {
		StringBuilder sb = new StringBuilder();

		boolean lineFinished = false;

		while (!lineFinished) {
			while (serialPort.getInputBufferBytesCount() < 1) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					LOGGER.error("Unable to wait", e);
				}

			}

			sb.append(serialPort.readString());

			lineFinished = sb.substring(sb.length() - LINESEP.length()).equals(LINESEP);
		}

		String answer = sb.substring(0, sb.length() - LINESEP.length());
		return answer;
	}

}
