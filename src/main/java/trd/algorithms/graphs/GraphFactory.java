package trd.algorithms.graphs;

import trd.algorithms.graphs.Graph.Mode;

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
	
	public static ShortestPaths<String> getCLRSPGraph1() {
		ShortestPaths<String> graph1 = new ShortestPaths<String>("getCLRSPGraph1");
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

	public static NetworkFlow<String> getCLRNetworkFlowGraph1() {
		NetworkFlow<String> graph1 = new NetworkFlow<String>("CLRNetworkFlowGraph1");
		graph1.addEdge("s", "v1", 16.0);
		graph1.addEdge("s", "v2", 13.0);
		graph1.addEdge("v1","v3", 12.0);
		graph1.addEdge("v2","v1", 4.0);
		graph1.addEdge("v2","v4", 14.0);
		graph1.addEdge("v3","v2", 9.0);
		graph1.addEdge("v3","t",  20.0);
		graph1.addEdge("v4","v3", 7.0);
		graph1.addEdge("v4","t",  4.0);
		return graph1;
	}

	public static NetworkFlow<String> getCLRNetworkFlowGraph2() {
		NetworkFlow<String> graph1 = new NetworkFlow<String>("CLRNetworkFlowGraph2");
		graph1.addEdge("s", "u", 100.0);
		graph1.addEdge("s", "v", 100.0);
		graph1.addEdge("u", "t", 100.0);
		graph1.addEdge("u", "v", 1.0);
		graph1.addEdge("v", "t", 100.0);
		return graph1;
	}
	
	public static BipartiteMatching<String> getBiPartiteGraph1() {
		BipartiteMatching<String> graph1 = new BipartiteMatching<String>("BiPartiteGraph1");
		graph1.addEdge("1", "7");
		graph1.addEdge("1", "6");
		graph1.addEdge("2", "8");
		graph1.addEdge("2", "6");
		graph1.addEdge("2", "5");
		graph1.addEdge("3", "8");
		graph1.addEdge("3", "6");
		graph1.addEdge("3", "5");
		graph1.addEdge("4", "5");
		return graph1;
	}

	public static BipartiteMatching<String> getBiPartiteGraph2() {
		BipartiteMatching<String> graph1 = new BipartiteMatching<String>("BiPartiteGraph2");
		graph1.addEdge("B", "1");
		graph1.addEdge("B", "4");
		graph1.addEdge("E", "7");
		graph1.addEdge("E", "3");
		graph1.addEdge("E", "6");
		graph1.addEdge("J", "2");
		graph1.addEdge("J", "5");
		graph1.addEdge("J", "4");
		graph1.addEdge("L", "7");
		graph1.addEdge("L", "2");
		graph1.addEdge("T", "7");
		graph1.addEdge("T", "6");
		graph1.addEdge("T", "5");
		graph1.addEdge("A", "3");
		graph1.addEdge("A", "6");
		graph1.addEdge("R", "6");
		graph1.addEdge("R", "7");
		return graph1;
	}
	
}
