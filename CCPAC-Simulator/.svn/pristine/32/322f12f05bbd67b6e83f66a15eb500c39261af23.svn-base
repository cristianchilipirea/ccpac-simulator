package node;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;

import packet.Packet;

import main.NodeGraph;

public class NodeDLife extends Node {
	private final static int DALILY_SAMPLE_COUNT = 24;
	private final static Double dumping_factor = 0.8; //TODO..play with this
	HourlyBundle[] dailyBundle;
	ArrayList<Packet> delivered = new ArrayList<Packet>();
	int currentHour = -1;

	public NodeDLife(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	private void update(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		if(currentHour == -1) {
			currentHour = getHour(nodeGraph.simTime.getCurrentTime());
		}
		for(Node nnode : neighborNodes) {
			NodeDLife node = (NodeDLife) nnode;
			mergeDilivered(node.delivered);
		}
		int hour = getHour(nodeGraph.simTime.getCurrentTime());
		if(hour != currentHour) {
			//compute stuff for the last time slice
			computeAverage(nodeGraph);
			computeWeights();
			computeImportance();
			currentHour = hour;
		} else {
			for(Node nnode : neighborNodes) {
				NodeDLife node = (NodeDLife) nnode;
				if (!dailyBundle[currentHour].tcd.containsKey(node)) {
					dailyBundle[currentHour].tcd.put(node, (long)1);
				} else {
					Long value = dailyBundle[currentHour].tcd.get(node);
					dailyBundle[currentHour].tcd.put(node, value + 1);
				}
			}
		}
	}

	public static void addTransfer(NodeGraph nodeGraph, Packet packet, Node currentNode, Node encounteredNode) {
		if(encounteredNode.packets.contains(packet))
			return;
		nodeGraph.addTransfer(packet, currentNode, encounteredNode);
	}

	private void mergeDilivered(ArrayList<Packet> diliveredNew) {
		for(Packet p : diliveredNew) {
			if(!delivered.contains(p))
				delivered.add(p);
		}
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		update(nodeGraph, neighborNodes);
		NodeDLife currentNode = this;
		for(Packet packet : packets) {
			if(delivered.contains(packet))
				continue;
			NodeDLife nodeDestination = (NodeDLife)packet.message.destinationNode;
			for(Node nnode : neighborNodes) {
				NodeDLife node = (NodeDLife) nnode;
				if(node.equals(nodeDestination)) {
					addTransfer(nodeGraph, packet, currentNode, node);
					delivered.add(packet);
					break;
				}

				Double wAD = unboxClean(currentNode.dailyBundle[currentHour].w.get(nodeDestination));
				Double wBD = unboxClean(node.dailyBundle[currentHour].w.get(nodeDestination));
				Double IA = currentNode.dailyBundle[currentHour].importance;
				Double IB = node.dailyBundle[currentHour].importance;
				if(wBD > wAD || IB > IA) {
					addTransfer(nodeGraph, packet, currentNode, node);
				}
			}
		}
	}


	@Override
	public void initialize(NodeGraph nodeGraph) {
		dailyBundle = new HourlyBundle[DALILY_SAMPLE_COUNT];
		for(int i = 0; i < DALILY_SAMPLE_COUNT; i++)
			dailyBundle[i] = new HourlyBundle();
	}

	private int getHour(Long time) {
		Calendar c = new GregorianCalendar();
		c.setTime(new Date(time * 1000));
		return c.get(Calendar.HOUR_OF_DAY);
	}

	private double unboxClean(Double num) {
		return (num == null) ? 0 : num;
	}

	private Long getDayCount(NodeGraph nodeGraph) {
		Calendar calendarStart = nodeGraph.simTime.calendarStart;
		Calendar calendarCurrent = new GregorianCalendar();
		calendarCurrent.setTime(new Date(nodeGraph.simTime.currentTime * 1000));
		return (long)( (calendarCurrent.getTimeInMillis() - calendarStart.getTimeInMillis()) / (1000 * 60 * 60 * 24));
	}

	private void computeAverage(NodeGraph nodeGraph) {
		Long j = getDayCount(nodeGraph) + 1;
		for(NodeDLife node : dailyBundle[currentHour].tcd.keySet()) {
			Long tcd = dailyBundle[currentHour].tcd.get(node);
			Double adOld = unboxClean(dailyBundle[currentHour].ad.get(node));
			Double ad = ((double) tcd + (double)(j - 1) * adOld) / (double)j;
			dailyBundle[currentHour].ad.put(node, ad);
		}
	}

	private void computeWeights(){
		for(NodeDLife node : dailyBundle[currentHour].tcd.keySet()) {
			Double w = 0.0;
			for(int j = currentHour; j < DALILY_SAMPLE_COUNT + currentHour; j++) {
				int index = j % DALILY_SAMPLE_COUNT;
				Double ad = unboxClean(dailyBundle[index].ad.get(node));
				w += (double)DALILY_SAMPLE_COUNT / (double) (DALILY_SAMPLE_COUNT + j - currentHour) * ad;
			}
			dailyBundle[currentHour].w.put(node, w);
		}
	}

	private void computeImportance() {
		Double importance = 0.0;
		Set<NodeDLife> keys = dailyBundle[currentHour].tcd.keySet();
		for(NodeDLife node : keys) {
			importance += unboxClean(dailyBundle[currentHour].w.get(node)) * node.dailyBundle[currentHour].importance / (double) keys.size();
		}
		importance = (1 - dumping_factor) + dumping_factor * importance;
		
		dailyBundle[currentHour].importance = importance;
	}

	@Override
	public void removePacket(Packet packet) {
	}

	public class HourlyBundle {
		//total contact duration within a daily sample
		HashMap<NodeDLife, Long> tcd = new HashMap<NodeDLife, Long>();
		//average between the same daily samples
		HashMap<NodeDLife, Double> ad = new HashMap<NodeDLife, Double>();
		//weight between nodes at a given daily sample
		HashMap<NodeDLife, Double> w = new HashMap<NodeDLife, Double>();
		Double importance = 1-dumping_factor;
		public String toString() {
			return tcd.toString();
		}
	}

}
