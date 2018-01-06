package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class InitBookRespMsg extends Message {

	public final ResponseCode RESPONSE_CODE;
	public final int          BOOK_TIME;

	public InitBookRespMsg(long timestamp, String resp_code, String book_time) {
		super(MessageType.INIT_BOOK_RESP, timestamp, resp_code);
		this.RESPONSE_CODE = ResponseCode.values()[Integer.valueOf(resp_code) - 1];
		this.BOOK_TIME = Integer.valueOf(book_time);
	}

	public ResponseCode getResponseCode() {
		return this.RESPONSE_CODE;
	}

	public int getBookTime() {
		return this.BOOK_TIME;
	}

	public enum ResponseCode {
		SUCCESS,
		FAILED_LOCKED,
		FAILED_BOOKED,
		FAILED_SEAT_DID_NOT_EXIST;
	}
}