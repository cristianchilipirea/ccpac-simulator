package messageGenerator;

import java.util.Random;

import main.Factory;
import main.Main;
import main.NodeGraph;
import node.Node;

public class MessageGeneratorExtended extends MessageGenerator {
	final static int MESSAGE_SIZE = 1;
	final static int MESSAGES_PER_NODE = 2;
	final static int STEPS = 20;
	static long stepSize;
	static long lastStep = 0;
	// MESSAGE_COUNT == STEPS * NODE_COUNT * MESSAGES_PER_NODE

	public void generateMessages(NodeGraph nodeGraph)
	{
		Random rand = Main.random;
		// we want to have STEPS points not STEPS regions
		stepSize = nodeGraph.simTime.getTotalTime() / (STEPS - 1);
		if(lastStep == 0)
			lastStep = nodeGraph.simTime.getStartTime();

		while(nodeGraph.simTime.getCurrentTime() >= lastStep) {
			for(Node n:nodeGraph.nodes) {
				if(n.canSendMessages) {
					for(int i = 0; i < MESSAGES_PER_NODE; i++) {
						Node destination;
						while(true) {
							destination = nodeGraph.nodes.get(rand.nextInt(nodeGraph.nodes.size()));
							if(!destination.canSendMessages)
								continue;
							if(destination != n)
								break;
						}
						nodeGraph.addMessage(Factory.getMessage(n, destination, MESSAGE_SIZE));
					}
				}
			}
			lastStep += stepSize;
		}
	}
}