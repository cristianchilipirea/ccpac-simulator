package scheduler;

import packet.Packet;
import main.Factory;
import main.NodeGraph;
import message.Message;

public class SchedulerMAPGreedy extends Scheduler {
	boolean canSchedule = true;


	public void scheduleMessages(NodeGraph nodeGraph)
	{
		boolean interCloudConnection[][][] = new boolean[4][4][(int)nodeGraph.simTime.stopTime-(int)nodeGraph.simTime.startTime];
		boolean startNodeConnection[][][] = new boolean[4][4][(int)nodeGraph.simTime.stopTime-(int)nodeGraph.simTime.startTime];
		boolean destinationNodeConnection[][][] = new boolean[4][4][(int)nodeGraph.simTime.stopTime-(int)nodeGraph.simTime.startTime];
		int destinationNodeCloud = 0;
		int startNodeCloud = 0;
		int startNode = 0;
		int destinationNode = 0;
		if(!canSchedule)
			return;
		canSchedule = false;
		for(Message m:nodeGraph.messages) {
			for(int i = 0; i < m.size; i++) {
				Packet packet = Factory.getPacket(m);
				if(packet==null)
					System.out.println("Packet is null, check factory");
				m.startNode.addPacket(packet);
				m.addPacket(packet);
				startNode = Integer.parseInt(m.startNode.name.charAt(1)+"")-1;
				startNodeCloud = Integer.parseInt(m.startNode.name.charAt(0)+"")-1;
				destinationNode = Integer.parseInt(m.destinationNode.name.charAt(1)+"")-1;
				destinationNodeCloud = Integer.parseInt(m.destinationNode.name.charAt(0)+"")-1;
				if(startNodeCloud!=destinationNodeCloud)
					for(int j = (int)nodeGraph.simTime.startTime; j < nodeGraph.simTime.stopTime; j++) {
						if(interCloudConnection[startNodeCloud][destinationNodeCloud][j]==false && 
								startNodeConnection[startNodeCloud][startNode][j]==false && 
								destinationNodeConnection[destinationNodeCloud][destinationNode][j] == false) {
							interCloudConnection[startNodeCloud][destinationNodeCloud][j] = true;
							startNodeConnection[startNodeCloud][startNode][j] = true;
							destinationNodeConnection[destinationNodeCloud][destinationNode][j] = true;
							packet.startTime = j;
							break;
						}
				}
			}
		}
	}
}
