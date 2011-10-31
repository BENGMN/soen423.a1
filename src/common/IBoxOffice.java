package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IBoxOffice extends Remote {

	public void reserve(int customer_id, String show_id, int qty) throws RemoteException;
	public void cancel(int customer_id, String show_id, int qty) throws RemoteException;
	public int show(String show_id) throws RemoteException;
	public ArrayList<String> allEvents() throws RemoteException;
	public void createEvent(String event_id, String title, int capacity) throws RemoteException;
	
}
