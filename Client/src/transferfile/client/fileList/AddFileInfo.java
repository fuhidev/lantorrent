package transferfile.client.fileList;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import transferfile.client.io.IOFile;
import transferfile.client.io.IOTorrent;
import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.Tracker;
import transferfile.lib.model.TrackerList;

public class AddFileInfo {
	private OutputStream outputStream;
	private IFileListManager observer;
	
	public void setObserver(IFileListManager observer) {
		this.observer = observer;
	}

	public AddFileInfo(OutputStream outputStream) {
		super();
		this.outputStream = outputStream;
	}

	public void add(File f) throws IOException {
		ConsoleOut.out(getClass(), "Đang thêm file....");
		TrackerList list = new TrackerList();
		try {
			list.add(new Tracker(InetAddress.getLocalHost().getHostAddress(),0,f.length()));
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		observer.loading();
		IOFile.move(f);
		FileInfo info = new FileInfo(f.getName(), f.length());
		IOTorrent.write(list, info);
		add(info);
		observer.finishMove();
	}


	private void add(FileInfo f) {
		ObjectOutputStream stream;
		try {
			stream = new ObjectOutputStream(outputStream);
			try {
				stream.writeObject(f);
				stream.flush();
				ConsoleOut.out(getClass(), "Gửi yêu cầu thêm " + f + " lên server thành công");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	public static void main(String[] args) {
		
		try {
			new AddFileInfo(null).add(new File("D:\\Software\\System\\Windows Server 2008.iso"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
