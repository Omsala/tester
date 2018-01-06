package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class ErrorMsg extends Message {

	private final String error_message;

	public ErrorMsg(long timestamp, String message) {
		super(MessageType.ERROR, timestamp, message);
		this.error_message = message;
	}

	public String getErrorMessage() {
		return this.error_message;
	}
}