package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class TerminateServerMsg extends Message {

	public TerminateServerMsg(long timestamp) {
		super(MessageType.TERMINATE_SERVER, timestamp, null);
	}
}
