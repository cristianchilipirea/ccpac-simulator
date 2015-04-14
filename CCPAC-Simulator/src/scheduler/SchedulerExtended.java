package scheduler;

import main.Factory;
import main.NodeGraph;
import message.Message;
import packet.Packet;

public class SchedulerExtended extends Scheduler {
	boolean canSchedule = true;

	public void scheduleMessages(NodeGraph nodeGraph)
	{
		for(Message m:nodeGraph.messages) {
			if(!m.scheduled) {
				m.scheduled = true;
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
}
