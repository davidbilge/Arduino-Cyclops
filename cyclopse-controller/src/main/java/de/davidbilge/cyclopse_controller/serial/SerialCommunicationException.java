package de.davidbilge.cyclopse_controller.serial;

public class SerialCommunicationException extends RuntimeException {
	private static final long serialVersionUID = 7424689673299062646L;

	public SerialCommunicationException(String message) {
		super(message);
	}

	public SerialCommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
