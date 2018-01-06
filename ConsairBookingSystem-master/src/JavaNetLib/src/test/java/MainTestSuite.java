import junit.framework.TestSuite;
import messages.MessageTestSuite;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import runnables.RunnableTestSuite;

import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestMessage.class, TestAirport.class, TestFlight.class})
public class MainTestSuite extends TestSuite {

	@BeforeClass
	public static void setUpClass() {
		Logger root = Logger.getLogger("");
		root.setLevel(Level.OFF);
	}
}
