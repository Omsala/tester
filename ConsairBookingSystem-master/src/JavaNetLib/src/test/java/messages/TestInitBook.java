package messages;

import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.InitBookMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestInitBook {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "5&1336&127&";

		InitBookMsg msg = (InitBookMsg) Message.parseMessage(data);

		assertEquals("127", msg.getSeatID());
	}

	@Test
	public void parseIncorrectBody() {
		String data = "5&1331&";

		try {
			Message.parseMessage(data);
			fail("TestInitBook.parseIncorrectBody() did not catch exception!");
		}
		catch(MalformedMessageException | UnsupportedEncodingException e) {
		}
	}

	@Test
	public void create1() throws UnsupportedEncodingException {
		InitBookMsg msg = new InitBookMsg(1337L, "127");

		assertEquals("5&1337&127&", msg.createMessage());
	}
}
