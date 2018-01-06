package runnables;

import org.junit.Test;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.messages.ErrorMsg;
import ospp.bookinggui.networking.runnables.PacketListener;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class TestPacketListener {

	@Test(timeout = 500)
	public void testOne() throws UnsupportedEncodingException {
		ByteArrayInputStream input = new ByteArrayInputStream("1&1337&yo&lo&".getBytes());

		Mailbox<Message> mailbox = new Mailbox<>();
		PacketListener pl = new PacketListener(mailbox, input);

		assertEquals(null, mailbox.getOldestIncoming());

		pl.run();

		Message msg = mailbox.getOldestIncoming();

		assertNotEquals(null, msg);

		assertEquals(MessageType.LOGIN, msg.getType());
		assertEquals(1337L, msg.getTimestamp());

		assertArrayEquals(new String[]{"yo", "lo"}, msg.getBody());
	}

	@Test(timeout = 500)
	public void testFail1() throws UnsupportedEncodingException {
		ByteArrayInputStream input = new ByteArrayInputStream("1&1337&".getBytes());

		Mailbox<Message> mailbox = new Mailbox<>();
		PacketListener pl = new PacketListener(mailbox, input);

		assertNull(mailbox.getOldestIncoming());
		assertNull(mailbox.getOldestOutgoing());

		// The parsing in the packet listener should trigger a MalformedMessageException
		// and the packet listener should then attempt to send an ErrorMsg with the exception message.
		pl.run();

		Message in = mailbox.getOldestIncoming();
//		Message out = mailbox.getOldestOutgoing();

		assertNotNull(in);
//		assertNotNull(out);

		assertTrue(in instanceof ErrorMsg);
//		assertTrue(out instanceof ErrorMsg);

		ErrorMsg errIn = (ErrorMsg) in;
//		ErrorMsg errOut = (ErrorMsg) out;

		assertEquals("The connection to the server was terminated!", errIn.getErrorMessage());
//		assertEquals("Could not parse message! The body is not correctly formed!", errOut.getErrorMessage());
	}
}