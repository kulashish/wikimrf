package in.ac.iitb.cse.mrf.util;

import in.ac.iitb.cse.mrf.data.WikiNode;

import org.jgrapht.ext.VertexNameProvider;

public class WikiNodeProvider implements VertexNameProvider<WikiNode> {

	@Override
	public String getVertexName(WikiNode vertex) {
		double[] f = vertex.getfVector();
		StringBuilder builder = null;
		if (null != f) {
			builder = new StringBuilder("(");
			for (double d : f)
				builder.append(d).append(',');
			builder.replace(builder.length() - 1, builder.length(), ")");
		}
		return vertex.getLabel()
				+ (null != builder ? builder.toString() + " - "
						+ vertex.isIncut() + "(" + vertex.isPredcut() + ")"
						: "");
	}
}
