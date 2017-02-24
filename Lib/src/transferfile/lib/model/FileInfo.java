package transferfile.lib.model;

import java.io.Serializable;

public class FileInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4898822953749326663L;
	private String name;
	private long size;
	public FileInfo(String name, long size) {
		super();
		this.name = name;
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "(FileInfo) "+ " Name: "+this.name + " Size: "+this.size;
	}
	@Override
	public boolean equals(Object obj) {
		FileInfo f = (FileInfo) obj;
		return  this.name.equals(f.name) && this.size==f.size;
	}
	
	

}
