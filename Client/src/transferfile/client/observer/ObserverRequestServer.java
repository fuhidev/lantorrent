package transferfile.client.observer;

import transferfile.lib.model.FileInfo;
import transferfile.lib.model.TrackerList;

public interface ObserverRequestServer {
	void update(FileInfo file, TrackerList trackerList);
}
