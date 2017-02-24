package transferfile.server.request;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;

public class RequestClientManager implements ClientHaveFile {
	private List<Socket> sockets;
	private FileInfo file;
	private List<Socket> result;

	public RequestClientManager(List<Socket> sockets, FileInfo file) {
		super();
		this.sockets = sockets;
		this.file = file;
		result = new ArrayList<>();
	}

	public List<Socket> request() {
		sockets.forEach(e -> {
			ConsoleOut.out(this.getClass(), "Khởi chạy tiểu trình yêu cầu tìm file đến " + e);
			RequestClient thread = new RequestClient(e, this, file);
			thread.setDaemon(true);
			thread.start();

		});
		ConsoleOut.out(this.getClass(), "Chờ trong 10s để nhận phản hồi từ Client");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ConsoleOut.out(this.getClass(),
				"Hết 10s, chốt danh sách và gửi " + result.size() + "client có file");
		return result;
	}

	public void addSocketResult(Socket s) {
		result.add(s);
		ConsoleOut.out(this.getClass(), "Thêm " + s + " vào danh sách kết quả");
	}

	@Override
	public void add(Socket s) {
		addSocketResult(s);
	}
}