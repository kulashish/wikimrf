package in.ac.iitb.cse.mrf.learn;

import in.ac.iitb.cse.mrf.data.AMNWeights;
import in.ac.iitb.cse.mrf.data.WikiNode;
import in.ac.iitb.cse.mrf.util.LearningProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class State {
	private static Logger logger = Logger.getLogger("in.ac.iitb.cse.mrf.learn");
	private AMNWeights weights;
	private double functionVal;
	private List<WikiNode> sourcePartition;
	private List<WikiNode> sinkPartition;
	private double cutWeight;
	private int numAgreements;
	private int nodes;
	private double[] sg;
	private int label0agreements;
	private int label1agreements;
	private int label0nodes;
	private int label1nodes;
	private int predLabel0Nodes = 0;
	private int predLabel1Nodes = 0;
	private double[] penalty;
	double label0Recall;
	double label1Recall;
	double label0Prec;
	double label1Prec;
	double label0F;
	double label1F;
	double overallRecall;
	double overallPrec;
	double overallF;

	public double[] getPenalty() {
		return penalty;
	}

	public void setPenalty(double[] penalty) {
		this.penalty = penalty;
	}

	public int getPredLabel0Nodes() {
		return predLabel0Nodes;
	}

	public void setPredLabel0Nodes(int predLabel0Nodes) {
		this.predLabel0Nodes += predLabel0Nodes;
	}

	public int getPredLabel1Nodes() {
		return predLabel1Nodes;
	}

	public void setPredLabel1Nodes(int predLabel1Nodes) {
		this.predLabel1Nodes += predLabel1Nodes;
	}

	public int getLabel0agreements() {
		return label0agreements;
	}

	public void setLabel0agreements(int label0agreements) {
		this.label0agreements = label0agreements;
	}

	public int getLabel1agreements() {
		return label1agreements;
	}

	public void setLabel1agreements(int label1agreements) {
		this.label1agreements = label1agreements;
	}

	public int getLabel0nodes() {
		return label0nodes;
	}

	public void setLabel0nodes(int label0nodes) {
		this.label0nodes = label0nodes;
	}

	public int getLabel1nodes() {
		return label1nodes;
	}

	public void setLabel1nodes(int label1nodes) {
		this.label1nodes = label1nodes;
	}

	public int getNodes() {
		return nodes;
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
	}

	public double[] getSg() {
		return sg;
	}

	public void setSg(double[] sg) {
		this.sg = sg;
	}

	public void print() {
		// System.out.println("printing optimal state");
		System.out.println("Function value : " + functionVal);
		try {
			if (null != weights)
				weights.print();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("source partition:");
		for (WikiNode node : sourcePartition)
			System.out.print(node.getLabel() + ", ");
		System.out.println();
		System.out.println("sink partition:");
		for (WikiNode node : sinkPartition)
			System.out.print(node.getLabel() + ", ");
		System.out.println();
		System.out.println("Cut weight : " + cutWeight);
		System.out.println("Agreements : " + numAgreements);
	}

	public double getCutWeight() {
		return cutWeight;
	}

	public void setCutWeight(double cutWeight) {
		this.cutWeight = cutWeight;
	}

	public double getNumAgreements() {
		return numAgreements;
	}

	public void setNumAgreements(int numAgreements) {
		this.numAgreements = numAgreements;
	}

	public AMNWeights getWeights() {
		return weights;
	}

	public void setWeights(AMNWeights weights) {
		// this.weights = weights;
		this.weights = new AMNWeights(weights.getW0(), weights.getW1(),
				weights.getW00(), weights.getW11());
	}

	public double getFunctionVal() {
		return functionVal;
	}

	public void setFunctionVal(double functionVal) {
		this.functionVal = functionVal;
	}

	public List<WikiNode> getSourcePartition() {
		return sourcePartition;
	}

	public void setSourcePartition(Set<WikiNode> sourcePartition) {
		if (null == this.sourcePartition)
			this.sourcePartition = new ArrayList<WikiNode>(sourcePartition);
		else
			this.sourcePartition.addAll(sourcePartition);
	}

	public List<WikiNode> getSinkPartition() {
		return sinkPartition;
	}

	public void setSinkPartition(Set<WikiNode> sinkPartition) {
		if (null == this.sinkPartition)
			this.sinkPartition = new ArrayList<WikiNode>(sinkPartition);
		else
			this.sinkPartition.addAll(sinkPartition);
	}

	public void computeMeasures() {
		label0Recall = getLabel0agreements() * 100.0d / getLabel0nodes();
		label1Recall = getLabel1agreements() * 100.0d / getLabel1nodes();
		label0Prec = getLabel0agreements() * 100.0d / getPredLabel0Nodes();
		label1Prec = getLabel1agreements() * 100.0d / getPredLabel1Nodes();
		label0F = 2 * label0Recall * label0Prec / (label0Recall + label0Prec);
		label1F = 2 * label1Recall * label1Prec / (label1Recall + label1Prec);
		overallRecall = (getLabel0agreements() + getLabel1agreements())
				* 100.0d / (getLabel0nodes() + getLabel1nodes());
		overallPrec = (getLabel0agreements() + getLabel1agreements()) * 100.0d
				/ (getPredLabel0Nodes() + getPredLabel1Nodes());
		overallF = 2 * overallRecall * overallPrec
				/ (overallRecall + overallPrec);
	}

	public void logall(Level logLevel) {
		logger.log(logLevel, "Function Value : " + getFunctionVal());
		logger.log(logLevel, "Total nodes : " + getNodes());
		logger.log(logLevel, "Agreements : " + getNumAgreements());
		logger.log(logLevel, "label 0 agreements : " + getLabel0agreements()
				+ " out of total " + getLabel0nodes() + " nodes");
		logger.log(logLevel, "label 1 agreements : " + getLabel1agreements()
				+ " out of total " + getLabel1nodes() + " nodes");
		if (getNodes() > 0)
			logger.log(logLevel, "Accuracy : "
					+ (getNumAgreements() * 100.0d / getNodes()));
		else {
			logger.log(logLevel, "label 0 Recall : " + label0Recall);
			logger.log(logLevel, "label 0 Precision : " + label0Prec);
			logger.log(logLevel, "label 0 F : " + label0F);
			logger.log(logLevel, "label 1 Recall : " + label1Recall);
			logger.log(logLevel, "label 1 Precision : " + label1Prec);
			logger.log(logLevel, "label 1 F : " + label1F);
			logger.log(logLevel, "Overall Recall : " + overallRecall);
			logger.log(logLevel, "Overall Precision : " + overallPrec);
			logger.log(logLevel, "Overall F : " + overallF);
		}
		if (LearningProperties.isTestOnly()) {
			logger.log(logLevel, "Source parition : ");
			StringBuilder builder = new StringBuilder();
			for (WikiNode n : getSourcePartition())
				builder.append(n.getLabel()).append(',');
			logger.log(logLevel, builder.toString());

			logger.log(logLevel, "Sink parition : ");
			builder = new StringBuilder();
			for (WikiNode n : getSinkPartition())
				builder.append(n.getLabel()).append(',');
			logger.log(logLevel, builder.toString());
		}
		getWeights().log(logLevel);
		if (null != penalty)
			logger.log(logLevel, "label 0 penalty : " + getPenalty()[0]
					+ " label 1 penalty : " + getPenalty()[1]);
	}

}
