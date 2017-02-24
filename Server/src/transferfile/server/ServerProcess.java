package transferfile.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import transferfile.lib.ConsoleOut;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.ListFilesInfo;
import transferfile.lib.model.Tracker;
import transferfile.lib.model.TrackerList;
import transferfile.server.collections.ListConnect;
import transferfile.server.fileList.ListFileInfoManager;
import transferfile.server.io.IOListFileInfo;
import transferfile.server.request.Calculator;
import transferfile.server.request.RequestClientManager;
import transferfile.server.request.TransferFile;

public class ServerProcess implements IServer {

	private ServerSocket serverSocket;
	private ThreadClientManager threadClientManager;
	private ListFileInfoManager fileInfoManager;
	private ListConnect listConnect;

	class ThreadClientManager {
		private HashMap<String, ThreadClient> hashMap;
//		private List<ThreadClient> threadClients;

		public ThreadClientManager() {
			// threadClients = new ArrayList<>();
			hashMap = new HashMap<>();
		}

		public void add(ThreadClient client) {
			// this.threadClients.add(client);
			this.hashMap.put(client.getIp(), client);
		}

		public Iterator<ThreadClient> getAll() {
			Collection<ThreadClient> values = hashMap.values();
			return values.iterator();
		}

		public void remove(String ip) {
			hashMap.remove(ip);
		}

	}

	public ServerProcess() {
		try {
			serverSocket = new ServerSocket(444);
			listConnect = new ListConnect();
			threadClientManager = new ThreadClientManager();
			fileInfoManager = new ListFileInfoManager();
		} catch (IOException e) {
			ConsoleOut.exception(getClass(), e);
		}
	}

	private Thread t = new Thread(() -> {
		Scanner scanner = new Scanner(System.in);
		if (scanner.nextLine().equals("exit")) {
			ConsoleOut.out(getClass(), "Đóng server");
			IOListFileInfo.write(fileInfoManager.getFilesInfo());
			System.exit(1);
		}
		scanner.close();
	});

	public void run() {
		try {
			ConsoleOut.out(getClass(), "Chờ nhận kết nối");

			t.start();
			while (true) {
				Socket socket = serverSocket.accept();
				listConnect.addSocket(socket);
				int num = listConnect.getNumArray(socket);
				ConsoleOut.out(getClass(), "Nhận kết nối từ " + socket.getInetAddress());
				if (num == ListConnect.SO_LUONG_SOCKET) {
					ConsoleOut.out(this.getClass(), "Khởi chạy tiến trình");
					ThreadClient threadClient = new ThreadClient(socket.getInetAddress().getHostAddress(),
							listConnect.layMangSocket(socket));
					threadClientManager.add(threadClient);
					threadClient.setServer(this);
					threadClient.start();
					this.sendFileInfo(threadClient);
				}
			}
		} catch (IOException e) {
			ConsoleOut.out(this.getClass(), "Trùng PORT");
		} finally {
			IOListFileInfo.write(ListFileInfoManager.getInstances().getFilesInfo());
		}
	}

	@Override
	public void disconnect(String ip, Socket[] socket) {
		ConsoleOut.out(this.getClass(), "Mất kết nối với " + socket[0].getInetAddress());
		listConnect.remove(socket);
		this.threadClientManager.remove(ip);
	}

	@Override
	public void updateListFile(FileInfo file) {
		fileInfoManager.add(file);

		Iterator<ThreadClient> iterator = this.threadClientManager.getAll();
		while (iterator.hasNext()) {
			ThreadClient next = iterator.next();
			sendFileInfo(next);
			// next.sendListFiles(filesInfo);
		}
	}

	public void sendFileInfo(ThreadClient thread) {
		ListFilesInfo filesInfo = this.fileInfoManager.getFilesInfo();
		thread.sendListFiles(filesInfo);
	}

	@Override
	public TrackerList notyClient(String ip, FileInfo file) {
		Socket myClient = listConnect.getSocket(ip, ListConnect.THONG_BAO);
		ConsoleOut.out(getClass(), ip + " gửi yêu cầu nhận" + file);
		List<Socket> sockets = listConnect.getNotiList();
		sockets.remove(myClient);
		ConsoleOut.out(this.getClass(), "Gửi yêu cầu tìm file đến tất cả Client");
		RequestClientManager requestClient = new RequestClientManager(sockets, file);
		TrackerList trackerList = new TrackerList();

		List<Socket> request = requestClient.request();
		ConsoleOut.out(this.getClass(),
				"Nhận danh sách từ các Client với " + request.size() + " client có file " + file);
		// Náº¿u nhÆ° khÃ´ng cÃ³ mÃ¡y nÃ o cÃ³ file thÃ¬ khÃ´ng cáº§n pháº£i tÃ­nh toÃ n
		// lÃ m gÃ¬ cho má»‡t
		if (request.size() != 0) {
			request.forEach(e -> {
				trackerList.add(new Tracker(e.getInetAddress().getHostAddress()));
			});
			ConsoleOut.out(this.getClass(), "Phân chia dung lượng");
			TransferFile nhanFile = new TransferFile(trackerList, new Calculator(file.getSize(), trackerList));
			nhanFile.calculator();
		}
		return trackerList;
	}

}
