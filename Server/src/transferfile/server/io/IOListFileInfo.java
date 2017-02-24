package transferfile.server.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import transferfile.lib.model.ListFilesInfo;

public class IOListFileInfo {
	private static final String FILE_NAME = "listFileInfo.dat";

	public static void init() {
		ListFilesInfo filesInfo = new ListFilesInfo();
		write(filesInfo);
	}

	public static boolean check() {
		File file = new File(FILE_NAME);
		return file.exists();
	}

	public static void write(ListFilesInfo filesInfo) {
		try {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
			stream.writeObject(filesInfo);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static ListFilesInfo read() {
		try {
			File file = new File(FILE_NAME);
			if (!file.exists()) {
				file.createNewFile();
				write(new ListFilesInfo());
			}
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
			try {
				return (ListFilesInfo) stream.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// return null;
			}
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}
		return null;
	}

	public static void main(String[] args) {
		// ListFilesInfo filesInfo = new ListFilesInfo();
		// IOListFileInfo.write(filesInfo);
	}
}
