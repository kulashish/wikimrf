package in.ac.iitb.cse.mrf.sample;

import in.ac.iitb.cse.mrf.data.AMNWeights;
import in.ac.iitb.cse.mrf.data.AugmentedGraph;
import in.ac.iitb.cse.mrf.data.MRFGraph;
import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;
import in.ac.iitb.cse.mrf.infer.GraphMincutInference;

import org.jgrapht.WeightedGraph;

public class GraphMincutInferenceTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WeightedGraph<WikiNode, WikiEdge> g = SampleGraph.createMRF();
		MRFGraph mrfObj = new MRFGraph(g);
		/*
		 * this will create default weight vectors with all elements set to 1.
		 * Look at the other constructor AMNWeights(double[] w0, double[] w1,
		 * double[] w00, double[] w11) for explicitly setting weights
		 */
		AMNWeights weights = new AMNWeights(8, 6);
		AugmentedGraph augGraph = new AugmentedGraph(mrfObj, weights);
		GraphMincutInference inference = new GraphMincutInference(augGraph);
		inference.computeMincut();
		for (WikiNode n : augGraph.getNodes())
			System.out.println("Predicted class for " + n.getLabel() + " : "
					+ n.isPredcut());
	}
}
