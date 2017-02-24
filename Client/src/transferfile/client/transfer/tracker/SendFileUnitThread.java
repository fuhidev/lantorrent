package transferfile.client.transfer.tracker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import transferfile.client.config.ConfigApp;
import transferfile.client.model.RequestModel;
import transferfile.lib.ConsoleOut;

public class SendFileUnitThread extends Thread {
	private Socket socket;

	private IUploadManager finish;

	private ProgressUpload progressUpload;
	
	public void setFinish(IUploadManager finish) {
		this.finish = finish;
	}




	public SendFileUnitThread(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			ConsoleOut.out(this.getClass(), "Chờ yêu cầu");
			RequestModel obj = (RequestModel) inputStream.readObject();
			ConsoleOut.out(this.getClass(), "Nhận dược yêu cầu " + obj);
			
			progressUpload = new ProgressUpload(obj.getFileInfo());
			finish.add(progressUpload);
			

			File file = new File(ConfigApp.DIRECTORY_SAVE_FILE + obj.getFileInfo().getName());
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
			int read = 0;
			long remaining = obj.getLength();
			int lengthBuff = Integer.parseInt(ConfigApp.TransferConstant.SIZE_TRANS);
			byte[] buff = new byte[lengthBuff];
			bufferedInputStream.mark((int) obj.getOffset());
			ConsoleOut.out(this.getClass(), "Đang gửi file " + obj.getFileInfo().getName());
			while (remaining > 0 && ((read = bufferedInputStream.read(buff))) != -1) {
				outputStream.write(buff, 0, read);
				outputStream.flush();
				progressUpload.addTotalDownload(read);
			}

			ConsoleOut.out(this.getClass(), "Hoàn thành việc gửi " + obj.getFileInfo() + " đến " + socket);
			bufferedInputStream.close();
			finish.finish(progressUpload);
		} catch (IOException | ClassNotFoundException/* | InterruptedException */ e) {
			ConsoleOut.exception(getClass(), e);
			return;
		} finally {
			try {
				finish.finish(progressUpload);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
