package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.Airport;
import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.*;
import ospp.pivotgui.Main;
import ospp.pivotgui.exceptions.DisconnectException;
import ospp.pivotgui.exceptions.IncorrectMessageTypeException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectionController extends Window implements Bindable {

	private static final Logger logger = Logger.getLogger(SelectionController.class.getName());
	private final ConcurrentHashMap<String, Airport> loaded_airports = new ConcurrentHashMap<>();
	// These fields are populated by the BXMLSerializer initializing this object.
	@BXML
	private TextInput  searchFrom   = null;
	@BXML
	private TextInput  searchTo     = null;
	@BXML
	private ListView   fromList     = null;
	@BXML
	private ListView   toList       = null;
	@BXML
	private PushButton selectButton = null;
	@BXML
	private PushButton reloadButton = null;
	private       ArrayList<String>                  airport_list    = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {
		addReloadButtonPressListener();
		addSelectButtonPressListener();

		addSearchAdapter();

		addFromListListener();
		addToListListener();
	}

	private void addToListListener() {
		toList.getListViewSelectionListeners().add(new ListViewSelectionListener() {
			@Override
			public void selectedRangeAdded(ListView listView, int i, int i1) {
			}

			@Override
			public void selectedRangeRemoved(ListView listView, int i, int i1) {
			}

			@Override
			public void selectedRangesChanged(ListView listView, Sequence<Span> sequence) {
			}

			@Override
			public void selectedItemChanged(ListView listView, Object o) {
				String to = (String) toList.getSelectedItem();
				searchTo.setText(to);
			}
		});
	}

	private void addFromListListener() {
		fromList.getListViewSelectionListeners().add(new ListViewSelectionListener() {
			@Override
			public void selectedRangeAdded(ListView listView, int i, int i1) {
			}

			@Override
			public void selectedRangeRemoved(ListView listView, int i, int i1) {
			}

			@Override
			public void selectedRangesChanged(ListView listView, Sequence<Span> sequence) {
			}

			@Override
			public void selectedItemChanged(ListView listView, Object o) {
				String from = (String) fromList.getSelectedItem();
				searchFrom.setText(from);
			}
		});
	}

	private void addSearchAdapter() {
		TextInputContentListener.Adapter adapter = new TextInputContentListener.Adapter() {
			@Override
			public void textInserted(final TextInput textInput, int index, int count) {
				String text = textInput.getText();

				int i = ArrayList.binarySearch(airport_list, text,
					airport_list.getComparator());

				if(i < 0) {
					i = -(i + 1);
					int n = airport_list.getLength();

					if(i < n) {
						text = text.toLowerCase();
						final String state = airport_list.get(i);

						if(state.toLowerCase().startsWith(text)) {
							String nextState = (i == n - 1) ?
								null : airport_list.get(i + 1);

							if(nextState == null
								|| !nextState.toLowerCase().startsWith(text)) {
								textInput.setText(state);

								int selectionStart = text.length();
								int selectionLength = state.length() - selectionStart;
								textInput.setSelection(selectionStart, selectionLength);
							}
						}
					}
				}
			}
		};

		searchFrom.getTextInputContentListeners().add(adapter);
		searchTo.getTextInputContentListeners().add(adapter);
	}

	private void addSelectButtonPressListener() {
		selectButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				final String fromAirport = searchFrom.getText();
				final String toAirport = searchTo.getText();

				if(fromAirport.equals("MissingAirports") || toAirport.equals("MissingAirports")) {
					Alert.alert(MessageType.ERROR,
						"\"MissingFlight\" is not a legal selection! Please wait for the client to load the airports!",
						SelectionController.this);
					return;
				}

				new Task<Flight[]>() {
					@Override
					public Flight[] execute() throws TaskExecutionException {

						Airport from = loaded_airports.get(fromAirport);
						Airport to = loaded_airports.get(toAirport);

						if(from == null) {
							throw new TaskExecutionException(new Exception("Could not find airport with the name \"" + fromAirport + "\"!"));
						}
						if(to == null) {
							throw new TaskExecutionException(new Exception("Could not find airport with the name \"" + toAirport + "\"!"));
						}

						Main.mailbox.send(new SearchAirportRouteMsg(System.currentTimeMillis(), from.getAirportID(), to.getAirportID()));

						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						if(msg instanceof SearchAirportRouteRespMsg) {
							SearchAirportRouteRespMsg resp = (SearchAirportRouteRespMsg) msg;
							return resp.getFlightList();
						}
						else if(msg instanceof DisconnectMsg) {
							throw new TaskExecutionException(new Exception("Received Disconnect message!"));
						}
						else {
							logger.severe("Client received incorrect message type! Received type: " + msg.getType());
							throw new TaskExecutionException(new IncorrectMessageTypeException());
						}
					}
				}.execute(new TaskAdapter<>(new TaskListener<Flight[]>() {
					@Override
					public void taskExecuted(Task<Flight[]> task) {
						loadBookingWindow(task.getResult());
					}

					@Override
					public void executeFailed(Task<Flight[]> task) {
						Throwable e = task.getFault();
						logger.log(Level.SEVERE, e.getMessage(), e);

						if(e instanceof DisconnectException) {
							Main.disconnectError(SelectionController.this);
						}
						else {
							Alert.alert(MessageType.ERROR, e.getMessage(), SelectionController.this);
						}
					}
				}));
			}
		});
	}

	private void addReloadButtonPressListener() {
		reloadButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {

				new Task<Airport[]>() {
					@Override
					public Airport[] execute() throws TaskExecutionException {
						Main.mailbox.send(new RequestAirportsMsg(System.currentTimeMillis(), null));

						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						if(msg instanceof RequestAirportsRespMsg) {
							RequestAirportsRespMsg resp = (RequestAirportsRespMsg) msg;
							return resp.getAirports();
						}
						else if(msg instanceof DisconnectMsg) {
							throw new TaskExecutionException(new Exception("Received Disconnect message!"));
						}
						else {
							logger.severe("Client received incorrect response type to RequestAirportsMsg! " +
								"Received type: " + msg.getType());
							throw new TaskExecutionException("Client received incorrect response type!");
						}
					}
				}.execute(new TaskAdapter<>(new TaskListener<Airport[]>() {
					@Override
					public void taskExecuted(Task<Airport[]> task) {
						storeAirports(task.getResult());
					}

					@Override
					public void executeFailed(Task<Airport[]> task) {
						Throwable e = task.getFault();
						logger.log(Level.SEVERE, e.getMessage(), e);
						Alert.alert(MessageType.ERROR, e.getMessage(), SelectionController.this);
					}
				}));
			}
		});
	}

	private void storeAirports(Airport[] airports) {
		airport_list = new ArrayList<>();

		for(Airport a : airports) {
			airport_list.add(a.getName());
			loaded_airports.put(a.getName(), a);
		}

		// Sort the list of airports
		airport_list.setComparator(String.CASE_INSENSITIVE_ORDER);

		// Display the list
		fromList.setListData(airport_list);
		toList.setListData(airport_list);
	}

	public void pushReloadButton() {
		reloadButton.press();
	}

	private void loadBookingWindow(Flight[] flights) {
		this.close();
		BookController c = (BookController) Main.loadWindow(BookController.class, "book.bxml");
		c.setFlights(flights);
	}
}
