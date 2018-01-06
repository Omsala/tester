package messages;

import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.FinBookRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestFinBookResp {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "8&1337247&1&";

		FinBookRespMsg resp = (FinBookRespMsg) Message.parseMessage(data);

		assertEquals(FinBookRespMsg.ResponseCode.SUCCESS, resp.getResponseCode());
	}

	@Test
	public void pars2() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "8&1337247&2&";

		FinBookRespMsg resp = (FinBookRespMsg) Message.parseMessage(data);

		assertEquals(FinBookRespMsg.ResponseCode.FAILED, resp.getResponseCode());
	}

	@Test
	public void parseIncorrectResponseCode() {
		String data = "8&1337&3";

		try {
			Message.parseMessage(data);
			fail("TestFinBookResp.parseIncorrectResponseCode() did not catch exception!");
		}
		catch(MalformedMessageException | UnsupportedEncodingException ignored) {
		}
	}
}
