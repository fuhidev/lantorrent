package transferfile.client.transfer.seeder;

import java.util.Observable;

import transferfile.client.collections.DownloadCollection;
import transferfile.client.observer.IAddActiveFile;
import transferfile.client.observer.ICloseProcess;
import transferfile.client.observer.ObserverRequestFileSharing;
import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.TrackerList;

public class TransferFileSharingManager extends Observable implements ObserverRequestFileSharing,IDownoadFinish {

	private ICloseProcess parent;
	private DownloadCollection collection;
	private IAddActiveFile activeFile;
	
	public void setActiveFile(IAddActiveFile activeFile) {
		this.activeFile = activeFile;
	}

	public TransferFileSharingManager() {
		this.collection = new DownloadCollection();
	}

	public DownloadCollection getCollection() {
		return collection;
	}

	public void setParent(ICloseProcess parent) {
		this.parent = parent;
	}

	public void excute(TrackerList trackerList, FileInfo file) {
		ConsoleOut.out(getClass(), "Khởi tạo tiến trình nhận file từ Tracker");
		ProgressDownload seeder = new ProgressDownload(file, trackerList.getSize());
		seeder.setFinish(this);
		this.collection.add(seeder);
		TransferFileSharingThread thread = new TransferFileSharingThread(trackerList, file, seeder);
		thread.setObserver(this);
		thread.start();
	}

	@Override
	public void update(FileInfo data) {
		activeFile.addActiveFile(data);
	}

	@Override
	public void error() {
		parent.close();
	}

	@Override
	public void update(ProgressDownload download) {
		
	}
}
