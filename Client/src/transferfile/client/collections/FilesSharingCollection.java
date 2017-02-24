package transferfile.client.collections;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import transferfile.client.config.ConfigApp;
import transferfile.client.io.IOListFilesSharing;
import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.ListFilesInfo;

public class FilesSharingCollection {

	private ListFilesInfo list;
	private PropertyChangeSupport support;
	public static final String PROPERTY_LIST = "item";
	public static final String PROPERTY_FILE = "file";

	public FilesSharingCollection() {
		support = new PropertyChangeSupport(this);
		File file = new File(ConfigApp.PATH_FILES_SHARING);
		if (!file.exists()) {
			IOListFilesSharing.write(new ListFilesInfo());
		}
		list = IOListFilesSharing.read();
	}

	public void addPropertyChange(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void setList(ListFilesInfo list) {
		this.list = list;
		support.firePropertyChange(PROPERTY_LIST, null, list);
	}

	public void add(FileInfo f) {
		ConsoleOut.out(this.getClass(), "Thêm + " + f);
		list.add(f);
		support.firePropertyChange(PROPERTY_LIST, null, list);
		support.firePropertyChange(PROPERTY_FILE, null, f);
	}

	public void add(File f) {
		this.add(new FileInfo(f.getName(), f.length()));
	}

}
