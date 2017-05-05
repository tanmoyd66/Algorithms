package trd.algorithms.graphs;

import trd.algorithms.graphs.Graph.Mode;

public class GraphFactory {
	public static <T extends Comparable<T>>  Graph<T> getEmptyGraph() {
		Graph<T> graph1 = new Graph<T>("Empty", Mode.Directed);
		return graph1;
	}

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

	public static Graph<String> getCLRNetworkFlowGraph1() {
		Graph<String> graph1 = new Graph<String>("CLRNetworkFlowGraph1");
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

	public static Graph<String> getEulerCircuitTestGraph() {
		Graph<String> graph1 = new Graph<String>("EulerCircuitTestGraph");
		graph1.addEdge("1", "0");
		graph1.addEdge("0", "2");
		graph1.addEdge("2", "1");
		graph1.addEdge("0", "3");
		graph1.addEdge("3", "4");
		graph1.addEdge("4", "0");
		return graph1;
	}

	public static Graph<String> getArticulationVertexTestGraph() {
		Graph<String> graph1 = new Graph<String>("ArticulationVertexTestGraph", Mode.Undirected);
		graph1.addEdge("a", "b");
		graph1.addEdge("a", "c");
		graph1.addEdge("b", "d");
		graph1.addEdge("c", "d");
		graph1.addEdge("d", "e");
		graph1.addEdge("e", "f");
		graph1.addEdge("f", "d");
		return graph1;
	}
	
	public static Graph<String> getCLRNetworkFlowGraph2() {
		Graph<String> graph1 = new Graph<String>("CLRNetworkFlowGraph2");
		graph1.addEdge("s", "u", 100.0);
		graph1.addEdge("s", "v", 100.0);
		graph1.addEdge("u", "t", 100.0);
		graph1.addEdge("u", "v", 1.0);
		graph1.addEdge("v", "t", 100.0);
		return graph1;
	}
	
	public static Graph<String> getBiPartiteGraph1() {
		Graph<String> graph1 = new Graph<String>("BiPartiteGraph1");
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

	public static Graph<String> getBiPartiteGraph2() {
		Graph<String> graph1 = new Graph<String>("BiPartiteGraph2");
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

	public static class Point {
		String		Label;
		Double		X;
		Double		Y;
		public Point(String Label, int X, int Y) {
			this.Label = Label; this.X = (Double)(double)X; this.Y = (Double)(double)Y;
		}
	}
	
	public static Graph<String> getTSPGraph1() {
		Graph<String> 	graph 	= new Graph<String>("TSP Graph1");
		Point[] 		locs	= new Point[] {
										new Point("1", 0, 6),
										new Point("2", 5, 4),
										new Point("3", 7, 5),
										new Point("4", 8, 2),
										new Point("5", 6, 1),
										new Point("6", 1, 0),
										new Point("7", 2, 3) };
		for (int i = 0; i < locs.length; i++) {
			for (int j = 0; j < locs.length; j++) {
				if (i == j)
					continue;
				Double cost = Math.sqrt((locs[i].X-locs[j].X)*(locs[i].X-locs[j].X) + 
										(locs[i].Y-locs[j].Y)*(locs[i].Y-locs[j].Y)); 
				graph.addEdge(locs[i].Label, locs[j].Label, cost);
				graph.addEdge(locs[j].Label, locs[i].Label, cost);
				graph.addNodeGarnish(locs[i].Label, locs[i]);
			}
		}
		return graph;
	}
}
