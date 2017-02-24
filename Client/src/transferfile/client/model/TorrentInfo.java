package transferfile.client.model;

import java.io.Serializable;

import transferfile.lib.model.FileInfo;
import transferfile.lib.model.TrackerList;

public class TorrentInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7667038820549270824L;
	private TrackerList trackerList;
	private FileInfo fileInfo;
	public TorrentInfo(TrackerList trackerList, FileInfo fileInfo) {
		super();
		this.trackerList = trackerList;
		this.fileInfo = fileInfo;
	}
	public TrackerList getTrackerList() {
		return trackerList;
	}
	public void setTrackerList(TrackerList trackerList) {
		this.trackerList = trackerList;
	}
	public FileInfo getFileInfo() {
		return fileInfo;
	}
	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}
	
}
