package common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ShowRepositoryServer extends UnicastRemoteObject implements IShowRepository {

	private static final long serialVersionUID = 1L;

	private HashMap<String, IBoxOffice> availableShows = new HashMap<String, IBoxOffice>();
	
	protected ShowRepositoryServer() throws RemoteException {
		super();
	}

	@Override
	public void createShow(String showID, IBoxOffice boxOffice)	throws RemoteException {
		this.availableShows.put(showID, boxOffice);
	}

	@Override
	public void cancelShow(String showID)	throws RemoteException {
		this.availableShows.remove(showID);
	}

	@Override
	public HashMap<String, IBoxOffice> getAllShows() throws RemoteException {
		return availableShows;
	}

}
