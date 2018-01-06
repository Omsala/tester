package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class DisconnectMsg extends Message {

	public DisconnectMsg(long timestamp) {
		super(MessageType.DISCONNECT, timestamp);
	}
}