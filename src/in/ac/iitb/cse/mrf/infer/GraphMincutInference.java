package in.ac.iitb.cse.mrf.infer;

import in.ac.iitb.cse.mrf.data.AugmentedGraph;
import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;
import in.ac.iitb.cse.mrf.util.MathHelper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgrapht.Graphs;
import org.jgrapht.alg.MinSourceSinkCut;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class GraphMincutInference {
	private static Logger logger = Logger.getLogger("in.ac.iitb.cse.mrf.infer");

	private AugmentedGraph graph;
	private MinSourceSinkCut<WikiNode, WikiEdge> stcutter;

	public GraphMincutInference() {

	}

	public GraphMincutInference(AugmentedGraph graph) {
		this.graph = graph;
		stcutter = new MinSourceSinkCut<WikiNode, WikiEdge>(graph.getGraph());
	}

	public int getSize(Set<WikiNode> nodes) {
		int size = 0;
		for (WikiNode n : nodes)
			size += n.getNumOccur();
		return size;
	}

	public Set<WikiNode> getSourcePartition() {
		return stcutter.getSourcePartition();
	}

	public Set<WikiNode> getSinkPartition() {
		return stcutter.getSinkPartition();
	}

	public void computeMincut() {
		stcutter.computeMinCut(graph.getSource(), graph.getSink());
//		 for (WikiEdge e : stcutter.getCutEdges()) {
//		 // System.out.println(graph.getGraph().getEdgeSource(e).getLabel()
//		 // + " --> " + graph.getGraph().getEdgeTarget(e).getLabel());
//		 e.setCut(true);
//		 }
		for (WikiNode n : stcutter.getSourcePartition())
			n.setPredcut(false);
		for (WikiNode n : stcutter.getSinkPartition())
			n.setPredcut(true);
		for (WikiEdge e : graph.getEdges())
			e.setCut(graph.getGraph().getEdgeSource(e).isPredcut() != graph
					.getGraph().getEdgeTarget(e).isPredcut());
//		getCutEdges(stcutter.getSourcePartition());
	}

	public Set<WikiEdge> getCutEdges(Set<WikiNode> minCut) {
		if (minCut == null)
			return null;
		Set<WikiEdge> cutEdges = new HashSet<WikiEdge>();
		WikiNode opposite = null;
		for (WikiNode vertex : minCut) {
			System.out.println("checking for " + vertex.getLabel());
			for (WikiEdge edge : graph.getGraph().outgoingEdgesOf(vertex)) {
				opposite = Graphs.getOppositeVertex(graph.getGraph(), edge,
						vertex);
				System.out.println("outgoing edge to - " + opposite.getLabel()
						+ "(" + opposite.isPredcut() + ")");
				if (!minCut.contains(Graphs.getOppositeVertex(graph.getGraph(),
						edge, vertex)))
					cutEdges.add(edge);
			}
		}
		return Collections.unmodifiableSet(cutEdges);
	}

	public void printPartitions() {
		logger.log(Level.INFO, "Printing source partition...");
		printPartition(getSourcePartition());
		logger.log(Level.INFO, "Printing sink partition...");
		printPartition(getSinkPartition());
	}

	public void printPartition(Set<WikiNode> partition) {
		StringBuilder builder = new StringBuilder();
		for (WikiNode n : partition)
			builder.append(n.getLabel()).append(',');
		logger.log(Level.INFO, builder.toString());
	}

	public static void main(String[] args) {
		SimpleDirectedWeightedGraph<WikiNode, WikiEdge> graph = new SimpleDirectedWeightedGraph<WikiNode, WikiEdge>(
				WikiEdge.class);
		WikiNode source = new WikiNode("s");
		WikiNode sink = new WikiNode("t");
		WikiNode eating = new WikiNode("eating");
		WikiNode eating_disorder = new WikiNode("eating disorder");
		double[] ed_feature = new double[] { 0.0038429651304559866,
				0.2526997840172786, 0.0, 0.5593000054359436, 0.285628574177472,
				0.0833333358168602, 1.0, 0.6279985182555846, 0.8716593538579024 };
		double[] eating_feature = new double[] { 0.001502072665203609,
				0.06911447084233262, 0.0, 1.0, 0.285628574177472,
				0.6666666865348816, 0.8738378831644158, 1.0, 0.6847404830755732 };
		double[] w0 = new double[] { 4.124947486997617, 4.490477707987684,
				4.135317186864141, 4.263017554624226, 4.433238054693188,
				3.8976889116418025, 4.2829923709713285, 4.152056327745383,
				4.333339400664292 };
		double[] w1 = new double[] { 4.286507803546038, 3.9209775825559707,
				4.276138103679513, 4.148437735919418, 3.9782172358504715,
				4.775410955591602, 4.128462919572326, 4.259398962798278,
				4.078115889879362 };
		double w00 = 0.021082182374938;
		double w11 = 0.009966496336148;
		eating.setfVector(eating_feature);
		eating_disorder.setfVector(ed_feature);
		graph.addVertex(source);
		graph.addVertex(sink);
		graph.addVertex(eating);
		graph.addVertex(eating_disorder);
		WikiEdge s_eating = graph.addEdge(source, eating);
		WikiEdge s_ed = graph.addEdge(source, eating_disorder);
		WikiEdge eating_t = graph.addEdge(eating, sink);
		WikiEdge ed_t = graph.addEdge(eating_disorder, sink);
		WikiEdge eating_ed = graph.addEdge(eating, eating_disorder);
		double eating_ed_feature = 0.6831095752467133;
		System.out.println(MathHelper.dotProduct(w0, eating_feature) + w00
				* eating_ed_feature + MathHelper.dotProduct(w0, ed_feature));
		System.out.println();
		graph.setEdgeWeight(s_eating, MathHelper.dotProduct(w0, eating_feature)
				+ w00 * eating_ed_feature);
		graph.setEdgeWeight(eating_ed, (w00 + w11) * eating_ed_feature);
		graph.setEdgeWeight(eating_t, MathHelper.dotProduct(w1, eating_feature));
		graph.setEdgeWeight(ed_t, MathHelper.dotProduct(w1, eating_feature)
				+ w11 * eating_ed_feature);
		graph.setEdgeWeight(s_ed, MathHelper.dotProduct(w0, ed_feature));

		MinSourceSinkCut<WikiNode, WikiEdge> cutter = new MinSourceSinkCut<WikiNode, WikiEdge>(
				graph);
		cutter.computeMinCut(source, sink);
		GraphMincutInference infer = new GraphMincutInference();
		System.out.println("printing source partition...");
		infer.printPartition(cutter.getSourcePartition());
		System.out.println("printing sink partition...");
		infer.printPartition(cutter.getSinkPartition());
		System.out.println(cutter.getCutWeight());
	}
}
