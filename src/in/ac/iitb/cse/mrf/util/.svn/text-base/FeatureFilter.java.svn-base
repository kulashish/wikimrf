package in.ac.iitb.cse.mrf.util;

import in.ac.iitb.cse.mrf.data.EdgeFeature;
import in.ac.iitb.cse.mrf.data.MRFGraph;
import in.ac.iitb.cse.mrf.data.NodeFeature;
import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;

import java.util.StringTokenizer;
import java.util.logging.Logger;

public class FeatureFilter {
	private static Logger logger = Logger.getLogger("in.ac.iitb.cse.mrf.util");
	private NodeFeature[] nodeFeatures;
	private EdgeFeature[] edgeFeatures;

	public FeatureFilter(NodeFeature[] node, EdgeFeature[] edge) {
		// logger.log(Level.INFO,
		// "Creating FeatureFilter with num nodes filtered " + node.length);
		nodeFeatures = node;
		edgeFeatures = edge;

	}

	public void apply(MRFGraph graph) {
		double[] vec = null;
		double[] newVec = null;
		int index = 0;
		if (null != nodeFeatures)
			for (WikiNode node : graph.getNodes()) {
				index = 0;
				vec = node.getfVector();
				newVec = new double[vec.length - nodeFeatures.length];
				for (int i = 0; i < vec.length; i++)
					if (!filteredNodeFeature(i))
						newVec[index++] = vec[i];
				node.setfVector(newVec);
			}
		if (null != edgeFeatures)
			for (WikiEdge edge : graph.getEdges()) {
				index = 0;
				vec = edge.getfVector();
				newVec = new double[vec.length - edgeFeatures.length];
				for (int i = 0; i < vec.length; i++)
					if (!filteredEdgeFeature(i))
						newVec[index++] = vec[i];
				edge.setfVector(newVec);
			}
	}

	private boolean filteredNodeFeature(int i) {
		boolean blnFiltered = false;
		for (NodeFeature nf : nodeFeatures) {
			if (nf.ordinal() == i) {
				blnFiltered = true;
				break;
			}
		}
		return blnFiltered;
	}

	private boolean filteredEdgeFeature(int i) {
		boolean blnFiltered = false;
		// for (EdgeFeature ef : edgeFeatures)
		// if (ef.ordinal == i) {
		// blnFiltered = true;
		// break;
		// }
		return blnFiltered;
	}

	public static FeatureFilter getInstance() {
		NodeFeature[] nfs = null;
		EdgeFeature[] efs = null;
		String nodeFs = LearningProperties.getNodeFeatureFilter();
		String edgeFs = LearningProperties.getEdgeFeatureFilter();
		StringTokenizer tokenizer = null;
		if (null != nodeFs && !"".equals(nodeFs)) {
			tokenizer = new StringTokenizer(nodeFs, ",");
			nfs = new NodeFeature[tokenizer.countTokens()];
			for (int i = 0; tokenizer.hasMoreTokens(); i++)
				nfs[i] = NodeFeature.valueOf(tokenizer.nextToken());
		}

		if (null != edgeFs && !"".equals(edgeFs)) {
			tokenizer = new StringTokenizer(edgeFs, ",");
			efs = new EdgeFeature[tokenizer.countTokens()];
			// for (int i = 0; tokenizer.hasMoreTokens(); i++)
			// efs[i] = EdgeFeature.valueOf(tokenizer.nextToken());
		}
		return new FeatureFilter(nfs, efs);
	}
}
