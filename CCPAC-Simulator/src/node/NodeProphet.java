package node;

import java.util.ArrayList;
import java.util.HashMap;
import packet.Packet;
import main.NodeGraph;

public class NodeProphet extends Node {
	// A == current Node
	// B == neighbor Node
	// C == destination Node
	final static double P_INIT = 0.5;
	final static double GAMMA = 0.999;
	final static double BETA = 0.9;
	final static double DELTA = 0.01;

	public HashMap<Node, Double> P = new HashMap<Node, Double>();
	HashMap<Node, Long> lastSeen = new HashMap<Node, Long>();

	public NodeProphet(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	private double unboxClean(Double num) {
		return (num == null) ? 0 : num;
	}

	private long unboxClean(Long num) {
		return (num == null) ? 0 : num;
	}

	void updateNodeProbability(Node nodeB) {
		double Pab = unboxClean(P.get(nodeB));
		Pab = Pab + (1 - DELTA - Pab) * P_INIT;
		P.put(nodeB, Pab);
	}

	void ageProbabilities(NodeGraph nodeGraph) {
		for(Node nodeB : lastSeen.keySet()) {
			if(nodeB.equals(this))
				continue;
			double Pab = unboxClean(P.get(nodeB));
			long k = nodeGraph.simTime.getCurrentTime() - unboxClean(lastSeen.get(nodeB));
			Pab = Pab * Math.pow(GAMMA, k/1000);
			P.put(nodeB, Pab);
		}
	}

	void updateTransitiveProbability(NodeProphet nodeB){
		double Pab = unboxClean(P.get(nodeB));
		for(Node nodeC : nodeB.P.keySet()) {
			if(nodeC.equals(nodeB))
				continue;
			double Pac = unboxClean(P.get(nodeC));
			double Pbc = unboxClean(nodeB.P.get(nodeC));
			Pac = Math.max(Pac, Pab * Pbc * BETA);
			P.put(nodeC, Pac);
		}
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		for(Packet packet : packets) {
			Node nodeC = packet.message.destinationNode;
			double Pac = unboxClean(P.get(nodeC));
			double Pmax = Pac;
			Node sendNode = null;
			for(Node node : neighborNodes) {
				NodeProphet nodeB = (NodeProphet) node;
				double Pbc = unboxClean(nodeB.P.get(nodeC));

				if(Pmax < Pbc) {
					Pmax = Pbc;
					sendNode = node;
				}
			}
			if( sendNode != null )
				if(!sendNode.packets.contains(packet))
					nodeGraph.addTransfer(packet, this, sendNode);
		}

		ageProbabilities(nodeGraph);
		for(Node node : neighborNodes) {
			updateNodeProbability(node);
			updateTransitiveProbability((NodeProphet)node);
			lastSeen.put(node, nodeGraph.simTime.getCurrentTime());
		}
	}

	@Override
	public void initialize(NodeGraph nodeGraph) {
		P.put(this, 1.0);
	}

	@Override
	public void removePacket(Packet packet) {
	}

}
