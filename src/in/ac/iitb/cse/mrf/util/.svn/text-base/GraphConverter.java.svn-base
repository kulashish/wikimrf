package in.ac.iitb.cse.mrf.util;

import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;

import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.TransformerConfigurationException;

import org.jgrapht.Graphs;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.xml.sax.SAXException;

public class GraphConverter {

	/**
	 * Converts an undirected weighted graph to directed weighted graph by
	 * adding forward and reverse edges for each edge in the undirected graph
	 * 
	 * @param graph
	 * @return SimpleDirectedWeightedGraph
	 */
	public SimpleDirectedWeightedGraph<WikiNode, WikiEdge> convert(
			SimpleWeightedGraph<WikiNode, WikiEdge> graph) {
		SimpleDirectedWeightedGraph<WikiNode, WikiEdge> dGraph = new SimpleDirectedWeightedGraph<WikiNode, WikiEdge>(
				WikiEdge.class);
		Graphs.addAllVertices(dGraph, graph.vertexSet());
		Graphs.addAllEdges(dGraph, graph, graph.edgeSet());
		WikiEdge reverseEdge = null;

		for (WikiEdge e : graph.edgeSet()) {
			reverseEdge = dGraph.addEdge(graph.getEdgeTarget(e),
					graph.getEdgeSource(e));
			WikiEdge.copy(reverseEdge, e);
			dGraph.setEdgeWeight(reverseEdge, graph.getEdgeWeight(e));
		}

//		new GraphWriter().write("/Users/ashish/wiki/fullgraph.dat", dGraph);
		return dGraph;
	}

}
