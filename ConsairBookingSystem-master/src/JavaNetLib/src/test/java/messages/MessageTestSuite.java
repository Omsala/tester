package messages;

import junit.framework.TestSuite;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(value = Suite.class)
@SuiteClasses(value = {
	TestLogin.class,
	TestDisconnect.class,
	TestRequestAirports.class,
	TestRequestAirportsResp.class,
	TestSearchRouteResp.class,
	TestFinBookResp.class,
	TestInitBook.class,
	TestInitBookResp.class,
	TestLoginResp.class,
	TestSearchRoute.class,
	TestError.class
})
public class MessageTestSuite extends TestSuite {

	@BeforeClass
	public static void setup() {
		Logger root = Logger.getLogger("");
		root.setLevel(Level.OFF);
	}
}
