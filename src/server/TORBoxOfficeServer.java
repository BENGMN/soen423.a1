package server;

import common.BoxOfficeServer;

public class TORBoxOfficeServer {
	public static void main (String[] args) {
		try {
			new BoxOfficeServer("TOR");
			System.out.println("The Box-Office Server is up and running in Toronto");
		}
		catch (Exception e) {
			System.out.println("The Box-Office Server failed:\n");
			e.printStackTrace();
		}
	}
}
