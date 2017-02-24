package transferfile.client.collections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import transferfile.client.transfer.seeder.IDownoadFinish;
import transferfile.client.transfer.seeder.ProgressDownload;

public class DownloadCollection implements IDownoadFinish, PropertyChangeListener {
	private List<ProgressDownload> list;
	private PropertyChangeSupport support;
	public static final String PROPERTY = "property";
	public static final String PROPERTY_PROGRESS = "propertyprogress";

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public DownloadCollection() {
		list = new ArrayList<>();
		support = new PropertyChangeSupport(this);
	}

	public void add(ProgressDownload d) {
		this.list.add(d);
		d.addPropertyChangeListener(this);
		d.setFinish(this);
		this.support.firePropertyChange(PROPERTY, null, d);
	}

	public Iterator<ProgressDownload> iterator() {
		return list.iterator();
	}

	public void remove(ProgressDownload d) {
		this.list.remove(d);
		this.support.firePropertyChange(PROPERTY, d, null);
	}

	public ProgressDownload get(int i) {
		return this.list.get(i);
	}

	@Override
	public void update(ProgressDownload download) {
		this.remove(download);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof ProgressDownload) {
			if (evt.getOldValue() != null && evt.getNewValue() != null) {
				support.firePropertyChange(PROPERTY_PROGRESS, (ProgressDownload) evt.getSource(), evt.getNewValue());
			}
		}
	}

}
