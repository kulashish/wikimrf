package in.ac.iitb.cse.mrf.infer;

import java.util.Arrays;

import in.ac.iitb.cse.mrf.util.LearningProperties;
import in.ac.iitb.cse.mrf.util.MathHelper;

public class StepsizeDecider {
	private static StepsizeType type;

	static {
		if (LearningProperties.getStepType().equalsIgnoreCase(
				StepsizeType.CONST_SIZE.getType()))
			type = StepsizeType.CONST_SIZE;
		if (LearningProperties.getStepType().equalsIgnoreCase(
				StepsizeType.CONST_LENGTH.getType()))
			type = StepsizeType.CONST_LENGTH;
		if (LearningProperties.getStepType().equalsIgnoreCase(
				StepsizeType.DIMINISHING.getType()))
			type = StepsizeType.DIMINISHING;
	}

	public static double[] getStepsize(double[] subgradient, int numIter) {
		double[] step = null;
		switch (type) {
		case CONST_SIZE:
			step = constSize();
			break;
		case CONST_LENGTH:
			step = constLength(subgradient);
			break;
		case DIMINISHING:
			step = diminishing(numIter);
		}
		return step;
	}

	private static double[] diminishing(int numIter) {
		System.out.println("Diminishing step");
		double[] step = new double[4];
		for (int i = 0; i < 2; i++)
			step[i] = LearningProperties.getNodeStep() / Math.sqrt(numIter);
		for (int i = 2; i < 4; i++)
			step[i] = LearningProperties.getEdgeStep() / Math.sqrt(numIter);
		return step;
	}

	private static double[] constLength(double[] subgradient) {
		System.out.println("CL step");
		int nd = LearningProperties.getNodeDim();
		int ed = LearningProperties.getEdgeDim();
		double defaultNodeStep = LearningProperties.getNodeStep();
		double defaultEdgeStep = LearningProperties.getEdgeStep();
		double[] step = new double[4];

		step[0] = defaultNodeStep
				/ MathHelper.vectorNorm(Arrays.copyOfRange(subgradient, 0, nd));
		step[1] = defaultNodeStep
				/ MathHelper.vectorNorm(Arrays.copyOfRange(subgradient, nd,
						2 * nd));
		step[2] = defaultEdgeStep
				/ MathHelper.vectorNorm(Arrays.copyOfRange(subgradient, 2 * nd,
						2 * nd + ed));
		step[3] = defaultEdgeStep
				/ MathHelper.vectorNorm(Arrays.copyOfRange(subgradient, 2 * nd
						+ ed, subgradient.length));

		return step;
	}

	private static double[] constSize() {
		System.out.println("CS step");
		double[] step = new double[4];
		for (int i = 0; i < 2; i++)
			step[i] = LearningProperties.getNodeStep();
		for (int i = 2; i < 4; i++)
			step[i] = LearningProperties.getEdgeStep();
		return step;
	}

}
