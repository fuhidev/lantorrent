package transferfile.server;

import java.net.Socket;

import transferfile.lib.model.FileInfo;
import transferfile.lib.model.TrackerList;

public interface IServer {
//	void disconnect(Socket socket);

	void disconnect(String ip, Socket[] socket);
	void updateListFile(FileInfo file);
	TrackerList notyClient(String ip,FileInfo file);
}
