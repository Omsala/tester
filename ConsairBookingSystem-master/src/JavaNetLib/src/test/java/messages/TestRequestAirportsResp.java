package messages;

import org.junit.Test;
import ospp.bookinggui.Airport;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.messages.RequestAirportsRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class TestRequestAirportsResp {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "11&123456789&12&ARN&Arlanda&";

		Message msg = Message.parseMessage(data);

		assertTrue(msg instanceof RequestAirportsRespMsg);

		RequestAirportsRespMsg resp = (RequestAirportsRespMsg) msg;

		assertEquals(MessageType.REQ_AIRPORTS_RESP, resp.getType());
		assertEquals(123456789L, resp.getTimestamp());
		assertArrayEquals(new String[]{"12", "ARN", "Arlanda"}, resp.getBody());

		Airport[] ports = resp.getAirports();

		assertTrue(ports.length == 1);

		Airport port = ports[0];

		String ex_id = "12";
		String ex_iata = "ARN";
		String ex_name = "Arlanda";

		assertEquals(ex_id, port.getAirportID());
		assertEquals(ex_iata, port.getIATA());
		assertEquals(ex_name, port.getName());
	}

	@Test
	public void parse2() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "11&1&12&ARN&Arlanda&13&KUK&KumlaAirport&";

		Message msg = Message.parseMessage(data);

		assertTrue(msg instanceof RequestAirportsRespMsg);

		RequestAirportsRespMsg resp = (RequestAirportsRespMsg) msg;

		assertEquals(MessageType.REQ_AIRPORTS_RESP, resp.getType());
		assertEquals(1L, resp.getTimestamp());

		String[] expected = new String[] {
			"12", "ARN", "Arlanda",
			"13", "KUK", "KumlaAirport"
		};

		assertArrayEquals(expected, resp.getBody());

		Airport[] airports = resp.getAirports();

		assertTrue(airports.length == 2);

		Airport one = airports[0];
		Airport two = airports[1];

		String ex_id_1 = "12";
		String ex_iata_1 = "ARN";
		String ex_name_1 = "Arlanda";

		String ex_id_2 = "13";
		String ex_iata_2 = "KUK";
		String ex_name_2 = "KumlaAirport";

		assertEquals(ex_id_1, one.getAirportID());
		assertEquals(ex_id_2, two.getAirportID());

		assertEquals(ex_iata_1, one.getIATA());
		assertEquals(ex_iata_2, two.getIATA());

		assertEquals(ex_name_1, one.getName());
		assertEquals(ex_name_2, two.getName());
	}

	@Test
	public void testCreate1() throws UnsupportedEncodingException {

		String[] body = new String[] {
			"12", "ARN", "Arlanda"
		};

		RequestAirportsRespMsg resp = new RequestAirportsRespMsg(1L, body);

		String data = resp.createMessage();

		assertEquals("11&1&12&ARN&Arlanda&", data);
	}

	@Test
	public void testCreate2() throws UnsupportedEncodingException {
		String[] body = new String[] {
			"12", "ARN", "Arlanda",
			"13", "FUU", "DickButt"
		};

		RequestAirportsRespMsg resp = new RequestAirportsRespMsg(1L, body);

		String data = resp.createMessage();

		String expected = "11&1&12&ARN&Arlanda&13&FUU&DickButt&";

		assertEquals(expected, data);
	}

//	@Test
//	public void testFail1() {
//		String[] body = new String[] {
//			"12", "ARN", "Arlanda",
//			"13"
//		};
//
//		try {
//			RequestAirportsRespMsg resp = new RequestAirportsRespMsg(1L, body);
//			fail("Creating a RequestAirportRespMsg with incorrect body does not throw an exception!");
//		}
//		catch(IllegalArgumentException e) {
//			assertEquals("Airport.parseBodyToArray() was given an array not divisable by three after subtracting the offset!", e.getMessage());
//		}
//	}
}
