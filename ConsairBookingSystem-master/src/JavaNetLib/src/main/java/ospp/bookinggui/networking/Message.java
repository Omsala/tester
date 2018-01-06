package ospp.bookinggui.networking;

import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.messages.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Message {

	/**
	 * This is what separates the different parts of a message.
	 */
	public static final String SEPARATOR = "&";

	/**
	 * This is the least amount of parts needed for each message.
	 * Current parts:
	 * First part:   ID
	 * Second part:  Timestamp (milliseconds)
	 * Third and up: The message body
	 */
	public static final int HEADER_SIZE = 2;

	private static final Logger logger = Logger.getLogger(Message.class.getName());

	private final MessageType TYPE;
	private final long        TIMESTAMP;
	private final String[]    BODY;

	/**
	 * Creates a message with the given parameters.
	 * <p/>
	 * WARNING! If this constructor is called manually and not from
	 * a child message constructor, you need to make absolutely sure that the way you call
	 * it complies with the defined protocol!
	 *
	 * @param type      The type of the message.
	 * @param timestamp The timestamp of the message.
	 * @param body      The body of the message.
	 */
	public Message(MessageType type, long timestamp, String... body) {
		this.TYPE = type;
		this.TIMESTAMP = timestamp;

		if(isNull(body)) {
			this.BODY = new String[0];
		}
		else {
			this.BODY = body;
		}
	}

	private static boolean isNull(String[] a) {
		if(a == null) {
			return true;
		}
		else if(a.length == 0) {
			return false;
		}
		else {
			for(String s : a) {
				if(s == null) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Attempts to parse the given string according to the defined protocol.
	 * Throws MalformedMessageException with a description of the problem if any error occurs during parsing.
	 *
	 * @param data The string to parse to a message.
	 * @return The message if successful.
	 * @throws MalformedMessageException If any problems occur during parsing.
	 */
	public static Message parseMessage(String data) throws MalformedMessageException, UnsupportedEncodingException {

		String[] parts = data.split(Message.SEPARATOR);

		if(parts.length < Message.HEADER_SIZE) {
			throw new MalformedMessageException("Could not parse message! The message is too small!");
		}

		MessageType type = retrieveType(parts);
		long timestamp = retrieveTimestamp(parts);
		String[] body = retrieveBody(parts);

		try {
			switch(type) {
				case LOGIN:
					return new LoginMsg(timestamp, body[0], body[1]);

				case LOGIN_RESP:
					return new LoginRespMsg(timestamp, body[0]);

				case DISCONNECT:
					return new DisconnectMsg(timestamp);

				case ERROR:
					return new ErrorMsg(timestamp, body[0]);

				case INIT_BOOK:
					return new InitBookMsg(timestamp, body[0]);

				case INIT_BOOK_RESP:
					return new InitBookRespMsg(timestamp, body[0], body[1]);

				case FIN_BOOK:
					return new FinBookMsg(timestamp);

				case FIN_BOOK_RESP:
					return new FinBookRespMsg(timestamp, body[0]);

				case ABORT_BOOK:
					return new AbortBookMsg(timestamp);

				case REQ_AIRPORTS:
					String iata = body == null ? null : body[0];
					return new RequestAirportsMsg(timestamp, iata);

				case REQ_AIRPORTS_RESP:
					return new RequestAirportsRespMsg(timestamp, body);

				case SEARCH_ROUTE:
					return new SearchAirportRouteMsg(timestamp, body[0], body[1]);

				case SEARCH_ROUTE_RESP:
					return new SearchAirportRouteRespMsg(timestamp, body);

				case REQ_FLIGHT_DETAILS:
					return new RequestFlightDetailsMsg(timestamp, body[0]);

				case REQ_FLIGHT_DETAILS_RESP:
					return new RequestFlightDetailsRespMsg(timestamp, body);

				case REQ_SEAT_SUGGESTION:
					return new RequestSeatSuggestionMsg(timestamp, body[0]);

				case REQ_SEAT_SUGGESTION_RESP:
					return new RequestSeatSuggestionRespMsg(timestamp, body);

				case TERMINATE_SERVER:
					return new TerminateServerMsg(timestamp);

				case REQ_SEAT_MAP:
				case REQ_SEAT_MAP_RESP:
				case REQ_PASSENGER_LIST:
				case REQ_PASSENGER_LIST_RESP:
				case REQ_SEAT_MAP_ADMIN:
				case REQ_SEAT_MAP_ADMIN_RESP:
				default:
					logger.warning("Message.parseMessage() is missing specific parsing for the type: " + type);
					return new Message(type, timestamp, body);
			}
		}
		catch(NullPointerException | IndexOutOfBoundsException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new MalformedMessageException("Could not parse message! The body is not correctly formed!");
		}
	}

	private static String[] retrieveBody(String[] parts) {
		String[] body = null;
		if(parts.length > Message.HEADER_SIZE) {
			body = new String[parts.length - Message.HEADER_SIZE];
			System.arraycopy(parts, 2, body, 0, parts.length - Message.HEADER_SIZE);
		}
		return body;
	}

	private static long retrieveTimestamp(String[] parts) throws MalformedMessageException {
		try {
			return Long.valueOf(parts[1]);
		}
		catch(NumberFormatException e) {
			throw new MalformedMessageException("The timestamp is not a valid long!");
		}
	}

	private static MessageType retrieveType(String[] parts) throws MalformedMessageException {
		try {
			return MessageType.getType(Integer.valueOf(parts[0]));
		}
		catch(NumberFormatException e) {
			throw new MalformedMessageException("The message ID is not an integer!");
		}
		catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			throw new MalformedMessageException("The supplied message ID is not supported!");
		}
	}

	/**
	 * Creates the message.
	 *
	 * @return The created message.
	 */
	public String createMessage() throws UnsupportedEncodingException {
		StringBuilder message = new StringBuilder();

		message.append(this.TYPE.ID).append(Message.SEPARATOR);
		message.append(this.TIMESTAMP).append(Message.SEPARATOR);

		for(String arg : this.BODY) {
			message.append(arg).append(Message.SEPARATOR);
		}

		return message.toString();
	}

	/**
	 * Retrieves the type of the message.
	 *
	 * @return
	 */
	public MessageType getType() {
		return this.TYPE;
	}

	/**
	 * Retrieves the timestamp of the message.
	 *
	 * @return
	 */
	public long getTimestamp() {
		return this.TIMESTAMP;
	}

	/**
	 * Retrieves the body of the message.
	 *
	 * @return
	 */
	public String[] getBody() {
		return this.BODY.clone();
	}
}