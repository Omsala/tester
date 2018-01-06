package demos;

import ospp.bookinggui.Airport;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.messages.*;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginGetAirportsDisconnect {

	private final static Logger logger = Logger.getLogger(LoginGetAirportsDisconnect.class.getName());

	public static void main(String[] args) throws IOException {
		final Mailbox<Message> mailbox = new Mailbox<>();

		new NetworkAdapter(mailbox, "localhost", 53535);

		// LOGIN
		mailbox.send(new LoginMsg(System.currentTimeMillis(), "carl", "asdasd"));

		Message msg;
		while((msg = mailbox.getOldestIncoming()) == null);

		LoginRespMsg resp = (LoginRespMsg) msg;

		if(resp.getPrivilegeLevel() != LoginRespMsg.PrivilegeLevel.ADMIN) {
			logger.severe("Incorrect privilege level!");
			System.exit(0);
		}

		// REQUEST A LIST OF ALL AIRPORTS ON SERVER
		mailbox.send(new RequestAirportsMsg(System.currentTimeMillis(), null));

		while((msg = mailbox.getOldestIncoming()) == null);

		RequestAirportsRespMsg resp2 = (RequestAirportsRespMsg) msg;

		for(Airport a : resp2.getAirports()) {
			logger.info(a.getName());
		}

		// DISCONNECT
		mailbox.send(new DisconnectMsg(System.currentTimeMillis()));
	}
}
