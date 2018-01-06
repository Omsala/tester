import org.junit.Test;
import ospp.bookinggui.Airport;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestAirport {

	@Test
	public void createBody1() {
		Airport[] airports = new Airport[]{
			new Airport("12", "ARN", "Arlanda"),
			new Airport("42", "KanDV", "FooBar")
		};

		String[] body = Airport.createBody(airports);

		String[] expected = new String[]{
			"12", "ARN", "Arlanda",
			"42", "KanDV", "FooBar"
		};

		assertArrayEquals(expected, body);
	}

	@Test
	public void testArgAmount() {
		assertEquals(3, Airport.ARG_AMOUNT);
	}
}
