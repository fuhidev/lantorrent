package transferfile.client.noty;

import transferfile.lib.model.ListFilesInfo;

public interface NotyListFile {
	void updateNotyListFile(ListFilesInfo arg);
	void loading();
	void finishMove();
}
