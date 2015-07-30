package in.ac.iitb.cse.mrf.sample;

import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

public class SampleGraph {

	public static WeightedGraph<Integer, DefaultWeightedEdge> create() {
		WeightedGraph<Integer, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		for (int i = 0; i <= 9; i++)
			graph.addVertex(i);
		graph.setEdgeWeight(graph.addEdge(0, 1), 5);
		graph.setEdgeWeight(graph.addEdge(0, 3), 7);
		graph.setEdgeWeight(graph.addEdge(0, 4), 11);// 11
		graph.setEdgeWeight(graph.addEdge(1, 2), 6);
		graph.setEdgeWeight(graph.addEdge(1, 3), 4);
		graph.setEdgeWeight(graph.addEdge(2, 3), 9);
		graph.setEdgeWeight(graph.addEdge(2, 7), 2);
		graph.setEdgeWeight(graph.addEdge(2, 9), 6);
		graph.setEdgeWeight(graph.addEdge(3, 4), 3);
		graph.setEdgeWeight(graph.addEdge(4, 5), 8);
		graph.setEdgeWeight(graph.addEdge(5, 6), 5);
		graph.setEdgeWeight(graph.addEdge(5, 8), 3);
		graph.setEdgeWeight(graph.addEdge(6, 7), 3);
		graph.setEdgeWeight(graph.addEdge(6, 8), 7);
		graph.setEdgeWeight(graph.addEdge(7, 9), 8);
		graph.setEdgeWeight(graph.addEdge(8, 9), 5);

		WeightedGraph<Integer, DefaultWeightedEdge> dgraph = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		Graphs.addAllVertices(dgraph, graph.vertexSet());
		Graphs.addAllEdges(dgraph, graph, graph.edgeSet());
		DefaultWeightedEdge reverse = null;
		for (DefaultWeightedEdge e : graph.edgeSet()) {
			reverse = dgraph.addEdge(graph.getEdgeTarget(e),
					graph.getEdgeSource(e));
			dgraph.setEdgeWeight(reverse, dgraph.getEdgeWeight(e));
		}
		return dgraph;
	}

	public static WeightedGraph<WikiNode, WikiEdge> createMRF() {
		WeightedGraph<WikiNode, WikiEdge> mrfgraph = new SimpleWeightedGraph<WikiNode, WikiEdge>(
				WikiEdge.class);

		WikiNode a = new WikiNode("a");
		a.setIncut(true); // set to true if label = 1 else false
		a.setfVector(new double[8]); // node feature dimension is 8
		mrfgraph.addVertex(a);

		WikiNode b = new WikiNode("b");
		b.setIncut(false); // set to true if label = 1 else false
		b.setfVector(new double[8]);
		mrfgraph.addVertex(b);

		WikiNode c = new WikiNode("c");
		c.setIncut(true); // set to true if label = 1 else false
		c.setfVector(new double[8]);
		mrfgraph.addVertex(c);

		WikiEdge e = mrfgraph.addEdge(a, b);
		e.setfVector(new double[6]); // edge feature dimension is 6
		e = mrfgraph.addEdge(b, c);
		e.setfVector(new double[6]);
		return mrfgraph;
	}

	public void createWikiGraph(WeightedGraph<WikiNode, WikiEdge> mrf) {
		WeightedGraph<WikiNode, WikiEdge> copy = (SimpleWeightedGraph<WikiNode, WikiEdge>) ((SimpleWeightedGraph<WikiNode, WikiEdge>) mrf)
				.clone();
		String[] nodes = { "Mexico", "Mexico (Epcot)", "Guatemala",
				"Guatemala (disambiguation)" };
		boolean found = false;
		for (WikiNode n : copy.vertexSet()) {
			found = false;
			for (String node : nodes)
				if (n.getLabel().equalsIgnoreCase(node)) {
					found = true;
					break;
				}
			if (!found)
				mrf.removeVertex(n);
		}
		System.out.println(mrf.vertexSet().size());
		System.out.println(mrf.edgeSet().size());

	}

}
