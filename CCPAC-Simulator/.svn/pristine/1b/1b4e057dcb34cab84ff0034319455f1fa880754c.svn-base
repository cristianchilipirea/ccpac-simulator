package printStatistics;

import main.NodeGraph;
import message.Message;

public class PrintStatistics {

	public void print(NodeGraph nodeGraph) {
		System.out.println("(StartNode DestinationNode) StartTime ArrivalTime TransitTime");
		int countMessages = 0;
		int countArrived = 0;
		for(Message m:nodeGraph.messages) {
			countMessages++;
			System.out.println(m+" "+m.getStartTime()+" "+m.getStopTime()+" "+(m.getStopTime()-m.getStartTime()));
			if(m.getStopTime()!=0)
				countArrived++;
		}
		System.out.println("Messages "+countMessages+" of which "+countArrived+" arrived");
	}
}
