package node;

import java.util.ArrayList;
import packet.Packet;
import main.NodeGraph;

public abstract class Node {
	public String name;
	public int id;
	public boolean canSendMessages;
	static int currentId=0;
	public ArrayList<Packet> packets = new ArrayList<Packet>();

	public Node(String name, Boolean canSendMessages) {
		super();
		this.name = name;
		this.id = currentId;
		this.canSendMessages = canSendMessages;
		currentId++;
	}

	public String toString() {
		return name;
	}

	public abstract void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes);

	public abstract void initialize(NodeGraph nodeGraph);

	public void startPacket(Packet packet) {
		packets.add(packet);
	}

	public void addPacket(Packet packet) {
		packets.add(packet);
	}

	public void removePacket(Packet packet) {
		packets.remove(packet);
	}

	public Boolean acceptsPackage(Packet packet) {
		return true;
	}
}
