package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class RequestFlightDetailsMsg extends Message {

	private final String flight_id;

	public RequestFlightDetailsMsg(long timestamp, String flight_ID) {
		super(MessageType.REQ_FLIGHT_DETAILS, timestamp, new String[]{flight_ID});
		this.flight_id = flight_ID;
	}

	public String getFlightID() {
		return this.flight_id;
	}
}
