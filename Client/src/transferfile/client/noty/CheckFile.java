package transferfile.client.noty;

import java.io.File;
import java.io.FilenameFilter;

import transferfile.client.config.ConfigApp;
import transferfile.client.io.IOTorrent;
import transferfile.lib.model.FileInfo;

public class CheckFile implements ExistFile {
	@Override
	public boolean check(FileInfo fileInfo) {
		String directory = ConfigApp.DIRECTORY_TORRENT;
		// try(Stream<Path> paths = Files.walk(Paths.get(directory))) {
		// paths.forEach(filePath -> {
		// if (Files.isRegularFile(filePath)) {
		// if (this.isTorrent(filePath)) {
		// System.out.println(filePath);
		// }
		// System.out.println(filePath);
		// }
		// });
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		File dir = new File(directory);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".torrent");
			}
		});
		if(files == null)
			return false;
		for (File f : files) {
			if (f.getName().equals(IOTorrent.getName(fileInfo))) {
				return true;
			}
			// System.out.println(f.getName());
		}
		return false;
	}
	public static void main(String[] args) {
		System.out.println(new CheckFile().check(new FileInfo("3.png", 47882)));
	}

}
