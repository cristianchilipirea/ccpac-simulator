package node;

import java.util.ArrayList;
import packet.Packet;
import main.NodeGraph;
import message.Message;

public class NodeMCP extends Node{
	static final int MAX_HOPS = 4;
	static final int MAX_COPIES = 4;
	public ArrayList<ReplicaPacket> replicaPackets = new ArrayList<ReplicaPacket>();

	class ReplicaPacket extends Packet {
		int replicaHops;
		int copies = 0;
		Packet packet;

		public ReplicaPacket(Message message, Packet packet, int replicaHops) {
			super(message);
			this.replicaHops = replicaHops;
			this.packet = packet;
		}

		public boolean equals(Object obj) {
			ReplicaPacket rp = (ReplicaPacket) obj;
			return rp.packet == packet;
		}

		public void setStopTime(long stopTime) {
			packet.setStopTime(stopTime);
		}

		public void setTotalHopsAtArrival(int hopsAtArrival) {
			packet.setTotalHopsAtArrival(hopsAtArrival);
		}

		public void increaseTotalHops() {
			packet.increaseTotalHops();
		}

		public int getTotalHops() {
			return packet.getTotalHops();
		}
	}

	public NodeMCP(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		if(packets.isEmpty())
			return;
		for(ReplicaPacket rp : replicaPackets) {
			if(rp.copies == MAX_COPIES)
				continue;

			for(Node node : neighborNodes) {
				NodeMCP nodeMCP = (NodeMCP) node;
				if(rp.replicaHops == MAX_HOPS)
					continue;
				if(rp.copies == MAX_COPIES)
					continue;
				if(!nodeMCP.replicaPackets.contains(rp)) {
					if(rp.replicaHops == MAX_HOPS - 1 && node.equals(rp.message.destinationNode)){
						nodeGraph.addTransfer(rp, this, node);
						rp.copies++;
					} else if(rp.replicaHops < MAX_HOPS - 1) {
						nodeGraph.addTransfer(rp, this, node);
						rp.copies++;
					}
				}
			}
		}
	}

	@Override
	public void initialize(NodeGraph nodeGraph) {
	}

	public void startPacket(Packet packet) {
		packets.add(packet);
		replicaPackets.add(new ReplicaPacket(packet.message, packet, 0));
	}

	public void addPacket(Packet packet) {
		ReplicaPacket rp = (ReplicaPacket) packet;
		replicaPackets.add(new ReplicaPacket(rp.message, rp.packet, rp.replicaHops+1));
	}

	public void removePacket(Packet packet) {
	}
}
