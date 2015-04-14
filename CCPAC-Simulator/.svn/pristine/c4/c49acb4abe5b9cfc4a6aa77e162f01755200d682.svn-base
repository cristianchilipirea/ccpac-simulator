package node;

import java.util.ArrayList;

import packet.Packet;

import main.NodeGraph;


public class NodeMAP extends Node {

	public NodeMAP(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		boolean [] neighbors = new boolean[10];
		boolean [] neighborsInterCloud = new boolean[10];
		Node nextHopNode;
		for(Packet packet:packets) {
			if(packet.startTime > nodeGraph.simTime.getCurrentTime())
				continue;
			String destinationName = packet.message.destinationNode.name;
			if(destinationName.charAt(0) == name.charAt(0))
				nextHopNode = packet.message.destinationNode;//send directly to destination
			else if(name.charAt(1) == '0')
				nextHopNode = nodeGraph.getNode(destinationName.charAt(0)+"0");//send between routers
			else
				nextHopNode = nodeGraph.getNode(name.charAt(0)+"0"); //send to my router

			if(name.charAt(1) != '0') {
				if (neighbors[Integer.parseInt(""+nextHopNode.name.charAt(1))])
					continue;
				neighbors[Integer.parseInt(""+nextHopNode.name.charAt(1))] = true;
			} else {
				if (nextHopNode.name.charAt(1) == '0') {
					if (neighborsInterCloud[Integer.parseInt(""+nextHopNode.name.charAt(0))])
						continue;
					neighborsInterCloud[Integer.parseInt(""+nextHopNode.name.charAt(0))] = true;
				} else {
					if (neighbors[Integer.parseInt(""+nextHopNode.name.charAt(1))])
						continue;
					neighbors[Integer.parseInt(""+nextHopNode.name.charAt(1))] = true;
				}
			}

			nodeGraph.addTransfer(packet, this, nextHopNode);
		}
	}

	@Override
	public void initialize(NodeGraph nodeGraph) {
		// TODO Auto-generated method stub
		
	}

}
