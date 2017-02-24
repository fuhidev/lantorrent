package transferfile.client.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import transferfile.client.config.ConfigApp;
import transferfile.lib.model.ListFilesInfo;

public class IOListFilesSharing {

	public static ListFilesInfo read() {
		try {
			File f = new File(ConfigApp.PATH_FILES_SHARING);
			if (!f.exists())
				f.mkdir();
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(f));
			ListFilesInfo result = (ListFilesInfo) inputStream.readObject();
			inputStream.close();
			return result;
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void write(ListFilesInfo f) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(ConfigApp.PATH_FILES_SHARING);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(f);
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
