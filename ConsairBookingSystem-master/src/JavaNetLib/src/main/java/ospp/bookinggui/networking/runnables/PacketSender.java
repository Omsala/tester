package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketSender implements Runnable {

	private static final Logger logger = Logger.getLogger(PacketSender.class.getName());

	private final Mailbox<Message> mailbox;
	private final PrintWriter      output;

	private volatile boolean keepRunning = true;

	/**
	 * Creates a new PacketSender.
	 * <p/>
	 * The packet sender polls the mailbox for new outgoing messages.
	 * If there are no new messages to send, it will sleep for 10 milliseconds then check again.
	 * This is repeated until it finds a new outgoing message in the mailbox.
	 * <p/>
	 * Upon retrieving a message, it calls message.createMessage() to form what should be written
	 * to the output stream. It then writes the message and flushes the buffer to send it.
	 *
	 * @param mailbox The mailbox to poll for outgoing messages.
	 * @param os      Where to write the messages.
	 */
	public PacketSender(Mailbox<Message> mailbox, OutputStream os) {
		this.mailbox = mailbox;
		this.output = new PrintWriter(os);
	}

	@Override
	public void run() {

		while(keepRunning && NetworkAdapter.isConnected) {
			Message m = mailbox.getOldestOutgoing();

			// If mailbox is empty, sleep for a while then check again.
			if(m == null) {
				try {
					Thread.sleep(10);
					continue;
				}
				catch(InterruptedException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}

			try {
				String msg = m.createMessage();

				logger.info("Sending message: " + msg);

				output.println(msg);
				output.flush();
			}
			catch(UnsupportedEncodingException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		logger.severe("PacketSender was terminated!");
	}

	public void stop() {
		this.keepRunning = false;
	}
}