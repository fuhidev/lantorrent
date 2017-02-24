package transferfile.gui.controller;

import transferfile.lib.model.FileInfo;

public interface IController {
	void requestFile(FileInfo f);
	void addFile(FileInfo f);
}
