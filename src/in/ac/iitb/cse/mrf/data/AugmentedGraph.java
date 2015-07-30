package in.ac.iitb.cse.mrf.data;

import in.ac.iitb.cse.mrf.util.MathHelper;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class AugmentedGraph {
	private static Logger logger = Logger.getLogger("in.ac.iitb.cse.mrf.data");
	private WeightedGraph<WikiNode, WikiEdge> graph;
	private WikiNode source;
	private WikiNode sink;
	private int nodeDim;
	private int edgeDim;
	private int numLable0Nodes = 0;
	private int numLable1Nodes = 0;
	private int numTerminalEdges = 0;
	private String name;

	public String getName() {
		return name;
	}

	public AugmentedGraph(MRFGraph mrfGraph, AMNWeights weights) {
		name = mrfGraph.getGraphName();
		nodeDim = weights.getW0().length;
		edgeDim = weights.getW00().length;

		graph = new SimpleDirectedWeightedGraph<WikiNode, WikiEdge>(
				WikiEdge.class);
		Graphs.addAllVertices(graph, mrfGraph.getNodes());
		Graphs.addAllEdges(graph, mrfGraph.getGraph(), mrfGraph.getEdges());

		source = new WikiNode("s");
		source.setIncut(false);
		sink = new WikiNode("t");
		sink.setIncut(true);
		graph.addVertex(source); // source
		graph.addVertex(sink); // sink

		double[] zeroEdgeFvector = new double[edgeDim];
		Arrays.fill(zeroEdgeFvector, 0.0d);
		for (WikiNode v : mrfGraph.getNodes()) {
			// if (v.isPredcut())
			// v.setIncut(true); // clamp the node to true
			if (v.isIncut())
				numLable1Nodes += v.getNumOccur();
			else
				numLable0Nodes += v.getNumOccur();
			
			addTerminalEdge(source, v,
					MathHelper.dotProduct(weights.getW0(), v.getfVector()),
					zeroEdgeFvector, v.isIncut());
			/*
			 * clamp node to the sink side if pred is set to true
			 */
			// addTerminalEdge(v, sink, v.isPredcut() ? Double.POSITIVE_INFINITY
			// : MathHelper.dotProduct(weights.getW1(), v.getfVector()),
			// zeroEdgeFvector, !v.isIncut());
			addTerminalEdge(v, sink,
					MathHelper.dotProduct(weights.getW1(), v.getfVector()),
					zeroEdgeFvector, !v.isIncut());
		}

		WikiNode es = null;
		WikiNode et = null;
		for (WikiEdge e : mrfGraph.getEdges()) {
			es = graph.getEdgeSource(e);
			et = graph.getEdgeTarget(e);
			graph.setEdgeWeight(
					e,
					MathHelper.dotProduct(
							MathHelper.addVectors(weights.getW00(),
									weights.getW11()), e.getfVector()));
			augmentTerminalEdge(source, es,
					MathHelper.dotProduct(weights.getW00(), e.getfVector()),
					e.getfVector());
//			augmentTerminalEdge(source, et,
//					MathHelper.dotProduct(weights.getW00(), e.getfVector()),
//					e.getfVector());

			augmentTerminalEdge(et, sink,
					MathHelper.dotProduct(weights.getW11(), e.getfVector()),
					e.getfVector());
//			augmentTerminalEdge(es, sink,
//					MathHelper.dotProduct(weights.getW11(), e.getfVector()),
//					e.getfVector());


			e.setDesiredCut(es.isIncut() != et.isIncut());
		}

		/*
		 * Normalize terminal edge feature vectors and set their weights
		 */
		// double[][] terminalEdgeFeatureVectors = new
		// double[numTerminalEdges][edgeDim];
		// int index = 0;
		// for (WikiEdge e : getEdges())
		// if (e.isTerminal())
		// terminalEdgeFeatureVectors[index++] = e.getfVector();
		// MathHelper.normalizeVectors(terminalEdgeFeatureVectors);
		for (WikiEdge e : graph.edgesOf(source))
			graph.setEdgeWeight(
					e,
					graph.getEdgeWeight(e)
							+ MathHelper.dotProduct(weights.getW00(),
									e.getfVector()));
		for (WikiEdge e : graph.edgesOf(sink))
			// if (graph.getEdgeWeight(e) < Double.POSITIVE_INFINITY)
			graph.setEdgeWeight(
					e,
					graph.getEdgeWeight(e)
							+ MathHelper.dotProduct(weights.getW11(),
									e.getfVector()));
	}

	public int getNumLable0Nodes() {
		return numLable0Nodes;
	}

	public int getNumLable1Nodes() {
		return numLable1Nodes;
	}

	private void augmentTerminalEdge(WikiNode edgeSource, WikiNode edgeTarget,
			double weight, double[] fVector) {
		WikiEdge edge = graph.getEdge(edgeSource, edgeTarget);
		// graph.setEdgeWeight(edge, graph.getEdgeWeight(edge) + weight);
		edge.setfVector(MathHelper.addVectors(edge.getfVector(), fVector));
	}

	public void addTerminalEdge(WikiNode edgeSource, WikiNode edgeTarget,
			double weight, double[] features, boolean cut) {
		WikiEdge edge = null;

		edge = graph.addEdge(edgeSource, edgeTarget);

		if (null != edge) {
			numTerminalEdges++;
			edge.setTerminal(true);
			edge.setfVector(features);
			edge.setDesiredCut(cut);
			graph.setEdgeWeight(edge, weight);
		}
	}

	public SimpleDirectedWeightedGraph<WikiNode, WikiEdge> getGraph() {
		return (SimpleDirectedWeightedGraph<WikiNode, WikiEdge>) graph;
	}

	public WikiNode getSource() {
		return source;
	}

	public WikiNode getSink() {
		return sink;
	}

	public Set<WikiEdge> getEdges() {
		return graph.edgeSet();
	}

	public Set<WikiNode> getNodes() {
		return graph.vertexSet();
	}

	public int getNumBasenodes() {
		return getNodes().size() - 2;
	}

	public double[] asLongFeatureVector(WikiEdge e) {
		double[] vec = new double[2 * (nodeDim + edgeDim)];
		if (e.isTerminal()) {
			WikiNode node = Graphs.getOppositeVertex(graph, e, Graphs
					.testIncidence(graph, e, getSource()) ? getSource()
					: getSink());
			vec = asLongFeatureVector(e, node);
		} else {
			Arrays.fill(vec, 0, 2 * nodeDim, 0.0d);
			System.arraycopy(e.getfVector(), 0, vec, 2 * nodeDim, edgeDim);
			System.arraycopy(e.getfVector(), 0, vec, 2 * nodeDim + edgeDim,
					edgeDim);
		}
		return vec;
	}

	private double[] asLongFeatureVector(WikiEdge e, WikiNode n) {
		WikiNode sourceOrSink = Graphs.getOppositeVertex(graph, e, n);
		double[] vec = new double[2 * (nodeDim + edgeDim)];
		Arrays.fill(vec, 0.0d);
		if (sourceOrSink.equals(getSource())) {
			System.arraycopy(n.getfVector(), 0, vec, 0, nodeDim);
			System.arraycopy(e.getfVector(), 0, vec, 2 * nodeDim, edgeDim);
		} else {
			System.arraycopy(n.getfVector(), 0, vec, nodeDim, nodeDim);
			System.arraycopy(e.getfVector(), 0, vec, 2 * nodeDim + edgeDim,
					edgeDim);
		}
		return vec;
	}

	public WikiNode connectsSource(WikiEdge e) {
		return Graphs.testIncidence(graph, e, source) ? graph.getEdgeTarget(e)
				: null;
	}

	public WikiNode connectsSink(WikiEdge e) {
		return Graphs.testIncidence(graph, e, sink) ? graph.getEdgeSource(e)
				: null;
	}

	public int sourceOrSinkDisagreement(WikiEdge e) {
		WikiNode n1 = graph.getEdgeSource(e);
		WikiNode n2 = graph.getEdgeTarget(e);
		return n1.isIncut() && !n1.isPredcut() || n2.isIncut()
				&& !n2.isPredcut() ? 1 : 0;
	}

	public void print() {
		logger.log(Level.INFO, "Printing nodes...");
		StringBuilder builder = null;
		for (WikiNode n : getNodes()) {
			if (n.equals(source) || n.equals(sink))
				continue;
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
			for (double f : asLongFeatureVector(e))
				builder.append(f).append(':');
			logger.log(Level.INFO, source.getLabel() + "--" + target.getLabel()
					+ "::" + builder.toString() + "::" + graph.getEdgeWeight(e));
		}
	}
}
