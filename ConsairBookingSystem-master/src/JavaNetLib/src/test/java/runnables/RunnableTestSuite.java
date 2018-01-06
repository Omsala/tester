package runnables;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(value = Suite.class)
@SuiteClasses(value = {
	TestPacketListener.class,
	TestPacketSender.class
})
public class RunnableTestSuite {

	@BeforeClass
	public static void setupSuite() {
		Logger root = Logger.getLogger("");
		root.setLevel(Level.OFF);
	}
}
