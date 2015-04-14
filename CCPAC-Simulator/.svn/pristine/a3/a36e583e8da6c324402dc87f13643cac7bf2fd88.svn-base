package node;

import java.util.ArrayList;
import packet.Packet;
import main.NodeGraph;
import message.Message;

public class NodeStats extends Node {
	final int SIZE = 20;
	int count = 0;
	int[] histogram = new int[SIZE];
	Packet packet = new Packet(new Message(this, this, 0));

	public NodeStats(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		count += neighborNodes.size();
		for(Node nextNode : neighborNodes)
			nodeGraph.addTransfer(packet, this, nextNode);
		int increment = (int) (nodeGraph.simTime.stopTime - nodeGraph.simTime.startTime) / SIZE;
		for(int i=0; i<SIZE;i++)
			if(nodeGraph.simTime.startTime+increment*i > nodeGraph.simTime.currentTime) {
				histogram[i-1] += neighborNodes.size();
				break;
			}
	}

	@Override
	public void initialize(NodeGraph nodeGraph) {
	}

	@Override
	public void addPacket(Packet packet) {
	}

	@Override
	public void removePacket(Packet packet) {
	}
	
	@Override
	public String toString() {
		String s="";
		for(int i=0;i<SIZE-1;i++)
			s += histogram[i]+"\t";
		return name + "	" + count +"\t[ " +s+" ]";
	}

}
