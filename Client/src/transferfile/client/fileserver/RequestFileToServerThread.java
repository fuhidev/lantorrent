package transferfile.client.fileserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import transferfile.client.observer.ObserverRequestServer;
import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.TrackerList;

public class RequestFileToServerThread extends Thread{
	private InputStream is;
	private OutputStream os;
	private FileInfo file;
	private ObserverRequestServer observer;
	
	public void setObserver(ObserverRequestServer observer) {
		this.observer = observer;
	}


	public RequestFileToServerThread(InputStream is, OutputStream os, FileInfo file) {
		super();
		this.is = is;
		this.os = os;
		this.file = file;
	}



	public void request() {
		try {
			ConsoleOut.out(this.getClass(), "Đang gửi yêu cầu nhận " + file
					+ " đến máy chủ");
			ObjectOutputStream outputStream = new ObjectOutputStream(os);
			outputStream.writeObject(file);
			outputStream.flush();
			ObjectInputStream inputStream = new ObjectInputStream(is);
			try {
				TrackerList listTracker = (TrackerList) inputStream.readObject();
				observer.update(file, listTracker);
//				new RequestFileSharing(listTracker, file);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		request();
	}
}
