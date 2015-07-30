package in.ac.iitb.cse.mrf.util;

import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;

import java.util.Arrays;

import org.jgrapht.WeightedGraph;

public class MRFFeatures {

	private WeightedGraph<WikiNode, WikiEdge> mrfGraph;
	private int numNodes;
	private int numEdges;
	private int nodeFeatureDim;
	private int edgeFeatureDim;

	public int getNodeFeatureDim() {
		return nodeFeatureDim;
	}

	public int getEdgeFeatureDim() {
		return edgeFeatureDim;
	}

	public WeightedGraph<WikiNode, WikiEdge> getMrfGraph() {
		return mrfGraph;
	}

	public MRFFeatures(WeightedGraph<WikiNode, WikiEdge> g, int nd, int ed) {
		mrfGraph = g;
		numNodes = g.vertexSet().size();
		numEdges = g.edgeSet().size();
		nodeFeatureDim = nd;
		edgeFeatureDim = ed;
	}

	public double[][] asFeatureMatrix() {
		double[][] mrfFeatureMatrix = new double[numNodes + numEdges][2 * (nodeFeatureDim + edgeFeatureDim)];
		double[] featureVector = null;
		int fIndex = 0;
		for (WikiNode n : mrfGraph.vertexSet()) {
			featureVector = n.getfVector();
			System.arraycopy(featureVector, 0, mrfFeatureMatrix[fIndex], 0,
					nodeFeatureDim);
			System.arraycopy(featureVector, 0, mrfFeatureMatrix[fIndex],
					nodeFeatureDim, nodeFeatureDim);
			Arrays.fill(mrfFeatureMatrix[fIndex], 2 * nodeFeatureDim,
					mrfFeatureMatrix[fIndex].length, 0.0d);
			fIndex++;
		}
		for (WikiEdge e : mrfGraph.edgeSet()) {
			featureVector = e.getfVector();
			Arrays.fill(mrfFeatureMatrix[fIndex], 0, 2 * nodeFeatureDim, 0.0d);
			System.arraycopy(featureVector, 0, mrfFeatureMatrix[fIndex],
					2 * nodeFeatureDim, edgeFeatureDim);
			System.arraycopy(featureVector, 0, mrfFeatureMatrix[fIndex], 2
					* nodeFeatureDim + edgeFeatureDim, edgeFeatureDim);
			fIndex++;
		}
		return mrfFeatureMatrix;
	}

	public void normalize() {
		double max[] = new double[nodeFeatureDim];
		double[] f = null;
		for (WikiNode n : mrfGraph.vertexSet()) {
			f = n.getfVector();
			for (int i = 0; i < nodeFeatureDim; i++)
				if (f[i] > max[i])
					max[i] = f[i];
		}
		for (WikiNode n : mrfGraph.vertexSet()) {
			f = n.getfVector();
			for (int i = 0; i < nodeFeatureDim && max[i] > 0; i++)
				f[i] /= max[i];
		}
		max = new double[edgeFeatureDim];
		for (WikiEdge e : mrfGraph.edgeSet()) {
			f = e.getfVector();
			for (int i = 0; i < edgeFeatureDim; i++)
				if (f[i] > max[i])
					max[i] = f[i];
		}
		for (WikiEdge e : mrfGraph.edgeSet()) {
			f = e.getfVector();
			for (int i = 0; i < edgeFeatureDim; i++)
				f[i] /= max[i];
		}
	}
}
