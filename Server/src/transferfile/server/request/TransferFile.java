package transferfile.server.request;

import transferfile.lib.model.Tracker;
import transferfile.lib.model.TrackerList;

public class TransferFile {
		private TrackerList list;
		private CalculatorCapacity cal;

		
		
		public TransferFile(TrackerList list, CalculatorCapacity cal) {
			super();
			this.list = list;
			this.cal = cal;
		}

		public void calculator() {
			cal.caculator();
		}

		public TrackerList getTrackers() {
			return list;
		}

		public void add(Tracker t) {
			list.add(t);
		}

		public void remove(Tracker t) {
			list.remove(t);
		}

	}