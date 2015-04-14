package aiTechniques;

import java.util.ArrayList;
import java.util.Map;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.functions.supportVector.Puk;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

public class WekaWrapper {
	private static final double OMEGA=0.5;
	private static final double SIGMA=0.5;
	private static final double NOISE=1.0;
	GaussianProcesses gaussProcess = new GaussianProcesses();

	public static void addInstance(Instances data, double[] attrVals, Double output) {
		if (data == null || attrVals == null || output == null || data.numAttributes() != (attrVals.length + 1))
			return;

		Instance inst = new Instance(data.numAttributes());
		for(int i = 0; i < data.numAttributes() - 1; i++) {
			inst.setValue(data.attribute(i), attrVals[i]);
		}
		inst.setValue(data.attribute(data.numAttributes() -1), output.doubleValue());
		data.add(inst);
	}

	public static Instances createInstances(String relationName, ArrayList<String> attributeNames, int size) {
		if(attributeNames == null || attributeNames.size() == 0)
			return null;
		FastVector fvWekaAttributes = new FastVector(attributeNames.size());

		for(String name : attributeNames) {
			Attribute attribute = new Attribute(name);
			fvWekaAttributes.addElement(attribute);
		}

		Instances data = new Instances(relationName, fvWekaAttributes, size);
		data.setClassIndex(attributeNames.size() - 1);
		return data;
	}

	public Instances createTestSet(String name, ArrayList<Map.Entry<double[],Double>> trainData){
		ArrayList<String> attrNames = new ArrayList<String>();
		if(trainData == null)
			return null;
		for(int i=1;i<=trainData.get(0).getKey().length; i++)
			attrNames.add("a"+i);
		attrNames.add("predicted");
		Instances instances = createInstances(name, attrNames, trainData.size());

		for(Map.Entry<double[],Double> item : trainData)
			addInstance(instances, item.getKey(), item.getValue());

		return instances;
	}

	public void train(Instances trainSet) {
		gaussProcess.setDebug(false);
		Puk kernel = null;
		try {
			kernel = new Puk(trainSet, 250007, OMEGA, SIGMA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		gaussProcess.setKernel(kernel);
		gaussProcess.setNoise(NOISE);
		gaussProcess.setFilterType(new SelectedTag(GaussianProcesses.FILTER_NORMALIZE, GaussianProcesses.TAGS_FILTER));

		try {
			gaussProcess.buildClassifier(trainSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getProbability(Instances testSet) {
		Evaluation eval = null;
		try {
			eval = new Evaluation(testSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double pred = 0;
		try {
			pred = eval.evaluateModelOnce(gaussProcess, testSet.instance(0));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pred;
	}

	/*
	public static void main(String[] args) {
		WekaWrapper ww = new WekaWrapper();
		ArrayList<Map.Entry<double[],Double>> trainData = new ArrayList<Map.Entry<double[],Double>>(); 
		for(int i = 1; i <= 24; i++) {
			double[] data = new double[1];
			data[0] = i;
			if(i==10 || i==15 || i==17 || i==20)
				trainData.add(new AbstractMap.SimpleEntry<double[],Double>(data, 1.0));
			else
				trainData.add(new AbstractMap.SimpleEntry<double[],Double>(data, 0.0));
		}
		ww.train(ww.createTestSet("test",trainData));
		trainData = new ArrayList<Map.Entry<double[],Double>>();
		double[] data = new double[1];
		data[0] = 16;
		trainData.add(new AbstractMap.SimpleEntry<double[],Double>(data, 1.0));
		System.out.println(ww.getProbability(ww.createTestSet("test",trainData)));
	}
	*/
}
