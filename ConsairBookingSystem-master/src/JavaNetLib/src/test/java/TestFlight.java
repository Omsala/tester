import org.junit.Test;
import ospp.bookinggui.Airport;
import ospp.bookinggui.Date;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Seat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestFlight {

	@Test
	public void createBody1() {

		Airport from = new Airport("12", "ARN", "Arlanda");
		Airport to = new Airport("13", "FOO", "FooBar");

		Date departure = new Date("1992", "03", "31", "12", "00", "00");
		Date arrival = new Date("2015", "05", "25", "15", "32", "20");

		Flight f = new Flight("TJOFRÄS", from, to, departure, arrival, "2");

		String[] expected_body = new String[] {
			"TJOFRÄS",
			"12", "ARN", "Arlanda",
			"13", "FOO", "FooBar",
			"1992", "03", "31", "12", "00", "00",
			"2015", "05", "25", "15", "32", "20",
			"2"
		};

		assertArrayEquals(expected_body, f.createBody());
	}

	@Test
	public void parse1() {
		String[] data = new String[] {
			"TJOFRÄS",
			"12", "ARN", "Arlanda",
			"13", "FOO", "FooBar",
			"1992", "03", "31", "12", "00", "00",
			"2015", "05", "25", "15", "32", "20",
			"2"
		};

		Flight flight = Flight.parseBody(data, 0);

		assertEquals("TJOFRÄS", flight.getFlightID());
		assertEquals("2", flight.getFlightNumber());

		Airport from = flight.getFrom();
		Airport to = flight.getTo();

		Date departure = flight.getDeparture();
		Date arrival = flight.getArrival();

		assertEquals("12", from.getAirportID());
		assertEquals("ARN", from.getIATA());
		assertEquals("Arlanda", from.getName());

		assertEquals("13", to.getAirportID());
		assertEquals("FOO", to.getIATA());
		assertEquals("FooBar", to.getName());

		assertEquals("1992", departure.getYear());
		assertEquals("03", departure.getMonth());
		assertEquals("31", departure.getDay());
		assertEquals("12", departure.getHour());
		assertEquals("00", departure.getMinute());
		assertEquals("00", departure.getSecond());

		assertEquals("2015", arrival.getYear());
		assertEquals("05", arrival.getMonth());
		assertEquals("25", arrival.getDay());
		assertEquals("15", arrival.getHour());
		assertEquals("32", arrival.getMinute());
		assertEquals("20", arrival.getSecond());
	}
}
