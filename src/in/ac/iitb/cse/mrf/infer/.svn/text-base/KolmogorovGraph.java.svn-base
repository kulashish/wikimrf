package in.ac.iitb.cse.mrf.infer;

import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;
import in.ac.iitb.cse.mrf.learn.State;
import in.ac.iitb.cse.mrf.util.GraphConverter;
import in.ac.iitb.cse.mrf.util.GraphSparseMaker;

import java.util.Set;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.MinSourceSinkCut;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

@Deprecated
public class KolmogorovGraph {
	private WeightedGraph<WikiNode, WikiEdge> mrfGraph;
	private WeightedGraph<WikiNode, WikiEdge> kGraph;
	private WikiNode source;
	private WikiNode sink;
	private double[] w0;
	private double[] w1;
	private double[] w00;
	private double[] w11;

	public WeightedGraph<WikiNode, WikiEdge> getkGraph() {
		return kGraph;
	}

	public WeightedGraph<WikiNode, WikiEdge> graphCut(State currentState) {
		// draw();
		SimpleDirectedWeightedGraph<WikiNode, WikiEdge> directedKGraph = new GraphConverter()
				.convert((SimpleWeightedGraph<WikiNode, WikiEdge>) kGraph);
		MinSourceSinkCut<WikiNode, WikiEdge> stcutter = new MinSourceSinkCut(
				directedKGraph);
		stcutter.computeMinCut(source, sink);
		if (null != currentState) {
			currentState.setSourcePartition(stcutter.getSourcePartition());
			currentState.setSinkPartition(stcutter.getSinkPartition());
			currentState.setCutWeight(stcutter.getCutWeight());
		}
		for (WikiEdge e : kGraph.edgeSet()) {
			e.setCut(isCut(stcutter.getSourcePartition(), e));
			// System.out.print((e.isCut() && e.isDesiredCut()) + ", ");
		}
		return kGraph;
	}

	private boolean isCut(Set<WikiNode> nodes, WikiEdge e) {
		boolean node1Found = false;
		boolean node2Found = false;
		WikiNode node1 = kGraph.getEdgeSource(e);
		WikiNode node2 = kGraph.getEdgeTarget(e);
		for (WikiNode node : nodes) {
			if (!node1Found && node.equals(node1))
				node1Found = true;
			else if (!node2Found && node.equals(node2))
				node2Found = true;
			if (node1Found && node2Found)
				break;
		}
		return node1Found != node2Found;
	}

	public WikiNode getSource() {
		return source;
	}

	public WikiNode getSink() {
		return sink;
	}

	public WeightedGraph<WikiNode, WikiEdge> createGraph() {
		kGraph = (SimpleWeightedGraph<WikiNode, WikiEdge>) ((SimpleWeightedGraph<WikiNode, WikiEdge>) mrfGraph)
				.clone();
		source = new WikiNode("s");
		sink = new WikiNode("t");
		kGraph.addVertex(source); // source
		kGraph.addVertex(sink); // sink
		WikiEdge edge = null;
		double edgeWeight = 0d;
		for (WikiNode v : mrfGraph.vertexSet()) {
			edge = kGraph.addEdge(source, v);
			edge.setTerminal(true);
			edge.setfVector(v.getfVector());
			edge.setDesiredCut(v.isIncut());
			edgeWeight = weight(w0, v.getfVector());
			kGraph.setEdgeWeight(edge, edgeWeight);

			edge = kGraph.addEdge(v, sink);
			edge.setTerminal(true);
			edge.setfVector(v.getfVector());
			edge.setDesiredCut(!v.isIncut());
			edgeWeight = weight(w1, v.getfVector());
			kGraph.setEdgeWeight(edge, edgeWeight);
		}
		System.out.println();
		WikiNode es = null;
		WikiNode et = null;
		WikiEdge s_es_edge = null;
		WikiEdge et_t_edge = null;
		WikiEdge s_et_edge = null;
		WikiEdge es_t_edge = null;
		for (WikiEdge e : mrfGraph.edgeSet()) {
			es = mrfGraph.getEdgeSource(e);
			et = mrfGraph.getEdgeTarget(e);
			s_es_edge = kGraph.getEdge(source, es);
			s_et_edge = kGraph.getEdge(source, et);
			et_t_edge = kGraph.getEdge(et, sink);
			es_t_edge = kGraph.getEdge(es, sink);
			kGraph.setEdgeWeight(s_es_edge, kGraph.getEdgeWeight(s_es_edge)
					+ weight(w00, e.getfVector()));
			kGraph.setEdgeWeight(et_t_edge, kGraph.getEdgeWeight(et_t_edge)
					+ weight(w11, e.getfVector()));
			kGraph.setEdgeWeight(s_et_edge, kGraph.getEdgeWeight(s_et_edge)
					+ weight(w00, e.getfVector()));
			kGraph.setEdgeWeight(es_t_edge, kGraph.getEdgeWeight(es_t_edge)
					+ weight(w11, e.getfVector()));
			kGraph.setEdgeWeight(e, cumweight(w00, w11, e.getfVector()));
			e.setDesiredCut(es.isIncut() != et.isIncut());
		}
		// System.out.println("Desired cut edges -");
		// for (WikiEdge e : kGraph.edgeSet()) {
		// if (!e.isDesiredCut()) {
		// System.out.print(kGraph.getEdgeSource(e).getLabel() + "--"
		// + kGraph.getEdgeTarget(e).getLabel() + ":");
		// for (double f : e.getfVector())
		// System.out.print(f + ",");
		// System.out.println();
		// }
		// }
		return kGraph;
	}

	public double cumweight(double[] w1, double[] w2, double[] f) {
		double weight = 0.0d;
		for (int i = 0; i < f.length; i++)
			weight += (w1[i] + w2[i]) * f[i];
		return weight;
	}

	public double weight(double[] w, double[] f) {
		double weight = 0.0d;
		for (int i = 0; i < f.length; i++)
			weight += f[i] * w[i];
		return weight;
	}

	public WeightedGraph<WikiNode, WikiEdge> getMrfGraph() {
		return mrfGraph;
	}

	public void setMrfGraph(WeightedGraph<WikiNode, WikiEdge> mrfGraph) {
		this.mrfGraph = mrfGraph;
	}

	public void setW0(double[] w0) {
		this.w0 = w0;
	}

	public void setW1(double[] w1) {
		this.w1 = w1;
	}

	public void setW00(double[] w00) {
		this.w00 = w00;
	}

	public void setW11(double[] w11) {
		this.w11 = w11;
	}

	public static void main(String[] args) {
		/*WeightedGraph<Integer, DefaultWeightedEdge> g = SampleGraph.create();
		MinSourceSinkCut<Integer, DefaultWeightedEdge> cutter = new MinSourceSinkCut<Integer, DefaultWeightedEdge>(
				(SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>) g);
		cutter.computeMinCut(0, 9);
		System.out.println("source partition");
		for (int i : cutter.getSourcePartition())
			System.out.print(i + ",");
		System.out.println("sink partition");
		for (int i : cutter.getSinkPartition())
			System.out.print(i + ",");
		System.out.println(cutter.getCutWeight());*/

		// System.out
		// .println(new StoerWagnerMinimumCut<Integer, DefaultWeightedEdge>(
		// gr).minCut());
	}

	private void draw() {
		for (WikiEdge e : kGraph.edgeSet()) {
			System.out.print(kGraph.getEdgeSource(e).getLabel() + "--"
					+ kGraph.getEdgeTarget(e).getLabel() + ":"
					+ kGraph.getEdgeWeight(e) + "---------");
			for (double f : e.getfVector())
				System.out.print(f + ",");
			System.out.println();
		}
	}
}
