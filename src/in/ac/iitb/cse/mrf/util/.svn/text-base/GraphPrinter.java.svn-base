package in.ac.iitb.cse.mrf.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import in.ac.iitb.cse.mrf.data.MRFGraph;
import in.ac.iitb.cse.mrf.exception.LearningException;
import in.ac.iitb.cse.mrf.learn.MultiLearner;

public class GraphPrinter {
	private static Logger logger = Logger.getLogger("in.ac.iitb.cse.mrf.util");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new LearningProperties(args[0]); // args[0] - learning properties
		} catch (FileNotFoundException e1) {
			logger.log(Level.FINE, e1.getMessage());
		} catch (IOException e1) {
			logger.log(Level.FINE, e1.getMessage());
		}
		MultiLearner learner = new MultiLearner(1);
		try {
			learner.loadTrainingData(args[1]);
		} catch (LearningException e) {
			e.printStackTrace();
		}
		for (MRFGraph g : learner.getMrfGraphs())
			g.print();

	}

}
