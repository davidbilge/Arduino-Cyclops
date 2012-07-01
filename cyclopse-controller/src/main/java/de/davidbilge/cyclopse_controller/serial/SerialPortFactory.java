package de.davidbilge.cyclopse_controller.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class SerialPortFactory {
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(SerialPortFactory.class);

	private static final int BAUD_RATE = 9600;

	private SerialPortFactory() {
	}

	public static List<String> listSerialPortIdentifiers() {
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> identifiers = CommPortIdentifier.getPortIdentifiers();

		List<String> portNames = new ArrayList<String>();

		while (identifiers.hasMoreElements()) {
			CommPortIdentifier identifier = identifiers.nextElement();
			int portType = identifier.getPortType();
			if (portType == CommPortIdentifier.PORT_SERIAL) {
				portNames.add(identifier.getName());
			}
		}

		return portNames;
	}

	public static SerialPort openPort(String portName) {
		CommPortIdentifier id;
		try {
			id = CommPortIdentifier.getPortIdentifier(portName);
		} catch (NoSuchPortException e) {
			throw new SerialCommunicationException("Unable to open port", e);
		}

		if (id.isCurrentlyOwned()) {
			throw new SerialCommunicationException("Port is already in use");
		}

		CommPort commPort;
		try {
			commPort = id.open("CyclopseController", 2000);
		} catch (PortInUseException e) {
			throw new SerialCommunicationException("Port is already in use", e);
		}

		if (commPort instanceof SerialPort) {
			SerialPort serialPort = (SerialPort) commPort;
			try {
				serialPort.setSerialPortParams(BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				throw new SerialCommunicationException("Unable to set serial port parameters", e);
			}

			return serialPort;
		} else {
			throw new SerialCommunicationException("Expected a SerialPort but found a " + commPort.getClass().getSimpleName() + " instead.");
		}

	}
}
