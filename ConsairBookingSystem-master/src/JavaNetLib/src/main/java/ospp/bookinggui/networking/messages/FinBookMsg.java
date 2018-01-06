package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class FinBookMsg extends Message {

	public FinBookMsg(long timestamp) {
		super(MessageType.FIN_BOOK, timestamp, null);
	}
}
