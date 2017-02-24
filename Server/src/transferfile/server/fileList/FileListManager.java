package transferfile.server.fileList;

import java.io.IOException;
import java.net.Socket;

import transferfile.lib.model.FileInfo;
import transferfile.lib.model.ListFilesInfo;
import transferfile.server.IThreadClient;

public class FileListManager extends Thread {
//	private Socket socket;
	private SendInfoList sendFileInfo;
	private ReceiveFileList receiveFileList;
	private IThreadClient parent;

	public void setParent(IThreadClient parent) {
		this.parent = parent;
	}


	public FileListManager(Socket socket) throws IOException {
		super();
//		this.socket = socket;
		sendFileInfo = new SendInfoList(socket.getOutputStream());
		receiveFileList = new ReceiveFileList(socket.getInputStream());
	}

	public void send() {
		sendFileInfo.send(ListFileInfoManager.getInstances().getFilesInfo());
	}

	@Override
	public void run() {
		FileInfo receive;
		while (true) {
			try {
				receive = receiveFileList.receive();
				parent.addFileInfo(receive);
			} catch (ClassNotFoundException | IOException e) {
				parent.close();
				break;
			}
		}

	}

	public void send(ListFilesInfo filesInfo) {
		sendFileInfo.send(filesInfo);
	}

}
