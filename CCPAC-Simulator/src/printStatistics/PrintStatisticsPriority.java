package printStatistics;

import java.util.ArrayList;
import java.util.Collections;

import main.NodeGraph;
import message.Message;
import message.MessageMAPPriority;


public class PrintStatisticsPriority extends PrintStatistics {

	public void print(NodeGraph nodeGraph) {
		ArrayList<MessageMAPPriority> messages = new ArrayList<MessageMAPPriority>();
		for(Message m:nodeGraph.messages) {
			messages.add((MessageMAPPriority)m);
		}
		Collections.sort(messages);

		System.out.println(" Priority (StartNode DestinationNode) StartTime ArrivalTime TransitTime");
		for(Message m:messages) {
			//System.out.println(((MessageMAPPriority)m).priority+" "+m+" "+m.getStartTime()+" "+m.getStopTime()+" "+(m.getStopTime()-m.getStartTime()));
			//System.out.println(i+"("+((MessageMAPPriority)m).priority+")"+"	"+m.getStopTime()+" ");
			System.out.println(m.getStopTime()+" ");
		}
	}
}
