package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class RequestSeatSuggestionMsg extends Message {

	private final String FLIGHT_ID;

	public RequestSeatSuggestionMsg(long timestamp, String flight_id) {
		super(MessageType.REQ_SEAT_SUGGESTION, timestamp, new String[]{flight_id});
		this.FLIGHT_ID = flight_id;
	}

	public String getFlightID() {
		return this.FLIGHT_ID;
	}
}
