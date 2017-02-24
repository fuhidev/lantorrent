package transferfile.client.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import transferfile.client.config.ConfigApp;
import transferfile.client.model.TorrentInfo;
import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.Tracker;
import transferfile.lib.model.TrackerList;

public class IOTorrent {
	public static String getName(FileInfo fileInfo) {
		return fileInfo.getName() + "_" + fileInfo.getSize() + ".torrent";
	}

	public static void write(TrackerList list, FileInfo file) {
		ConsoleOut.out(IOTorrent.class.getClass(), "Đang ghi torrent " + file);
		try {
			File file2 = new File(ConfigApp.DIRECTORY_TORRENT + getName(file));
			file2.createNewFile();
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file2));
			// outputStream.writeObject(list);
			// outputStream.writeObject(file);
			TorrentInfo torrentInfo = new TorrentInfo(list, file);
			outputStream.writeObject(torrentInfo);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static TorrentInfo read(FileInfo file) {
		ConsoleOut.out(IOTorrent.class.getClass(), "Đang đọc torrent " + file);
		try {
			ObjectInputStream stream = new ObjectInputStream(
					new FileInputStream(ConfigApp.DIRECTORY_TORRENT + getName(file)));
			try {
				return (TorrentInfo) stream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		TrackerList list = new TrackerList();
		try {
			list.add(new Tracker(InetAddress.getLocalHost().getHostAddress()));

		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		IOTorrent.write(list, new FileInfo("[LinksVIP.Net] SinhvienIT.Net-Microsoft.Office.Pro.2010.exe", 613999272));
		// TorrentInfo read = IOTorrent.read(new FileInfo("A", 3213));
		// System.out.println(read.getTrackerList());
	}

	public static List<File> getAll() {
		File folder = new File(ConfigApp.DIRECTORY_TORRENT);
		List<File> result = new ArrayList<>();
		for (final File fileEntry : folder.listFiles()) {
			result.add(fileEntry);
		}
		return result;
	}
}
