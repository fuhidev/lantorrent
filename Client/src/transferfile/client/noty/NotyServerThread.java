package transferfile.client.noty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import transferfile.client.observer.ICloseProcess;
import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;

public class NotyServerThread extends Thread {
	private Socket socket;
	private ExistFile checkFile = new CheckFile();
	private ICloseProcess parent;

	public void setParent(ICloseProcess parent) {
		this.parent = parent;
	}

	public NotyServerThread(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Chờ nhận thông báo từ server - " + this.getClass().getName());
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

				FileInfo info = (FileInfo) inputStream.readObject();
				System.out.println("Nhận được thông báo " + info + " - " + this.getClass().getName());
				boolean check = checkFile.check(info);
				System.out.println("Gửi thông báo " + check + " đến server - " + this.getClass().getName());
				socket.getOutputStream().write(check ? 1 : 0);
			} catch (IOException | ClassNotFoundException e) {
				parent.close();
				ConsoleOut.exception(getClass(), e);
				// e.printStackTrace();
			}
		}
	}
}
