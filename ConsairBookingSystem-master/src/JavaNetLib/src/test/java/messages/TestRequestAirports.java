package messages;

import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.messages.RequestAirportsMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestRequestAirports {

	@Test
	public void testCreate1() throws UnsupportedEncodingException {
		long timestamp = 123456789L;

		RequestAirportsMsg msg = new RequestAirportsMsg(timestamp, "ARN");

		String data = msg.createMessage();

		String expected = "10&123456789&ARN&";

		assertEquals(expected, data);
	}

	@Test
	public void testCreate2() throws UnsupportedEncodingException {
		long timestamp = 123456789L;

		RequestAirportsMsg msg = new RequestAirportsMsg(timestamp, null);

		String data = msg.createMessage();

		String expected = "10&123456789&";

		assertEquals(expected, data);
	}

	@Test
	public void testParse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "10&123456789&ARN&";

		Message msg = Message.parseMessage(data);

		assertTrue(msg instanceof RequestAirportsMsg);

		RequestAirportsMsg req = (RequestAirportsMsg) msg;

		assertEquals(MessageType.REQ_AIRPORTS, req.getType());
		assertEquals(123456789L, req.getTimestamp());
		assertEquals("ARN", req.airport_id);
	}

	@Test
	public void testParse2() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "10&123456789&";

		Message msg = Message.parseMessage(data);

		assertTrue(msg instanceof RequestAirportsMsg);

		RequestAirportsMsg req = (RequestAirportsMsg) msg;

		assertEquals(MessageType.REQ_AIRPORTS, req.getType());
		assertEquals(123456789L, req.getTimestamp());
		assertNull(req.airport_id);
	}
}
