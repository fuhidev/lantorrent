package transferfile.client.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import transferfile.client.config.ConfigApp;

public class IOFile {
	public static void move(File f) throws IOException {
		File f1 = new File(ConfigApp.DIRECTORY_SAVE_FILE + f.getName());
		if (f1.exists())
			return;
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(f));

		FileOutputStream fileOutputStream = new FileOutputStream(f1);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		int read = 0;
		while ((read = bufferedInputStream.read()) != -1) {
			bufferedOutputStream.write(read);
			// download.addTotalDownload(read);
			// buffer.put(buff);
			// System.out.println("read " + totalRead + " bytes.");
			// fos.write(buffer, 0, read);
		}
		bufferedOutputStream.close();
		bufferedInputStream.close();
	}

	public static File[] f(String direc, String fileName) {
		File dir = new File(direc);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().startsWith(fileName);
			}
		});
		return files;
	}

	public static void main(String[] args) {
		// System.out.println(IOFile.f(ConfigConstApp.DIRECTORY_SAVE_FILE,
		// "Net.rar").length);
		File file = new File(
				"C:\\Users\\hph\\thtTorrent\\save\\[LinksVIP.Net] SinhvienIT.Net-Microsoft.Office.Pro.2010");
		deleteDirectory(file);
	}

	public static boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}

	public static void joinFiles(File destination, File[] sources) throws IOException {
		OutputStream output = null;
		try {
			output = createAppendableStream(destination);
			for (File source : sources) {
				appendFile(output, source);
			}
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	private static BufferedOutputStream createAppendableStream(File destination) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(destination, true));
	}

	private static void appendFile(OutputStream output, File source) throws IOException {
		InputStream input = null;
		try {
			input = new BufferedInputStream(new FileInputStream(source));
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
}
