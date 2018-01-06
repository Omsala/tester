package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class LoginMsg extends Message {

	public final String username;
	public final String password;

	public LoginMsg(long timestamp, String username, String password) {
		super(MessageType.LOGIN, timestamp, username, password);
		this.username = username;
		this.password = password;
	}
}