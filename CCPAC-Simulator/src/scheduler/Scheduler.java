package scheduler;

import packet.Packet;
import main.Factory;
import main.NodeGraph;
import message.Message;

public class Scheduler {
	boolean canSchedule = true;

	public void scheduleMessages(NodeGraph nodeGraph)
	{
		if(!canSchedule)
			return;
		canSchedule = false;

		for(Message m:nodeGraph.messages) {
			for(int i = 0; i < m.size; i++) {
				Packet packet = Factory.getPacket(m);
				if(packet==null)
					System.out.println("Packet is null, check factory");
				packet.startTime = nodeGraph.simTime.getCurrentTime();
				m.startNode.startPacket(packet);
				m.addPacket(packet);
			}
		}
	}

}
