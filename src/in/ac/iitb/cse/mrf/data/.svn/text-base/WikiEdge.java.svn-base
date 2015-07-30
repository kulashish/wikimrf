package in.ac.iitb.cse.mrf.data;

import java.io.Serializable;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WikiEdge extends DefaultWeightedEdge implements Serializable {
	private double[] fVector;
	private boolean cut;
	private boolean desiredCut;
	private boolean terminal;

	public boolean isTerminal() {
		return terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	public boolean isDesiredCut() {
		return desiredCut;
	}

	public void setDesiredCut(boolean desiredCut) {
		this.desiredCut = desiredCut;
	}

	public double[] getfVector() {
		return fVector;
	}

	public void setfVector(double[] fVector) {
		this.fVector = fVector;
	}

	public boolean isCut() {
		return cut;
	}

	public void setCut(boolean cut) {
		this.cut = cut;
	}

//	public double fVectorNormSquare() {
//		double sum = 0d;
//		for (double f : fVector)
//			sum += f * f;
//		return sum;
//	}

	public static void copy(WikiEdge target, WikiEdge source) {
		target.setDesiredCut(source.isDesiredCut());
		target.setfVector(source.getfVector());
		target.setTerminal(source.isTerminal());
		target.setCut(source.isCut());
	}

}
