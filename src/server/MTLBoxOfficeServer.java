package server;

import common.BoxOfficeServer;

public class MTLBoxOfficeServer {
	
	public static void main (String[] args) {
		try {
			new BoxOfficeServer("MTL");
			System.out.println("The Box-Office Server is up and running");
		}
		catch (Exception e) {
			System.out.println("The Box-Office Server failed:\n");
			e.printStackTrace();
		}
	}
	
}
