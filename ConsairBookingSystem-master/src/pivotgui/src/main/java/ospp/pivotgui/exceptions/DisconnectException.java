package ospp.pivotgui.exceptions;

public class DisconnectException extends Exception {
	public DisconnectException() {
		super("The server terminated the connection!");
	}
}
