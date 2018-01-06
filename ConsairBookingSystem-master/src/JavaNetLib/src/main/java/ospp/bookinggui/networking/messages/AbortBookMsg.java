package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class AbortBookMsg extends Message {

	public AbortBookMsg(long timestamp) {
		super(MessageType.ABORT_BOOK, timestamp, null);
	}
}
