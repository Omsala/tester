package runnables;

import org.junit.Test;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.runnables.PacketSender;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class TestPacketSender {

	@Test(timeout = 500)
	public void testOne() throws UnsupportedEncodingException {
		Mailbox<Message> mailbox = new Mailbox<>();

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		long timestamp = 1337L;

		mailbox.send(new Message(MessageType.LOGIN, timestamp, "yo", "lo"));

		PacketSender sender = new PacketSender(mailbox, output);

		// We need to fool the PacketSender that it is connected to a server.
		NetworkAdapter.isConnected = true;

		// Run the PacketSender in a separate thread since it is completely blocking.
		new Thread(sender).start();

		// Wait a small time period for the PacketSender to do its job
		try {
			Thread.sleep(100);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}

		// Stop the sender runnable, killing the thread.
		sender.stop();

		String sent = output.toString();

		String expected = "1&1337&yo&lo&" + System.lineSeparator();

		assertEquals(expected, sent);
	}
}