package transferfile.lib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrackerList implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = -8415062617872880683L;
		private List<Tracker> list;
		
		public TrackerList() {
			super();
			list = new ArrayList<>();
		}
		public void add(Tracker t){
			list.add(t);
		}
		public void remove(Tracker t){
			list.remove(t);
		}
		public int getSize(){
			return list.size();
		}
		public Iterator<Tracker> iterator(){
			return list.iterator();
		}
		@Override
		public String toString() {
			String s ="";
			for (Tracker a : list) {
				s+=a.toString();
			}
		return s;
		}
		
	}