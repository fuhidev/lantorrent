package transferfile.client.config;

public class ConfigApp {
	public static String HOST_ADRESS = "192.168.204.1";
	public static String HOST_PORT = "444";
	public static String TRACKER_PORT = "333";
	public static String PATH_FILES_SHARING = userHome() + "\\thtTorrent\\filesshare.dat";
	public static String DIRECTORY_SAVE_FILE = userHome() + "\\thtTorrent\\save\\";
	public static String DIRECTORY_TORRENT = userHome() + "\\thtTorrent\\torrent\\";
//	public static String IS_AUTO_SAVE = "true";
//	public static String AUTO_DOWNLOAD = "true";

	private static String userHome() {
		 return System.getProperty("user.home");
//		return "data";
	}

	public static class TransferConstant {
		public static String SIZE_TRANS = "1048576";
	}

}