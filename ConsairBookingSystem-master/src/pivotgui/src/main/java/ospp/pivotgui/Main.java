package ospp.pivotgui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.pivotgui.controllers.ConnectAndLoginController;

import java.io.IOException;
import java.util.logging.Logger;

public class Main implements Application {

	public static final  Mailbox<Message> mailbox = new Mailbox<Message>();
	private static final Logger           logger  = Logger.getLogger(Main.class.getName());
	private static       Display          display = null;

	public static void main(String[] args) {
		DesktopApplicationContext.main(Main.class, args);
	}

	public static void activateIndicator(PushButton b, ActivityIndicator i) {
		b.setButtonData("Please wait...");
		b.setEnabled(false);
		i.setActive(true);
	}

	public static void disableIndicator(PushButton b, ActivityIndicator i) {
		b.setButtonData("Connect");
		b.setEnabled(true);
		i.setActive(false);
	}

	public static Window loadWindow(Class<? extends Window> c, String xmlFile) {
		BXMLSerializer serializer = new BXMLSerializer();

		Window w;
		try {
			w = (Window) serializer.readObject(c, "/bxml/" + xmlFile);
		}
		catch(IOException | SerializationException e) {
			e.printStackTrace();
			return null;
		}

		w.open(display);

		return w;
	}

	public static void disconnectError(Window window) {
		Alert.alert(MessageType.ERROR, "The server terminated the connection!", window);
	}

	public void startup(final Display display, Map<String, String> map) throws Exception {
		this.display = display;
		loadWindow(ConnectAndLoginController.class, "connectAndLogin.bxml");
	}

	public boolean shutdown(boolean b) throws Exception {
		return false;
	}

	public void suspend() throws Exception {
	}

	public void resume() throws Exception {
	}
}
