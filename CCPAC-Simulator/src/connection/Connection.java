package connection;

import node.Node;

public class Connection {

	public Node nodeA;
	public Node nodeB;
	int packetsPerSecound;
	public long startTime;
	public long stopTime;

	public Connection(Node nodeA, Node nodeB, Integer packetsPerSecound, Long startTime, Long stopTime) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.packetsPerSecound = packetsPerSecound;
		this.startTime = startTime;
		this.stopTime = stopTime;
	}

	public String toString() {
		return "(" + nodeA.name + " " + nodeB.name+") " + packetsPerSecound + "<" + startTime + " - " + stopTime + ">";
	}

	@Override
	public boolean equals(Object arg0) {
		Connection o = (Connection) arg0;
		return o.nodeA.id == nodeA.id && o.nodeB.id == nodeB.id;
	}
}
