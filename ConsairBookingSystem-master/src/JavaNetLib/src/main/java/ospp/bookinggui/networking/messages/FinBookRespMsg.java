package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class FinBookRespMsg extends Message {

	public enum ResponseCode {
		SUCCESS,
		FAILED;
	}

	private final ResponseCode RESP_CODE;

	public FinBookRespMsg(long timestamp, String resp_code) {
		super(MessageType.FIN_BOOK_RESP, timestamp, new String[]{resp_code});
		this.RESP_CODE = ResponseCode.values()[Integer.valueOf(resp_code) - 1];
	}

	public ResponseCode getResponseCode() {
		return this.RESP_CODE;
	}
}
