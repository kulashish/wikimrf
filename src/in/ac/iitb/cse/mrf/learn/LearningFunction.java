package in.ac.iitb.cse.mrf.learn;

import in.ac.iitb.cse.mrf.data.AMNWeights;
import in.ac.iitb.cse.mrf.data.AugmentedGraph;
import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.util.LearningProperties;
import in.ac.iitb.cse.mrf.util.MathHelper;

import java.util.Arrays;

import Jama.Matrix;

public class LearningFunction {
	protected static final double C = LearningProperties.getPenalty();
	protected AugmentedGraph[] graphs;
	protected AMNWeights weights;
	private int agreements = 0;
	private int numNodes = 0;

	public LearningFunction(AugmentedGraph[] augGraphs, AMNWeights w) {
		graphs = augGraphs;
		weights = w;
	}

	public double functionValue() {
		double val = 0.0d;
		val += weights.normsqaure();
		double sum = 0.0d;
		for (AugmentedGraph graph : graphs) {
			numNodes += graph.getNumBasenodes();
			for (WikiEdge e : graph.getEdges()) {
				if (e.isTerminal() && e.isCut() && e.isDesiredCut())
					agreements++;
				if (e.isDesiredCut() && !e.isCut())
					sum += MathHelper.dotProduct(weights.asVector(),
							graph.asLongFeatureVector(e));
				else if (!e.isDesiredCut() && e.isCut())
					sum -= MathHelper.dotProduct(weights.asVector(),
							graph.asLongFeatureVector(e));
			}
		}
		if (sum < 0.0d)
			sum = -sum;

		return val += C * (numNodes + sum - agreements);
	}

	public double[] subgradient() {
		boolean dcut = false;
		boolean pcut = false;
		double[] weightVector = weights.asVector();
		double[] d = new double[weightVector.length];
		Arrays.fill(d, 0);
		Matrix signedSumVector = new Matrix(d, 1);

		Matrix longFeatureVector = null;
		for (AugmentedGraph graph : graphs) {
			for (WikiEdge e : graph.getEdges()) {
				dcut = e.isDesiredCut();
				pcut = e.isCut();
				longFeatureVector = new Matrix(graph.asLongFeatureVector(e), 1);
				if (pcut && !dcut) { // subtract long vector
					signedSumVector.minusEquals(longFeatureVector);
				} else if (!pcut && dcut) { // add long vector
					signedSumVector.plusEquals(longFeatureVector);
				}
			}
		}
		Matrix wVector = new Matrix(weightVector, 1);
		Matrix sgVector = wVector.times(2).plus(signedSumVector.times(C));
		double[] sg = sgVector.getColumnPackedCopy();
		return sg;
	}

	public int getAgreements() {
		return agreements;
	}

	public int getNumNodes() {
		return numNodes;
	}

}
