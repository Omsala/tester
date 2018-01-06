package ospp.bookinggui;

public class Date {

	public static final int ARG_AMOUNT;

	static {
		ARG_AMOUNT = Date.class.getDeclaredFields().length - 1;
	}

	private final String YEAR;
	private final String MONTH;
	private final String DAY;
	private final String HOUR;
	private final String MINUTE;
	private final String SECOND;

	public Date(String year, String month, String day, String hour, String minute, String second) {
		this.YEAR = year;
		this.MONTH = month;
		this.DAY = day;
		this.HOUR = hour;
		this.MINUTE = minute;
		this.SECOND = second;
	}

	public static Date parseBody(String[] parts, int offset) {
		if(parts.length - offset < ARG_AMOUNT) {
			throw new IllegalArgumentException("Date.parseBodyToArray() was given a too small array!");
		}

		return new Date(
			parts[offset],
			parts[offset + 1],
			parts[offset + 2],
			parts[offset + 3],
			parts[offset + 4],
			parts[offset + 5]
		);
	}

	public String getYear() {
		return YEAR;
	}

	public String getMonth() {
		return MONTH;
	}

	public String getDay() {
		return DAY;
	}

	public String getHour() {
		return HOUR;
	}

	public String getMinute() {
		return MINUTE;
	}

	public String getSecond() {
		return SECOND;
	}

	public String[] createBody() {
		String[] body = new String[]{
			YEAR, MONTH, DAY,
			HOUR, MINUTE, SECOND
		};

		return body;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(YEAR).append('-');
		sb.append(MONTH).append('-');
		sb.append(DAY).append(' ');

		sb.append(HOUR).append(':');
		sb.append(MINUTE).append(':');
		sb.append(SECOND);

		return sb.toString();
	}
}
