package node;

import java.util.ArrayList;

import main.NodeGraph;

public class NodeMobileCloudMarket extends Node {

	boolean isServiceProvider;
	int count = 0;
	
	// seller
	final double P_MIN = 0.0;
	final double P_MAX = 500.0;
	final double ALPHA = 0.1;
	final int N = 10;
	double[] pop = new double[N];
	double[] fitness = new double[N];
	boolean inInitial = true;
	long lastTime = -1;
	int iPop = -1;
	
	//buyer
	int battery=100;
	
	
	public NodeMobileCloudMarket(String name, Boolean canSendMessages) {
		super(name, canSendMessages);
		isServiceProvider = main.Main.random.nextBoolean();
	}

	@Override
	public void prepareSendPackets(NodeGraph nodeGraph,	ArrayList<Node> neighborNodes) {
		
		count++;
		//if(count>500)
		//	return;
		double minOffer = 200;//-5 * battery + 500;
		
		NodeMobileCloudMarket minOfferNode = null;
		if(!isServiceProvider) {
			for(Node neighborNode:neighborNodes) {
				if(((NodeMobileCloudMarket)neighborNode).isServiceProvider) {
					double aux = ((NodeMobileCloudMarket)neighborNode).getOffer(nodeGraph);
					if(minOffer > aux) {
						minOffer = aux;
						minOfferNode = ((NodeMobileCloudMarket)neighborNode);
					}
				}
			}
			for(Node neighborNode:neighborNodes) {
				if(((NodeMobileCloudMarket)neighborNode).isServiceProvider) {
					if(neighborNode.equals(minOfferNode)) {
						minOfferNode.buy(1);
					} else {
						((NodeMobileCloudMarket)neighborNode).buy(0);
					}
				}
			}
		}
		battery-=0.05;
	}
	
	public double getOffer(NodeGraph nodeGraph) {
		if(inInitial) {
			if(lastTime != nodeGraph.simTime.getCurrentTime()) {
				lastTime = nodeGraph.simTime.getCurrentTime();
				iPop++;
				if(iPop == pop.length) {
					inInitial = false;
					return getOffer(nodeGraph);
				}
				return pop[iPop];
			} else {
				return pop[iPop];
			}
		} else {
			ArrayList<Integer> options = new ArrayList<Integer>();
			for(int i = 0; i < N; i++)
				options.add(i);

			int x1 = options.get(main.Main.random.nextInt(N));
			options.remove(options.indexOf(x1));
			int x2 = options.get(main.Main.random.nextInt(N-1));
			options.remove(options.indexOf(x2));
			int x3 = options.get(main.Main.random.nextInt(N-2));
			options.remove(options.indexOf(x3));
			int x4 = options.get(main.Main.random.nextInt(N-3));
			options.remove(options.indexOf(x4));
			int min, max;
			
			min = x1<x2?x1:x2;
			max = x1>x2?x1:x2;
			int c1 = (main.Main.random.nextInt(10)==0) ? min : max ;
			min = x3<x4?x3:x4;
			max = x3>x4?x3:x3;
			int c2 = (main.Main.random.nextInt(10)==0) ? min : max ;

			double o = (pop[c1] + pop[c2]) / 2;
			
			o += (main.Main.random.nextDouble() / 5.0) - 0.1;
			
			double minFitness = P_MAX;

			if(minFitness > fitness[x1]) {
				minFitness = fitness[x1];
				iPop = x1;
			}
			if(minFitness > fitness[x2]) {
				minFitness = fitness[x2];
				iPop = x2;
			}
			if(minFitness > fitness[x3]) {
				minFitness = fitness[x3];
				iPop = x3;
			}
			if(minFitness > fitness[x4]) {
				minFitness = fitness[x4];
				iPop = x4;
			}
			
			pop[iPop] = o;

			return o;
		}
	}
	
	public void buy(double ammount) {
		fitness[iPop] = pop[iPop] * ammount;
		//TODO add to processing queue and prepare for return
	}

	@Override
	public void initialize(NodeGraph nodeGraph) {
		if(isServiceProvider) {
			for(int i = 0; i < pop.length; i++) {
				pop[i] = main.Main.random.nextFloat() * P_MAX;
			}
		}
	}
	
	@Override
	public String toString() {
		String s="";
		if(isServiceProvider) {
			for(int i = 0; i < N; i++)
				s+="\t"+pop[i];
			return name+" "+isServiceProvider+" "+count+" "+s;
		} else { 
			return name+" "+isServiceProvider+" "+count;
		}
	}
}
