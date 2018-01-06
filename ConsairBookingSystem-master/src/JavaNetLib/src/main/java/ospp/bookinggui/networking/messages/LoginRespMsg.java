package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class LoginRespMsg extends Message {

	public enum PrivilegeLevel {
		USER,
		ADMIN,
		FAIL
	}

	private final PrivilegeLevel privilege_level;

	public LoginRespMsg(long timestamp, String privilege_level) {
		super(MessageType.LOGIN_RESP, timestamp, privilege_level);
		this.privilege_level = PrivilegeLevel.values()[Integer.valueOf(privilege_level) - 1];
	}

	public PrivilegeLevel getPrivilegeLevel() {
		return this.privilege_level;
	}
}