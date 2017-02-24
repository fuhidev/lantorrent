package transferfile.client.fileList;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import transferfile.lib.ConsoleOut;
import transferfile.lib.model.ListFilesInfo;

public class ReceiveFileList {
	private InputStream inputStream;

	public ReceiveFileList(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
	}

	private ListFilesInfo result;

	public ListFilesInfo getResult() {
		return result;
	}

	public void receive() throws IOException, ClassNotFoundException {
		ObjectInputStream stream = new ObjectInputStream(inputStream);
		ListFilesInfo object = (ListFilesInfo) stream.readObject();
		ConsoleOut.out(getClass(), "Nhận được danh sách tệp tin tù Server");
		this.result = object;
	}

	// @Override
	// public void run() {
	// receive();
	// }
}
