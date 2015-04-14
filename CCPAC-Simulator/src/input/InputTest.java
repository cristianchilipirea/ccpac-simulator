package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import connection.Connection;

import node.Node;

import main.Factory;
import main.NodeGraph;

public class InputTest extends Input {
	@Override
	public NodeGraph readNodeGraph(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		NodeGraph nodeGraph = new NodeGraph();
		String line = "";
		String[] columns;

		while((line = br.readLine()) != null) {
			columns = line.split("\t");

			String nodeAName = columns[0].trim();
			String nodeBName = columns[1].trim();

			if(nodeAName.equals(nodeBName))
				continue;

			Long startTime = Long.parseLong(columns[2]);
			Long stopTime = Long.parseLong(columns[3]);

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
