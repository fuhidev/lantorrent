package transferfile.server.fileList;

import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.ListFilesInfo;
import transferfile.server.io.IOListFileInfo;

public class ListFileInfoManager {
	private static ListFileInfoManager object;
	private ListFilesInfo filesInfo;

	public ListFilesInfo getFilesInfo() {
		return filesInfo;
	}

	public void add(FileInfo f) {
		filesInfo.add(f);
		ConsoleOut.out(getClass(), "Thêm phần tử vào danh sách file");
	}

	public ListFileInfoManager() {
		if (!IOListFileInfo.check()) {
			IOListFileInfo.init();
		}
		filesInfo = IOListFileInfo.read();
	}

	public static ListFileInfoManager getInstances() {
		if (object == null)
			object = new ListFileInfoManager();
		return object;
	}

}
