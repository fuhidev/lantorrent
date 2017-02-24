package transferfile.client.transfer.tracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import transferfile.client.collections.UpLoadCollection;
import transferfile.client.observer.ICloseProcess;
import transferfile.lib.ConsoleOut;

public class SendFileThread extends Thread implements IUploadManager{
	private ServerSocket serverSocket;
	private ICloseProcess parent;
	private UpLoadCollection collection;
	
	public UpLoadCollection getCollection() {
		return collection;
	}

	public void setParent(ICloseProcess parent) {
		this.parent = parent;
	}

	public SendFileThread(ServerSocket serverSocket) {
		super();
		this.serverSocket = serverSocket;
		collection = new UpLoadCollection();
	}

	@Override
	public void run() {
		while (true) {
			try {
				ConsoleOut.out(this.getClass(), "Chờ nhận request từ client");
				Socket socket = serverSocket.accept();
				ConsoleOut.out(this.getClass(), "Đã nhận request từ " + socket);
				ConsoleOut.out(this.getClass(), "Khởi chạy tiến tình gửi file");
				SendFileUnitThread thread = new SendFileUnitThread(socket);
				thread.setFinish(this);
				thread.start();
			} catch (IOException e) {
				ConsoleOut.exception(getClass(), e);
				parent.close();
			}
		}
	}

	@Override
	public void finish(ProgressUpload download) {
		collection.remove(download);
	}

	@Override
	public void add(ProgressUpload upload) {
		collection.add(upload);
		
	}

}
