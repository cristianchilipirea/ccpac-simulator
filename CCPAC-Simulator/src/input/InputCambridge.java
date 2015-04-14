package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import main.Factory;
import main.NodeGraph;
import node.Node;
import connection.Connection;

public class InputCambridge extends Input {
	private static final String startDate = "28-10-2005 09:55:32";
	private static final int MAX_NODE_ID = 54;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	@Override
	public NodeGraph readNodeGraph(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		NodeGraph nodeGraph = new NodeGraph();
		String line = "";
		String[] columns;
		long startDateTime = 0;
		try {
			startDateTime = sdf.parse(startDate).getTime();
		} catch (ParseException e) {
			startDateTime = 0;
		}

		while((line = br.readLine()) != null) {
			columns = line.split(" ");

			String nodeAName = columns[0].trim();
			String nodeBName = columns[1].trim();

			if(nodeAName.equals(nodeBName))
				continue;

			if(Integer.parseInt(nodeAName) > MAX_NODE_ID || Integer.parseInt(nodeBName) > MAX_NODE_ID)
				continue;

			Long startTime = (Long.parseLong(columns[2]) + startDateTime) / 1000;
			Long stopTime = (Long.parseLong(columns[3]) + startDateTime) / 1000;

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
