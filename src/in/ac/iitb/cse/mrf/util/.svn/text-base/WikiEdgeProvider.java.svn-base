package in.ac.iitb.cse.mrf.util;

import in.ac.iitb.cse.mrf.data.WikiEdge;

import org.jgrapht.ext.EdgeNameProvider;

public class WikiEdgeProvider implements EdgeNameProvider<WikiEdge> {

	@Override
	public String getEdgeName(WikiEdge edge) {
		double[] f = edge.getfVector();
		StringBuilder builder = new StringBuilder("(");
		for (double d : f)
			builder.append(d).append(',');
		builder.replace(builder.length() - 1, builder.length(), ")");
		builder.append(edge.isDesiredCut()).append('(').append(edge.isCut())
				.append(')');
		return builder.toString();
	}
}
