package in.ac.iitb.cse.mrf.util;

import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

@Deprecated
public class GraphSparseMaker {
	private static final double MIN_NORM = 0d;

	public void makeSparse(WeightedGraph<WikiNode, WikiEdge> g) {
		WeightedGraph<WikiNode, WikiEdge> newgraph = (SimpleWeightedGraph<WikiNode, WikiEdge>) ((SimpleWeightedGraph<WikiNode, WikiEdge>) g)
				.clone();
		System.out
				.println("original graph has " + g.edgeSet().size() + "edges");
		for (WikiEdge e : newgraph.edgeSet())
			if (fVectorNormSquare(e.getfVector()) <= MIN_NORM)
				g.removeEdge(e);
		System.out.println("after making sparse it has " + g.edgeSet().size()
				+ "edges");
	}

	private double fVectorNormSquare(double[] fvector) {
		double sum = 0d;
		for (double f : fvector)
			sum += f * f;
		return sum;
	}

}
