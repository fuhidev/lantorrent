package transferfile.client.collections;

import java.util.HashMap;

import transferfile.lib.model.FileInfo;
import transferfile.lib.model.TrackerList;

public class TrackerListManager {
//	private static TrackerListManager manager;
//	public static TrackerListManager getInstances(){
//		if(manager == null)
//			manager = new TrackerListManager();
//		return manager;
//	}
	private HashMap<FileInfo, TrackerList> hashMap;

	private TrackerListManager() {
		hashMap = new HashMap<>();
	}
	
	public void add(FileInfo key, TrackerList value){
		hashMap.put(key, value);
	}
	public void remove(FileInfo key){
		hashMap.remove(key);
	}
	public TrackerList getTrackerList(FileInfo key){
		return this.hashMap.get(key);
	}
	
}
