package simulationTime;

import java.util.ArrayList;
import java.util.Collections;

import main.NodeGraph;

import connection.Connection;


public class SimulationTimeEvent extends SimulationTime {
	static final int TICK_SIZE = 1;
	ArrayList<Long> events = new ArrayList<Long>();
	boolean messagesTransfered = true;

	public void setNodeGraph(NodeGraph n)
	{
		for(Connection c : n.connections) {
			if(!events.contains(c.startTime))
				events.add(c.startTime);
			if(!events.contains(c.stopTime))
				events.add(c.stopTime);
			if(startTime > c.startTime)
				startTime = c.startTime;
			if(stopTime < c.stopTime)
				stopTime = c.stopTime;
		}
		calendarStart.setTimeInMillis(startTime * 1000);
		calendarStop.setTimeInMillis(stopTime * 1000);
		currentTime = startTime;
		Collections.sort(events);
		events.remove(0); // the first one does not need to be there
		n.simTime=this;
	}

	public Long getNextTime()
	{
		if (events.size() == 0)
			return null;
		if (messagesTransfered)
			currentTime += TICK_SIZE;
		else
			currentTime = events.remove(0);

		while (events.size()!=0 && currentTime >= events.get(0))
			events.remove(0);

		messagesTransfered = false;
		if(currentTime <= stopTime)
			return new Long(currentTime);
		else
			return null;
	}

	public void setMessagesTransfered() {
		messagesTransfered = true;
	}

	public void printTime() {
		System.out.println("Start time: "+calendarStart.getTime());
		System.out.println("Stop time: "+calendarStop.getTime());
		System.out.println("Days in trace: "+ getDays());
		System.out.println("Weeks in trace: "+ getWeeks());
	}
}