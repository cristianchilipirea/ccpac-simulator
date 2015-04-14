package main;

import input.Input;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import messageGenerator.MessageGenerator;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import printStatistics.PrintStatistics;
import scheduler.Scheduler;
import simulationTime.SimulationTime;

//http://www.youtube.com/watch?v=gUy8uX7thJ4

public class Main {
	static Input in;
	static String nodeGraphFileName = null;
	public static  String inputDataConfigFileName = null;
	static String nodeGraphSocialFileName = null;
	static MessageGenerator messageGenerator = null;
	static SimulationTime simTime = null;
	static Scheduler sched = null;
	static PrintStatistics print = null;
	static boolean useSocialData = false;
	public static  Random random = new Random(11); // Everyone should use this random!

	private static String getSectionElement(Ini.Section section, String name) throws IOException {
		String aux = section.get(name);
		if(aux == null)
			throw new IOException("Config File ERROR: Can't find element "+name);
		return aux;
		
	}
	
	private static void readConfig() throws InvalidFileFormatException, IOException {
		Ini ini = new Ini();
		File f = new File("config.ini");
		Ini inputIni = new Ini();
		ini.load(f);

		Ini.Section section = ini.get("Settings");
		if(section == null)
			throw new IOException("INPUT ERROR: Can't find section Settings");

		inputDataConfigFileName = getSectionElement(section,"inputDataConfigFileName");

		File f2 = new File(inputDataConfigFileName);
		inputIni.load(f2);

		Ini.Section sectionInput = inputIni.get("Input");
		if(sectionInput == null)
			throw new IOException("INPUT ERROR: Can't find section Input");

		nodeGraphFileName = sectionInput.get("nodeGraphFileName");
		if(nodeGraphFileName == null)
			throw new IOException();

		nodeGraphSocialFileName = sectionInput.get("nodeGraphSocialFileName");
		if(nodeGraphSocialFileName == null)
			throw new IOException();

		Factory.setNodeType(getSectionElement(section,"nodeType"));

		Factory.setConnectionType(getSectionElement(section,"connectionType"));

		Factory.setMessageType(getSectionElement(section,"messageType"));

		Factory.setPacketType(getSectionElement(section,"packetType"));

		sched = Factory.getScheduler(getSectionElement(section, "schedulerType"));

		print = Factory.getPrintStatistics(getSectionElement(section,"printStatisticsType"));

		in = Factory.getInput(getSectionElement(sectionInput, "inputType"));

		simTime = Factory.getSimulationTime(getSectionElement(section,"simulationTimeType"));

		messageGenerator = Factory.getMessageGenerator(getSectionElement(section,"messageGeneratorType"));

		if(getSectionElement(section,"useSocialData").equals("true"))
			useSocialData = true;
		else
			useSocialData = false;
	}

	public static void main(String[] args) {
		NodeGraph nodeGraph;
		try {
			readConfig();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: Config File");
			return;
		}

		try {
			nodeGraph = in.readNodeGraph(nodeGraphFileName);
			if(useSocialData)
				nodeGraph = in.readNodeGraphSocial(nodeGraphSocialFileName, nodeGraph);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: Graph file");
			return;
		}

		nodeGraph.initialize();
		simTime.setNodeGraph(nodeGraph);
		simTime.printTime();
		int count=0;
		System.out.print("[");
		for(Long i=simTime.getStartTime(); i != null; i=simTime.getNextTime()) {
			//System.out.println(i);
			if(i > ((simTime.getStopTime()-simTime.getStartTime()) * count / 50  + simTime.getStartTime())) {
				System.out.print("#");
				count++;
			}
			messageGenerator.generateMessages(nodeGraph);
			sched.scheduleMessages(nodeGraph);
			nodeGraph.prepareSendPackets();
			nodeGraph.sendPackets();
		}
		System.out.println("]");
		print.print(nodeGraph);
	}
}
