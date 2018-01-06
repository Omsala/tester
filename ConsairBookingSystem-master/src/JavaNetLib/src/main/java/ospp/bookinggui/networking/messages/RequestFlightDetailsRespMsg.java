package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Flight;
import ospp.bookinggui.Seat;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class RequestFlightDetailsRespMsg extends Message {

	private final Flight flight;
	private final Seat[] seat_list;

	public RequestFlightDetailsRespMsg(long timestamp, String[] body) {
		super(MessageType.REQ_FLIGHT_DETAILS_RESP, timestamp, body);

		if((body.length - Flight.ARG_AMOUNT) % Seat.ARG_AMOUNT != 0) {
			throw new IllegalArgumentException("The body was not properly formed to be read in ReqFlightDetResp!");
		}

		this.flight = Flight.parseBody(body, 0);

		Seat[] seats = new Seat[(body.length - Flight.ARG_AMOUNT) / Seat.ARG_AMOUNT];

		for(int i = 0; i < seats.length; i++) {
			seats[i] = Seat.parseBody(body, (Flight.ARG_AMOUNT + i * Seat.ARG_AMOUNT));
		}

		this.seat_list = seats;
	}

	public Flight getFlight() {
		return this.flight;
	}

	public Seat[] getSeatList() {
		return this.seat_list.clone();
	}
}
