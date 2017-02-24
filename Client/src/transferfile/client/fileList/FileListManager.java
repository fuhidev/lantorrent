package transferfile.client.fileList;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import transferfile.client.collections.FilesSharingCollection;
import transferfile.client.noty.NotyListFile;
import transferfile.client.observer.IAddActiveFile;
import transferfile.client.observer.ICloseProcess;
import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;

public class FileListManager extends Thread implements IFileListManager {
	// private Socket socket;
	private AddFileInfo addFileInfo;
	
	private NotyListFile notyListFile;
	private ICloseProcess parent;
	private ReceiveFileList receiveFileList;
	private IAddActiveFile activeFile;
	
	public void setActiveFile(IAddActiveFile activeFile) {
		this.activeFile = activeFile;
	}

	private FilesSharingCollection filesSharing;
	
	public FileListManager(Socket socket) {
		try {
			filesSharing = new FilesSharingCollection();
			addFileInfo = new AddFileInfo(socket.getOutputStream());
			addFileInfo.setObserver(this);
			receiveFileList = new ReceiveFileList(socket.getInputStream());
		} catch (IOException e) {
			ConsoleOut.exception(getClass(), e);
		}
	}
	

	public void addFileInfo(File file) throws IOException {
		addFileInfo.add(file);
		this.filesSharing.add(file);
		this.activeFile.addActiveFile(new FileInfo(file.getName(), file.length()));
	}

	public FilesSharingCollection getListFiles() {
		return filesSharing;
	}

	public void receive() {
		try {
			receiveFileList.receive();
			ConsoleOut.out(getClass(), "Chuyển danh sách nhận file nhận được cho Client Process");
			if(receiveFileList.getResult() == null)
				return;
			filesSharing.setList(receiveFileList.getResult());
			notyListFile.updateNotyListFile(receiveFileList.getResult());
		} catch (ClassNotFoundException | IOException e) {
			// Disconnect.getInstances().disconnect();
//			ConsoleOut.exception(getClass(), e);
			parent.close();
			// e.printStackTrace();
		}
	}

	@Override
	public void run() {
		ConsoleOut.out(getClass(), "Chờ nhận danh sách file");
		while (true) {
			receive();
		}
	}

	public void setNotyListFile(NotyListFile notyListFile) {
		this.notyListFile = notyListFile;
	}

	public void setParent(ICloseProcess parent) {
		this.parent = parent;
	}


	@Override
	public void loading() {
		notyListFile.loading();
	}


	@Override
	public void finishMove() {
		notyListFile.finishMove();
	}
}
