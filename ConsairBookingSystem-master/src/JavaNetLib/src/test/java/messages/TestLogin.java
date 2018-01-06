package messages;

import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.messages.LoginMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class TestLogin {

	@Test
	public void parseCorrectData() throws MalformedMessageException, UnsupportedEncodingException {
		String data = "1&1337&yo&lo&";

		Message msg = Message.parseMessage(data);

		assertTrue(msg instanceof LoginMsg);
		assertEquals(MessageType.LOGIN, msg.getType());
		assertEquals(1337L, msg.getTimestamp());
		assertArrayEquals(new String[]{"yo", "lo"}, msg.getBody());
	}

	@Test
	public void parseIncorrectData1() throws UnsupportedEncodingException {
		String data = "1&1337&";

		try {
			Message.parseMessage(data);
			fail("Parsing Login message with no body did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("Could not parse message! The body is not correctly formed!", e.getMessage());
		}
	}

	@Test
	public void parseIncorrectData2() throws UnsupportedEncodingException {
		String data = "1&1337&yolo";

		try {
			Message.parseMessage(data);
			fail("Parsing Login message with a too small body did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("Could not parse message! The body is not correctly formed!", e.getMessage());
		}
	}
}
