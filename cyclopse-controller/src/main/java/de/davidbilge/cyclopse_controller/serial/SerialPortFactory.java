package de.davidbilge.cyclopse_controller.serial;

import java.util.Arrays;
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public final class SerialPortFactory {
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(SerialPortFactory.class);

	private static final int BAUD_RATE = 9600;

	private SerialPortFactory() {
	}

	public static List<String> listSerialPortIdentifiers() {
		return Arrays.asList(SerialPortList.getPortNames());
	}

	public static SerialPort openPort(String portName) {
		SerialPort serialPort = new SerialPort(portName);

		try {
			serialPort.openPort();
			serialPort.setParams(BAUD_RATE, 8, 1, 0);
			return serialPort;
		} catch (SerialPortException e) {
			throw new SerialCommunicationException("Unable to open port with name " + portName, e);
		}
	}
}
