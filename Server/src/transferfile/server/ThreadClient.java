package transferfile.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.ListFilesInfo;
import transferfile.lib.model.TrackerList;
import transferfile.server.collections.ListConnect;
import transferfile.server.fileList.FileListManager;

public class ThreadClient extends Thread implements IThreadClient {
	private String ip;
	private Socket[] socket;
	private IServer server;

	public String getIp() {
		return ip;
	}

	private FileListManager fileListManager;

	public void setServer(IServer server) {
		this.server = server;
	}

	public ThreadClient(String ip, Socket[] socket) {
		super();
		this.ip = ip;
		this.socket = socket;
		try {
			fileListManager = new FileListManager(socket[ListConnect.DANH_SACH_TEP]);
			fileListManager.setParent(this);
		} catch (IOException e) {
			close();
			return;
		}

	}

	private boolean isClose = false;

	public void sendListFiles(ListFilesInfo filesInfo) {
		fileListManager.send(filesInfo);
	}

	@Override
	public void run() {
		fileListManager.start();

		while (!isClose) {
			try {

				Socket skRequest = socket[ListConnect.NHAN_YEU_CAU_TAI];

				ObjectInputStream inputStream = new ObjectInputStream(skRequest.getInputStream());
				FileInfo file = null;
				ConsoleOut.out(this.getClass(), "Nhận được yêu cầu tải tệp từ " + skRequest);
				file = (FileInfo) inputStream.readObject();

				TrackerList trackerList = server.notyClient(ip, file);
				sendTrackerList(trackerList);
			} catch (IOException | ClassNotFoundException e) {
				close();
				break;
			}
		}
	}

	public void sendTrackerList(TrackerList trackerList) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				socket[ListConnect.NHAN_YEU_CAU_TAI].getOutputStream());
		ConsoleOut.out(this.getClass(), "Gửi Tracker xuống " + socket[ListConnect.NHAN_YEU_CAU_TAI]);
		objectOutputStream.writeObject(trackerList);
		objectOutputStream.flush();
	}

	@Override
	public void close() {
		isClose = true;
		server.disconnect(ip, socket);
	}

	@Override
	public void addFileInfo(FileInfo fileInfo) {
		server.updateListFile(fileInfo);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String) {
			String ip = (String) obj;
			return this.ip.equals(ip);
		}
		return false;
	}
}