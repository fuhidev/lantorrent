package transferfile.client.transfer.seeder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import transferfile.client.config.ConfigApp;
import transferfile.client.io.IOFile;
import transferfile.client.io.IOTorrent;
import transferfile.client.model.RequestModel;
import transferfile.client.observer.ObserverRequestFileSharing;
import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.Tracker;
import transferfile.lib.model.TrackerList;

public class TransferFileSharingThread extends Thread implements ITransferThread {

	// private ByteBuffer byteBuffer;
	private ProgressDownload download;
	private FileInfo file;
	private TrackerList trackerList;
	private ObserverRequestFileSharing observer;

	public void setObserver(ObserverRequestFileSharing observer) {
		this.observer = observer;
	}

	public TransferFileSharingThread(TrackerList trackerList, FileInfo file, ProgressDownload download) {
		super();
		this.trackerList = trackerList;
		this.file = file;
		this.download = download;
		setName(file.getName());
	}

	public void receive() {
		try {
			Iterator<Tracker> iterator = trackerList.iterator();
			ConsoleOut.out(getClass(), "" + trackerList.getSize());
			int count = 0;
			String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
			File folder = new File(ConfigApp.DIRECTORY_SAVE_FILE + fileName);
			folder.mkdir();
			List<File> files = new ArrayList<>();
			while (iterator.hasNext()) {

				Tracker i = iterator.next();
				ConsoleOut.out(this.getClass(), "Khởi tạo tiến tình truy vấn file đến" + i.getAddress());
				int trackerPort = Integer.parseInt(ConfigApp.TRACKER_PORT);
				Socket socket = new Socket(i.getAddress(), trackerPort);

				RequestModel requestModel = new RequestModel(file, i.getOffset(), i.getLength());
				File file = new File(folder.getAbsolutePath() + "/" + count + ".temp");
				System.out.println(file.getAbsolutePath());
				files.add(file);
				TransferFileUnitThread thread = new TransferFileUnitThread(socket, requestModel);
				thread.setParent(this);
				thread.setFile(file);
				thread.setName(getName() + count);
				thread.setDownload(download.get(count));
				ConsoleOut.out(this.getClass(), "Khởi chạy tiến trình " + thread.getName());
				thread.start();
				count++;
			}

			while (this.count < files.size()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			ConsoleOut.out(getClass(), "Đang ghi file");

			File fileOut = new File(ConfigApp.DIRECTORY_SAVE_FILE + file.getName());
			fileOut.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(fileOut);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

			byte[] buff = new byte[1024 * 1024];
			long totalRead = 0;
			for (File file : files) {
				ConsoleOut.out(getClass(), "Đang đọc dữ liệu từ " + file.getAbsolutePath());
				BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
				int read = 0;
				while ((read = bufferedInputStream.read(buff)) != -1) {
					bufferedOutputStream.write(buff);
					totalRead += read;
					System.out.println(totalRead + "/" + this.file.getSize());
				}
				bufferedInputStream.close();
			}

			bufferedOutputStream.close();
			ConsoleOut.out(getClass(), "Ghi file thành công vào " + fileOut.getAbsolutePath());
			// ConsoleOut.out(this.getClass(), "�?ang dữ liệu file " +
			// file.getName());
			IOFile.deleteDirectory(folder);
			IOTorrent.write(trackerList, file);
			this.observer.update(file);

		} catch (IOException e1) {
			ConsoleOut.exception(getClass(), e1);
			observer.error();
		}
	}

	@Override
	public void run() {
		receive();
	}

	private int count = 0;

	@Override
	public void update() {
		count++;
	}

	public static void main(String[] args) {

		File file = new File("abc.dat");
		try {
			file.createNewFile();
			FileChannel channel = FileChannel.open(Paths.get(file.getAbsolutePath()));
			channel.position(500);
			channel.write(ByteBuffer.wrap("hsadkfhakdjfhaksdf".getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
