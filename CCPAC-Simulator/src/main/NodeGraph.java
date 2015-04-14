package main;

import java.util.ArrayList;
import packet.Packet;
import simulationTime.SimulationTime;
import node.Node;
import message.Message;
import connection.Connection;
import connection.SocialConnection;


public class NodeGraph {

	class Transfer {
		Packet packet;
		Node currentNode;
		Node nextNode;

		public Transfer(Packet packet, Node currentNode, Node nextNode) {
			this.packet = packet;
			this.currentNode = currentNode;
			this.nextNode = nextNode;
		}
	}

	public ArrayList<Node> nodes = new ArrayList<Node>();
	public ArrayList<Connection> connections = new ArrayList<Connection>();
	public ArrayList<SocialConnection> socialConnections = new ArrayList<SocialConnection>();
	public ArrayList<Message> messages = new ArrayList<Message>();
	public ArrayList<Transfer> transfers = new ArrayList<Transfer>();
	public SimulationTime simTime;

	public NodeGraph() {
	}

	public NodeGraph(ArrayList<Node> nodes, ArrayList<Connection> connections) {
		this.nodes = nodes;
		this.connections = connections;
	}

	public void addNode(Node node) {
		nodes.add(node);
	}

	public Node getNode(String name) {
		for(Node node : nodes) {
			if(node.name.equals(name))
				return node;
		}
		return null;
	}

	public void addConnection(Connection connection) {
		connections.add(connection);
	}

	public void addSocialConnection(SocialConnection socialConnection) {
		socialConnections.add(socialConnection);
	}

	public void addMessage(Message message)	{
		messages.add(message);
	}

	public void prepareSendPackets() {
		ArrayList<Node> activeNodes = new ArrayList<Node>();
		ArrayList<Connection> currentConnections = new ArrayList<Connection>();
		ArrayList<Node> neighborNodes = new ArrayList<Node>();

		// Get all active nodes
		for(Connection c:connections)
			if(c.startTime <= simTime.currentTime && c.stopTime >= simTime.currentTime) {
				if(!activeNodes.contains(c.nodeA))
					activeNodes.add(c.nodeA);
				if(!activeNodes.contains(c.nodeB))
					activeNodes.add(c.nodeB);
				currentConnections.add(c);
			}

		for(Node n:activeNodes) {
			neighborNodes.clear();
			for(Connection c : currentConnections) {
				if(n.equals(c.nodeA) && !neighborNodes.contains(c.nodeB))
					neighborNodes.add(c.nodeB);
				if(n.equals(c.nodeB) && !neighborNodes.contains(c.nodeA))
					neighborNodes.add(c.nodeA);
			}
			n.prepareSendPackets(this, neighborNodes);
		}
	}

	public void sendPackets() {
		if(transfers.isEmpty())
			return;

		simTime.setMessagesTransfered();

		for(Transfer transfer:transfers) { //TODO: Parallelize
			transfer.packet.increaseTotalHops();

			if(transfer.nextNode.equals(transfer.packet.message.destinationNode)) {
				transfer.packet.setStopTime(simTime.getCurrentTime());
				transfer.packet.setTotalHopsAtArrival(transfer.packet.getTotalHops());
			}

			transfer.currentNode.removePacket(transfer.packet);
			transfer.nextNode.addPacket(transfer.packet);
		}
		transfers.clear();
	}

	public void addTransfer(Packet packet, Node currentNode, Node nextNode)	{
		if(nextNode.acceptsPackage(packet))
			transfers.add(new Transfer(packet, currentNode, nextNode));
	}

	public void initialize() {
		for(Node n:nodes) {
			n.initialize(this);
		}
	}
}
