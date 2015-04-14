package node;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import simulationTime.SimulationTime;

import main.NodeGraph;

import aiTechniques.WekaWrapper;



public class NodeGaussianProcess extends Node {
	ArrayList<double[]> trainLogs = new ArrayList<double[]>();
	ArrayList<double[]> testLogs = new ArrayList<double[]>();
	int maxSeenNodes = 0;
	WekaWrapper ww = new WekaWrapper();
	boolean trainedNN = false;
	double limit = 0;
	static boolean needsInitialization = true;
	
	public NodeGaussianProcess(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
	}

	private int getWeekTrainLimit(SimulationTime simTime) {
		return (int) Math.ceil(simTime.getWeeks()*50.0/100.0);//50 here
	}

	private void addEventToLogs(ArrayList<Node> neighborNodes, SimulationTime simTime, ArrayList<double[]> logs) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(simTime.getCurrentTimeInMillis());
		double input[] = new double[4];
		input[0] = calendar.get(Calendar.DAY_OF_WEEK);
		input[1] = calendar.get(Calendar.HOUR_OF_DAY);
		for(Node n : neighborNodes) {
			input[2] = Integer.parseInt(n.name);
			logs.add(input);
		}
		input[3] = simTime.getCurrentWeek();
	}

	private void trainGaussianProcess(SimulationTime simTime) {
		ArrayList<Map.Entry<double[],Double>> trainData = new ArrayList<Map.Entry<double[],Double>>();
		int allEntrys[][] = new int[8][24];
		if(trainLogs.isEmpty())
			return;
		for(int j = 1; j <= 7; j++) {
			for(int i = 0; i < 24; i++) {
				double[] entry = new double[3];
				entry[0] = j;
				entry[1] = i;
				entry[2] = 0;
				for(int week = 0; week < getWeekTrainLimit(simTime); week++) {
					boolean seenNodes[][] = new boolean[simTime.getWeeks()][100];// this is bad
					for(double[] aux : trainLogs)
						if(j == aux[0] && aux[1] == i && aux[3] == week)
							seenNodes[week][(int)aux[2]] = true;
					for(int k = 0 ; k < 100; k++) // this is bad
						if(seenNodes[week][k]) {
							entry[2]++;
						}
				}
				entry[2] = entry[2]/(getWeekTrainLimit(simTime)-1);
				entry[2] = Math.ceil(entry[2]);
				allEntrys[j][i] = (int) entry[2];
				trainData.add(new AbstractMap.SimpleEntry<double[],Double>(entry, 1.0));
				double[] falseEntry = new double[3];
				falseEntry[0] = j;
				falseEntry[1] = i;
				falseEntry[2] = 0;
				if(entry[2]>maxSeenNodes)
					maxSeenNodes = (int)entry[2];
			}
		}
		for(int j = 1; j <= 7; j++) {
			for(int i = 0; i < 24; i++) {
				for(int k = 0; k <= maxSeenNodes; k++) {
					double falseInput[] = new double[3];
					falseInput[0] = j;
					falseInput[1] = i;
					if(k==allEntrys[j][i])
						continue;
					falseInput[2] = k;
					trainData.add(new AbstractMap.SimpleEntry<double[],Double>(falseInput, 0.0));
				}
			}
		}
		ww.train(ww.createTestSet(name, trainData));

		// Calculate maximum and median probability for the trained Gaussian process
		double max = 0;
		for(int j = 1; j <= 7; j++) {
			for(int i = 0; i < 24; i++) {
				for(int k = 0; k <= maxSeenNodes; k++) {
					double[] entry = new double[3];
					entry[0] = j;
					entry[1] = i;
					entry[2] = k;
					trainData = new ArrayList<Map.Entry<double[],Double>>();
					trainData.add(new AbstractMap.SimpleEntry<double[],Double>(entry, 1.0));
					double probability = ww.getProbability(ww.createTestSet(name,trainData));
					if (max < probability)
						max = probability;
				}
			}
		}
		limit = max / 4;
		trainedNN = true;
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph, ArrayList<Node> neighborNodes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(nodeGraph.simTime.getCurrentTimeInMillis());
		if(nodeGraph.simTime.getStartOfWeek(getWeekTrainLimit(nodeGraph.simTime)) > nodeGraph.simTime.getCurrentTimeInMillis())
				addEventToLogs(neighborNodes, nodeGraph.simTime, trainLogs);
		else {
			if (!trainedNN)
				trainGaussianProcess(nodeGraph.simTime);
			if (trainedNN)
				addEventToLogs(neighborNodes, nodeGraph.simTime, testLogs);
		}
	}

	public void printNN(SimulationTime simTime) {
		double[] x = new double[7*24];
		double[] y = new double[7*24];
		if(!trainedNN)
			return;
		for(int j = 1; j <= 7; j++) {
			for(int i=0; i < 24; i++) {
				System.out.print(j+"	"+i+"	");
				int sum = 0;
				int count = 0;
				double realSum = 0;
				for(int k = getWeekTrainLimit(simTime); k <= simTime.getWeeks(); k++) {
					boolean seenNodes[] = new boolean[100];
					for(double[] aux : testLogs)
						if(j == aux[0] && aux[1] == i && aux[3] == k)
							seenNodes[(int)aux[2]] = true;
					for(int l = 0 ; l < 100; l++) // this is bad
						if(seenNodes[l])
							realSum++;
				}
				realSum /= simTime.getWeeks() - getWeekTrainLimit(simTime);
				for(int k = 0; k <= maxSeenNodes; k++) {
					ArrayList<Map.Entry<double[],Double>> trainData = new ArrayList<Map.Entry<double[],Double>>();
					double []input = new double[3];
					input[0] = j;
					input[1] = i;
					input[2] = k;
					trainData.add(new AbstractMap.SimpleEntry<double[],Double>(input,0.0));
					double probability = ww.getProbability(ww.createTestSet(name,trainData));
					if(probability > limit) {
						sum += k;
						count++;
					}
				}
				y[(j-1)*24 + i] = realSum;
				if (count>0) {
					System.out.println((double)sum/(double)count+"	"+realSum);
					x[(j-1)*24 + i] = (double)sum/(double)count;
				}
				else {
					System.out.println(0.0+"	"+realSum);
					x[(j-1)*24 + i] = 0.0;
				}
			}
		}
		double[] rez = utils.Statistics.crossCorelate(x,y,10);
		for(int i=0;i<rez.length;i++)
			System.out.println(rez[i]);
		double rezult = utils.Statistics.chiSquareTest(x,y);
		System.out.println("Chi-Square Test = "+rezult);
	}

	@Override
	public void initialize(NodeGraph nodeGraph) {
	}
}
