package ospp.pivotgui.exceptions;

public class IncorrectMessageTypeException extends Exception {
	public IncorrectMessageTypeException() {
		super("The server sent a message of incorrect type!");
	}
}
