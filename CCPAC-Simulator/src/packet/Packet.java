package packet;

import message.Message;

public class Packet {
	public long startTime;
	public long stopTime;
	public Message message;
	public int totalHops = 0;
	public int totalHopsAtArrival = 0;

	public Packet(Message message) {
		this.message = message;
	}

	public void setStopTime(long stopTime) {
		if(this.stopTime == 0)
			this.stopTime = stopTime;
	}

	public void setTotalHopsAtArrival(int totalHopsAtArrival) {
		if(this.totalHopsAtArrival == 0)
			this.totalHopsAtArrival = totalHopsAtArrival;
	}

	public void increaseTotalHops() {
		totalHops++;
	}

	public int getTotalHops() {
		return totalHops;
	}
}
