package in.ac.iitb.cse.mrf.learn;

import in.ac.iitb.cse.mrf.data.AMNWeights;
import in.ac.iitb.cse.mrf.data.AugmentedGraph;
import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;
import in.ac.iitb.cse.mrf.util.LearningProperties;
import in.ac.iitb.cse.mrf.util.WikiEdgeProvider;
import in.ac.iitb.cse.mrf.util.WikiNodeProvider;

import java.io.FileWriter;
import java.util.Arrays;

import org.jgrapht.WeightedGraph;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.VertexNameProvider;

import Jama.Matrix;

public class SeparatePenaltyLearningFunction extends LearningFunction {
	private static final double[] penalty = {
			LearningProperties.getLabel0Penalty(),
			LearningProperties.getLabel1Penalty() };
	// private static final double C1 = LearningProperties.getLabel1Penalty();
	private int totalLabel1Nodes = 0;
	private int totalLabel0Nodes = 0;
	private int label0Agreements = 0;
	private int label1Agreements = 0;
	private double[] pen = null;

	public SeparatePenaltyLearningFunction(AugmentedGraph[] augGraphs,
			AMNWeights w) {
		this(augGraphs, w, null);
	}

	public SeparatePenaltyLearningFunction(AugmentedGraph[] augGraphs,
			AMNWeights w, double[] p) {
		super(augGraphs, w);
		pen = null != p ? p : penalty;
	}

	public int getTotalLabel1Nodes() {
		return totalLabel1Nodes;
	}

	public int getTotalLabel0Nodes() {
		return totalLabel0Nodes;
	}

	public int getLabel0Agreements() {
		return label0Agreements;
	}

	public int getLabel1Agreements() {
		return label1Agreements;
	}

	public double functionValue() {
		double val = 0.0d;
		val += weights.normsqaure();
		double sum = 0.0d;
		WikiNode node = null;
		for (AugmentedGraph graph : graphs) {
			totalLabel0Nodes += graph.getNumLable0Nodes();
			totalLabel1Nodes += graph.getNumLable1Nodes();
			for (WikiEdge e : graph.getEdges()) {
				if (e.isTerminal() && e.isCut() && e.isDesiredCut())
					if (null != (node = graph.connectsSource(e)))
						label1Agreements += node.getNumOccur();
					else if (null != (node = graph.connectsSink(e)))
						label0Agreements += node.getNumOccur();
				// one of the nodes(source or sink side) labeled incorrectly
				if (e.isDesiredCut() != e.isCut())
					// sum += pen[graph.sourceOrSinkDisagreement(e)]
					// * MathHelper.dotProduct(weights.asVector(),
					// graph.asLongFeatureVector(e));
					sum += pen[graph.sourceOrSinkDisagreement(e)]
							* graph.getGraph().getEdgeWeight(e);
				// one of the nodes(source or sink side) labeled incorrectly
//				else if (!e.isDesiredCut() && e.isCut())
					// sum -= pen[graph.sourceOrSinkDisagreement(e)]
					// * MathHelper.dotProduct(weights.asVector(),
					// graph.asLongFeatureVector(e));
//					sum -= pen[graph.sourceOrSinkDisagreement(e)]
//							* graph.getGraph().getEdgeWeight(e);
			}
		}
//		if (sum < 0.0d)
//			sum = -sum;

		return val += sum + pen[0] * (totalLabel0Nodes - label0Agreements)
				+ pen[1] * (totalLabel1Nodes - label1Agreements);
	}

	private void serializeGraph(WeightedGraph<WikiNode, WikiEdge> graph,
			String name) {

		// Also export it to a text file in GraphML format
		VertexNameProvider<WikiNode> vertexProvider = new WikiNodeProvider();
		EdgeNameProvider<WikiEdge> edgeProvider = new WikiEdgeProvider();
		try {
			new GraphMLExporter<WikiNode, WikiEdge>(vertexProvider,
					vertexProvider, edgeProvider, edgeProvider).export(
					new FileWriter(name + ".graphml"), graph);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
//			serializeGraph(graph.getGraph(), "/Users/ashish/wiki/train_graphs/"
//					+ graph.getName() + "_sub");
			for (WikiEdge e : graph.getEdges()) {
				dcut = e.isDesiredCut();
				pcut = e.isCut();
				longFeatureVector = new Matrix(graph.asLongFeatureVector(e), 1);
				if (pcut && !dcut) { // subtract long vector
					signedSumVector.minusEquals(longFeatureVector
							.times(pen[graph.sourceOrSinkDisagreement(e)]));
				} else if (!pcut && dcut) { // add long vector
					signedSumVector.plusEquals(longFeatureVector
							.times(pen[graph.sourceOrSinkDisagreement(e)]));
				}
			}
		}
		Matrix wVector = new Matrix(weightVector, 1);
		Matrix sgVector = wVector.times(2).plus(signedSumVector);
		double[] sg = sgVector.getColumnPackedCopy();
		return sg;
	}
}
