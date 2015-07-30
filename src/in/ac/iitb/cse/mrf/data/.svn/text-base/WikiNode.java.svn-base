package in.ac.iitb.cse.mrf.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class WikiNode implements Serializable {
	private String label;
	private double[] fVector;
	boolean incut = false; // true label of this node
	boolean predcut = false; // predicted label of this node
	private AdditionalNodeInfo nodeInfo;
	private int numOccur = 0;

	public int getNumOccur() {
		return numOccur;
	}

	public void incrNumOccur() {
		numOccur++;
	}

	public AdditionalNodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(AdditionalNodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public boolean isPredcut() {
		return predcut;
	}

	public void setPredcut(boolean predcut) {
		this.predcut = predcut;
	}

	public boolean isIncut() {
		return incut;
	}

	public void setIncut(boolean incut) {
		this.incut = incut;
	}

	public WikiNode(String string) {
		label = string;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double[] getfVector() {
		return fVector;
	}

	public void setfVector(double[] fVector) {
		this.fVector = fVector;
	}

	@Override
	public boolean equals(Object arg0) {
		WikiNode that = (WikiNode) arg0;
		return this.getLabel().equals(that.getLabel());
	}

	public class AdditionalNodeInfo implements Serializable {
		public Map<String, Double> context2tfidfAnchor;
		public Map<String, Double> context2tfidfAnchorContext;
		public Map<String, Double> context2tfidfText;

		public AdditionalNodeInfo(Map<String, Double> aMap,
				Map<String, Double> acMap, Map<String, Double> tMap) {
			context2tfidfAnchor = aMap;
			context2tfidfAnchorContext = acMap;
			context2tfidfText = tMap;
		}

		public void unionAll(HashMap<String, Double> aMap,
				HashMap<String, Double> acMap, HashMap<String, Double> tMap) {
			MapUnion.union(context2tfidfAnchor, aMap);
			MapUnion.union(context2tfidfAnchorContext, acMap);
			MapUnion.union(context2tfidfText, tMap);
		}

		public Map<String, Double> getContext2tfidfAnchor() {
			return context2tfidfAnchor;
		}

		public Map<String, Double> getContext2tfidfAnchorContext() {
			return context2tfidfAnchorContext;
		}

		public Map<String, Double> getContext2tfidfText() {
			return context2tfidfText;
		}

	}

	public static class MapUnion {
		public static void union(Map<String, Double> thisMap,
				Map<String, Double> thatMap) {
			String key = null;
			for (Entry<String, Double> thatEntry : thatMap.entrySet()) {
				key = thatEntry.getKey();
				if (thisMap.containsKey(key))
					thisMap.put(key, thisMap.get(key) + thatEntry.getValue());
				else
					thisMap.put(key, thatEntry.getValue());
			}
		}
	}

}
