package transferfile.client.observer;

import transferfile.lib.model.FileInfo;

public interface ObserverRequestFileSharing {
	void update(FileInfo f);
	void error();
}
