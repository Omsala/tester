package ospp.bookinggui.networking;

import ospp.bookinggui.networking.runnables.PacketListener;
import ospp.bookinggui.networking.runnables.PacketSender;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class NetworkAdapter {

	private static final   Logger  logger      = Logger.getLogger(NetworkAdapter.class.getName());
	/**
	 * The value of this variable can be used to tell whether or not
	 * a working connection to the server is established and alive.
	 * <p/>
	 * WARNING! You are only allowed to read this value!
	 * Setting this value to false if it is true can have unpredictable consequences.
	 */
	public static volatile boolean isConnected = false;

	/**
	 * Determines the timeout for each read call on the underlying InputStream
	 * from the created socket. If a read on the stream is blocking for a longer period
	 * than the value of this variable an IOException will be triggered.
	 */
	public static int so_timeout_millis = 30000;
	private final Mailbox<Message> mailbox;

	/**
	 * Initiates the network adapter.
	 * It will try to connect to the given host address and port on the calling thread!
	 *
	 * @param box  The mailbox this network adapter should work with.
	 * @param host The address of the host.
	 * @param port The port on the host machine to target.
	 * @throws IOException If an IOException occurred when attempting to connect to the host machine.
	 */
	public NetworkAdapter(Mailbox<Message> box, String host, int port) throws IOException {
		this.mailbox = box;

		logger.info("Attempting to connect to host!");

		Socket socket = new Socket(host, port);

		// Set a timeout on read calls.
//		socket.setSoTimeout(NetworkAdapter.so_timeout_millis);

		isConnected = true;
		logger.info("Connection established!");

		new Thread(new PacketListener(box, socket.getInputStream()), "PacketListener").start();
		new Thread(new PacketSender(box, socket.getOutputStream()), "PacketSender").start();
	}
}