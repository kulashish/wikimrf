package in.ac.iitb.cse.mrf.learn;

import in.ac.iitb.cse.mrf.data.AMNWeights;
import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;
import in.ac.iitb.cse.mrf.infer.KolmogorovGraph;
import in.ac.iitb.cse.mrf.util.GraphSparseMaker;
import in.ac.iitb.cse.mrf.util.MRFFeatures;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;

import Jama.Matrix;

@Deprecated
public class Learner {
	private static final int NODE_DIMENSION = 8;
	private static final int EDGE_DIMENSION = 6;
	private static final String BASE_PATH = "/Users/ashish/wiki/graphs/";
	private static final double C = 10d;
	private static double STEP_SIZE = 0.005d;
	private static final int MAX_ITER = 20;

	private MRFFeatures mrfFeatures;
	private AMNWeights weights;
	private State optimalState;
	private State currentState;

	private KolmogorovGraph kg;
	private double[] lastSG;

	public Learner(MRFFeatures features) {
		mrfFeatures = features;
		kg = new KolmogorovGraph();
		new GraphSparseMaker().makeSparse(mrfFeatures.getMrfGraph());
		kg.setMrfGraph(mrfFeatures.getMrfGraph());
	}

	public MRFFeatures getMrfFeatures() {
		return mrfFeatures;
	}

	public void setMrfFeatures(MRFFeatures mrfFeatures) {
		this.mrfFeatures = mrfFeatures;
	}

	public AMNWeights getWeights() {
		return weights;
	}

	public void learn() {
		double minFuncValue = Double.POSITIVE_INFINITY;
		if (null == weights) {
			weights = new AMNWeights(mrfFeatures.getNodeFeatureDim(),
					mrfFeatures.getEdgeFeatureDim());

		}

		long st = 0l;
		for (int i = 0; i < MAX_ITER; i++) {
			System.out
					.println("----------------------------------------------------");
			System.out.println("Iteration " + i);
			currentState = new State();
			currentState.setWeights(weights);
			currentState.setSg(lastSG);
			try {
				weights.print();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			st = System.currentTimeMillis();
			learningIteration();
			System.out.println("Iteration " + i + " completed ["
					+ (System.currentTimeMillis() - st) + "ms]");
			currentState.print();
			System.out
					.println("----------------------------------------------------");
		}
		System.out.println("printing optimal ");
		optimalState.print();
	}

	private void learningIteration() {
		System.out.println("Starting inference");
		long st = System.currentTimeMillis();
		inferenceStep();
		System.out.println("Done inference ["
				+ (System.currentTimeMillis() - st) + "ms]");
		currentState.setFunctionVal(functionValue());
		if (null == optimalState
				|| currentState.getFunctionVal() <= optimalState
						.getFunctionVal()) {
			optimalState = currentState;
			// STEP_SIZE *= 5;
		}
		/*
		 * if the function value is more than the optimal function value found
		 * till now, then average these weights with the optimal weights found
		 * till now and proceed
		 */
		else {
			// STEP_SIZE /= 5;
			System.out.println("function value has increased!! "
					+ currentState.getFunctionVal());
			double[] sg = averageSubgradient(currentState.getSg(),
					optimalState.getSg());
			lastSG = sg;
			int index = updateWeights(weights.getW0(), sg, 0);
			index = updateWeights(weights.getW1(), sg, index);
			index = updateWeights(weights.getW00(), sg, index);
			updateWeights(weights.getW11(), sg, index);

			// weights.average(optimalState.getWeights());
			currentState = new State();
			currentState.setSg(lastSG);
			currentState.setWeights(weights);
			inferenceStep();
			System.out.println("Ran inference with average weights");
			currentState.setFunctionVal(functionValue());
			System.out.println("new function value : "
					+ currentState.getFunctionVal());
			if (null == optimalState
					|| currentState.getFunctionVal() <= optimalState
							.getFunctionVal())
				optimalState = currentState;
		}
		System.out.println("Starting subgradient evaluation");
		st = System.currentTimeMillis();
		double[] sg = subgradient();
		System.out.println("Done subgradient evaluation ["
				+ (System.currentTimeMillis() - st) + "ms]");
		int index = updateWeights(weights.getW0(), sg, 0);
		index = updateWeights(weights.getW1(), sg, index);
		index = updateWeights(weights.getW00(), sg, index);
		updateWeights(weights.getW11(), sg, index);
		System.out.println("Updated weights");
	}

	private double[] averageSubgradient(double[] sg, double[] sg2) {
		double[] newsg = new double[sg.length];
		for (int i = 0; i < sg.length; i++)
			newsg[i] = (sg[i] + sg2[i]) / 2;
		return newsg;
	}

	private int updateEdgeWeights(double[] w, double[] source, int start) {
		for (int i = 0; i < w.length; i++) {
			w[i] -= STEP_SIZE * source[start++];
			if (w[i] < AMNWeights.MIN_WEIGHT) // Project to positive orthant
				w[i] = AMNWeights.MIN_WEIGHT;
		}
		return start;
	}

	private int updateNodeWeights(double[] w, double[] source, int start) {
		for (int i = 0; i < w.length; i++)
			w[i] -= STEP_SIZE * source[start++];

		return start;
	}

	private int updateWeights(double[] w, double[] source, int start) {
		for (int i = 0; i < w.length; i++) {
			w[i] -= STEP_SIZE * source[start++];
			if (w[i] < AMNWeights.MIN_WEIGHT) // Project to positive orthant
				w[i] = AMNWeights.MIN_WEIGHT;
		}
		return start;
	}

	private double functionValue() {
		WeightedGraph<WikiNode, WikiEdge> g = kg.getkGraph();
		double val = 0.0d;
		val += weights.normsqaure();
		Matrix weightVector = new Matrix(weights.asVector(), 1); // 1 x 2(nd+ne)
		Matrix longFeatureVector = null;
		double sum = g.vertexSet().size() - 2; // number of nodes excluding 's'
												// and 't'
		int agreements = 0;
		for (WikiEdge e : g.edgeSet()) {
			if (e.isTerminal() && e.isCut() && e.isDesiredCut())
				agreements++;
			longFeatureVector = new Matrix(longVector(g, e), 1); // 1 x 2(nd+ne)
			if (e.isDesiredCut() && !e.isCut())
				sum += weightVector.times(longFeatureVector.transpose())
						.getRowPackedCopy()[0];
			else if (!e.isDesiredCut() && e.isCut())
				sum -= weightVector.times(longFeatureVector.transpose())
						.getRowPackedCopy()[0];
		}
		currentState.setNumAgreements(agreements);
		return val += C * (sum - agreements);
	}

	private double[] subgradient() {
		WeightedGraph<WikiNode, WikiEdge> g = kg.getkGraph();
		boolean dcut = false;
		boolean pcut = false;
		double[] d = new double[2 * (mrfFeatures.getNodeFeatureDim() + mrfFeatures
				.getEdgeFeatureDim())];
		Arrays.fill(d, 0);
		Matrix signedSumVector = new Matrix(d, 1);
		System.out.println("initial signed sum vector");
		for (double f : signedSumVector.getColumnPackedCopy())
			System.out.print(f + ",");
		Matrix longFeatureVector = null;
		for (WikiEdge e : g.edgeSet()) {
			dcut = e.isDesiredCut();
			pcut = e.isCut();
			// System.out.print(g.getEdgeSource(e).getLabel() + "---"
			// + g.getEdgeTarget(e).getLabel() + "--desired " + dcut
			// + "--pred " + pcut);
			longFeatureVector = new Matrix(longVector(g, e), 1);
			if (pcut && !dcut) { // subtract long vector
				// System.out.print(g.getEdgeSource(e).getLabel() + "---"
				// + g.getEdgeTarget(e).getLabel());
				// System.out.println("---not in desired .. subtracting ");
				// for (double f : longFeatureVector.getColumnPackedCopy())
				// System.out.print(f + ",");
				// System.out.println();
				signedSumVector.minusEquals(longFeatureVector);
				// for (double f : signedSumVector.getColumnPackedCopy())
				// System.out.print(f + ",");
				// System.out.println();
			} else if (!pcut && dcut) { // add long vector
				// System.out.print(g.getEdgeSource(e).getLabel() + "---"
				// + g.getEdgeTarget(e).getLabel());
				// System.out.println("in desired .. adding");
				// for (double f : longFeatureVector.getColumnPackedCopy())
				// System.out.print(f + ",");
				// System.out.println();
				signedSumVector.plusEquals(longFeatureVector);
				// for (double f : signedSumVector.getColumnPackedCopy())
				// System.out.print(f + ",");
				// System.out.println();
			}
		}
		// System.out.println();
		// System.out.println("Signed sum :");
		// for (double f : signedSumVector.getColumnPackedCopy())
		// System.out.print(f + ",");
		// System.out.println();
		Matrix wVector = new Matrix(weights.asVector(), 1);
		Matrix sgVector = wVector.times(2).plus(signedSumVector.times(C));
		// Matrix sgVector = signedSumVector.times(C);
		// if (null != lastSG)
		// sgVector.plusEquals(lastSG).timesEquals(.5d);
		// lastSG = sgVector;
		double[] sg = sgVector.getColumnPackedCopy();
		lastSG = sg;
		System.out.println();
		System.out.print("Subgradient : ");
		for (double s : sg)
			System.out.print(s + ",");
		System.out.println();
		return sg;
	}

	private double[] longVector(WeightedGraph<WikiNode, WikiEdge> g, WikiEdge e) {
		int nd = mrfFeatures.getNodeFeatureDim();
		int ed = mrfFeatures.getEdgeFeatureDim();
		double[] vec = null;
		WikiNode node = null;
		if (e.isTerminal()) {
			node = Graphs.getOppositeVertex(g, e, Graphs.testIncidence(g, e,
					kg.getSource()) ? kg.getSource() : kg.getSink());
			vec = longVector(g, e, node);
		}

		// if (source.equals(kg.getSource()))
		// vec = longVector(g, target);
		// else if (target.equals(kg.getSink()))
		// vec = longVector(g, source);
		else {
			vec = new double[2 * (nd + ed)];
			Arrays.fill(vec, 0, 2 * nd, 0.0d);
			System.arraycopy(e.getfVector(), 0, vec, 2 * nd, ed);
			System.arraycopy(e.getfVector(), 0, vec, 2 * nd + ed, ed);
		}

		return vec;
	}

	private double[] longVector(WeightedGraph<WikiNode, WikiEdge> g,
			WikiEdge e, WikiNode n) {
		WikiNode sourceOrSink = Graphs.getOppositeVertex(g, e, n);
		int nd = mrfFeatures.getNodeFeatureDim();
		int ed = mrfFeatures.getEdgeFeatureDim();
		double[] vec = new double[2 * (nd + ed)];
		if (sourceOrSink.equals(kg.getSource())) {
			System.arraycopy(n.getfVector(), 0, vec, 0, nd);
			Arrays.fill(vec, nd, nd, 0.0d);
		} else {
			Arrays.fill(vec, 0, nd, 0.0d);
			System.arraycopy(n.getfVector(), 0, vec, nd, nd);
		}
		Arrays.fill(vec, 2 * nd, vec.length, 0.0d);
		return vec;
	}

	private void inferenceStep() {
		kg.setW0(weights.getW0());
		kg.setW1(weights.getW1());
		kg.setW00(weights.getW00());
		kg.setW11(weights.getW11());
		kg.createGraph();
		kg.graphCut(currentState);
	}

	public static void main(String args[]) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					BASE_PATH + "Mayan.graph"));
			WeightedGraph<WikiNode, WikiEdge> mrf = (WeightedGraph<WikiNode, WikiEdge>) ois
					.readObject();
			System.out.println("Number of nodes in the MRF :"
					+ mrf.vertexSet().size());
			System.out.println("Number of edges in the MRF :"
					+ mrf.edgeSet().size());

			// Sample
			// new SampleGraph().createWikiGraph(mrf);

			System.out.println("True nodes");
			for (WikiNode n : mrf.vertexSet())
				if (n.isIncut())
					System.out.print(n.getLabel() + ", ");
			System.out.println();

			MRFFeatures features = new MRFFeatures(mrf, NODE_DIMENSION,
					EDGE_DIMENSION);
			features.normalize();
			Learner learner = new Learner(features);
			learner.learn();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
