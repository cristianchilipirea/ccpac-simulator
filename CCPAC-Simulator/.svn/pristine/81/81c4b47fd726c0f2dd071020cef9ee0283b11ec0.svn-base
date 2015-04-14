package node;

import java.util.ArrayList;
import packet.Packet;
import main.NodeGraph;

public class NodeWait extends Node {

	public NodeWait(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		if(packets.isEmpty())
			return;
		for(Node node : neighborNodes) {
			for(Packet p : packets) {
				if(p.message.destinationNode.equals(node)) {
					nodeGraph.addTransfer(p, this, node);
				}
			}
		}
	}

	@Override
	public void initialize(NodeGraph nodeGraph) {
		
	}
	
	public void addPacket(Packet packet) {
		
	}
}
