package connection;

import node.Node;

public class SocialConnection {
	Node nodeA;
	Node nodeB;

	public SocialConnection(Node nodeA, Node nodeB) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
	}

	public String toString() {
		return super.toString() + " social";
	}

	public boolean equals(Object arg0) {
		SocialConnection o = (SocialConnection) arg0;
		return o.nodeA.id == nodeA.id && o.nodeB.id == nodeB.id;
	}
	
	
}
