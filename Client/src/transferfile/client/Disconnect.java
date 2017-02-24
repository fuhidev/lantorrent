package transferfile.client;

public class Disconnect {
	private static Disconnect dis;
	public static Disconnect getInstances(){
		if(dis == null)
			dis = new Disconnect();
		return dis;
	}
	private Disconnect(){
		
	}
	public void disconnect(){
		
	}
}
