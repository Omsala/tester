package messages;

import org.junit.Test;
import ospp.bookinggui.Airport;
import ospp.bookinggui.Date;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Seat;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.RequestFlightDetailsRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestRequestFlightDetailsResp {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "15&1337&" +
			"1&" +
			"1&ARN&Arlanda&" +
			"2&FOO&FooBar&" +
			"1992&03&31&12&00&00&" +
			"2015&05&25&12&00&00&" +
			"3&" +
			"1&3&FIRST&true&12&12&false&" +
			"2&3&ECO&true&12&13&false&";

		RequestFlightDetailsRespMsg msg = (RequestFlightDetailsRespMsg) Message.parseMessage(data);

		Flight flight = msg.getFlight();

		assertEquals("1", flight.getFlightID());
		assertEquals("3", flight.getFlightNumber());

		Airport from = flight.getFrom();
		Airport to = flight.getTo();

		assertEquals("1", from.getAirportID());
		assertEquals("ARN", from.getIATA());
		assertEquals("Arlanda", from.getName());

		assertEquals("2", to.getAirportID());
		assertEquals("FOO", to.getIATA());
		assertEquals("FooBar", to.getName());

		Date departure = flight.getDeparture();
		Date arrival = flight.getArrival();

		assertEquals("1992", departure.getYear());
		assertEquals("03", departure.getMonth());
		assertEquals("31", departure.getDay());
		assertEquals("12", departure.getHour());
		assertEquals("00", departure.getMinute());
		assertEquals("00", departure.getSecond());

		assertEquals("2015", arrival.getYear());
		assertEquals("05", arrival.getMonth());
		assertEquals("25", arrival.getDay());
		assertEquals("12", arrival.getHour());
		assertEquals("00", arrival.getMinute());
		assertEquals("00", arrival.getSecond());

		Seat[] seats = msg.getSeatList();

		assertEquals(2, seats.length);

		Seat s1 = seats[0];
		Seat s2 = seats[1];

		assertEquals("1", s1.getSeatID());
		assertEquals("3", s1.getFlightID());
		assertEquals("FIRST", s1.getKlass());
		assertEquals("true", s1.getWindow());
		assertEquals("12", s1.getRow());
		assertEquals("12", s1.getCol());
		assertEquals("true", s1.getLocked());

		assertEquals("2", s2.getSeatID());
		assertEquals("3", s2.getFlightID());
		assertEquals("ECO", s2.getKlass());
		assertEquals("true", s2.getWindow());
		assertEquals("12", s2.getRow());
		assertEquals("13", s2.getCol());
		assertEquals("false", s2.getLocked());
	}

	@Test
	public void testFail1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "15&1337&" +
			"1&" +
			"1&ARN&Arlanda&" +
			"2&FOO&FooBar&" +
			"2015&05&25&12&00&00&" +
			"3&" +
			"1&3&FIRST&true&12&12&false&" +
			"2&3&ECO&true&12&13&false&";

		try {
			Message.parseMessage(data);
			fail("TestRequestFlightDetailsResp.testFail1() did not throw an exception!");
		}
		catch(IllegalArgumentException e) {
			assertEquals("The body was not properly formed to be read in ReqFlightDetResp!", e.getMessage());
		}
	}
}
