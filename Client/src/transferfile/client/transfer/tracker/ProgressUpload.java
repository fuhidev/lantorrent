package transferfile.client.transfer.tracker;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import transferfile.lib.model.FileInfo;

public class ProgressUpload {

	public FileInfo getFile() {
		return file;
	}

	private FileInfo file;
	private long totalUpload = 0;
	private IUploadManager finish;
	private PropertyChangeSupport support;
	public static final String PROPERTY_TOTAL = "totaldownload";
	public static final String PROPERTY_ADDTOTAL = "addtotaldownload";

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void setFinish(IUploadManager finish) {
		this.finish = finish;

	}

	public ProgressUpload(FileInfo file) {
		super();
		support = new PropertyChangeSupport(this);
		this.file = file;
	}

	public void addTotalDownload(long totalUpload) {
		long tmp = this.totalUpload;
		this.totalUpload += totalUpload;
		System.out.println(String.format("%.2f", this.progress() * 100) + "%");
		support.firePropertyChange(PROPERTY_TOTAL, tmp, this.totalUpload);
		if (this.totalUpload >= this.file.getSize()) {
			finish.finish(this);
		}
	}

	public long getTotalDownload() {
		return totalUpload;
	}

	public float progress() {
		return (float) this.totalUpload / file.getSize();
	}

}
