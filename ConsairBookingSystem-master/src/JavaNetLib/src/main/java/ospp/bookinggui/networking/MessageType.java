package ospp.bookinggui.networking;

public enum MessageType {

	/*
	 * IMPORTANT! The order of these types determine the ID of the message!
	 * The first type in the constants below is thus the ID #1 etc.
	 */

	// General types
	LOGIN,
	LOGIN_RESP,
	DISCONNECT,
	ERROR,

	// User commands
	INIT_BOOK,
	INIT_BOOK_RESP,

	FIN_BOOK,
	FIN_BOOK_RESP,

	ABORT_BOOK,

	REQ_AIRPORTS,
	REQ_AIRPORTS_RESP,

	SEARCH_ROUTE,
	SEARCH_ROUTE_RESP,

	REQ_FLIGHT_DETAILS,
	REQ_FLIGHT_DETAILS_RESP,

	REQ_SEAT_SUGGESTION,
	REQ_SEAT_SUGGESTION_RESP,

	REQ_SEAT_MAP,
	REQ_SEAT_MAP_RESP,

	// Admin commands
	REQ_PASSENGER_LIST,
	REQ_PASSENGER_LIST_RESP,

	REQ_SEAT_MAP_ADMIN,
	REQ_SEAT_MAP_ADMIN_RESP,

	TERMINATE_SERVER;

	public final byte ID;

	MessageType() {
		this.ID = (byte) ((this.ordinal() + 1) & 0xFF);
	}

	public static MessageType getType(int id) {
		if(id > MessageType.values().length) {
			throw new IllegalArgumentException("Incorrect ID! ID is too large!");
		}
		return MessageType.values()[id - 1];
	}
}