package printStatistics;

import main.NodeGraph;
import node.Node;

public class PrintStatisticsNode extends PrintStatistics {

	@Override
	public void print(NodeGraph nodeGraph) {
		for(Node node : nodeGraph.nodes)
			System.out.println(node);
	}
}
