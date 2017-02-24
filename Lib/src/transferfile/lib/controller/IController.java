package transferfile.lib.controller;

import java.io.File;

import transferfile.lib.model.FileInfo;

public interface IController {
	void requestFile(FileInfo f);
	void addFile(File f);
}
