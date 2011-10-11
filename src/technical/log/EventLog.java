package technical.log;

import java.io.IOException;

public interface EventLog {
	
	public void create();
	public void read();
	public void insert();
	public void update();
	public void destroy();
	void insert(String event_id, String customer_id, int qty) throws IOException;

}
