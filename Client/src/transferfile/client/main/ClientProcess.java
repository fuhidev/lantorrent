package transferfile.client.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import transferfile.client.collections.ActiveCollection;
import transferfile.client.collections.DownloadCollection;
import transferfile.client.collections.FilesSharingCollection;
import transferfile.client.collections.UpLoadCollection;
import transferfile.client.config.ConfigApp;
import transferfile.client.fileList.FileListManager;
import transferfile.client.fileserver.IRequestFileToServer;
import transferfile.client.fileserver.RequestFileToServerManager;
import transferfile.client.io.IOConfig;
import transferfile.client.noty.NotyListFile;
import transferfile.client.noty.NotyServerThread;
import transferfile.client.observer.IAddActiveFile;
import transferfile.client.observer.ICloseProcess;
import transferfile.client.transfer.seeder.ProgressDownload;
import transferfile.client.transfer.seeder.TransferFileSharingManager;
import transferfile.client.transfer.tracker.ProgressUpload;
import transferfile.client.transfer.tracker.SendFileThread;
import transferfile.client.view.IView;
import transferfile.lib.ConsoleOut;
import transferfile.lib.controller.IController;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.ListFilesInfo;
import transferfile.lib.model.TrackerList;

public class ClientProcess extends Thread implements NotyListFile, IRequestFileToServer, IAddActiveFile, ICloseProcess,
		PropertyChangeListener, IController {
	private RequestFileToServerManager fileToServerManager;
	private TransferFileSharingManager fileSharingManager;
	private SendFileThread sendFileThread;
	private NotyServerThread notyServerThread;

	private FileListManager fileListManager;

	// Khai báo socket
	private Socket socketeFilesinfo;
	private Socket socketRequestFileServer;
	private ServerSocket serverSocket;
	private Socket socketNoti;

	private List<IView> views = new ArrayList<>();;

	private ActiveCollection activeCollection;

	public ClientProcess() {

	}

	@Override
	public void addActiveFile(FileInfo f) {
		this.activeCollection.add(f);

	}

	public void addFile(File fileInfo) {
		try {
			this.fileListManager.addFileInfo(fileInfo);
		} catch (IOException e) {
			ConsoleOut.exception(getClass(), e);
		}
	}

	public void addView(IView view) {
		this.views.add(view);
	}

	public synchronized void close() {
		ConsoleOut.exception(getClass(), new Exception("Mất kết nối đến máy chủ"));
		// IOConfig.write();
		System.exit(1);
	}

	private void connectServer() {
		try {
			int hostPort = Integer.parseInt(ConfigApp.HOST_PORT);
			socketeFilesinfo = new Socket(ConfigApp.HOST_ADRESS, hostPort);
			socketRequestFileServer = new Socket(ConfigApp.HOST_ADRESS, hostPort);
			int trackerPort = Integer.parseInt(ConfigApp.TRACKER_PORT);
			serverSocket = new ServerSocket(trackerPort);
			socketNoti = new Socket(ConfigApp.HOST_ADRESS, hostPort);
		} catch (IOException e) {
			ConsoleOut.exception(getClass(), e);
		}
	}

	public void createFolder() {
		File file = new File(ConfigApp.DIRECTORY_SAVE_FILE);
		if (file.exists())
			return;
		file.mkdirs();
		file = new File(ConfigApp.DIRECTORY_TORRENT);
		file.mkdir();
		IOConfig.write();
	}

	public DownloadCollection getDownload() {
		return this.fileSharingManager.getCollection();
	}

	public FilesSharingCollection getListFilesInfo() {
		return fileListManager.getListFiles();
	}

	public void init() {
		File file = new File(System.getProperty("user.home") + "\\thtTorrent");
		if (!file.exists())
			file.mkdirs();
		file = new File(IOConfig.PATH_CONFIG);
		if (!file.exists()) {
			IOConfig.write();
		}
		IOConfig.read();
		connectServer();
		initProcess();

		activeCollection.loadData();

	}

	private void initProcess() {

		activeCollection = new ActiveCollection();
		activeCollection.addPropertyChangeSupport(this);
		// Danh sách file
		fileListManager = new FileListManager(socketeFilesinfo);
		fileListManager.setParent(this);
		fileListManager.setNotyListFile(this);
		fileListManager.setActiveFile(this);
		fileListManager.getListFiles().addPropertyChange(this);

		fileToServerManager = new RequestFileToServerManager(socketRequestFileServer);
		fileToServerManager.setParent(this);
		fileToServerManager.setRequest(this);

		// Truy vấn file đến Tracker
		fileSharingManager = new TransferFileSharingManager();
		fileSharingManager.setParent(this);
		fileSharingManager.setActiveFile(this);
		fileSharingManager.getCollection().addPropertyChangeListener(this);
		// Thread gửi tệp đến seeder
		sendFileThread = new SendFileThread(serverSocket);
		sendFileThread.setParent(this);
		sendFileThread.getCollection().addPropertyChangeListener(this);

		// Thread nhận và trả lời thông báo từ server
		notyServerThread = new NotyServerThread(socketNoti);
		notyServerThread.setParent(this);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof DownloadCollection) {
			if (evt.getPropertyName().equals(DownloadCollection.PROPERTY)) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					views.forEach(view -> {
						view.addDownload((ProgressDownload) evt.getNewValue());
					});

				} else if (evt.getOldValue() != null && evt.getNewValue() == null) {
					views.forEach(view -> {
						view.removeDownload((ProgressDownload) evt.getOldValue());
					});
				}
			}
			if (evt.getPropertyName().equals(DownloadCollection.PROPERTY_PROGRESS)) {
				if (evt.getOldValue() != null && evt.getNewValue() != null) {
					views.forEach(view -> {
						view.updateProgress((ProgressDownload) evt.getOldValue(), (long) evt.getNewValue());
					});

				}
			}
		}
		if (evt.getSource() instanceof UpLoadCollection) {
			if (evt.getPropertyName().equals(UpLoadCollection.PROPERTY)) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					views.forEach(view -> {
						view.addSendingFile((ProgressUpload) evt.getNewValue());
					});
					
				} else if (evt.getOldValue() != null && evt.getNewValue() == null) {
					views.forEach(view -> {
						view.removeSendingFile((ProgressUpload) evt.getOldValue());
					});
				}
			}
			if (evt.getPropertyName().equals(UpLoadCollection.PROPERTY_PROGRESS)) {
				if (evt.getOldValue() != null && evt.getNewValue() != null) {
					views.forEach(view -> {
						view.updateProgressUpload((ProgressUpload) evt.getOldValue(), (long) evt.getNewValue());
					});
					
				}
			}
		}
		if (evt.getSource() instanceof FilesSharingCollection) {
			if (evt.getPropertyName().equals(FilesSharingCollection.PROPERTY_LIST)) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {

					views.forEach(view -> {
						view.updateNonActiveFile((ListFilesInfo) evt.getNewValue());
					});
				}
			}
			if (evt.getPropertyName().equals(FilesSharingCollection.PROPERTY_FILE)) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					views.forEach(view -> {
						view.addNonActiveFile((FileInfo) evt.getNewValue());
					});

				}
			}
		}
		if (evt.getSource() instanceof ActiveCollection) {
			if (evt.getPropertyName().equals(ActiveCollection.PROPERTY_LIST)) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					views.forEach(view -> {
						view.updateActiveFile((ListFilesInfo) evt.getNewValue());
					});

				}
			}
			if (evt.getPropertyName().equals(ActiveCollection.PROPERTY_FILE)) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					views.forEach(view -> {
						view.addActiveFile((FileInfo) evt.getNewValue());
					});

				}
			}
		}
	}

	@Override
	public void receiveTrackerList(FileInfo file, TrackerList trackerList) {
		// Nếu không nhận được trackerList thì thôi, yêu cầu tải file đến
		// thằng nào?????
		// Cái này thông báo cho người dùng rằng không có thằng nào có file
		// để cho nó khỏi chờ đời
		if (trackerList == null || trackerList.getSize() == 0)
			return;
		fileSharingManager.excute(trackerList, file);
	}

	public void request(FileInfo fileInfo) {
		fileToServerManager.request(fileInfo);
	}

	@Override
	public void requestFile(FileInfo f) {
		this.request(f);
	}

	public void run() {
		fileListManager.start();
		notyServerThread.start();
		sendFileThread.start();
	}

	@Override
	public void updateNotyListFile(ListFilesInfo arg) {
		if (ListFilesInfo.isNull(arg))
			return;
		System.out.println(arg);
	}

	@Override
	public void loading() {
		views.forEach(e->{
			e.displayWaiting();
		});
	}

	@Override
	public void finishMove() {
		views.forEach(e->{
			e.finishDisplayWaiting();
		});
	}

}
