package transferfile.server.fileList;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import transferfile.lib.ConsoleOut;
import transferfile.lib.model.ListFilesInfo;

public class SendInfoList extends Thread {
	private OutputStream outputStream;

	public SendInfoList(OutputStream outputStream) {
		super();
		this.outputStream = outputStream;
	}

	public void send(ListFilesInfo f) {
		 ObjectOutputStream stream;
		 try {
		 stream = new ObjectOutputStream(outputStream);
		 stream.writeObject(f);
		 stream.flush();
		 ConsoleOut.out(getClass(), "Gửi dah sách file cho client");
		 } catch (IOException e1) {
		 ConsoleOut.exception(getClass(), e1);
		 }

	}

	@Override
	public void run() {

	}
}
