package message;

import java.util.ArrayList;
import packet.Packet;
import node.Node;


public class Message {
	public Node startNode;
	public Node destinationNode;
	public int size;
	public boolean scheduled = false;

	ArrayList<Packet> packets = new ArrayList<Packet>();

	public Message(Node startNode, Node destinationNode, Integer size) {
		super();
		this.startNode = startNode;
		this.destinationNode = destinationNode;
		this.size = size;
	}

	public void addPacket(Packet packet) {
		packets.add(packet);
	}

	public String toString() {
		return "("+startNode.name+" "+destinationNode.name+")";
	}

	public long getStopTime() {
		long max=0;
		for (Packet p : packets)
			if(max < p.stopTime)
				max = p.stopTime;
		return max;
	}

	public long getStartTime() {
		long min = Long.MAX_VALUE;
		for (Packet p : packets)
			if (min > p.startTime)
				min = p.startTime;
		return min;
	}

	public int getHopsAtArrival() {
		int sum = 0;
		for (Packet p : packets)
			sum +=p.totalHopsAtArrival;
		return sum;
	}
}
