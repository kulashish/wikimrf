package in.ac.iitb.cse.mrf.data;

import in.ac.iitb.cse.mrf.util.LearningProperties;
import in.ac.iitb.cse.mrf.util.MathHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

public class MRFGraph {
	private static Logger logger = Logger.getLogger("in.ac.iitb.cse.mrf.data");
	private static final double MIN_NORM = 0d;
	private WeightedGraph<WikiNode, WikiEdge> graph;
	private String graphName;

	public MRFGraph() {

	}

	public MRFGraph(WeightedGraph<WikiNode, WikiEdge> g) {
		graph = g;
	}

	public WeightedGraph<WikiNode, WikiEdge> getGraph() {
		return graph;
	}

	public void setGraph(WeightedGraph<WikiNode, WikiEdge> graph) {
		this.graph = graph;
	}

	public static MRFGraph loadFromFile(File trainingFile) throws IOException,
			ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				trainingFile));
		MRFGraph mrf = new MRFGraph(
				(WeightedGraph<WikiNode, WikiEdge>) ois.readObject());
		ois.close();
		mrf.setGraphName(trainingFile.getName());
		return mrf;
	}

	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public WeightedGraph<WikiNode, WikiEdge> copy() {
		return (SimpleWeightedGraph<WikiNode, WikiEdge>) ((SimpleWeightedGraph<WikiNode, WikiEdge>) graph)
				.clone();
	}

	public Set<WikiNode> getNodes() {
		return graph.vertexSet();
	}

	public Set<WikiEdge> getEdges() {
		return graph.edgeSet();
	}

	public void normalizeFeatures(int nodeFeatureDim, int edgeFeatureDim) {
		double[][] nodeFeatureVectors = new double[getNodes().size()][nodeFeatureDim];
		double[][] edgeFeatureVectors = new double[getEdges().size()][edgeFeatureDim];
		int index = 0;
		for (WikiNode n : getNodes())
			nodeFeatureVectors[index++] = n.getfVector();
		MathHelper.normalizeVectors(nodeFeatureVectors);

		index = 0;
		if (null != getEdges() && getEdges().size() > 0) {
			for (WikiEdge e : getEdges())
				edgeFeatureVectors[index++] = e.getfVector();
			MathHelper.normalizeVectors(edgeFeatureVectors);
		}
	}

	public void makeSparse() {
//		System.out.println("before making sparse it has " + getEdges().size()
//				+ "edges");
		/*
		 * Filter out nodes
		 */
		WeightedGraph<WikiNode, WikiEdge> newgraph = (SimpleWeightedGraph<WikiNode, WikiEdge>) ((SimpleWeightedGraph<WikiNode, WikiEdge>) graph)
				.clone();
		for (WikiNode n : newgraph.vertexSet())
			if (LearningProperties.isNodeFulltext()
					&& n.getfVector()[NodeFeature.FULL_COSINE.ordinal()] < LearningProperties
							.getNodeFulltextThreshold())
				graph.removeVertex(n);

		/*
		 * Filter out edges
		 */
		newgraph = (SimpleWeightedGraph<WikiNode, WikiEdge>) ((SimpleWeightedGraph<WikiNode, WikiEdge>) graph)
				.clone();
		for (WikiEdge e : newgraph.edgeSet()) {
			if (isLowEdge(e))
				graph.removeEdge(e);
		}
//		System.out.println("after making sparse it has " + getEdges().size()
//				+ "edges");
	}

	private boolean isLowEdge(WikiEdge e) {
		return MathHelper.vectorNormSquare(e.getfVector()) <= MIN_NORM
				|| (LearningProperties.isCategory() && e.getfVector()[EdgeFeature.CATEGORY] < LearningProperties
						.getCategoryThreshold())
				|| (LearningProperties.isOutlink() && e.getfVector()[EdgeFeature.OUTLINK] < LearningProperties
						.getOutlinkThreshold())
				|| (LearningProperties.isInlink() && e.getfVector()[EdgeFeature.INLINK] < LearningProperties
						.getInlinkThreshold())
				|| (LearningProperties.isFrequent() && e.getfVector()[EdgeFeature.CONTEXT_FREQUENT] < LearningProperties
						.getFrequentThreshold())
				|| (LearningProperties.isSynopsis() && e.getfVector()[EdgeFeature.CONTEXT_SYNOPSIS] < LearningProperties
						.getSynopsisThreshold())
				|| (LearningProperties.isSynvbadj() && e.getfVector()[EdgeFeature.CONTEXT_SYNOPSIS_VBADJ] < LearningProperties
						.getSynvbadjThreshold())
				|| (LearningProperties.isFulltext() && e.getfVector()[EdgeFeature.CONTEXT_FULLTEXT] < LearningProperties
						.getFulltextThreshold());

	}

	public void printTrueNodes() {
		logger.log(Level.INFO, "Printing true nodes...");
		StringBuilder builder = new StringBuilder();
		for (WikiNode n : getNodes())
			if (n.isIncut())
				builder.append(n.getLabel()).append(',');
		logger.log(Level.INFO, builder.toString());
	}

	public void print() {
		logger.log(Level.INFO, "Printing nodes...");
		StringBuilder builder = null;
		for (WikiNode n : getNodes()) {
			builder = new StringBuilder();
			for (double f : n.getfVector())
				builder.append(f).append(':');
			logger.log(Level.INFO, n.getLabel() + "::" + n.isIncut() + "::"
					+ builder.toString());
		}
		WikiNode source = null;
		WikiNode target = null;
		for (WikiEdge e : getEdges()) {
			source = graph.getEdgeSource(e);
			target = graph.getEdgeTarget(e);
			builder = new StringBuilder();
			for (double f : e.getfVector())
				builder.append(f).append(':');
			logger.log(Level.INFO, source.getLabel() + "--" + target.getLabel()
					+ "::" + builder.toString());
		}
	}
}
