package technical.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ReservationLog {

	private static final String LOG_DIR = "/home/ben/workspace/soen423.a1/src/server/sales";
	
	public ReservationLog(String show_id) throws IOException {
		super();
		File f = new File(LOG_DIR, show_id);
		if (!f.exists()) {
			f.createNewFile();
		}
	}
	

	public static void insert(String show_id, String customer_id, int qty) throws IOException {
		File f = new File(LOG_DIR, show_id);
		BufferedWriter out = new BufferedWriter(new FileWriter(f));
		out.write(customer_id+","+qty);
	}
	

	public static void destroy(String show_id, String customer_id) throws IOException {
		File temp = new File(LOG_DIR,"destroy_tmp");
		File f = new File(LOG_DIR, show_id);
		
		BufferedReader in = new BufferedReader(new FileReader(f));
		BufferedWriter out = new BufferedWriter(new FileWriter(temp));
		
		String line;
		while ((line = in.readLine()) != null){
			String[] s = line.split(",");
			if(!s[0].equals(customer_id)) {
				out.write(s[0]+","+s[1]+"\n");
			}
		}
		in.close();
		out.close();
		f.delete();
		temp.renameTo(f);		
	}


	public static String[] read(String show_id, String customer_id) throws IOException {
		File f = new File(LOG_DIR, show_id);
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line;
		while ((line = in.readLine()) != null) {
			String[] s = line.split(",");
			if(s[0].equals(customer_id)) {
				return s;
			}
		}
		return null;
	}
	
	public static HashMap<String, Integer> readAll(String show_id) throws NumberFormatException, IOException {
		HashMap<String, Integer> reservations = new HashMap<String, Integer>();
		File f = new File(LOG_DIR, show_id);
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line;
		while ((line = in.readLine()) != null) {
			String[] s = line.split(",");
			reservations.put(s[0], Integer.parseInt(s[1]));
		}
		return reservations;
	}


	
	public static void update(String show_id, String customer_id, int qty) throws IOException {
		File temp = new File(LOG_DIR,"update_tmp");
		File f = new File(LOG_DIR, show_id);
		
		BufferedReader in = new BufferedReader(new FileReader(f));
		BufferedWriter out = new BufferedWriter(new FileWriter(temp));
		
		String line;
		while ((line = in.readLine()) != null){
			String[] s = line.split(",");
			if(s[0].equals(customer_id)) {
				out.write(customer_id+","+qty+"\n");
			}
			else {
				out.write(s[0]+","+s[1]+"\n");
			}
		}
		in.close();
		out.close();
		f.delete();
		temp.renameTo(f);
	}

}
