package transferfile.client.fileserver;

import java.io.IOException;
import java.net.Socket;

import transferfile.client.observer.ICloseProcess;
import transferfile.client.observer.ObserverRequestServer;
import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.TrackerList;

public class RequestFileToServerManager  implements ObserverRequestServer {
	private Socket socket;
	private ICloseProcess parent;
	private IRequestFileToServer client;
	
	public void setRequest(IRequestFileToServer client) {
		this.client = client;
	}
	
	public void setParent(ICloseProcess parent) {
		this.parent = parent;
	}

	public RequestFileToServerManager(Socket socket) {
		super();
		this.socket = socket;
	}

	public void request(FileInfo fileInfo) {
		try {
			ConsoleOut.out(this.getClass(), "Khởi tạo tiến trình yêu cầu nhận file đến server với " + fileInfo);
			RequestFileToServerThread thread = new RequestFileToServerThread(socket.getInputStream(), socket.getOutputStream(), fileInfo);
			thread.setObserver(this);
			thread.start();
		} catch (IOException e) {
			ConsoleOut.exception(getClass(), e);
			parent.close();
		}
	}

	@Override
	public void update(FileInfo file, TrackerList trackerList) {
		client.receiveTrackerList(file, trackerList);
		ConsoleOut.out(this.getClass(), "Thông báo đã nhận được trackerList cho file" + file);
		ConsoleOut.out(this.getClass(), "Ghi file torrent");
		if(trackerList == null || trackerList.getSize() == 0)
			ConsoleOut.out(getClass(), "Không tìm thấy "+file+" yêu cầu");
	}

}