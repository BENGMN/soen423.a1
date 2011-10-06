package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.IBoxOffice;

public class ACustomerClient {

	private static final String REPOSITORY_HOST   = "localhost";
	private static final int    REPOSITORY_PORT   = 1099;
	private Registry registry = null; 
	
	public ACustomerClient() throws RemoteException, NotBoundException {
		 registry = LocateRegistry.getRegistry(REPOSITORY_HOST, REPOSITORY_PORT);
		 String[] rmiObjects = registry.list();
		 for(String s : rmiObjects) {
			 System.out.println(s);
			 IBoxOffice box = (IBoxOffice)registry.lookup(s);
			 
		 }
	 }
	
	public static void main (String[] args) {
		try {
			new ACustomerClient();
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
