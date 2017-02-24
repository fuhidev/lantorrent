package transferfile.client.transfer.seeder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import transferfile.lib.model.FileInfo;

public class ProgressDownload implements PropertyChangeListener {

	class Seeder {
		private long download = 0;
		private PropertyChangeSupport support;

		public void setPropertyChangeSupport(PropertyChangeListener listener) {
			support.addPropertyChangeListener(listener);
		}

		public Seeder() {
			support = new PropertyChangeSupport(this);
		}

		public void addDownload(long d) {
			support.firePropertyChange("download", this.download, d);
			this.download += d;
		}

	}

	public FileInfo getFile() {
		return file;
	}

	private FileInfo file;
	private Seeder[] seeders;
	private long totalDownload = 0;
	private IDownoadFinish finish;
	private PropertyChangeSupport support;
	public static final String PROPERTY_TOTAL = "totaldownload";
	public static final String PROPERTY_ADDTOTAL = "addtotaldownload";

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void setFinish(IDownoadFinish finish) {
		this.finish = finish;

	}

	public ProgressDownload(FileInfo file, int num) {
		super();
		support = new PropertyChangeSupport(this);
		this.file = file;
		seeders = new Seeder[num];
		for (int i = 0; i < seeders.length; i++) {
			seeders[i] = new Seeder();
			seeders[i].setPropertyChangeSupport(this);
		}
	}

	public Seeder get(int i) {
		if (i < seeders.length)
			return seeders[i];
		return null;
	}

	public synchronized void addTotalDownload(long totalDownload) {
		long tmp = this.totalDownload;
		this.totalDownload += totalDownload;
		System.out.println(String.format("%.2f", this.progress() * 100) + "%");
		support.firePropertyChange(PROPERTY_TOTAL, tmp, this.totalDownload);
		if (this.totalDownload >= this.file.getSize()) {
			finish.update(this);
		}
	}

	public long getTotalDownload() {
		return totalDownload;
	}

	public float progress() {
		return (float) this.totalDownload / file.getSize();
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		addTotalDownload((long) e.getNewValue());
	}
}
