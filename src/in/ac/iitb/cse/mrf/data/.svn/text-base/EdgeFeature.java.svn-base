package in.ac.iitb.cse.mrf.data;

import in.ac.iitb.cse.mrf.util.LearningProperties;

public class EdgeFeature {
	public static int CATEGORY;
	public static int OUTLINK;
	public static int INLINK;
	public static int CONTEXT_FREQUENT;
	public static int CONTEXT_SYNOPSIS;
	public static int CONTEXT_SYNOPSIS_VBADJ;
	public static int CONTEXT_FULLTEXT;

	static {
		int index = 0;
		if (LearningProperties.isCategory())
			CATEGORY = index++;
		if (LearningProperties.isOutlink())
			OUTLINK = index++;
		if (LearningProperties.isInlink())
			INLINK = index++;
		if (LearningProperties.isFrequent())
			CONTEXT_FREQUENT = index++;
		if (LearningProperties.isSynopsis())
			CONTEXT_SYNOPSIS = index++;
		if (LearningProperties.isSynvbadj())
			CONTEXT_SYNOPSIS_VBADJ = index++;
		if (LearningProperties.isFulltext())
			CONTEXT_FULLTEXT = index++;
	}

}
