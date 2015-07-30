package in.ac.iitb.cse.mrf.util;

import in.ac.iitb.cse.mrf.data.WikiEdge;
import in.ac.iitb.cse.mrf.data.WikiNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.jgrapht.WeightedGraph;

public class GraphWriter {
	private static final char SEPARATOR ='|';

	public void writeNodeCSV(BufferedWriter writer,
			WeightedGraph<WikiNode, WikiEdge> g) {
		try {
			for (WikiNode n : g.vertexSet()) {
				writer.append(n.getLabel()).append(SEPARATOR);
				for (double f : n.getfVector())
					writer.append(String.valueOf(f)).append(SEPARATOR);
				writer.append(n.isIncut() ? "1" : "0");
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeEdgeCSV(BufferedWriter writer,
			WeightedGraph<WikiNode, WikiEdge> g) {
		try {
			WikiNode source = null;
			WikiNode target = null;
			for (WikiEdge e : g.edgeSet()) {
				source = g.getEdgeSource(e);
				target = g.getEdgeTarget(e);
				writer.append(source.getLabel()).append(SEPARATOR);
				writer.append(target.getLabel()).append(SEPARATOR);
				for (double f : e.getfVector())
					writer.append(String.valueOf(f)).append(SEPARATOR);
				writer.append(source.isIncut() != target.isIncut() ? "01"
						: (source.isIncut() && target.isIncut() ? "11" : "00"));
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String file, WeightedGraph<WikiNode, WikiEdge> g) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.append("Number of nodes : " + g.vertexSet().size());
			writer.newLine();
			writer.append("Number of edges : " + g.edgeSet().size());
			writer.newLine();
			writer.append("List of nodes :");
			writer.newLine();
			for (WikiNode n : g.vertexSet())
				writer.append(n.getLabel() + ", ");
			writer.newLine();
			writer.append("List of edges :");
			writer.newLine();
			for (WikiEdge e : g.edgeSet()) {
				writer.append(g.getEdgeSource(e).getLabel() + "-->"
						+ g.getEdgeTarget(e).getLabel() + " : "
						+ g.getEdgeWeight(e));
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		String graphFolderPath = args[0];
		String csvFile = args[1];
		String indicator = args[2];
		boolean blnNodes = "-n".equals(indicator) ? true : false;
		try {
			BufferedWriter buffwriter = new BufferedWriter(new FileWriter(
					csvFile));
			File graphFolder = new File(graphFolderPath);
			GraphWriter writer = new GraphWriter();
			for (File graphFile : graphFolder.listFiles()) {
				System.out.println("Processing file " + graphFile.getName());
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(graphFile));
				if (blnNodes)
					writer.writeNodeCSV(buffwriter,
							(WeightedGraph<WikiNode, WikiEdge>) ois
									.readObject());
				else
					writer.writeEdgeCSV(buffwriter,
							(WeightedGraph<WikiNode, WikiEdge>) ois
									.readObject());
			}
			buffwriter.flush();
			buffwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
