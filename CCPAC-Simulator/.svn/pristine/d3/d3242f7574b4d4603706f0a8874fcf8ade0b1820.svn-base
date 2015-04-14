package input;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import node.Node;

import main.Factory;
import main.NodeGraph;

import connection.Connection;


public class Input {

	public NodeGraph readNodeGraph(String fileName) throws IOException {
		int nodesNumber = 0;
		int connectionsNumber = 0;
		Path path = FileSystems.getDefault().getPath(fileName);
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		int i = 0;
		NodeGraph nodeGraph = new NodeGraph();

		String[] columns = lines.get(i++).split("\t");
		if(columns[0].equals("nodesNumber"))
			nodesNumber = Integer.parseInt(columns[1]);
		else
			throw new IOException("INPUT ERROR: Can't find nodesNumber");

		columns = lines.get(i++).split("\t");
		if(columns[0].equals("connectionsNumber"))
			connectionsNumber = Integer.parseInt(columns[1]);
		else
			throw new IOException("INPUT ERROR: Can't find connectionsNumber");

		columns = lines.get(i++).split("\t");
		if(!columns[0].equals("nodeNames"))
			throw new IOException("INPUT ERROR: Can't find Node header");

		for(int n = i + nodesNumber;i<n;) {
			columns = lines.get(i++).split("\t");
			if(columns.length < 2)
				throw new IOException("INPUT ERROR: Corrupted Node data");
			Node node = Factory.getNode(columns[0], Boolean.parseBoolean(columns[1]));
			nodeGraph.addNode(node);
		}

		columns = lines.get(i++).split("\t");
		if(!columns[0].equals("nodeAName"))
			throw new IOException("INPUT ERROR: Can't find Connection header");

		for(int n = i + connectionsNumber;i<n;) {
			columns = lines.get(i++).split("\t");
			if(columns.length < 5)
				throw new IOException("INPUT ERROR: Corupted Connection data");
			Node nodeAName = nodeGraph.getNode(columns[0]);
			Node nodeBName = nodeGraph.getNode(columns[1]);
			Integer packetsPerSecond = Integer.parseInt(columns[2]);
			Long startTime = Long.parseLong(columns[3]);
			Long stopTime = Long.parseLong(columns[4]);
			Connection connection = Factory.getConnection(nodeAName, nodeBName, packetsPerSecond, startTime, stopTime);
			nodeGraph.addConnection(connection);
		}

		return nodeGraph;
	}

	public NodeGraph readNodeGraphSocial(String fileName, NodeGraph nodeGraph) throws IOException {
		return nodeGraph;
	}
}