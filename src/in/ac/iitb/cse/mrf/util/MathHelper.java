package in.ac.iitb.cse.mrf.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MathHelper {

	public static double dotProduct(double[] vec1, double[] vec2) {
		double result = 0.0d;
		for (int i = 0; i < vec1.length; i++)
			result += vec1[i] * vec2[i];
		return result;
	}

	public static double[] addVectors(double[] vec1, double[] vec2) {
		double[] result = new double[vec1.length];
		for (int i = 0; i < vec1.length; i++)
			result[i] = vec1[i] + vec2[i];
		return result;
	}

	public static void normalizeVectors(double[][] vectors) {
		int vecLen = vectors[0].length;
		double max[] = new double[vecLen];
		double min[] = new double[vecLen];
		Arrays.fill(max, 0.0d);
		Arrays.fill(min, Double.POSITIVE_INFINITY);

		for (double[] v : vectors)
			for (int i = 0; i < vecLen; i++) {
				if (v[i] > max[i])
					max[i] = v[i];
				if (v[i] < min[i])
					min[i] = v[i];
			}

		for (double[] v : vectors)
			MathHelper.normalizeVector(v, min, max);
	}

	public static void normalizeVector(double[] fVector, double[] min,
			double[] max) {
		for (int i = 0; i < fVector.length; i++) {
			if (max[i] - min[i] > 0)
				fVector[i] = (fVector[i] - min[i]) / (max[i] - min[i]);
		}
	}

	public static double vectorNormSquare(double[] v) {
		double sum = 0.0d;
		for (double f : v)
			sum += f * f;
		return sum;
	}

	public static double vectorNorm(double[] v) {
		return Math.sqrt(vectorNormSquare(v));
	}

	public static double[] subtractVectors(double[] vec1, double[] vec2) {
		double[] result = new double[vec1.length];
		for (int i = 0; i < vec1.length; i++)
			result[i] = vec1[i] - vec2[i];
		return result;
	}

	public static double[] multiply(double val, double[] vec) {
		for (int i = 0; i < vec.length; i++)
			vec[i] *= val;
		return vec;
	}

	public static double[] divide(double[] vec, int num) {
		double result[] = new double[vec.length];
		for (int i = 0; i < result.length; i++)
			result[i] = vec[i] / num;
		return result;
	}

	public static double[] asArray(String commaSep) {
		StringTokenizer tokenizer = new StringTokenizer(commaSep, ",");
		double[] weights = new double[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreTokens())
			weights[i++] = Double.valueOf(tokenizer.nextToken());
		return weights;
	}

	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}

	public static int arrayCopy(double[] target, int start, double[] source) {
		for (int i = 0; i < source.length; i++)
			target[start++] = source[i];
		return start;
	}

	public static void main(String[] args) {
		double a[][] = { { 2, 4 }, { 6, 8 } };
		normalizeVectors(a);
		for (double t[] : a) {
			System.out.println(Arrays.toString(t));
		}
	}

	public static void makeNonNegative(double[] arr) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i] < 0d)
				arr[i] = 0d;
	}

	public static String asString(double[] arr) {
		StringBuilder builder = new StringBuilder();
		for (double d : arr)
			builder.append(d).append(',');
		return builder.substring(0, builder.length() - 1);
	}
}
