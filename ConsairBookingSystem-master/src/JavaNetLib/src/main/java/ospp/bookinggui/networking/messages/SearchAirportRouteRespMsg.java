package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class SearchAirportRouteRespMsg extends Message {

	private final Flight[] flight_list;

	public SearchAirportRouteRespMsg(long timestamp, String[] body) {
		super(MessageType.SEARCH_ROUTE_RESP, timestamp, body);
		this.flight_list = parse(body);
	}

	private static Flight[] parse(String[] body) {
		if(body.length % Flight.ARG_AMOUNT != 0) {
			throw new IllegalArgumentException("SearchRoute cannot parse body not divisible by Flight.ARG_AMOUNT!");
		}

		Flight[] flights = new Flight[body.length / Flight.ARG_AMOUNT];

		for(int i = 0; i < flights.length; i++) {
			flights[i] = Flight.parseBody(body, i * Flight.ARG_AMOUNT);
		}

		return flights;
	}

	public Flight[] getFlightList() {
		return this.flight_list;
	}
}
