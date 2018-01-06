package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Seat;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class RequestSeatSuggestionRespMsg extends Message {

	private final Seat[] seat_list;

	public RequestSeatSuggestionRespMsg(long timestamp, String[] body) {
		super(MessageType.REQ_SEAT_SUGGESTION_RESP, timestamp, body);

		if(body.length % Seat.ARG_AMOUNT != 0) {
			throw new IllegalArgumentException("RequestSeatSuggestionRespMsg received malformed body!");
		}

		Seat[] seats = new Seat[body.length / Seat.ARG_AMOUNT];

		for(int i = 0; i < seats.length; i++) {
			seats[i] = Seat.parseBody(body, i * Seat.ARG_AMOUNT);
		}

		this.seat_list = seats;
	}

	public Seat[] getSeatList() {
		return this.seat_list.clone();
	}
}
