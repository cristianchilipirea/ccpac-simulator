package node;

import java.util.ArrayList;
import packet.Packet;
import main.NodeGraph;


public class NodeEpidemic extends Node{

	public NodeEpidemic(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		if(packets.isEmpty())
			return;
		for(Node node : neighborNodes) {
			for(Packet p : packets) {
				if(!node.packets.contains(p)) {
					nodeGraph.addTransfer(p, this, node);
				}
			}
		}
	}

	@Override
	public void initialize(NodeGraph nodeGraph) {

	}

	@Override
	public void removePacket(Packet packet) {
	}
}
