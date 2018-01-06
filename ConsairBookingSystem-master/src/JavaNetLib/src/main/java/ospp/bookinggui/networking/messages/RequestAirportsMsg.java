package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class RequestAirportsMsg extends Message {

	public final String airport_id;

	public RequestAirportsMsg(long timestamp, String airport_id) {
		super(MessageType.REQ_AIRPORTS, timestamp, airport_id);
		this.airport_id = airport_id;
	}
}
