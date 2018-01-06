import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.messages.LoginMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class TestMessage {

	@Test
	public void parseRandomData1() throws UnsupportedEncodingException {
		byte[] data = new byte[]{
			0x23, 0x21, 0x65, (byte) 0xf1, (byte) 0x88, 0x76
		};

		try {
			Message.parseMessage(new String(data));
			fail("Message.parseMessage did not throw the correct exceptions with random data!");
		}
		catch(MalformedMessageException ignored) {
		}
	}

	@Test
	public void testParseLogin() throws MalformedMessageException, UnsupportedEncodingException {
		String data = "1&100203002&tjenare&yolo&";

		Message msg = Message.parseMessage(data);

		assertEquals(MessageType.LOGIN, msg.getType());
		assertEquals(100203002L, msg.getTimestamp());

		String[] body = msg.getBody();

		assertArrayEquals(new String[]{"tjenare", "yolo"}, body);
	}

	@Test
	public void constructMessage1() throws UnsupportedEncodingException {

		long timestamp = 1337L;

		LoginMsg loginMsg = new LoginMsg(timestamp, "göran", "persson");

		String data = loginMsg.createMessage();

		assertEquals("1&1337&göran&persson&", data);
	}

	@Test
	public void constructMessage2() throws UnsupportedEncodingException {
		long timestamp = 1338L;

		Message msg = new Message(MessageType.LOGIN, timestamp, "arg1", "arg2", "arg3");

		String data = msg.createMessage();

		assertEquals("1&1338&arg1&arg2&arg3&", data);
	}

	@Test
	public void parseIncorrectData() throws UnsupportedEncodingException {
		String data = "asddisfuhs839i2no34";

		try {
			Message.parseMessage(data);
			fail("Parsing with incorrect data did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("Could not parse message! The message is too small!", e.getMessage());
		}
	}

	@Test
	public void parseIncorrectID() throws UnsupportedEncodingException {
		String data = "sdflk&lkaksjd&";

		try {
			Message.parseMessage(data);
			fail("Parsing with incorrect id did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("The message ID is not an integer!", e.getMessage());
		}
	}

	@Test
	public void parseIncorrectID2() throws UnsupportedEncodingException {
		String data = "1231241422&1231243245&";

		try {
			Message.parseMessage(data);
			fail("Parsing with unsupported id did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("The supplied message ID is not supported!", e.getMessage());
		}
	}

	@Test
	public void parseIncorrectTimestamp() throws UnsupportedEncodingException {
		String data = "1&234234jkj24&";

		try {
			Message.parseMessage(data);
			fail("Parsing with incorrect timestamp did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("The timestamp is not a valid long!", e.getMessage());
		}
	}

//	@Test
//	public void constructSpecialChars() throws UnsupportedEncodingException {
//
//		String arg1 = "&&&&";
//		String arg2 = "(/&(¤%$";
//
//		Message msg = new Message(MessageType.DISCONNECT, 1337L, arg1, arg2);
//
//		String data = msg.createMessage();
//
//		assertEquals("3&1337&%26%26%26%26&%28%2F%26%28%C2%A4%25%24&", data);
//	}

//	@Test
//	public void parseSpecialChars() throws UnsupportedEncodingException, MalformedMessageException {
//		String data = "1&1337&%26%26%26%26&%28%2F%26%28%C2%A4%25%24&";
//
//		Message msg = Message.parseMessage(data);
//
//		assertTrue(msg instanceof LoginMsg);
//		assertArrayEquals(new String[]{"&&&&", "(/&(¤%$"}, msg.getBody());
//	}

	@Test
	public void constructWithNullVararg() throws UnsupportedEncodingException {
		Message msg = new Message(MessageType.LOGIN, 1337L, null);

		String constructed = msg.createMessage();

		String expected = "1&1337&";

		assertEquals(expected, constructed);
	}

	@Test
	public void constructWithNullVararg2() throws UnsupportedEncodingException {
		Message msg = new Message(MessageType.LOGIN, 1337L, null, "tjosan");

		String constructed = msg.createMessage();

		String expected = "1&1337&";

		assertEquals(expected, constructed);
	}

	@Test
	public void constructWithNullVararg3() throws UnsupportedEncodingException {
		Message msg = new Message(MessageType.LOGIN, 1337, "tjosan", null);

		String constructed = msg.createMessage();

		String expected = "1&1337&";

		assertEquals(expected, constructed);
	}

	@Test
	public void constructWithEmptyVararg() throws UnsupportedEncodingException {
		Message msg = new Message(MessageType.LOGIN, 1337);

		String constructed = msg.createMessage();

		String expected = "1&1337&";

		assertEquals(expected, constructed);
	}
}