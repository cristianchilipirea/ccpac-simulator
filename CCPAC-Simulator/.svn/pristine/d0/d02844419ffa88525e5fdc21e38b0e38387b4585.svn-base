package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import node.Node;
import connection.Connection;

import main.Factory;
import main.NodeGraph;

public class InputGenerated extends Input {

	@Override
	public NodeGraph readNodeGraph(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		NodeGraph nodeGraph = new NodeGraph();
		String line = "";
		String[] columns;
		line = br.readLine();

		while((line = br.readLine()) != null) {
			columns = line.split("\t");

			String nodeAName = columns[0].trim();
			String nodeBName = columns[1].trim();
			if(nodeAName.equals(nodeBName))
				continue;

			Long startTime = (long)Long.parseLong(columns[2]);
			Long stopTime = (long)Long.parseLong(columns[3]);

			Node nodeA;
			Node nodeB;

			if((nodeA = nodeGraph.getNode(nodeAName)) == null) {
				nodeA = Factory.getNode(nodeAName, true);
				nodeGraph.addNode(nodeA);
			}
			if((nodeB = nodeGraph.getNode(nodeBName)) == null) {
				nodeB = Factory.getNode(nodeBName, true);
				nodeGraph.addNode(nodeB);
			}

			nodeGraph.addConnection(new Connection(nodeA, nodeB, 1, startTime, stopTime));
		}
		br.close();
		return nodeGraph;
	}

}
