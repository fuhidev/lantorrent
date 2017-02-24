package transferfile.client.collections;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;

import transferfile.client.io.IOData;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.ListFilesInfo;

public class ActiveCollection {
	public static final String PROPERTY_LIST = "list";
	public static final String PROPERTY_FILE = "file";
	private ListFilesInfo filesInfo;
	private PropertyChangeSupport support;

	public void addPropertyChangeSupport(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public ActiveCollection() {
		support = new PropertyChangeSupport(this);
	}

	public void loadData() {
		filesInfo = new ListFilesInfo();

		List<File> list = IOData.getAllFile();
		list.forEach(e -> {
			filesInfo.add(e);
		});

		support.firePropertyChange(PROPERTY_LIST, null, filesInfo);
	}

	public void add(FileInfo f) {
		filesInfo.add(f);
		support.firePropertyChange(PROPERTY_LIST, null, filesInfo);
		support.firePropertyChange(PROPERTY_FILE, null, f);
	}

}
