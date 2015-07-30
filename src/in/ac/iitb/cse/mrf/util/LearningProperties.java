package in.ac.iitb.cse.mrf.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LearningProperties {
	private static Properties props = null;

	public LearningProperties(String propsFile) throws FileNotFoundException,
			IOException {
		if (null == props) {
			props = new Properties();
			props.load(new FileInputStream(propsFile));
		}
	}

	public static double getValidLow() {
		return Double.valueOf(props.getProperty("learning.valid.low"));
	}

	public static double getValidHigh() {
		return Double.valueOf(props.getProperty("learning.valid.high"));
	}

	public static double getValidStep() {
		return Double.valueOf(props.getProperty("learning.valid.step"));
	}

	public static String getInferenceType() {
		return props.getProperty("learning.inference.type");
	}

	public static double getPenalty() {
		return Double.valueOf(props.getProperty("learning.penalty"));
	}

	public static double getNodeStep() {
		return Double.valueOf(props.getProperty("learning.step.node.default"));
	}

	public static double getEdgeStep() {
		return Double.valueOf(props.getProperty("learning.step.edge.default"));
	}

	public static int getNodeDim() {
		return Integer.valueOf(props.getProperty("data.dim.node"));
	}

	public static int getEdgeDim() {
		return Integer.valueOf(props.getProperty("data.dim.edge"));
	}

	public static double getDefaultWeight() {
		return Double.valueOf(props.getProperty("learning.weight.default"));
	}

	public static String getStepType() {
		return props.getProperty("learning.step.type");
	}

	public static String getTrainingPath() {
		return props.getProperty("data.train.graph.path");
	}

	public static String getTrainingTextPath() {
		return props.getProperty("data.train.text.path");
	}

	public static String getTrainingTextFilesLog() {
		return props.getProperty("data.train.text.files");
	}

	public static String getTrainingXMLPath() {
		return props.getProperty("data.train.xml.path");
	}

	public static String getTrainingXMLWikiPath() {
		return props.getProperty("data.train.xml.wiki.path");
	}

	public static String getTestPath() {
		return props.getProperty("data.test.path");
	}

	public static String getValidPath() {
		return props.getProperty("data.valid.path");
	}

	public static boolean isTestOnly() {
		return Boolean.valueOf(props.getProperty("testonly"));
	}

	public static boolean isKDD() {
		return Boolean.valueOf(props.getProperty("data.kdd"));
	}

	public static boolean isContinue() {
		return Boolean.valueOf(props.getProperty("learning.continue"));
	}

	public static int getIterCount() {
		return Integer.parseInt(props.getProperty("learning.iter.current"));
	}

	/*
	 * Check edge features begin
	 */
	public static boolean isInlink() {
		return Boolean.valueOf(props.getProperty("edge.inlink"));
	}

	public static boolean isOutlink() {
		return Boolean.valueOf(props.getProperty("edge.outlink"));
	}

	public static boolean isCategory() {
		return Boolean.valueOf(props.getProperty("edge.category"));
	}

	public static boolean isFrequent() {
		return Boolean.valueOf(props.getProperty("edge.frequent"));
	}

	public static boolean isSynopsis() {
		return Boolean.valueOf(props.getProperty("edge.synopsis"));
	}

	public static boolean isSynvbadj() {
		return Boolean.valueOf(props.getProperty("edge.synvbadj"));
	}

	public static boolean isFulltext() {
		return Boolean.valueOf(props.getProperty("edge.full"));
	}

	/*
	 * Check edge features end
	 */

	/*
	 * Check node features begin
	 */
	public static boolean isNodeInlink() {
		return Boolean.valueOf(props.getProperty("node.inlink"));
	}

	public static boolean isNodeOutlink() {
		return Boolean.valueOf(props.getProperty("node.outlink"));
	}

	public static boolean isNodeRedirect() {
		return Boolean.valueOf(props.getProperty("node.redirect"));
	}

	public static boolean isNodePagetitle() {
		return Boolean.valueOf(props.getProperty("node.pagetitle"));
	}

	public static boolean isNodeAnchor() {
		return Boolean.valueOf(props.getProperty("node.anchor"));
	}

	public static boolean isNodeSense() {
		return Boolean.valueOf(props.getProperty("node.sense"));
	}

	public static boolean isNodeAnchorcosine() {
		return Boolean.valueOf(props.getProperty("node.anchorcosine"));
	}

	public static boolean isNodeAnchortextcosine() {
		return Boolean.valueOf(props.getProperty("node.anchortextcosine"));
	}

	public static boolean isNodeFulltext() {
		return Boolean.valueOf(props.getProperty("node.fulltext"));
	}

	/*
	 * Check node features end
	 */

	public static double getNodeFulltextThreshold() {
		return Double.valueOf(props.getProperty("node.fulltext.threshold"));
	}

	public static double getInlinkThreshold() {
		return Double.valueOf(props.getProperty("edge.inlink.threshold"));
	}

	public static double getOutlinkThreshold() {
		return Double.valueOf(props.getProperty("edge.outlink.threshold"));
	}

	public static double getCategoryThreshold() {
		return Double.valueOf(props.getProperty("edge.category.threshold"));
	}

	public static double getFrequentThreshold() {
		return Double.valueOf(props.getProperty("edge.frequent.threshold"));
	}

	public static double getSynopsisThreshold() {
		return Double.valueOf(props.getProperty("edge.synopsis.threshold"));
	}

	public static double getSynvbadjThreshold() {
		return Double.valueOf(props.getProperty("edge.synvbadj.threshold"));
	}

	public static double getFulltextThreshold() {
		return Double.valueOf(props.getProperty("edge.full.threshold"));
	}

	public static double getLabel0Penalty() {
		return Double.valueOf(props.getProperty("learning.penalty.label0"));
	}

	public static double getLabel1Penalty() {
		return Double.valueOf(props.getProperty("learning.penalty.label1"));
	}

	public static String getW0() {
		return props.getProperty("learning.weight.w0");
	}

	public static String getW1() {
		return props.getProperty("learning.weight.w1");
	}

	public static String getW00() {
		return props.getProperty("learning.weight.w00");
	}

	public static String getW11() {
		return props.getProperty("learning.weight.w11");
	}

	public static String getNodeFeatureFilter() {
		return props.getProperty("filter.feature.node");
	}

	public static String getEdgeFeatureFilter() {
		return props.getProperty("filter.feature.edge");
	}

	public static int getNodeFeatureFilterSize() {
		return Integer.parseInt(props.getProperty("filter.feature.node.size"));
	}

	public static int getEdgeFeatureFilterSize() {
		return Integer.parseInt(props.getProperty("filter.feature.edge.size"));
	}

	public static int getLearningIterations() {
		return Integer.parseInt(props.getProperty("learning.iterations"));
	}
}
