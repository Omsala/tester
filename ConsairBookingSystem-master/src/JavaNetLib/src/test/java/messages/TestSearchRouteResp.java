package messages;

import org.junit.Test;
import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.messages.SearchAirportRouteRespMsg;

import static org.junit.Assert.*;

public class TestSearchRouteResp {

	@Test
	public void parse1() {
		String[] body = new String[]{
			"TJOFRÄS",
			"12", "ARN", "Arlanda",
			"13", "FOO", "FooBar",
			"1992", "03", "31", "12", "00", "00",
			"2015", "12", "24", "12", "00", "00",
			"2",
			"YOLO",
			"12", "ARN", "Arlanda",
			"13", "FOO", "FooBar",
			"1992", "03", "31", "12", "00", "00",
			"2015", "12", "24", "12", "00", "00",
			"3"
		};

		SearchAirportRouteRespMsg resp = new SearchAirportRouteRespMsg(1337L, body);

		Flight[] flights = resp.getFlightList();

		assertTrue(flights.length == 2);

		Flight one = flights[0];
		Flight two = flights[1];

		assertEquals("TJOFRÄS", one.getFlightID());
		assertEquals("YOLO", two.getFlightID());
	}

	@Test
	public void parseIncorrect1() {
		String[] body = new String[]{
			"TJOFRÄS",
			"12", "ARN", "Arlanda",
			"1992", "03", "31", "12", "00", "00"
		};

		try {
			SearchAirportRouteRespMsg resp = new SearchAirportRouteRespMsg(1337L, body);
			fail("TestSearchRouteResp.parseIncorrect1() did not catch exception!");
		}
		catch(Exception e) {

		}
	}

	@Test
	public void parseIncorrect2() {
		String[] body = new String[]{
			"TJOFRÄS",
			"1992", "03", "31", "12", "00", "00",
			"2"
		};

		try {
			SearchAirportRouteRespMsg resp = new SearchAirportRouteRespMsg(1337L, body);
			fail("TestSearchRouteResp.parseIncorrect2() did not catch exception!");
		}
		catch(Exception e) {

		}
	}
}
