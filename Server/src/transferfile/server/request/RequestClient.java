package transferfile.server.request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;

public class RequestClient extends Thread {
	private Socket e;
	private ClientHaveFile result;
	private FileInfo file;

	public RequestClient(Socket e, ClientHaveFile result, FileInfo file) {
		super();
		this.e = e;
		this.result = result;
		this.file = file;
	}

	@Override
	public void run() {
		try {
			ConsoleOut.out(this.getClass(), "Gửi yêu cầu tìm file " + file + "đến " + e);
			ObjectOutputStream outputStream = new ObjectOutputStream(e.getOutputStream());
			outputStream.writeObject(file);
			outputStream.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			boolean coFile;

			coFile = e.getInputStream().read() == 1;
			if (coFile) {
				ConsoleOut.out(this.getClass(), "Nhận được thông báo có " + file + " từ " + e);
				// tiepNhanFile.add(new
				// Tracker(next[0].getInetAddress().getHostAddress()));
				result.add(e);
			}
		} catch (IOException e1) {
			ConsoleOut.exception(getClass(), e1);
			return;
//			e1.printStackTrace();
		}

	}
}
