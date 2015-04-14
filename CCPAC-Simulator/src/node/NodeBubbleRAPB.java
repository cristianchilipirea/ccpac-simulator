package node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import main.Main;
import main.NodeGraph;
import packet.Packet;

public class NodeBubbleRAPB extends Node {
	static boolean needsInitialization = true;
	int community;
	long localRank = 0;
	long globalRank = 0;
	ArrayList<Packet> removePackets = new ArrayList<Packet>();
	public NodeBubbleRAPB(String name, Boolean canSendMessages) {
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
							NodeBubbleRAPB brNode = (NodeBubbleRAPB) node;
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
			NodeBubbleRAPB encNode = (NodeBubbleRAPB) node;
			if (LabelOf(encNode) == LabelOf(this))
				localRank++;
			globalRank++;
		}
	}

	public static int LabelOf(NodeBubbleRAPB node) {
		return node.community;
	}

	public static long LocalRankOf(NodeBubbleRAPB node) {
		return node.localRank;
	}

	public static long GlobalRankOf(NodeBubbleRAPB node) {
		return node.globalRank;
	}

	public static void addTransfer(NodeGraph nodeGraph, Packet packet, Node currentNode, Node encounteredNode) {
		if(encounteredNode.packets.contains(packet))
			return;
		nodeGraph.addTransfer(packet, currentNode, encounteredNode);
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		NodeBubbleRAPB currentNode = this;
		computeRank(neighborNodes);
		if (packets.isEmpty())
			return;
		for (Packet packet : packets) {
			if (packet.stopTime!=0)
				continue;
			if(removePackets.contains(packet))
				continue;
			NodeBubbleRAPB destination = (NodeBubbleRAPB) packet.message.destinationNode;
			for (Node encounteredNode : neighborNodes) {
				if(destination.equals(encounteredNode)) {
					addTransfer(nodeGraph, packet, currentNode, encounteredNode);
					break;
				}
				NodeBubbleRAPB encNode = (NodeBubbleRAPB) encounteredNode;
				if (LabelOf(currentNode) == LabelOf(destination)) {
					if (LabelOf(encNode) == LabelOf(destination) && LocalRankOf(encNode) > LocalRankOf(currentNode))
						addTransfer(nodeGraph, packet, currentNode, encNode);
				} else {
					if (LabelOf(encNode) == LabelOf(destination) || GlobalRankOf(encNode) > GlobalRankOf(currentNode)) {
						addTransfer(nodeGraph, packet, currentNode, encNode);
						if (LabelOf(encNode) == LabelOf(destination))
							removePackets.add(packet);
					}
				}
			}
		}
	}

	public void removePacket(Packet packet) {
	}
}
