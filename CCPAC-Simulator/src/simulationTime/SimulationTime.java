package simulationTime;

import java.util.Calendar;

import connection.Connection;

import main.NodeGraph;


public class SimulationTime {
	public long startTime = Integer.MAX_VALUE;
	public long stopTime = Integer.MIN_VALUE;
	public long currentTime;
	public Calendar calendarStart = Calendar.getInstance();
	public Calendar calendarStop = Calendar.getInstance();

	public void setNodeGraph(NodeGraph n)
	{
		for(Connection c : n.connections) {
			if(startTime > c.startTime)
				startTime = c.startTime;
			if(stopTime < c.stopTime)
				stopTime = c.stopTime;
		}
		calendarStart.setTimeInMillis(startTime*1000);
		calendarStop.setTimeInMillis(stopTime*1000);
		currentTime = startTime;
		n.simTime=this;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public long getTotalTime() {
		return getStopTime() - getStartTime();
	}
	
	public Long getNextTime() {
		return new Long(currentTime++);
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public long getCurrentTimeInMillis() {
		return currentTime * 1000;
	}

	public void setMessagesTransfered() {
	}

	public void printTime() {
	}

	public int getDays() {
		return (int)( (calendarStop.getTimeInMillis() - calendarStart.getTimeInMillis()) / (1000 * 60 * 60 * 24));
	}

	public int getWeeks() {
		return (int)Math.ceil(getDays() / 7.0);
	}

	public int getCurrentWeek() {
		int i;
		for (i = 0; i < getWeeks(); i++) {
			if (currentTime * 1000 < getStartOfWeek(i))
				break;
		}
		i--;
		return i;
	}

	public long getStartOfWeek(int week) {
		Calendar calendarAux = Calendar.getInstance();
		calendarAux.setTimeInMillis(calendarStart.getTimeInMillis());

		for(int i=0; i < week; i++) {
			calendarAux.add(Calendar.DATE, 1);
			while (calendarAux.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
				calendarAux.add(Calendar.DATE, 1);
			}
		}
		calendarAux.set(Calendar.HOUR_OF_DAY, 0);
		calendarAux.set(Calendar.MINUTE, 0);
		calendarAux.set(Calendar.SECOND, 0);
		calendarAux.set(Calendar.MILLISECOND, 0);

		return calendarAux.getTimeInMillis();
	}
}
