package trd.Algorithms.graphs;

import trd.Algorithms.graphs.Graph.Mode;

public class GraphFactory {
	public static Graph<String> getSimpleUGraph() {
		Graph<String> graph1 = new Graph<String>("Simple", Mode.Undirected);
		graph1.addEdge("a", "b");
		graph1.addEdge("b", "c");
		graph1.addEdge("c", "a");
		return graph1;
	}

	public static Graph<String> getCLRMSTUGraph() {
		Graph<String> graph1 = new Graph<String>("CLRMSTUGraph", Mode.Undirected);
		graph1.addEdge("a", "b", 4.0);
		graph1.addEdge("a", "h", 8.0);
		graph1.addEdge("b", "h", 11.0);
		graph1.addEdge("b", "c", 8.0);
		graph1.addEdge("c", "i", 2.0);
		graph1.addEdge("c", "f", 4.0);
		graph1.addEdge("c", "d", 7.0);
		graph1.addEdge("d", "f", 14.0);
		graph1.addEdge("d", "e", 9.0);
		graph1.addEdge("e", "f", 10.0);
		graph1.addEdge("f", "g", 2.0);
		graph1.addEdge("g", "h", 1.0);
		graph1.addEdge("g", "i", 6.0);
		graph1.addEdge("h", "i", 7.0);
		return graph1;
	}

	public static Graph<String> getNetworkFlowGraph1() {
		Graph<String> graph1 = new Graph<String>("NetworkFlowGraph1");
		graph1.addEdge("s", "v", 1.0);
		graph1.addEdge("s", "w", 100.0);
		graph1.addEdge("v", "w", 100.0);
		graph1.addEdge("w", "v", 1.0);
		graph1.addEdge("v", "t", 100.0);
		graph1.addEdge("w", "t", 1.0);
		return graph1;
	}

	public static Graph<String> getCLRDFSGraph1() {
		Graph<String> graph1 = new Graph<String>("CLRDFSGraph1");
		graph1.addEdge("u", "x", 1.0);
		graph1.addEdge("x", "v", 1.0);
		graph1.addEdge("u", "v", 1.0);
		graph1.addEdge("v", "y", 1.0);
		graph1.addEdge("y", "x", 1.0);
		graph1.addEdge("w", "y", 1.0);
		graph1.addEdge("w", "z", 1.0);
		graph1.addEdge("z", "z", 1.0);
		return graph1;
	}

	public static Graph<String> getCLRSCCGraph() {
		Graph<String> graph1 = new Graph<String>("CLRSCCGraph");
		graph1.addEdge("a", "b", 1.0);
		graph1.addEdge("b", "e", 1.0);
		graph1.addEdge("b", "f", 1.0);
		graph1.addEdge("b", "c", 1.0);
		graph1.addEdge("c", "g", 1.0);
		graph1.addEdge("c", "d", 1.0);
		graph1.addEdge("d", "c", 1.0);
		graph1.addEdge("d", "h", 1.0);
		graph1.addEdge("e", "a", 1.0);
		graph1.addEdge("e", "f", 1.0);
		graph1.addEdge("f", "g", 1.0);
		graph1.addEdge("g", "f", 1.0);
		graph1.addEdge("g", "h", 1.0);
		graph1.addEdge("h", "h", 1.0);
		return graph1;
	}
	
	public static Graph<String> getCLRSPGraph1() {
		Graph<String> graph1 = new Graph<String>("getCLRSPGraph1");
		graph1.addEdge("s", "t", 10.0);
		graph1.addEdge("s", "y", 5.0);
		graph1.addEdge("t", "y", 2.0);
		graph1.addEdge("t", "x", 1.0);
		graph1.addEdge("y", "t", 3.0);
		graph1.addEdge("y", "x", 9.0);
		graph1.addEdge("y", "z", 2.0);
		graph1.addEdge("x", "z", 4.0);
		graph1.addEdge("z", "x", 6.0);
		graph1.addEdge("z", "s", 7.0);
		return graph1;
	}

}
