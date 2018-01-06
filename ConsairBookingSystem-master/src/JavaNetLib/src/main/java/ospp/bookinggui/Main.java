package ospp.bookinggui;

import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.logging.ConsoleFormatter;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.runnables.PacketListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	private static final Logger root_logger = Logger.getLogger("");

	public static void main(String[] args) throws IOException {

		for(Handler h : root_logger.getHandlers()) {
			if(h instanceof ConsoleHandler) {
				h.setFormatter(new ConsoleFormatter());
			}
		}

		root_logger.setLevel(Level.ALL);

//		PacketListener.debug = true;

		final Mailbox<Message> mailbox = new Mailbox<>();

		Scanner scanner = new Scanner(System.in);

		while(true) {
			try {
				root_logger.info("Please enter address:");
				new NetworkAdapter(mailbox, scanner.next(), 53535);
				break;
			}
			catch(IOException e) {
				root_logger.warning("Connection failed! Error message: " + e.getMessage());
			}
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					Message msg = mailbox.getOldestIncoming();

					if(msg == null) {
						try {
							Thread.sleep(100);
						} catch(InterruptedException e) {}

						continue;
					}

					root_logger.info("Received message:");
					root_logger.info("\tType: " + msg.getType());
					root_logger.info("\tTime: " + msg.getTimestamp());

					root_logger.info("\tBody: ");

					StringBuilder sb = new StringBuilder();
					for(String s : msg.getBody()) {
						sb.append(s).append(", ");
					}
					root_logger.info("\t\t" + sb.toString());
				}
			}
		}).start();

		String input;
		while(true) {
			input = scanner.next();

			try {
				Message msg = Message.parseMessage(input);
				mailbox.send(msg);
			}
			catch(MalformedMessageException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
}
