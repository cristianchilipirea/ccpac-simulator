package messageGenerator;

import java.util.Random;

import main.Factory;
import main.Main;
import main.NodeGraph;
import node.Node;


public class MessageGenerator {
	final int SIZE = 1;
	boolean canGenerate = true;

	public void generateMessages(NodeGraph nodeGraph)
	{
		Node destination;
		Random rand = Main.random;

		if(!canGenerate)
			return;
		canGenerate = false;
		for(Node n:nodeGraph.nodes) {
			if(n.canSendMessages) {
				for(int i = 0; i < 100; i++) {
					while(true) {
						destination = nodeGraph.nodes.get(rand.nextInt(nodeGraph.nodes.size()));
						if(!destination.canSendMessages)
							continue;
						if(destination != n)
							break;
					}
					nodeGraph.addMessage(Factory.getMessage(n, destination, SIZE));
				}
			}
		}
	}
}
