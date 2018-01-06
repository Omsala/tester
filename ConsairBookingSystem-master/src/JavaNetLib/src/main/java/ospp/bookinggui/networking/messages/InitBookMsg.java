package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class InitBookMsg extends Message {

	private final String seat_id;

	public InitBookMsg(long timestamp, String seat_id) {
		super(MessageType.INIT_BOOK, timestamp, seat_id);
		this.seat_id = seat_id;
	}

	public String getSeatID() {
		return this.seat_id;
	}
}