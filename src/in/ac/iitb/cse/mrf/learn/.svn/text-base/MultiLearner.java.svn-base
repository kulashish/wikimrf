package in.ac.iitb.cse.mrf.learn;

import in.ac.iitb.cse.mrf.data.AMNWeights;
import in.ac.iitb.cse.mrf.data.AugmentedGraph;
import in.ac.iitb.cse.mrf.data.MRFGraph;
import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;
import in.ac.iitb.cse.mrf.exception.LearningException;
import in.ac.iitb.cse.mrf.infer.GraphMincutInference;
import in.ac.iitb.cse.mrf.infer.StepsizeDecider;
import in.ac.iitb.cse.mrf.util.LearningProperties;
import in.ac.iitb.cse.mrf.util.MathHelper;
import in.ac.iitb.cse.mrf.util.WikiEdgeProvider;
import in.ac.iitb.cse.mrf.util.WikiNodeProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgrapht.WeightedGraph;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.VertexNameProvider;

public class MultiLearner {
	private static Logger logger = Logger.getLogger("in.ac.iitb.cse.mrf.learn");
	private MRFGraph[] mrfGraphs;
	private AugmentedGraph[] augGraphs;
	private int nodeFeatureDim;
	private int edgeFeatureDim;
	private int totalIterations;
	private AMNWeights weights;

	private int iterCount = 0;
	private double[] weightSum;

	private State currentState;
	private State optimalState;

	public MultiLearner(int numIter) {
		nodeFeatureDim = LearningProperties.getNodeDim()
				- LearningProperties.getNodeFeatureFilterSize();
		edgeFeatureDim = LearningProperties.getEdgeDim()
				- LearningProperties.getEdgeFeatureFilterSize();
		totalIterations = numIter;
		if (null == weights)
			weights = new AMNWeights(nodeFeatureDim, edgeFeatureDim);
	}

	public void setIterCount(int iterCount) {
		this.iterCount = iterCount;
	}

	public void loadTrainingData(String trainingFolderPath)
			throws LearningException {
		File trainingFolder = new File(trainingFolderPath);
		File[] trainingFiles = trainingFolder.listFiles();
		mrfGraphs = new MRFGraph[trainingFiles.length];
		int trainIndex = 0;
		// FeatureFilter fFilter = FeatureFilter.getInstance();
		for (File trainingFile : trainingFiles)
			try {
				logger.log(Level.INFO, "Loading " + trainingFile.getName());
				mrfGraphs[trainIndex] = MRFGraph.loadFromFile(trainingFile);
				// fFilter.apply(mrfGraphs[trainIndex]);
				// mrfGraphs[trainIndex].printTrueNodes();
				// mrfGraphs[trainIndex].normalizeFeatures(nodeFeatureDim,
				// edgeFeatureDim);
				mrfGraphs[trainIndex++].makeSparse();
//				serializeGraph(mrfGraphs[trainIndex - 1].getGraph(),
//						"/Users/ashish/wiki/train_graphs/"
//								+ mrfGraphs[trainIndex - 1].getGraphName()
//								+ "_iter" + iterCount);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "IOException while loading graph "
						+ trainingFile.getName());
				throw new LearningException(e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				logger.log(Level.SEVERE,
						"ClassNotFoundException while loading graph "
								+ trainingFile.getName());
				throw new LearningException(e.getMessage(), e);
			}
	}

	public void validate(AMNWeights w, double low, double high, double inc) {
		double[] hyper = null;
		State opt = null;
		State cur = null;
		for (double pen0 = low; pen0 <= high; pen0 += inc)
			for (double pen1 = low; pen1 <= high; pen1 += inc) {
				iterCount = 0;
				totalIterations = 1;
				optimalState = null;
				setWeights(w);
				hyper = new double[2];
				hyper[0] = pen0;
				hyper[1] = pen1;
				cur = learn(hyper);
				if (null == opt || cur.getFunctionVal() < opt.getFunctionVal()) // {
					opt = cur;
			}
		logger.log(Level.INFO,
				"-----Logging State for optimal hyperparameters------");
		opt.logall(Level.INFO);
	}

	public State learn(double[] hyperParams) {
		LearningFunction learningFunction = null;
		while (++iterCount <= totalIterations) {
			logger.log(Level.INFO, "Running iteration " + iterCount);
			currentState = new State();
			currentState.setWeights(weights);
			currentState.setPenalty(hyperParams);
			inferenceStep();
			learningFunction = new SeparatePenaltyLearningFunction(augGraphs,
					weights, hyperParams);
			currentState.setFunctionVal(learningFunction.functionValue());
			if (learningFunction instanceof SeparatePenaltyLearningFunction) {
				currentState
						.setLabel0agreements(((SeparatePenaltyLearningFunction) learningFunction)
								.getLabel0Agreements());
				currentState
						.setLabel1agreements(((SeparatePenaltyLearningFunction) learningFunction)
								.getLabel1Agreements());
				currentState
						.setLabel0nodes(((SeparatePenaltyLearningFunction) learningFunction)
								.getTotalLabel0Nodes());
				currentState
						.setLabel1nodes(((SeparatePenaltyLearningFunction) learningFunction)
								.getTotalLabel1Nodes());
				currentState.computeMeasures();
			} else {
				currentState.setNumAgreements(learningFunction.getAgreements());
				currentState.setNodes(learningFunction.getNumNodes());
			}
			if (null == optimalState
					|| currentState.getFunctionVal() < optimalState
							.getFunctionVal()) // {
				optimalState = currentState;
			if (iterCount < totalIterations)
				updateWeights(learningFunction.subgradient());
			// } else
			// averageWeights();
			currentState.logall(Level.INFO);
		}
		return optimalState;
	}

	public State learn() {
		return learn(null);
	}

	private void inferenceStep() {
		augGraphs = new AugmentedGraph[mrfGraphs.length];
		GraphMincutInference mincutter = null;
		for (int i = 0; i < augGraphs.length; i++) {
			// mrfGraphs[i].printTrueNodes();
			augGraphs[i] = new AugmentedGraph(mrfGraphs[i], weights);
			// augGraphs[i].print();
			mincutter = new GraphMincutInference(augGraphs[i]);
			mincutter.computeMincut();
//			serializeGraph(augGraphs[i].getGraph(),
//					"/Users/ashish/wiki/train_graphs/" + augGraphs[i].getName()
//							+ "_iter" + iterCount);
//			mincutter.printPartitions();
			currentState.setSourcePartition(mincutter.getSourcePartition());
			currentState.setSinkPartition(mincutter.getSinkPartition());
			currentState.setPredLabel0Nodes(mincutter.getSize(mincutter
					.getSourcePartition()));
			currentState.setPredLabel1Nodes(mincutter.getSize(mincutter
					.getSinkPartition()));
		}
	}

	private void serializeGraph(WeightedGraph<WikiNode, WikiEdge> graph,
			String name) {

		// Also export it to a text file in GraphML format
		VertexNameProvider<WikiNode> vertexProvider = new WikiNodeProvider();
		EdgeNameProvider<WikiEdge> edgeProvider = new WikiEdgeProvider();
		try {
			new GraphMLExporter<WikiNode, WikiEdge>(vertexProvider,
					vertexProvider, edgeProvider, edgeProvider).export(
					new FileWriter(name + ".graphml"), graph);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateWeights(double[] sg) {
		StringBuilder builder = new StringBuilder("Subgradient :: ");
		for (double f : sg)
			builder.append(f).append(':');
		logger.log(Level.INFO, builder.toString());

		// if (null == weightSum)
		// weightSum = weights.asVector();
		// else
		// weightSum = MathHelper.addVectors(weightSum, weights.asVector());
		// double[] newWeights = MathHelper.subtractVectors(weights.asVector(),
		// MathHelper.multiply(StepsizeDecider.getStepsize(sg, iterCount),
		// sg));
		double[] step = StepsizeDecider.getStepsize(sg, iterCount);
		logger.log(Level.INFO, "Step : " + Arrays.toString(step));
		weights.update(step, sg);
		// double[] newNodeWeights = MathHelper.subtractVectors(
		// weights.nodeWeightsAsVector(),
		// MathHelper.multiply(nodeStep,
		// Arrays.copyOfRange(sg, 0, 2 * nodeFeatureDim)));
		// double[] newEdgeWeights = MathHelper.subtractVectors(
		// weights.edgeWeightsAsVector(),
		// MathHelper.multiply(edgeStep,
		// Arrays.copyOfRange(sg, 2 * nodeFeatureDim, sg.length)));
		// // weights.setWeights(newWeights);
		// weights.setWeights(newNodeWeights, newEdgeWeights);
	}

	private void averageWeights() {
		weights.setWeights(MathHelper.divide(weights.asVector(), iterCount + 1));
	}

	public void setWeights(AMNWeights weights) {
		this.weights = weights;
	}

	public MRFGraph[] getMrfGraphs() {
		return mrfGraphs;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Logger.getLogger("in.ac.iitb.cse.mrf").setLevel(Level.ALL);
		try {
			new LearningProperties(args[0]); // args[0] - learning properties
		} catch (FileNotFoundException e1) {
			logger.log(Level.FINE, e1.getMessage());
		} catch (IOException e1) {
			logger.log(Level.FINE, e1.getMessage());
		}
		if (LearningProperties.isTestOnly()) {
			String testPath = LearningProperties.getTestPath();
			if (null != testPath && !"".equals(testPath)) {

				logger.log(Level.INFO, "Running on test set...");
				MultiLearner tester = new MultiLearner(1);
				try {
					tester.loadTrainingData(testPath);
				} catch (LearningException e) {
					logger.log(Level.FINE, e.getMessage());
				}
				tester.setWeights(AMNWeights.loadFromFile());
				State result = tester.learn();
				result.logall(Level.INFO);

				for (AugmentedGraph g : tester.augGraphs) 
					tester.serializeGraph(g.getGraph(), LearningProperties.getTestPath()
							+ g.getName());
			}
		} else {
			int numIter = LearningProperties.getLearningIterations();
			MultiLearner learner = new MultiLearner(numIter);

			State optimal = null;
			try {
				learner.loadTrainingData(LearningProperties.getTrainingPath());
				if(LearningProperties.isContinue())
				{
					learner.setWeights(AMNWeights.loadFromFile());
					learner.setIterCount(LearningProperties.getIterCount());
				}
				optimal = learner.learn();
				optimal.logall(Level.INFO);
			} catch (LearningException e) {
				logger.log(Level.FINE, e.getMessage());
			}

			String validPath = LearningProperties.getValidPath();
			if (null != validPath && !"".equals(validPath)) {

				logger.log(Level.INFO, "Running on validation set...");
				MultiLearner validator = new MultiLearner(1);
				try {
					validator.loadTrainingData(validPath);
				} catch (LearningException e) {
					logger.log(Level.FINE, e.getMessage());
				}
				// validator.validate(optimal.getWeights(),
				// LearningProperties.getValidLow(),
				// LearningProperties.getValidHigh(),
				// LearningProperties.getValidStep());
				validator.setWeights(optimal.getWeights());
				State result = validator.learn();
				result.logall(Level.INFO);
			}
		}
	}
}
