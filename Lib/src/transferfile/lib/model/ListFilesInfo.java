package transferfile.lib.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import transferfile.lib.ConsoleOut;

public class ListFilesInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5644876093154653533L;
	private List<FileInfo> list;

	public ListFilesInfo() {
		list = new ArrayList<>();
	}

	public void add(FileInfo f) {
		list.add(f);
		ConsoleOut.out(getClass(), "Thêm " + f + " vào danh sách");
	}

	public void remove(FileInfo f) {
		list.remove(f);
	}

	public Iterator<FileInfo> iterator() {
		return this.list.iterator();
	}

	public FileInfo getFileInfo(int num) {
		return this.list.get(num);
	}

	public static boolean isNull(ListFilesInfo l) {
		if (l == null || l.size() == 0) {
			return true;
		}
		return false;
	}
	
	public int size() {
		return this.list.size();
	}

	@Override
	public String toString() {
		String s = "";
		for (FileInfo f : list) {
			s += f.toString() + "\n";
		}
		return s;
	}

	public void add(File file) {
		this.add(new FileInfo(file.getName(), file.length()));
	}
}
