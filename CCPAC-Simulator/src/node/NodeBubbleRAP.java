package node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import main.Main;
import main.NodeGraph;
import packet.Packet;

public class NodeBubbleRAP extends Node {
	static boolean needsInitialization = true;
	int community;
	long localRank = 0;
	long globalRank = 0;

	public NodeBubbleRAP(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	public void initialize(NodeGraph nodeGraph) {
		if (!needsInitialization)
			return;
		needsInitialization = false;

		int index = Main.inputDataConfigFileName.lastIndexOf("/");
		String communityPath = Main.inputDataConfigFileName.substring(0, index + 1) + "communities.txt";
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(communityPath));
			String line = "";
			line = br.readLine(); // header
			while ((line = br.readLine()) != null) {
				String[] columns = line.split("\t");
				if (columns.length > 1) {
					String nodeId = columns[0];
					for (Node node : nodeGraph.nodes) {
						if (node.name.equals(nodeId)) {
							NodeBubbleRAP brNode = (NodeBubbleRAP) node;
							brNode.community = Integer.parseInt(columns[1]);
							break;
						}
					}
				} else {
					System.out.println("Eroare fisier comunitati");
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void computeRank(ArrayList<Node> neighborNodes) {
		for (Node node : neighborNodes) {
			NodeBubbleRAP encNode = (NodeBubbleRAP) node;
			if (LabelOf(encNode) == LabelOf(this))
				localRank++;
			globalRank++;
		}
	}

	public static int LabelOf(NodeBubbleRAP node) {
		return node.community;
	}

	public static long LocalRankOf(NodeBubbleRAP node) {
		return node.localRank;
	}

	public static long GlobalRankOf(NodeBubbleRAP node) {
		return node.globalRank;
	}

	public static void addTransfer(NodeGraph nodeGraph, Packet packet, Node currentNode, Node encounteredNode) {
		if(encounteredNode.packets.contains(packet))
			return;
		nodeGraph.addTransfer(packet, currentNode, encounteredNode);
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		NodeBubbleRAP currentNode = this;
		computeRank(neighborNodes);
		if (packets.isEmpty())
			return;

		for (Packet packet : packets) {
			if (packet.stopTime!=0)
				continue;
			NodeBubbleRAP destination = (NodeBubbleRAP) packet.message.destinationNode;
			for (Node encounteredNode : neighborNodes) {
				if(destination.equals(encounteredNode)) {
					addTransfer(nodeGraph, packet, currentNode, encounteredNode);
					break;
				}
				NodeBubbleRAP encNode = (NodeBubbleRAP) encounteredNode;
				if (LabelOf(currentNode) == LabelOf(destination)) {
					if (LabelOf(encNode) == LabelOf(destination) && LocalRankOf(encNode) > LocalRankOf(currentNode))
						addTransfer(nodeGraph, packet, currentNode, encNode);
				} else {
					if (LabelOf(encNode) == LabelOf(destination) || GlobalRankOf(encNode) > GlobalRankOf(currentNode))
						addTransfer(nodeGraph, packet, currentNode, encNode);
				}
			}
		}
	}

	public void removePacket(Packet packet) {
	}
}
