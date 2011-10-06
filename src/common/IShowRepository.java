package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface IShowRepository extends Remote {

	public void createShow(String showID, IBoxOffice boxOffice) throws RemoteException;
	public void cancelShow(String showID) throws RemoteException;
	public HashMap<String, IBoxOffice> getAllShows() throws RemoteException;
	
}
