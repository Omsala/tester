package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Airport;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class SearchAirportRouteMsg extends Message {

	private final String from;
	private final String to;

	public SearchAirportRouteMsg(long timestamp, String from_id, String to_id) {
		super(MessageType.SEARCH_ROUTE, timestamp, new String[]{from_id, to_id});
		this.from = from_id;
		this.to = to_id;
	}

	public String getFrom() {
		return this.from;
	}

	public String getTo() {
		return this.to;
	}
}
