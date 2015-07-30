package in.ac.iitb.cse.mrf.data;

import in.ac.iitb.cse.mrf.util.LearningProperties;
import in.ac.iitb.cse.mrf.util.MathHelper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AMNWeights {
	private static Logger logger = Logger.getLogger("in.ac.iitb.cse.mrf.data");
	private static final double DEFAULT_WEIGHT = LearningProperties
			.getDefaultWeight();
	private static final double SCALE = 1d;
	public static final double MIN_WEIGHT = 0d;

	private double[] w0;
	private double[] w1;
	private double[] w00;
	private double[] w11;

	private int w0len;
	private int w1len;
	private int w00len;
	private int w11len;

	private Random random;

	public AMNWeights() {

	}

	public AMNWeights(int nd, int ed) {
		random = new Random();
		w0len = w1len = nd;
		w00len = w11len = ed;
		w0 = new double[nd];
		Arrays.fill(w0, DEFAULT_WEIGHT);
		// fillRandom(w0);
		w1 = new double[nd];
		Arrays.fill(w1, DEFAULT_WEIGHT);
		// fillRandom(w1);
		w00 = new double[ed];
		Arrays.fill(w00, DEFAULT_WEIGHT);
		// fillRandom(w00);
		w11 = new double[ed];
		Arrays.fill(w11, DEFAULT_WEIGHT);
		// fillRandom(w11);
	}

	public AMNWeights(double[] w0, double[] w1, double[] w00, double[] w11) {
		this.w0 = copy(w0);
		this.w1 = copy(w1);
		this.w00 = copy(w00);
		this.w11 = copy(w11);
		w0len = w0.length;
		w1len = w1.length;
		w00len = w00.length;
		w11len = w11.length;
	}

	public static AMNWeights loadFromFile() {
		AMNWeights weights = new AMNWeights();
		weights.setW0(MathHelper.asArray(LearningProperties.getW0()));
		weights.setW1(MathHelper.asArray(LearningProperties.getW1()));
		weights.setW00(MathHelper.asArray(LearningProperties.getW00()));
		weights.setW11(MathHelper.asArray(LearningProperties.getW11()));
		return weights;
	}
	
	private double[] copy(double[] w) {
		double[] neww = new double[w.length];
		for (int i = 0; i < w.length; i++)
			neww[i] = w[i];
		return neww;
	}

	private void fillRandom(double[] w) {
		for (int i = 0; i < w.length; i++)
			w[i] = random.nextDouble() * SCALE;
	}

	public double[] asVector() {
		int wlen = w0len + w1len + w00len + w11len;
		double w[] = new double[wlen];
		int last = arrayCopy(w, 0, w0);
		last = arrayCopy(w, last, w1);
		last = arrayCopy(w, last, w00);
		last = arrayCopy(w, last, w11);
		return w;
	}

	public double[] nodeWeightsAsVector() {
		int wlen = w0len + w1len;
		double w[] = new double[wlen];
		int last = arrayCopy(w, 0, w0);
		last = arrayCopy(w, last, w1);
		return w;
	}

	public double[] edgeWeightsAsVector() {
		int wlen = w00len + w11len;
		double w[] = new double[wlen];
		int last = arrayCopy(w, 0, w00);
		last = arrayCopy(w, last, w11);
		return w;
	}

	private int arrayCopy(double[] target, int start, double[] source) {
		for (int i = 0; i < source.length; i++)
			target[start++] = source[i];
		return start;
	}

	public double[] getW0() {
		return w0;
	}

	public void setW0(double[] w0) {
		MathHelper.makeNonNegative(w0);
		this.w0 = w0;
		w0len = w0.length;
	}

	public double[] getW1() {
		return w1;
	}

	public void setW1(double[] w1) {
		MathHelper.makeNonNegative(w1);
		this.w1 = w1;
		w1len = w1.length;
	}

	public double[] getW00() {
		return w00;
	}

	public void setW00(double[] w00) {
		MathHelper.makeNonNegative(w00);
		this.w00 = w00;
		w00len = w00.length;
	}

	public double[] getW11() {
		return w11;
	}

	public void setW11(double[] w11) {
		MathHelper.makeNonNegative(w11);
		this.w11 = w11;
		w11len = w11.length;
	}

	public double normsqaure() {
		double[] ws = asVector();
		double normSq = 0.0d;
		for (double w : ws)
			normSq += w * w;
		return normSq;
	}

	public void print() throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				System.out));
		writeWeights(writer, "w0", w0);
		writeWeights(writer, "w1", w1);
		writeWeights(writer, "w00", w00);
		writeWeights(writer, "w11", w11);
		writer.flush();
		// writer.close();
	}
	
	public void readFromFile(String file) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader(file));
	    String line = reader.readLine();
	    while (line != null){
	        String[] parts = line.split(":");
	        if ("w0".equals(parts[0])) setW0(MathHelper.asArray(parts[1]));
		    else if ("w1".equals(parts[0])) setW1(MathHelper.asArray(parts[1]));
		    else if ("w00".equals(parts[0])) setW00(MathHelper.asArray(parts[1]));
		    else if ("w11".equals(parts[0])) setW11(MathHelper.asArray(parts[1]));
		    line = reader.readLine();
	    }
	}

	public void serialize(String file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		writeWeights(writer, "w0", w0);
		writeWeights(writer, "w1", w1);
		writeWeights(writer, "w00", w00);
		writeWeights(writer, "w11", w11);
		writer.close();
	}

	private void writeWeights(BufferedWriter writer, String label, double[] w)
			throws IOException {
		writer.append(label + ":");
		for (int i = 0; i < w.length; i++)
			writer.append(w[i] + ",");
		writer.newLine();
	}

	public void average(AMNWeights that) {
		average(this.getW0(), that.getW0());
		average(this.getW1(), that.getW1());
		average(this.getW00(), that.getW00());
		average(this.getW11(), that.getW11());
	}

	private void average(double[] thisw, double[] thatw) {
		for (int i = 0; i < thisw.length; i++)
			thisw[i] = (thisw[i] + thatw[i]) / 2;
	}

	public void setWeights(double[] newWeights) {
		for (int i = 0; i < newWeights.length; i++)
			if (newWeights[i] <= AMNWeights.MIN_WEIGHT)
				newWeights[i] = AMNWeights.MIN_WEIGHT;
		setW0(Arrays.copyOfRange(newWeights, 0, w0len));
		setW1(Arrays.copyOfRange(newWeights, w0len, w0len + w1len));
		setW00(Arrays.copyOfRange(newWeights, w0len + w1len, w0len + w1len
				+ w00len));
		setW11(Arrays.copyOfRange(newWeights, w0len + w1len + w00len,
				newWeights.length));
	}

	public void setWeights(double[] nodeWeights, double[] edgeWeights) {
		double[] w = new double[nodeWeights.length + edgeWeights.length];
		int index = 0;
		for (int i = 0; i < nodeWeights.length; i++)
			w[index++] = nodeWeights[i];
		for (int i = 0; i < edgeWeights.length; i++)
			w[index++] = edgeWeights[i];
		setWeights(w);
	}

	public void log(Level logLevel) {
		log("W0 : ", w0, logLevel);
		log("W1 : ", w1, logLevel);
		log("W00 : ", w00, logLevel);
		log("W11 : ", w11, logLevel);
	}

	private void log(String msg, double[] w, Level logLevel) {
		StringBuilder builder = new StringBuilder(msg);
		for (double f : w)
			builder.append(f).append(',');
		logger.log(logLevel, builder.toString());
	}

	public void update(double[] step, double[] sg) {
		w0 = MathHelper.subtractVectors(w0,
				MathHelper.multiply(step[0], Arrays.copyOfRange(sg, 0, w0len)));
		MathHelper.makeNonNegative(w0);
		w1 = MathHelper.subtractVectors(
				w1,
				MathHelper.multiply(step[1],
						Arrays.copyOfRange(sg, w0len, w0len + w1len)));
		MathHelper.makeNonNegative(w1);
		w00 = MathHelper.subtractVectors(
				w00,
				MathHelper.multiply(
						step[2],
						Arrays.copyOfRange(sg, w0len + w1len, w0len + w1len
								+ w00len)));
		MathHelper.makeNonNegative(w00);
		w11 = MathHelper.subtractVectors(
				w11,
				MathHelper.multiply(step[3], Arrays.copyOfRange(sg, w0len
						+ w1len + w00len, sg.length)));
		MathHelper.makeNonNegative(w11);

	}
}
