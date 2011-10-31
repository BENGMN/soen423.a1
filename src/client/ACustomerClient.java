package client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import common.IBoxOffice;

public class ACustomerClient implements Runnable {

	private static final String REPOSITORY_HOST   = "localhost";
	private static final int    REPOSITORY_PORT   = 1099;
	private Registry registry = null; 
	private int customer_id = 0;
	
	public ACustomerClient(int customer_id) throws RemoteException, NotBoundException {
		this.customer_id = customer_id; 
		registry = LocateRegistry.getRegistry(REPOSITORY_HOST, REPOSITORY_PORT);
	}
	
	public void reserve(String show_id, int qty) throws AccessException, RemoteException, NotBoundException {
	  IBoxOffice box_office = (IBoxOffice)registry.lookup(show_id.substring(0, 3));
	  box_office.reserve(customer_id, show_id, qty);
	}
	
	public void cancel(String show_id, int qty) throws AccessException, RemoteException, NotBoundException {
		  IBoxOffice show = (IBoxOffice)registry.lookup(show_id.substring(0,3));
		  show.cancel(customer_id, show_id, qty);
	}
	
	public ArrayList<String> getAllEvents() throws AccessException, RemoteException, NotBoundException {
		String[] offices = registry.list();
		ArrayList<String> events = new ArrayList<String>();
		for(String s : offices) {
			IBoxOffice box = (IBoxOffice)registry.lookup(s);
			for(String t : box.allEvents()) {
				events.add(t);
			}
		}
		return events;
	}
	
	@Override
	public void run() {
		try {
           
			ArrayList<String> events = getAllEvents();
			for (String e : events) {
				reserve(e, 10);
			    Thread.sleep(1000);
			}	
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
	    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args) throws InterruptedException {
		try {
			ACustomerClient a = new ACustomerClient(123456);
			ACustomerClient b = new ACustomerClient(654321);
			ACustomerClient c = new ACustomerClient(100000);
			
			Thread t_a = new Thread(a);
			t_a.start();
			Thread t_b = new Thread(b);
			

			
			//t_a.start();
			//System.out.println("Thread A running");
			//t_a.yield();
			//t_b.start();
			//System.out.println("Thread B running");
			//t_b.sleep(100);
			//t_c.start();
			//t_b.join();
			//System.out.println("Thread C running");
			//t_c.sleep(100);
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

}
