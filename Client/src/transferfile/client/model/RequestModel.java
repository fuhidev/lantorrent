package transferfile.client.model;

import java.io.Serializable;

import transferfile.lib.model.FileInfo;

public class RequestModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5835660645564275274L;
	private FileInfo fileInfo;
	private long offset;
	private long length;
	
	
	public RequestModel(FileInfo fileInfo, long offset, long length) {
		super();
		this.fileInfo = fileInfo;
		this.offset = offset;
		this.length = length;
	}
	
	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public long getOffset() {
		return offset;
	}
	public void setOffset(long Offset) {
		this.offset = Offset;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	
	@Override
	public String toString() {
		return this.fileInfo + " offset: " + this.offset + " length: "+this.length;
	}
	
	
}
