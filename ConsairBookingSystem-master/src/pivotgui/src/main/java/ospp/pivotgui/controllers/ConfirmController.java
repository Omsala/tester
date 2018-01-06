package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.HashMap;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.Seat;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.*;
import ospp.pivotgui.Main;
import ospp.pivotgui.exceptions.DisconnectException;
import ospp.pivotgui.exceptions.IncorrectMessageTypeException;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfirmController extends Window implements Bindable {

	private static final Logger logger = Logger.getLogger(ConfirmController.class.getName());

	@BXML
	private TableView  seatTable    = null;
	@BXML
	private PushButton selectButton = null;
	@BXML
	private PushButton cancelButton = null;

	private Seat[] seat_list = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {
		addSelectButtonPressListener();
		addCancelButtonPressListener();
		addTableSelectionListener();

		// Load seats into the table view
		if(seat_list != null) {
			displaySeats();
		}
	}

	private void addTableSelectionListener() {
		seatTable.getTableViewSelectionListeners().add(new TableViewSelectionListener() {
			@Override
			public void selectedRangeAdded(TableView tableView, int i, int i1) {
			}

			@Override
			public void selectedRangeRemoved(TableView tableView, int i, int i1) {
			}

			@Override
			public void selectedRangesChanged(TableView tableView, Sequence<Span> sequence) {
			}

			@Override
			public void selectedRowChanged(TableView tableView, Object o) {
				new Task<InitBookRespMsg.ResponseCode>() {

					@Override
					public InitBookRespMsg.ResponseCode execute() throws TaskExecutionException {

						HashMap<String, String> row = (HashMap<String, String>) seatTable.getSelectedRow();
						String seatID = row.get("seatID");

						Main.mailbox.send(new InitBookMsg(System.currentTimeMillis(), seatID));

						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						if(msg instanceof InitBookRespMsg) {
							InitBookRespMsg resp = (InitBookRespMsg) msg;
							return resp.getResponseCode();
						}
						else if(msg instanceof DisconnectMsg) {
							throw new TaskExecutionException(new DisconnectException());
						}
						else {
							throw new TaskExecutionException(new Exception("Client received incorrect message type!"));
						}
					}

				}.execute(new TaskAdapter<>(new TaskListener<InitBookRespMsg.ResponseCode>() {

					@Override
					public void taskExecuted(Task<InitBookRespMsg.ResponseCode> task) {
						InitBookRespMsg.ResponseCode resp = task.getResult();

						switch(resp) {
							case SUCCESS:
								Alert.alert(MessageType.INFO, "Your seat has been locked for you for a time period!", ConfirmController.this);
								break;

							case FAILED_LOCKED:
								Alert.alert(MessageType.ERROR, "That seat was already locked! Please choose another!", ConfirmController.this);
								break;

							case FAILED_BOOKED:
								Alert.alert(MessageType.ERROR, "That seat was already booked! Please choose another!", ConfirmController.this);
								break;

							case FAILED_SEAT_DID_NOT_EXIST:
								Alert.alert(MessageType.ERROR, "That seat did not exist! Pleas choose another!", ConfirmController.this);
								break;
						}
					}

					@Override
					public void executeFailed(Task<InitBookRespMsg.ResponseCode> task) {
						Throwable e = task.getFault();
						logger.log(Level.SEVERE, e.getMessage(), e);

						if(e instanceof DisconnectException) {
							Main.disconnectError(ConfirmController.this);
						}
						else {
							Alert.alert(MessageType.ERROR, e.getMessage(), ConfirmController.this);
						}
					}
				}));
			}
		});
	}

	private void addSelectButtonPressListener() {
		selectButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				new Task<FinBookRespMsg.ResponseCode>() {

					@Override
					public FinBookRespMsg.ResponseCode execute() throws TaskExecutionException {

						Main.mailbox.send(new FinBookMsg(System.currentTimeMillis()));

						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						if(msg instanceof FinBookRespMsg) {
							FinBookRespMsg resp = (FinBookRespMsg) msg;
							return resp.getResponseCode();
						}
						else if(msg instanceof DisconnectMsg) {
							throw new TaskExecutionException(new DisconnectException());
						}
						else {
							throw new TaskExecutionException(new IncorrectMessageTypeException());
						}
					}

				}.execute(new TaskAdapter<>(new TaskListener<FinBookRespMsg.ResponseCode>() {
					@Override
					public void taskExecuted(Task<FinBookRespMsg.ResponseCode> task) {
						FinBookRespMsg.ResponseCode code = task.getResult();

						switch(code) {
							case SUCCESS:
								Alert.alert(MessageType.INFO, "You successfully booked the seat! " +
									"You can now close the application.", ConfirmController.this);
								break;

							case FAILED:
								Alert.alert(MessageType.ERROR, "The booking failed!", ConfirmController.this);
								break;
						}
					}

					@Override
					public void executeFailed(Task<FinBookRespMsg.ResponseCode> task) {
						Throwable e = task.getFault();
						logger.log(Level.SEVERE, e.getMessage(), e);

						if(e instanceof DisconnectException) {
							Main.disconnectError(ConfirmController.this);
						}
						else {
							Alert.alert(MessageType.ERROR, e.getMessage(), ConfirmController.this);
						}
					}
				}));
			}
		});
	}

	private void addCancelButtonPressListener() {
		cancelButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				new Task<Void>() {
					@Override
					public Void execute() throws TaskExecutionException {
						Main.mailbox.send(new AbortBookMsg(System.currentTimeMillis()));
						return null;
					}
				}.execute(new TaskAdapter<Void>(new TaskListener<Void>() {
					@Override
					public void taskExecuted(Task<Void> task) {
						Alert.alert(MessageType.INFO, "Successfully sent booking cancel.", ConfirmController.this);
					}

					@Override
					public void executeFailed(Task<Void> task) {

					}
				}));
			}
		});
	}

	public void setSeats(Seat[] seats) {
		this.seat_list = seats;

		// Load seats into the table view
		if(seatTable != null) {
			displaySeats();
		}
	}

	private void displaySeats() {
		ArrayList<HashMap> table = new ArrayList<>();

		for(Seat s : seat_list) {
			HashMap<String, String> row = new HashMap<>();

			row.put("seatID", s.getSeatID());
			row.put("class", s.getKlass());
			row.put("user", s.getUser());
			row.put("window", s.getWindow());
			row.put("aisle", s.getAisle());
			row.put("row", s.getRow());
			row.put("column", s.getCol());
			row.put("price", s.getPrice());
			row.put("locked", s.getLocked());

			table.add(row);
		}

		seatTable.setTableData(table);
	}
}
