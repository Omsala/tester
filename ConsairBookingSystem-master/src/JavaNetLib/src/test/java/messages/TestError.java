package messages;

import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.ErrorMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class TestError {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "4&1337247&SHIT HAPPEND&";

		ErrorMsg msg = (ErrorMsg) Message.parseMessage(data);

		assertEquals("SHIT HAPPEND", msg.getErrorMessage());
	}

	@Test
	public void parse2() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "4&1337247&HOLY SHITBRICK BATMAN&";

		ErrorMsg msg = (ErrorMsg) Message.parseMessage(data);

		assertEquals("HOLY SHITBRICK BATMAN", msg.getErrorMessage());
	}

	@Test
	public void create1() throws UnsupportedEncodingException {
		ErrorMsg msg = new ErrorMsg(1337247L, "HOLY SHITBRICK BATMAN");

		assertEquals("HOLY SHITBRICK BATMAN", msg.getErrorMessage());

		assertEquals("4&1337247&HOLY SHITBRICK BATMAN&", msg.createMessage());
	}
}
