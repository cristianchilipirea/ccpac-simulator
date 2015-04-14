package node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import main.Main;
import main.NodeGraph;
import packet.Packet;

public class NodeLabel extends Node {
	static boolean needsInitialization = true;
	int community;

	public NodeLabel(String name, Boolean canSendMessages) {
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
							NodeLabel brNode = (NodeLabel) node;
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

	public static int LabelOf(NodeLabel node) {
		return node.community;
	}

	public static void addTransfer(NodeGraph nodeGraph, Packet packet, Node currentNode, Node encounteredNode) {
		if(encounteredNode.packets.contains(packet))
			return;
		nodeGraph.addTransfer(packet, currentNode, encounteredNode);
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		NodeLabel currentNode = this;
		if (packets.isEmpty())
			return;
		for (Packet packet : packets) {
			if (packet.stopTime!=0)
				continue;
			NodeLabel destination = (NodeLabel) packet.message.destinationNode;
			for (Node encounteredNode : neighborNodes) {
				if(destination.equals(encounteredNode)) {
					addTransfer(nodeGraph, packet, currentNode, encounteredNode);
					break;
				}
				NodeLabel encNode = (NodeLabel) encounteredNode;
				if (LabelOf(encNode) == LabelOf(destination)) {
					addTransfer(nodeGraph, packet, currentNode, encNode);
					break;
				}
			}
		}
	}

	public void removePacket(Packet packet) {
	}
}
