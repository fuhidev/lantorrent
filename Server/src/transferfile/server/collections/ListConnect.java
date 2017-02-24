package transferfile.server.collections;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ListConnect {

	private HashMap<String, Socket[]> hmDanhSach = new HashMap<>();
	public static final int SO_LUONG_SOCKET = 3;
	public static final int DANH_SACH_TEP = 0;
	public static final int NHAN_YEU_CAU_TAI = 1;
	public static final int THONG_BAO = 2;
	// public static final int GUI_YEU_CAU_TEP_DEN_MAY_TRAM_VA_NHAN_FILE = 3;
	
	public Socket[] getSocket(String ip){
		return hmDanhSach.get(ip);
	}
	public Socket getSocket(String ip,int index){
		return hmDanhSach.get(ip)[index];
	}
	

	public List<Socket> getNotiList() {
		List<Socket> result = new ArrayList<>();
		Collection<Socket[]> values = hmDanhSach.values();
		values.forEach(e -> {
			if (e.length == SO_LUONG_SOCKET)
				result.add(e[THONG_BAO]);
		});
		return result;
	}
	

	public void remove(String ip, Socket[] sockets) {
		hmDanhSach.remove(ip, sockets);
		System.out.println("XÃ³a " + ip + " ra khá»�i danh sÃ¡ch káº¿t ná»‘i - " + this.getClass().getName());
	}
	public void remove(Socket[] sockets) {
		String ip = sockets[0].getInetAddress().getHostAddress();
		hmDanhSach.remove(ip, sockets);
		System.out.println("XÃ³a " + ip + " ra khá»�i danh sÃ¡ch káº¿t ná»‘i - " + this.getClass().getName());
	}

	public Socket[] layMangSocket(Socket s) {
		return hmDanhSach.get(this.getHostAddress(s));
	}

	private String getHostAddress(Socket s) {
		return s.getInetAddress().getHostAddress();
	}

	public int getNumArray(Socket s) {
		Socket[] sockets = hmDanhSach.get(this.getHostAddress(s));
		if (sockets == null)
			return -1;
		int count = 0;
		for (Socket sk : sockets) {
			if (sk == null) {
				break;
			} else {
				count++;
			}
		}
		return count;
	}

	/*
	 * public int layChiSoCuaMangSocket(MayTram mayTram) { for (int i = 0; i <
	 * danhSachMayTram.size(); i++) { if
	 * (danhSachMayTram.get(i).equals(mayTram)) return i; } return -1; }
	 */

	public void addSocket(Socket s) {
		Socket[] current = hmDanhSach.get(this.getHostAddress(s));
		// Náº¿u nhÆ° cÃ³ rá»“i thÃ¬ thÃªm vÃ o tuáº§n tá»± trong máº£ng
		if (current != null) {
			// Láº¥y máº£ng ra
			// ThÃªm socket s tuáº§n tá»± vÃ o máº£ng
			for (int i = 0; i < current.length; i++) {
				if (current[i] == null) {
					current[i] = s;
					break;
				}
			}
		}
		// Náº¿u chÆ°a cÃ³ thÃ¬ táº¡o máº£ng rá»“i thÃªm vÃ o pháº§n tá»­ Ä‘áº§u tiÃªn
		else {

			Socket[] mangSocket = this.initSocket();
			mangSocket[0] = s;
			hmDanhSach.put(this.getHostAddress(s), mangSocket);
			System.out.println("Thêm " + s.getInetAddress() + " vào danh sách");
		}
	}

	/*
	 * Khá»Ÿi táº¡o má»™t máº£ng Socket thÃªm vÃ o danh sÃ¡ch
	 */
	private Socket[] initSocket() {
		Socket[] traVe = new Socket[SO_LUONG_SOCKET];
		return traVe;
	}

	public Iterator<Socket[]> iterator() {
		return hmDanhSach.values().iterator();
	}

	public ListConnect() {
	}

}
