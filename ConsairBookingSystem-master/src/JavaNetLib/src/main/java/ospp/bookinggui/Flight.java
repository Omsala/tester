package ospp.bookinggui;

public class Flight {

	public static final int ARG_AMOUNT = 2 + Airport.ARG_AMOUNT * 2 + Date.ARG_AMOUNT * 2;

	private final String  FLIGHT_ID;
	private final Airport from;
	private final Airport to;
	private final Date    departure;
	private final Date    arrival;
	private final String  FLIGHT_NR;

	public Flight(String flight_id, Airport from, Airport to, Date depart_date, Date arrive_date, String flight_nr) {
		this.FLIGHT_ID = flight_id;
		this.from = from;
		this.to = to;
		this.departure = depart_date;
		this.arrival = arrive_date;
		this.FLIGHT_NR = flight_nr;
	}

	public static Flight parseBody(String[] body, int offset) {
		String flight_id = body[offset++];

		Airport from = Airport.parseBody(body, offset);
		offset += Airport.ARG_AMOUNT;

		Airport to = Airport.parseBody(body, offset);
		offset += Airport.ARG_AMOUNT;

		Date depart_date = Date.parseBody(body, offset);
		offset += Date.ARG_AMOUNT;

		Date arrive_date = Date.parseBody(body, offset);
		offset += Date.ARG_AMOUNT;

		String flight_nr = body[offset];

		return new Flight(flight_id, from, to, depart_date, arrive_date, flight_nr);
	}

	public String getFlightID() {
		return this.FLIGHT_ID;
	}

	public Airport getFrom() {
		return this.from;
	}

	public Airport getTo() {
		return this.to;
	}

	public Date getDeparture() {
		return this.departure;
	}

	public Date getArrival() {
		return this.arrival;
	}

	public String getFlightNumber() {
		return this.FLIGHT_NR;
	}

	public String[] createBody() {
		String[] from_bod = from.createBody();
		String[] to_bod = to.createBody();

		String[] depart_bod = departure.createBody();
		String[] arrive_bod = arrival.createBody();

		String[] body = new String[from_bod.length + to_bod.length + depart_bod.length + arrive_bod.length + 2];

		int index = 0;

		body[index++] = FLIGHT_ID;

		System.arraycopy(from_bod, 0, body, index, from_bod.length);
		index += from_bod.length;

		System.arraycopy(to_bod, 0, body, index, to_bod.length);
		index += to_bod.length;

		System.arraycopy(depart_bod, 0, body, index, depart_bod.length);
		index += depart_bod.length;

		System.arraycopy(arrive_bod, 0, body, index, arrive_bod.length);
		index += arrive_bod.length;

		body[index] = FLIGHT_NR;

		return body;
	}
}
