package ospp.bookinggui.networking;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Mailbox<E> {

	private final ConcurrentLinkedQueue<E> inbox  = new ConcurrentLinkedQueue<>();
	private final ConcurrentLinkedQueue<E> outbox = new ConcurrentLinkedQueue<>();

	/**
	 * Adds the given object to the end of the inbox.
	 *
	 * @param o The object to add to the inbox.
	 */
	public void receive(E o) {
		inbox.add(o);
	}

	/**
	 * Adds the given object to the end of the outbox.
	 *
	 * @param o The object to add to the outbox.
	 */
	public void send(E o) {
		outbox.add(o);
	}

	/**
	 * Polls the head of the inbox and returns it.
	 *
	 * @return The polled object, null if inbox is empty.
	 */
	public E getOldestIncoming() {
		return inbox.poll();
	}

	/**
	 * Polls the head of the outbox and returns it.
	 *
	 * @return The polled object, null if outbox is empty.
	 */
	public E getOldestOutgoing() {
		return outbox.poll();
	}
}