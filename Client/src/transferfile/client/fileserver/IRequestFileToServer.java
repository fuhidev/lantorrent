package transferfile.client.fileserver;

import transferfile.lib.model.FileInfo;
import transferfile.lib.model.TrackerList;

public interface IRequestFileToServer {
	void receiveTrackerList(FileInfo file, TrackerList trackerList);
}
