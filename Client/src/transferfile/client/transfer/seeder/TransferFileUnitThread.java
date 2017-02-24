package transferfile.client.transfer.seeder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import transferfile.client.config.ConfigApp;
import transferfile.client.model.RequestModel;
import transferfile.client.transfer.seeder.ProgressDownload.Seeder;
import transferfile.lib.ConsoleOut;

public class TransferFileUnitThread extends Thread {
	private Socket socket;
	private RequestModel requestModel;
	private Seeder download;
	private File file;
	private ITransferThread parent;

	public void setParent(ITransferThread parent) {
		this.parent = parent;
	}

	public void setFile(File file) {
		this.file = file;
		try {
			file.createNewFile();
		} catch (IOException e) {
			ConsoleOut.exception(getClass(), e);
		}
	}

	public void setDownload(Seeder download) {
		this.download = download;
	}

	public TransferFileUnitThread(Socket socket, RequestModel requestModel) {
		super();
		this.socket = socket;
		this.requestModel = requestModel;
	}

	@Override
	public void run() {
		try {
			ConsoleOut.out(this.getClass(), "Gửi " + requestModel);
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(this.requestModel);
			outputStream.flush();

			if (file == null)
				return;
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));

			BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
			int read = 0;
			int lengthBuff = Integer.parseInt(ConfigApp.TransferConstant.SIZE_TRANS);
			byte[] buff = new byte[lengthBuff];
			while ((read = inputStream.read(buff)) != -1) {
				download.addDownload(read);
				bufferedOutputStream.write(buff, 0, read);
			}
			bufferedOutputStream.close();

			this.parent.update();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
