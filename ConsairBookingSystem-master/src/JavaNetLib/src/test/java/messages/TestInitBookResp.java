package messages;

import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.InitBookRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestInitBookResp {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "6&126&1&127";

		InitBookRespMsg msg = (InitBookRespMsg) Message.parseMessage(data);

		assertEquals(InitBookRespMsg.ResponseCode.SUCCESS, msg.getResponseCode());
		assertEquals(127, msg.getBookTime());
	}

	@Test
	public void parse2() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "6&126&2&126&";

		InitBookRespMsg msg = (InitBookRespMsg) Message.parseMessage(data);

		assertEquals(InitBookRespMsg.ResponseCode.FAILED_LOCKED, msg.getResponseCode());
		assertEquals(126, msg.getBookTime());
	}

	@Test
	public void parse3() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "6&1726&3&12&";

		InitBookRespMsg msg = (InitBookRespMsg) Message.parseMessage(data);

		assertEquals(InitBookRespMsg.ResponseCode.FAILED_BOOKED, msg.getResponseCode());
	}

	@Test
	public void parse4() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "6&217348&4&23423&";

		InitBookRespMsg msg = (InitBookRespMsg) Message.parseMessage(data);

		assertEquals(InitBookRespMsg.ResponseCode.FAILED_SEAT_DID_NOT_EXIST, msg.getResponseCode());
	}

	@Test
	public void parseIncorrectCode1() {
		String data = "6&1236&5&1263&";

		try {
			Message.parseMessage(data);
			fail("TestInitBookResp.parseIncorrectCode1() did not catch exception!");
		}
		catch(MalformedMessageException | UnsupportedEncodingException e) {
		}
	}
}
