package messages;

import org.junit.Test;
import ospp.bookinggui.Airport;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.SearchAirportRouteMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestSearchRoute {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "12&123&1&2&";

		SearchAirportRouteMsg msg = (SearchAirportRouteMsg) Message.parseMessage(data);

		Airport from = new Airport("1", "ARN", "Arlanda");
		Airport to = new Airport("2", "FOO", "FooBar");

		assertEquals(from.getAirportID(), msg.getFrom());

		assertEquals(to.getAirportID(), msg.getTo());
	}

//	@Test
//	public void create1() throws UnsupportedEncodingException {
//		Airport from = new Airport("1", "ARN", "Arlanda");
//		Airport to = new Airport("2", "FOO", "FooBar");
//
//		SearchAirportRouteMsg msg = new SearchAirportRouteMsg(1337L, SearchAirportRouteMsg.constructBody(from, to));
//
//		assertEquals("12&1337&1&ARN&Arlanda&2&FOO&FooBar&", msg.createMessage());
//	}
}
