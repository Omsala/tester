package ospp.bookinggui;

public class Seat {

	public static final int ARG_AMOUNT;

	static {
		ARG_AMOUNT = Seat.class.getDeclaredFields().length - 1;
	}

	private final String SEAT_ID;
	private final String FLIGHT_ID;
	private final String KLASS;
	private final String USER;
	private final String WINDOW;
	private final String AISLE;
	private final String ROW;
	private final String COL;
	private final String PRICE;
	private final String LOCKED;

	public Seat(String id, String flight_id, String klass, String user, String window, String aisle,
				String row, String col, String price, String locked) {

		this.SEAT_ID = id;
		this.FLIGHT_ID = flight_id;
		this.KLASS = klass;
		this.USER = user;
		this.WINDOW = window;
		this.AISLE = aisle;
		this.ROW = row;
		this.COL = col;
		this.PRICE = price;
		this.LOCKED = locked;
	}

	public static Seat parseBody(String[] parts, int offset) {
		if(parts.length - offset < ARG_AMOUNT) {
			throw new IllegalArgumentException("Seat.parseBodyToArray() was given a too small array!");
		}

		return new Seat(
			parts[offset],
			parts[offset + 1],
			parts[offset + 2],
			parts[offset + 3],
			parts[offset + 4],
			parts[offset + 5],
			parts[offset + 6],
			parts[offset + 7],
			parts[offset + 8],
			parts[offset + 9]
		);
	}

	public String getSeatID() {
		return this.SEAT_ID;
	}

	public String getFlightID() {
		return this.FLIGHT_ID;
	}

	public String getKlass() {
		return this.KLASS;
	}

	public String getUser() {
		return this.USER;
	}

	public String getWindow() {
		return this.WINDOW;
	}

	public String getAisle() {
		return this.AISLE;
	}

	public String getRow() {
		return this.ROW;
	}

	public String getCol() {
		return this.COL;
	}

	public String getPrice() {
		return this.PRICE;
	}

	public String getLocked() {
		return this.LOCKED;
	}
}
