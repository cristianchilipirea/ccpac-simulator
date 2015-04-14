package message;

import node.Node;

public class MessageMAPPriority extends Message implements Comparable<MessageMAPPriority> {
	int priority = 0;
	
	public MessageMAPPriority(Node startNode, Node destinationNode, int size, int priority) {
		super(startNode, destinationNode, size);
		this.priority = priority;
	}

	@Override
	public int compareTo(MessageMAPPriority o) {
		return o.priority-priority;
	}
}
