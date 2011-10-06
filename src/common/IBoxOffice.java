package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBoxOffice extends Remote {

	public void reserve(int customerID, String showID, int qtyTickets) throws RemoteException;
	public void cancel(int customerID, String showID, int qtyTickets) throws RemoteException;
	public int show(String showID) throws RemoteException;
	
}
