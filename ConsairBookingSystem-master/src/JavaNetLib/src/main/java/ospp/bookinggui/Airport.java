package ospp.bookinggui;

import java.lang.reflect.Field;

public class Airport {

	public static final int ARG_AMOUNT;

	static {
		Field[] fields = Airport.class.getDeclaredFields();
		ARG_AMOUNT = fields.length - 1;
	}

	private final String ID;
	private final String IATA;
	private final String NAME;

	public Airport(String airport_id, String iata, String name) {
		this.ID = airport_id;
		this.IATA = iata;
		this.NAME = name;
	}

	public static String[] createBody(Airport[] airports) {

		String[] body = new String[airports.length * 3];

		for(int i = 0; i < airports.length; i++) {
			Airport port = airports[i];

			body[i * 3] = port.getAirportID();
			body[i * 3 + 1] = port.getIATA();
			body[i * 3 + 2] = port.getName();
		}

		return body;
	}

	public static Airport[] parseBodyToArray(String[] body, int offset) {

		if((body.length - offset) % 3 != 0) {
			throw new IllegalArgumentException("Airport.parseBodyToArray() was given an array not divisable by three after subtracting the offset!");
		}

		Airport[] ports = new Airport[body.length / 3];

		for(int i = 0; i < ports.length; i++) {
			String id = body[offset + i * 3];
			String iata = body[offset + i * 3 + 1];
			String name = body[offset + i * 3 + 2];

			ports[i] = new Airport(id, iata, name);
		}

		return ports;
	}

	public static Airport parseBody(String[] body, int offset) {
		if(body.length - offset < ARG_AMOUNT) {
			throw new IllegalArgumentException("Airport.parseBody() was given a too small array!");
		}

		return new Airport(body[offset], body[offset + 1], body[offset + 2]);
	}

	public String getAirportID() {
		return this.ID;
	}

	public String getIATA() {
		return this.IATA;
	}

	public String getName() {
		return this.NAME;
	}

	public String[] createBody() {
		return Airport.createBody(new Airport[]{this});
	}
}
