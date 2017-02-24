package transferfile.lib.model;

import java.io.Serializable;

public class Tracker implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7792212344567463540L;
	private String address;
	private long offset;
	private long length;

	public Tracker(String address) {
		super();
		this.address = address;
	}

	public Tracker(String address, long offset, long length) {
		super();
		this.address = address;
		this.offset = offset;
		this.length = length;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}
	
	@Override
	public String toString() {
		return "(Tracker) Địa chỉ: "+this.address +" offset: "+ this.offset+" length: "+this.length;
	}

	


}
