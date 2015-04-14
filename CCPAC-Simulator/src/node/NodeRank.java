package node;

import java.util.ArrayList;

import main.NodeGraph;
import packet.Packet;

public class NodeRank extends Node {
	long globalRank = 0;

	public NodeRank(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	public void initialize(NodeGraph nodeGraph) {
	}

	private void computeRank(ArrayList<Node> neighborNodes) {
		globalRank+=neighborNodes.size();
	}

	public static long GlobalRankOf(NodeRank node) {
		return node.globalRank;
	}

	public static void addTransfer(NodeGraph nodeGraph, Packet packet, Node currentNode, Node encounteredNode) {
		if(encounteredNode.packets.contains(packet))
			return;
		nodeGraph.addTransfer(packet, currentNode, encounteredNode);
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		NodeRank currentNode = this;
		computeRank(neighborNodes);
		if (packets.isEmpty())
			return;
		for (Packet packet : packets) {
			if(packet.stopTime!=0)
				continue;
			NodeRank destination = (NodeRank) packet.message.destinationNode;
			for (Node encounteredNode : neighborNodes) {
				if(destination.equals(encounteredNode)) {
					addTransfer(nodeGraph, packet, currentNode, encounteredNode);
					break;
				}
				NodeRank encNode = (NodeRank) encounteredNode;
				if (GlobalRankOf(encNode) > GlobalRankOf(currentNode)) {
					addTransfer(nodeGraph, packet, currentNode, encNode);
					break;
				}
			}
		}
	}

	public void removePacket(Packet packet) {
	}
}
