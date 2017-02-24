package transferfile.client.collections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import transferfile.client.transfer.tracker.IUploadManager;
import transferfile.client.transfer.tracker.ProgressUpload;

public class UpLoadCollection implements IUploadManager, PropertyChangeListener {
	private List<ProgressUpload> list;
	private PropertyChangeSupport support;
	public static final String PROPERTY = "property";
	public static final String PROPERTY_PROGRESS = "propertyprogress";

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public UpLoadCollection() {
		list = new ArrayList<>();
		support = new PropertyChangeSupport(this);
	}

	public void add(ProgressUpload d) {
		this.list.add(d);
		d.addPropertyChangeListener(this);
		d.setFinish(this);
		this.support.firePropertyChange(PROPERTY, null, d);
	}

	public Iterator<ProgressUpload> iterator() {
		return list.iterator();
	}

	public void remove(ProgressUpload d) {
		this.list.remove(d);
		this.support.firePropertyChange(PROPERTY, d, null);
	}

	public ProgressUpload get(int i) {
		return this.list.get(i);
	}

	@Override
	public void finish(ProgressUpload upload) {
		this.remove(upload);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof ProgressUpload) {
			if (evt.getOldValue() != null && evt.getNewValue() != null) {
				support.firePropertyChange(PROPERTY_PROGRESS, (ProgressUpload) evt.getSource(), evt.getNewValue());
			}
		}
	}


}
