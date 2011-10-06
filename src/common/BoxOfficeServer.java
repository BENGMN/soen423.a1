package common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class BoxOfficeServer extends UnicastRemoteObject implements IBoxOffice {

	private static final long serialVersionUID = 1L;
	
	private static final String PATH = "/home/ben/workspace/soen423.a1/src/server/sales";
	
	// Data needed to connect to the RMI repository
	private static final String REPOSITORY_HOST   = "localhost";
	private static final int    REPOSITORY_PORT   = 1099;

	private String city = null;
	
	// Data members needed to implement the IBoxOffice interface
	private Map<String, Integer> availableTickets = new HashMap<String, Integer>(); // <showId, title>
	private Map<String, String>  availableShows   = new HashMap<String, String>();  // <showId, title>
	private ArrayList<File>      salesFiles       = new ArrayList<File>();
	
	private Registry registry = LocateRegistry.getRegistry(REPOSITORY_HOST, REPOSITORY_PORT);
	
	public BoxOfficeServer(String city) throws NotBoundException, AlreadyBoundException, IOException {
		super();
		this.city = city;
		initialize();
	}
	
	private void initialize() throws AlreadyBoundException, IOException {
		for(int i = 100; i < 104; i++) {
			String showID    = this.city+i;
			String showTitle = "Show"+i;
			
			availableShows.put(showID, showTitle);
			availableTickets.put(showID, 100);
			salesFiles.add(createSalesFile(PATH, showID+".csv"));
			registry.bind(showID, this);
		}
	}

	@Override
	public void reserve(int customerID, String showID, int qtyTickets) {
		recordSale(customerID, showID, qtyTickets);
	}

	@Override
	public void cancel(int customerID, String showID, int qtyTickets) throws RemoteException {
		cancelSale(customerID, showID, qtyTickets);
	}

	@Override
	public int show(String showID) throws RemoteException {
		return availableTickets.get(showID);
	}
	
	private File createSalesFile(String path, String name) throws IOException {
		File f = new File(path, name);
		if (!f.exists()) {
			System.out.println(f.getParent());
			f.createNewFile();
		}
		return f;
	}
	
	private void recordSale(int customerID, String showID, int qtyTickets) {
		try {
			File f = salesFiles.get(salesFiles.indexOf(showID));
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(customerID+","+qtyTickets+"\n");
			int total = availableTickets.get(showID);
			availableTickets.put(showID, total-qtyTickets);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void cancelSale(int customerID, String showID, int qtyTickets) {
		try {
			File temp = new File(PATH,"tmp");
			File f = salesFiles.get(salesFiles.indexOf(showID));
			
			BufferedReader in = new BufferedReader(new FileReader(f));
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			
			String line;
			while ((line = in.readLine()) != null){
				String[] s = line.split(",");
				if(s[0].equals("customerID")) {
					int balance = Integer.parseInt(s[1])-qtyTickets;
					int total = availableTickets.get(showID);
					if(balance > 0) {
						out.write(s[0]+","+balance+"\n");
						availableTickets.put(showID, total+qtyTickets);
					}
					else {
						availableTickets.put(showID, total+qtyTickets);
					}
				}
				else {
					out.write(s[0]+","+s[1]+"\n");
				}
			}
			
			in.close();
			out.close();
			f.delete();
			temp.renameTo(f);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
