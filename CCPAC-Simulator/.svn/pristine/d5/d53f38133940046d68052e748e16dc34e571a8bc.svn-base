package printStatistics;

import main.NodeGraph;
import message.Message;

public class PrintStatisticsPacketTTL extends PrintStatistics {

	public void print(NodeGraph nodeGraph) {
		final int STEPS = 12;
		long[] TTLs = new long[STEPS];
		long[] countMessages = new long[STEPS];
		long[] countHops = new long[STEPS];
		int messages = 0;
		TTLs[0] = 0;
		TTLs[1] = 120; // 2 minutes
		TTLs[2] = 600; // 10 minutes
		TTLs[3] = 3600; // 1 hour
		TTLs[4] = 3600 * 3; // 3 hours
		TTLs[5] = 3600 * 6; // 6 hours
		TTLs[6] = 3600 * 24; // 1 day
		TTLs[7] = 3600 * 24 * 2; // 2 days
		TTLs[8] = 3600 * 24 * 4; // 4 days
		TTLs[9] = 3600 * 24 * 7; // 1 week
		TTLs[10] = 3600 * 24 * 7 * 3; // 3 weeks
		TTLs[11] = Long.MAX_VALUE; // forever

		for(Message m : nodeGraph.messages) {
			long transitDuration = m.getStopTime() - m.getStartTime();
			messages ++;
			if(transitDuration < 0)
				continue;
			for(int i = 0; i < STEPS; i++) {
				if(transitDuration <= TTLs[i]) {
					countMessages[i]++;
					countHops[i] += m.getHopsAtArrival();
				}
			}
		}
		System.out.println("Total Node Count");
		System.out.println(nodeGraph.nodes.size());
		System.out.println("Total Message Count");
		System.out.println(messages);
		System.out.println("Success Ratio");
		for(int i = 0; i < STEPS; i++) {
			System.out.println((float)countMessages[i]/(float)messages);
		}
		System.out.println("Total Cost");
		for(int i = 0; i < STEPS; i++) {
			System.out.println((float)countHops[i]/(float)messages);
		}
	}
}
