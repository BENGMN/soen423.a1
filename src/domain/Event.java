package domain;

import java.util.HashMap;
import java.util.Map;

import technical.log.ReservationLog;

public class Event {
	
	private String title = null;
	private String id    = null;
	private volatile int availability = 0;
	private Map<String, Integer> reservations = new HashMap<String, Integer>(); // customer_id, quantity
	
	public Event(String title, String id, int capacity) throws Exception {
		if (capacity < 1) { throw new Exception("Capacity must be greater than 1");}
		this.title = title;
		this.id = id;
		this.availability = capacity;
	}
	
	public synchronized void reserve(String customer_id, int qty) throws Exception {
		if (qty > 0 && qty <= this.availability) {
			if (reservations.containsKey(customer_id)) {
				ReservationLog.update(this.id, customer_id, qty);
			}
			else {
				ReservationLog.insert(this.id, customer_id, qty);
			}
			reservations.put(customer_id, qty);
			this.availability -= qty;
			notifyAll();
		}
		else {
			throw new Exception("Insufficient number of tickets for the request");
		}
	}
	
	public synchronized void cancelReservation(String customer_id, int qty) throws Exception {
		if (reservations.containsKey(customer_id)) {
			int reserved_qty = reservations.get(customer_id);
			if (qty > reserved_qty) {
				throw new Exception("Attempting to cancel more tickets than purchased");
			}
			else {
				int balance = qty - reserved_qty; 
				if (balance > 0) {
					reservations.put(customer_id, balance);
					ReservationLog.update(this.id, customer_id, balance);
				}
				else {
					reservations.remove(customer_id);
					ReservationLog.destroy(this.id, customer_id);
				}
				this.availability += qty;
			}
		}
		notifyAll();
	}
	
	public int availability() {
		return this.availability;
	}

	public String getTitle() {
		return this.title;
	}

	public String getEventID() {
		return this.id;
	}
}
