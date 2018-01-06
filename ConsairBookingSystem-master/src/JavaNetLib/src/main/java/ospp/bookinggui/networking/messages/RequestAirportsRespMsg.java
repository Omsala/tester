package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Airport;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class RequestAirportsRespMsg extends Message {

	private final Airport[] airports;

	public RequestAirportsRespMsg(long timestamp, String[] body) {
		super(MessageType.REQ_AIRPORTS_RESP, timestamp, body);
		this.airports = Airport.parseBodyToArray(body, 0);
	}

	public Airport[] getAirports() {
		return this.airports.clone();
	}
}
