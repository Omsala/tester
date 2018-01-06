package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.HashMap;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Seat;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.DisconnectMsg;
import ospp.bookinggui.networking.messages.RequestSeatSuggestionMsg;
import ospp.bookinggui.networking.messages.RequestSeatSuggestionRespMsg;
import ospp.pivotgui.Main;
import ospp.pivotgui.exceptions.DisconnectException;
import ospp.pivotgui.exceptions.IncorrectMessageTypeException;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookController extends Window implements Bindable {

	private static final Logger logger = Logger.getLogger(BookController.class.getName());

	@BXML
	private TableView  flightTable  = null;
	@BXML
	private PushButton selectButton = null;

	private Flight[] flights = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {

		// Load flights into flightTable
		if(flights != null) {
			displayFlights();
		}

		selectButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				new Task<Seat[]>() {

					@Override
					public Seat[] execute() throws TaskExecutionException {
						HashMap<String, String> row = (HashMap<String, String>) flightTable.getSelectedRow();

						Main.mailbox.send(new RequestSeatSuggestionMsg(System.currentTimeMillis(), row.get("flightID")));

						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						if(msg instanceof RequestSeatSuggestionRespMsg) {
							RequestSeatSuggestionRespMsg resp = (RequestSeatSuggestionRespMsg) msg;
							return resp.getSeatList();
						}
						else if(msg instanceof DisconnectMsg) {
							throw new TaskExecutionException(new DisconnectException());
						}
						else {
							logger.severe("Client received incorrect message type! Type: " + msg.getType());
							throw new TaskExecutionException(new IncorrectMessageTypeException());
						}
					}

				}.execute(new TaskAdapter<>(new TaskListener<Seat[]>() {

					@Override
					public void taskExecuted(Task<Seat[]> task) {
						openBookConfirmWindow(task.getResult());
					}

					@Override
					public void executeFailed(Task<Seat[]> task) {
						Throwable e = task.getFault();
						logger.log(Level.SEVERE, e.getMessage(), e);

						if(e instanceof DisconnectException) {
							Main.disconnectError(BookController.this);
						}
						else {
							Alert.alert(MessageType.ERROR, e.getMessage(), BookController.this);
						}
					}
				}));
			}
		});
	}

	public void setFlights(Flight[] flights) {
		this.flights = flights;

		// Load flights into flightTable
		if(flightTable != null) {
			displayFlights();
		}
	}

	private void displayFlights() {
		ArrayList<HashMap> tableRows = new ArrayList<>();

		for(Flight f : flights) {
			HashMap<String, String> row = new HashMap<>();

			row.put("flightID", f.getFlightID());
			row.put("flightNumber", f.getFlightNumber());
			row.put("fromAirport", f.getFrom().getName());
			row.put("toAirport", f.getTo().getName());
			row.put("departure", f.getDeparture().toString());
			row.put("arrival", f.getArrival().toString());

			tableRows.add(row);
		}

		flightTable.setTableData(tableRows);
	}

	private void openBookConfirmWindow(Seat[] seat_list) {
		this.close();
		ConfirmController c = (ConfirmController) Main.loadWindow(ConfirmController.class, "confirm.bxml");
		c.setSeats(seat_list);
	}
}
