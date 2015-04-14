package main;

import input.Input;

import java.lang.reflect.Constructor;
import packet.Packet;
import printStatistics.PrintStatistics;
import scheduler.Scheduler;
import simulationTime.SimulationTime;
import node.Node;
import message.Message;
import messageGenerator.MessageGenerator;
import connection.Connection;


public class Factory {
	static String nodeType = null;

	static public void setNodeType(String nodeType) {
		Factory.nodeType = "node."+nodeType;
		@SuppressWarnings("rawtypes")
		Class nodeClass;
		try {
			nodeClass = Class.forName(Factory.nodeType);
			if(!nodeClass.getPackage().getName().equals("node")) {
				System.out.println("CONFIG ERROR: " + nodeType +" nodeType is wrong!");
				System.exit(1);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("CONFIG ERROR: " + nodeType +" nodeType is wrong!");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Node getNode(String name, Boolean canSendMessages) {
		if (nodeType == null)
			return null;

		try {
			@SuppressWarnings("rawtypes")
			Class nodeClass = Class.forName(nodeType);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor nodeCons = nodeClass.getConstructor(String.class, Boolean.class);
			nodeCons.newInstance(name, canSendMessages);
			return (Node)nodeCons.newInstance(name, canSendMessages);
		} catch (Exception e) {
			System.out.println("PROGRAMMING ERROR: FACTORY: Could not find the class named " + nodeType);
			e.printStackTrace();
		}
		return null;
	}

	static String connectionType = null;

	static public void setConnectionType(String connectionType)	{
		Factory.connectionType = "connection."+connectionType;
		@SuppressWarnings("rawtypes")
		Class connectionClass;
		try {
			connectionClass = Class.forName(Factory.connectionType);
			if(!connectionClass.getPackage().getName().equals("connection")) {
				System.out.println("CONFIG ERROR: " + connectionType +" connectionType is wrong!");
				System.exit(1);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("CONFIG ERROR: " + connectionType +" connectionType is wrong!");
			e.printStackTrace();
			System.exit(1);
		}	
	}

	public static Connection getConnection(Node nodeA, Node nodeB, Integer packetsPerSecound, Long startTime, Long stopTime) {
		if (connectionType == null)
			return null;

		try {
			@SuppressWarnings("rawtypes")
			Class nodeClass = Class.forName(connectionType);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor nodeCons = nodeClass.getConstructor(Node.class, Node.class, Integer.class, Long.class, Long.class);
			return (Connection)nodeCons.newInstance(nodeA, nodeB, packetsPerSecound, startTime, stopTime);
		} catch (Exception e) {
			System.out.println("PROGRAMMING ERROR: FACTORY: Could not find the class named " + connectionType);
			e.printStackTrace();
		}
		return null;
	}
	
	static String messageType = null;

	public static void setMessageType(String messageType) {
		Factory.messageType = "message."+messageType;
		@SuppressWarnings("rawtypes")
		Class connectionClass;
		try {
			connectionClass = Class.forName(Factory.messageType);
			if(!connectionClass.getPackage().getName().equals("message")) {
				System.out.println("CONFIG ERROR: " + messageType +" messageType is wrong!");
				System.exit(1);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("CONFIG ERROR: " + messageType +" messageType is wrong!");
			e.printStackTrace();
			System.exit(1);
		}	
	}

	public static Message getMessage(Node startNode, Node destinationNode, Integer size) {
		if (messageType == null)
			return null;

		try {
			@SuppressWarnings("rawtypes")
			Class nodeClass = Class.forName(messageType);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor nodeCons = nodeClass.getConstructor(Node.class, Node.class, Integer.class);
			return (Message)nodeCons.newInstance(startNode, destinationNode, size);
		} catch (Exception e) {
			System.out.println("PROGRAMMING ERROR: FACTORY: Could not find the class named " + messageType);
			e.printStackTrace();
		}
		return null;
	}

	
	static String packetType = null;

	public static void setPacketType(String packetType) {
		Factory.packetType = "packet."+packetType;
		@SuppressWarnings("rawtypes")
		Class connectionClass;
		try {
			connectionClass = Class.forName(Factory.packetType);
			if(!connectionClass.getPackage().getName().equals("packet")) {
				System.out.println("CONFIG ERROR: " + Factory.packetType +" packetType is wrong!");
				System.exit(1);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("CONFIG ERROR: " + Factory.packetType +" packetType is wrong!");
			e.printStackTrace();
			System.exit(1);
		}	
	}

	public static Packet getPacket(Message message) {
		if (packetType == null)
			return null;

		try {
			@SuppressWarnings("rawtypes")
			Class nodeClass = Class.forName(packetType);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor nodeCons = nodeClass.getConstructor(Message.class);
			return (Packet)nodeCons.newInstance(message);
		} catch (Exception e) {
			System.out.println("PROGRAMMING ERROR: FACTORY: Could not find the class named " + packetType);
			e.printStackTrace();
		}
		return null;
	}
	
	public static PrintStatistics getPrintStatistics(String name) {
		try {
			@SuppressWarnings("rawtypes")
			Class printStatsClass = Class.forName("printStatistics." + name);
			
			if(!printStatsClass.getPackage().getName().equals("printStatistics")) {
				System.out.println("CONFIG ERROR: " + name +" prinStatisticsType is wrong!");
				System.exit(1);
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor nodeCons = printStatsClass.getConstructor();
			return (PrintStatistics)nodeCons.newInstance();
		} catch (Exception e) {
			System.out.println("PROGRAMMING ERROR: FACTORY: Could not find the class named " + "printStatistics." + name);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Input getInput(String name) {
		try {
			@SuppressWarnings("rawtypes")
			Class inputClass = Class.forName("input." + name);
			
			if(!inputClass.getPackage().getName().equals("input")) {
				System.out.println("CONFIG ERROR: " + name +" inputType is wrong!");
				System.exit(1);
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor nodeCons = inputClass.getConstructor();
			return (Input)nodeCons.newInstance();
		} catch (Exception e) {
			System.out.println("PROGRAMMING ERROR: FACTORY: Could not find the class named " + "input." + name);
			e.printStackTrace();
		}
		return null;
	}
	
	public static SimulationTime getSimulationTime(String name) {
		try {
			@SuppressWarnings("rawtypes")
			Class inputClass = Class.forName("simulationTime." + name);
			
			if(!inputClass.getPackage().getName().equals("simulationTime")) {
				System.out.println("CONFIG ERROR: " + name +" simulationTimeType is wrong!");
				System.exit(1);
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor nodeCons = inputClass.getConstructor();
			return (SimulationTime)nodeCons.newInstance();
		} catch (Exception e) {
			System.out.println("PROGRAMMING ERROR: FACTORY: Could not find the class named " + "simualtionTime." + name);
			e.printStackTrace();
		}
		return null;
	}
	
	public static MessageGenerator getMessageGenerator(String name) {
		try {
			@SuppressWarnings("rawtypes")
			Class messageGenClass = Class.forName("messageGenerator." + name);
			
			if(!messageGenClass.getPackage().getName().equals("messageGenerator")) {
				System.out.println("CONFIG ERROR: " + name +" messageGeneratorType is wrong!");
				System.exit(1);
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor nodeCons = messageGenClass.getConstructor();
			return (MessageGenerator)nodeCons.newInstance();
		} catch (Exception e) {
			System.out.println("PROGRAMMING ERROR: FACTORY: Could not find the class named " + "messageGenerator." + name);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Scheduler getScheduler(String name) {
		try {
			@SuppressWarnings("rawtypes")
			Class messageGenClass = Class.forName("scheduler." + name);
			
			if(!messageGenClass.getPackage().getName().equals("scheduler")) {
				System.out.println("CONFIG ERROR: " + name +" schedulerType is wrong!");
				System.exit(1);
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor nodeCons = messageGenClass.getConstructor();
			return (Scheduler)nodeCons.newInstance();
		} catch (Exception e) {
			System.out.println("PROGRAMMING ERROR: FACTORY: Could not find the class named " + "scheduler." + name);
			e.printStackTrace();
		}
		return null;
	}
}
