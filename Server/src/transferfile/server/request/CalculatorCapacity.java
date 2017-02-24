package transferfile.server.request;

import transferfile.lib.model.TrackerList;

public abstract class CalculatorCapacity {
	
	protected TrackerList list;
	
	
	

	public void setList(TrackerList list) {
		this.list = list;
	}






	public CalculatorCapacity(TrackerList list) {
		super();
		this.list = list;
	}






	public abstract void caculator();
}
