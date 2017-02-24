package transferfile.server.fileList;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;

public class ReceiveFileList {
	private InputStream inputStream;

	public ReceiveFileList(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
	}

	public FileInfo receive() throws IOException, ClassNotFoundException {

		ObjectInputStream stream = new ObjectInputStream(inputStream);

		ConsoleOut.out(getClass(), "Nhận danh sách từ client");
		return (FileInfo) stream.readObject();
	}

}
