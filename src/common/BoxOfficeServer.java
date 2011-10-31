package common;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import domain.Event;

public class BoxOfficeServer extends UnicastRemoteObject implements IBoxOffice {

	private static final long serialVersionUID = 1L;
	
	// Data needed to connect to the RMI repository
	private static final String REPOSITORY_HOST   = "localhost";
	private static final int    REPOSITORY_PORT   = 1099;
	private Registry registry = LocateRegistry.getRegistry(REPOSITORY_HOST, REPOSITORY_PORT);
	
	// Data members needed to implement the IBoxOffice interface
	private volatile Map<String, Event>  available_shows = new HashMap<String, Event>();  // <show_id, Event>
	private String city = null;
	
	
	public BoxOfficeServer(String city) throws Exception {
		super();
		this.city = city;
		initialize();
		registry.bind(this.city, this);
	}
	
	public synchronized void createEvent(String event_id, String title, int capacity) throws RemoteException {
		if (available_shows.containsKey(event_id)) { throw new RemoteException("Duplicate event"); }
		try {
			Event e = new Event(event_id, title, capacity);
			available_shows.put(event_id, e);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initialize() throws Exception {
		for(int i = 100; i < 104; i++) {
			String show_id    = this.city+i;
			String show_title = "Show"+i;
			
			createEvent(show_id, show_title, 100);
		}
		
	}

	@Override
	public void reserve(int customer_id, String show_id, int qty) throws RemoteException {
		if(available_shows.containsKey(show_id)) {
			try {
				available_shows.get(show_id).reserve(customer_id, qty);
				System.out.println(customer_id+" reserved "+qty+" ticket(s) for "+show_id);
				System.out.println("Number of available tickets left: "+show(show_id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void cancel(int customer_id, String show_id, int qty) throws RemoteException {
		if(available_shows.containsKey(show_id)) {
			try {
				available_shows.get(show_id).cancelReservation(customer_id, qty);
				System.out.println(customer_id+" cancelled "+qty+" ticket(s) for "+show_id);
				System.out.println("Number of available tickets left: "+show(show_id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public int show(String show_id) throws RemoteException {
		if(available_shows.containsKey(show_id)) {
			return available_shows.get(show_id).availability();
		}
		return 0;
	}
	
	public ArrayList<String> allEvents() {
		ArrayList<String> all = new ArrayList<String>(available_shows.size());
		for (Map.Entry<String, Event> event : available_shows.entrySet()) {
			all.add(event.getKey());
		}
		return all;
	}

}
