package transferfile.server.request;

import java.util.Iterator;

import transferfile.lib.ConsoleOut;
import transferfile.lib.model.Tracker;
import transferfile.lib.model.TrackerList;

public class Calculator extends CalculatorCapacity {

	private long total = 0;
	private long offset = 0;
	private long rest = 0;
	private int num = 0;
	private long length = 0;

	public Calculator(long total, TrackerList list) {
		super(list);
		this.total = total;
	}

	@Override
	public void caculator() {
		ConsoleOut.out(this.getClass(), "Đang tính dung lượng");
		offset = 0;
		rest = total;
		num = list.getSize();
		length = 0;
		Iterator<Tracker> iterator = list.iterator();
		// int tt=0;
		while (iterator.hasNext()) {
			Tracker e = iterator.next();
			offset = length;
			// dungLuongKetThuc += rest / soLuong;
			length = rest / num;
			rest -= length;
			num--;
			// tt+=length;
			e.setOffset(offset);
			e.setLength(length);
			ConsoleOut.out(this.getClass(), "Set dung lượng cho " + e);
			// e.setCapacity(dungLuongBatDau, dungLuongKetThuc);
		}
		// System.out.println(tt);

	}

	public static void main(String[] args) {
		// TrackerList trackerList = new TrackerList();
		// trackerList.add(new Tracker("1"));
		// trackerList.add(new Tracker("2"));
		// trackerList.add(new Tracker("3"));
		//// trackerList.add(new Tracker("1"));
		// new Calculator(2321, trackerList).caculator();

	}

}
