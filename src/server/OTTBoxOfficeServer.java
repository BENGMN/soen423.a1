package server;

import common.BoxOfficeServer;

public class OTTBoxOfficeServer {
	public static void main (String[] args) {
		try {
			new BoxOfficeServer("OTT");
			System.out.println("The Box-Office Server is up and running in Ottawa");
		}
		catch (Exception e) {
			System.out.println("The Box-Office Server failed:\n");
			e.printStackTrace();
		}
	}
}
