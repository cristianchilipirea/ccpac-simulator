package printStatistics;

import main.NodeGraph;
import message.Message;

public class PrintStatisticsPacket extends PrintStatistics {

	public void print(NodeGraph nodeGraph) {
		long hops = 0;
		long messages = 0;
		long duration = 0;

		for(Message m : nodeGraph.messages) {
			long transitDuration = m.getStopTime() - m.getStartTime();
			
			if(transitDuration < 0)
				continue;
			messages ++;
			duration += transitDuration;
			hops += m.getHopsAtArrival();
		}
		System.out.println("Total Node Count");
		System.out.println(nodeGraph.nodes.size());
		System.out.println("Total Message Count");
		System.out.println(messages);
		System.out.println("Mean latency (time)");
		System.out.println((float)duration/(float)messages);
		System.out.println("Mean latency (hop count)");
		System.out.println((float)hops/(float)messages);
	}
}
