package messages;

import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.LoginRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestLoginResp {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "2&126&1&";

		LoginRespMsg msg = (LoginRespMsg) Message.parseMessage(data);

		assertEquals(LoginRespMsg.PrivilegeLevel.USER, msg.getPrivilegeLevel());
	}

	@Test
	public void parseIncorrectPriv() {
		String data = "2&123&5&";

		try {
			Message.parseMessage(data);
			fail("TestLoginResp.parseIncorrectPriv() did not catch exception!");
		}
		catch(MalformedMessageException | UnsupportedEncodingException e) {
		}
	}
}
