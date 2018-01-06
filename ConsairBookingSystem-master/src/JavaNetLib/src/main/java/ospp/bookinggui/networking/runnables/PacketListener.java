package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.messages.ErrorMsg;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketListener implements Runnable {

	private static final Logger logger = Logger.getLogger(PacketListener.class.getName());

	public static volatile boolean debug = false;

	private final Mailbox<Message> mailbox;
	private final BufferedReader   input;

	/**
	 * Creates a PacketListener.
	 * <p/>
	 * A PacketListener reads each line of the InputStream and attempts to parse
	 * them as messages using Message.parseMessage().
	 * <p/>
	 * If the parsing was successful, the packet listener will add the new message to the inbox of the
	 * given mailbox and continue on reading the input stream.
	 *
	 * @param m  The mailbox to add new messages to.
	 * @param is The InputStream the packet listener should read.
	 */
	public PacketListener(Mailbox<Message> m, InputStream is) throws UnsupportedEncodingException {
		this.mailbox = m;
		this.input = new BufferedReader(new InputStreamReader(is, "UTF8"));
	}

	@Override
	public void run() {

		String data;

		try {
			while((data = this.input.readLine()) != null) {
				if(!debug) {
					try {
						logger.info("PacketListener received message: " + data);
						Message msg = Message.parseMessage(data);
						mailbox.receive(msg);
					}
					catch(MalformedMessageException e) {

						logger.log(Level.SEVERE, "PacketListener received malformed message! " +
							"Message: \"" + e.getMessage() + "\"", e);

//						mailbox.send(new ErrorMsg(System.currentTimeMillis(), e.getMessage()));
					}
				}
				else {
					logger.info("Received message: " + data);
				}
			}

			logger.severe("END OF STREAM!");
			mailbox.receive(new ErrorMsg(System.currentTimeMillis(), "The connection to the server was terminated!"));
			NetworkAdapter.isConnected = false;
		}
		catch(IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}