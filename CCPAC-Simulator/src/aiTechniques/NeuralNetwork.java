package aiTechniques;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class NeuralNetwork {

	private int inputSize;
	private int hiddenSize;
	private Random rand = new Random(11); // Keep random at 11 (CCCP standard)
	private static final int EPOCHS = 500;
	private static final double LEARNING_RATE = 0.3;
	private static final double EPS = 0.0004;
	private boolean trained = false;

	private double momentum = 0;
	private double[][] weightIH;
	private double[][] weightHO;
	private double[] outputHidden;

	public NeuralNetwork(int inputSize) {
		this.inputSize = inputSize + 1; //adding bias
		this.hiddenSize = inputSize * 2;

		weightIH = new double[this.inputSize][hiddenSize-1];
		weightHO = new double[hiddenSize][1];
		outputHidden = new double[hiddenSize];

		initializeWeights(weightIH, this.inputSize, this.hiddenSize - 1);
		initializeWeights(weightHO, this.hiddenSize, 1);
	}

	private double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

	private double sigmoidDerivative(double x) {
		return x * (1.0 - x);
	}

	private void initializeWeights(double[][] weights, int inSize, int outSize) {
		for (int i = 0; i < inSize; i++) {
			for (int j = 0; j < outSize; j ++) {
				weights[i][j] = rand.nextDouble();
			}
		}
	}

	private double feedForward(double[] input) {
		double sum = 0.0;
		outputHidden[0] = 1; //bias
		for (int j = 1; j < hiddenSize; j ++) {
			sum = 0.0;
			for ( int i = 0; i < inputSize; i ++) {
				sum += input[i] * weightIH[i][j - 1];
			}
			outputHidden[j] = sigmoid(sum);
		}

		sum = 0.0;
		for (int i = 0; i < hiddenSize; i++) {
			sum += outputHidden[i] * weightHO[i][0];
		}
		return sigmoid(sum);
	}

	private double backPropagation(double output, double target, double[] input, double[][] deltaIH, double[] deltaHO) {
		double deltaO = (target - output) * sigmoidDerivative(output);
		double[] deltaH = new double[hiddenSize];

		for (int i = 0; i < hiddenSize; i++) {
			deltaH[i] = weightHO[i][0] * deltaO * sigmoidDerivative(outputHidden[i]);
		}

		//update HO weights
		for (int i = 0; i < hiddenSize; i++) {
			deltaHO[i] = LEARNING_RATE * outputHidden[i] * deltaO + momentum * deltaHO[i];
			weightHO[i][0] += deltaHO[i];
		}

		//update IH weights
		//ignore bias from the hidden layer since it has no effect in the IH weights
		for (int i = 1; i < hiddenSize; i++) {
			for (int j = 0; j < inputSize; j++) {
				deltaIH[j][i - 1] = LEARNING_RATE * input[j] * deltaH[i] + momentum * deltaIH[j][i - 1];
				weightIH[j][i - 1] += deltaIH[j][i - 1];
			}
		}

		return (target - output) * (target - output) / 2.;
	}

	public double getMomentum() {
		return momentum;
	}

	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

	public double train(double[] input, double output) throws Exception {
		if (input == null || input.length != (inputSize - 1)) {
			throw new Exception("Input parameter error.");
		}
		double nOutput = 0.0;
		double[][] deltaIH = new double[inputSize][hiddenSize - 1];//automatically initialized with 0
		double[] deltaHO = new double[hiddenSize];

		trained = true;

		double[] inputWithBias = new double[input.length + 1];
		inputWithBias[0] = 1.0;
		System.arraycopy(input, 0, inputWithBias, 1, input.length);
		nOutput = feedForward(inputWithBias);

		return backPropagation(nOutput, output, inputWithBias, deltaIH, deltaHO);
	}

	public double train(ArrayList<Map.Entry<double[], Double>> data) throws RuntimeException {
		if (data == null) {
			throw new RuntimeException("Neural Network: Input parameter error.");
		}

		double error = 0.0;
		double nOutput = 0.0;
		double[][] deltaIH = new double[inputSize][hiddenSize - 1];
		double[] deltaHO = new double[hiddenSize];

		trained = true;

		for (int i = 0; i < EPOCHS; i++) {
			error = 0.0;
			for (int j = 0; j < data.size(); j ++) {
				double[] input = data.get(j).getKey();
				double[] inputWithBias = new double[input.length + 1];
				inputWithBias[0] = 1.0;
				System.arraycopy(input, 0, inputWithBias, 1, input.length);
				nOutput = feedForward(inputWithBias);
				error += backPropagation(nOutput, data.get(j).getValue(), inputWithBias, deltaIH, deltaHO);
			}
			if (error < EPS) {
				break;
			}
		}
		return error;
	}

	public double test(double[] input) throws RuntimeException {
		if (!trained) {
			throw new RuntimeException("Neural Network: Not trained.");
		}

		if (input == null || input.length != (inputSize - 1)) {
			throw new RuntimeException("Neural Network: Input parameter error.");
		}
		double[] inputWithBias = new double[input.length + 1];
		inputWithBias[0] = 1.0;
		System.arraycopy(input, 0, inputWithBias, 1, input.length);
		return feedForward(inputWithBias);
	}

	public void printWeights(double[][] weight, int n, int m) {
		for(int i = 0; i < n; i ++) {
			for(int j = 0; j < m; j++) {
				System.out.print(weight[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		NeuralNetwork nn = new NeuralNetwork(2);

		//training data
		ArrayList<Map.Entry<double[], Double>> data = new ArrayList<Map.Entry<double[], Double>>();
		double[] input1 = {1, 2};
		double[] input2 = {24, 24};
		double[] input3 = {12, 5};
		double[] input4 = {24, 24};
		data.add(new AbstractMap.SimpleEntry<double[], Double>(input1, new Double(1)));
		data.add(new AbstractMap.SimpleEntry<double[], Double>(input2, new Double(0)));
		data.add(new AbstractMap.SimpleEntry<double[], Double>(input3, new Double(1)));
		data.add(new AbstractMap.SimpleEntry<double[], Double>(input4, new Double(0)));

		try {
			double error = nn.train(data);
			System.out.println("Neural network error: " + error);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			double output = nn.test(input3);
			System.out.println("Neural network output: " + output);
			output = nn.test(input2);
			System.out.println("Neural network output: " + output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}