package transferfile.client.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import transferfile.client.config.ConfigApp;
import transferfile.lib.model.FileInfo;

public class IOData {
	public static String getName(FileInfo fileInfo) {
		return fileInfo.getName() + "_" + fileInfo.getSize();
	}

	
	public static List<File> getAllFile() {
		File dir = new File(ConfigApp.DIRECTORY_SAVE_FILE);
		List<File> result = new ArrayList<>();
	    for (final File fileEntry : dir.listFiles()) {
	        if (fileEntry.isFile()) {
	        	result.add(fileEntry);
	        }
	    }
	    return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getAllFile());
	}
	
}
