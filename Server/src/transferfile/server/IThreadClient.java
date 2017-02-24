package transferfile.server;

import transferfile.lib.model.FileInfo;

public interface IThreadClient {
	void close();
	void addFileInfo(FileInfo fileInfo);
}
