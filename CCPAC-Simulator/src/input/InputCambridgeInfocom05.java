package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import connection.Connection;

import node.Node;

import main.Factory;
import main.NodeGraph;

public class InputCambridgeInfocom05 extends Input {
	private static final String startDate = "07-03-2005";
	private static final int MAX_NODE_ID = 41;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	@Override
	public NodeGraph readNodeGraph(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		NodeGraph nodeGraph = new NodeGraph();
		String line = "";
		String[] columns;
		long startDateTime = 0;
		try {
			startDateTime = sdf.parse(startDate).getTime() / 1000;
		} catch (ParseException e) {
			startDateTime = 0;
		}

		while((line = br.readLine()) != null) {
			columns = line.split("\t");

			String nodeAName = columns[0].trim();
			String nodeBName = columns[1].trim();

			if(nodeAName.equals(nodeBName))
				continue;

			if(Integer.parseInt(nodeAName) > MAX_NODE_ID || Integer.parseInt(nodeBName) > MAX_NODE_ID)
				continue;

			Long startTime = Long.parseLong(columns[2]) + startDateTime;
			Long stopTime = Long.parseLong(columns[3]) + startDateTime;

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
