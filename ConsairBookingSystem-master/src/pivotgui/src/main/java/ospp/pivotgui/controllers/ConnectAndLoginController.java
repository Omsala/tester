package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.messages.DisconnectMsg;
import ospp.bookinggui.networking.messages.ErrorMsg;
import ospp.bookinggui.networking.messages.LoginMsg;
import ospp.bookinggui.networking.messages.LoginRespMsg;
import ospp.pivotgui.Main;
import ospp.pivotgui.exceptions.DisconnectException;
import ospp.pivotgui.exceptions.IncorrectMessageTypeException;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectAndLoginController extends Window implements Bindable {

	private static final Logger logger = Logger.getLogger(ConnectAndLoginController.class.getName());

	@BXML
	private TextInput         hostURL   = null;
	@BXML
	private TextInput         port      = null;
	@BXML
	private TextInput         username  = null;
	@BXML
	private TextInput         password  = null;
	@BXML
	private PushButton        connect   = null;
	@BXML
	private ActivityIndicator indicator = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {

		connect.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				Main.activateIndicator(connect, indicator);

				if(username.getText().equals("test")) {
					loadSelectionWindow();
					return;
				}

				// Connect and login on an async thread to prevent GUI lock ups.
				new Task<LoginRespMsg.PrivilegeLevel>() {
					@Override
					public LoginRespMsg.PrivilegeLevel execute() throws TaskExecutionException {

						int prt;
						try {
							prt = Integer.valueOf(port.getText());
						}
						catch(NumberFormatException e) {
							throw new TaskExecutionException("Please enter a correct port number!");
						}

						// Connect to the server
						try {
							new NetworkAdapter(Main.mailbox, hostURL.getText(), prt);
						}
						catch(IOException e) {
							throw new TaskExecutionException(e);
						}

						// Send a login message
						Main.mailbox.send(new LoginMsg(System.currentTimeMillis(), username.getText(), password.getText()));

						// Wait for answer from server
						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						// Handle the response
						if(msg instanceof LoginRespMsg) {
							LoginRespMsg resp = (LoginRespMsg) msg;
							return resp.getPrivilegeLevel();
						}
						else if(msg instanceof DisconnectMsg) {
							throw new TaskExecutionException(new Exception("Received Disconnect message!"));
						}
						else if(msg instanceof ErrorMsg) {
							throw new TaskExecutionException(new Exception(((ErrorMsg) msg).getErrorMessage()));
						}
						else {
							throw new TaskExecutionException(new IncorrectMessageTypeException());
						}
					}
				}.execute(new TaskAdapter<>(new TaskListener<LoginRespMsg.PrivilegeLevel>() {

					// These two methods are executed on the main GUI-thread.

					@Override
					public void taskExecuted(Task<LoginRespMsg.PrivilegeLevel> task) {
						Main.disableIndicator(connect, indicator);
						loadSelectionWindow();
					}

					@Override
					public void executeFailed(Task<LoginRespMsg.PrivilegeLevel> task) {
						Main.disableIndicator(connect, indicator);

						Throwable e = task.getFault();
						Alert.alert(MessageType.ERROR, e.getMessage(), ConnectAndLoginController.this);

						if(e instanceof DisconnectException) {
							Main.disconnectError(ConnectAndLoginController.this);
						}
						else {
							logger.log(Level.SEVERE, e.getMessage(), e);
						}
					}
				}));
			}
		});
	}

	private void loadSelectionWindow() {
		this.close();
		SelectionController c = (SelectionController) Main.loadWindow(SelectionController.class, "selection.bxml");
		c.pushReloadButton();
	}
}
