package trd.algorithms.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

import trd.algorithms.graphs.Graph.AlgoSpecificNode;
import trd.algorithms.graphs.Graph.DFSCallbackReturnTypes;
import trd.algorithms.graphs.Graph.Edge;
import trd.algorithms.linkedlists.SinglyLinkedList;

public class ShortestPathAlgorithms<T extends Comparable<T>> {

	private Graph<T>	graph;
	public ShortestPathAlgorithms(Graph<T> graph) {
		this.graph = graph;
	}
	

	// Calculate the cost of a path
	public Double GetPathCost(List<Edge<T>> ePath) { 
		return ePath.stream().map(x -> x.weight).reduce(0.0, (x, y) -> x + y);
	}
	
	protected void RelaxEdge(HashMap<Integer, AlgoSpecificNode<T>> nodeMap, Edge<T> e, Function<Edge<T>, Double> getWeightOfEdge) {
		AlgoSpecificNode<T> u = nodeMap.get(e.source);
		AlgoSpecificNode<T> v = nodeMap.get(e.target);
		if (v.node.weight > u.node.weight + getWeightOfEdge.apply(e)) {
			v.node.weight = u.node.weight + getWeightOfEdge.apply(e);
			v.parent = u;
		}
	}

	// Bellman Ford Algorithm:
	//		Iterate over all vertices and edges and keep relaxing edges
	//		The shortest path will be guaranteed only after all the edges and vertices are seen
	public List<Edge<T>> ShortestPath_BellmanFord(T start, T end, Function<Edge<T>, Double> getWeightOfEdge) {

		HashMap<Integer, AlgoSpecificNode<T>> nodeMap = graph.InitializeVertexMap(graph.getVertexId(start), Double.MAX_VALUE, 0.0);
		Set<Integer> 	vertexSet = graph.getVertexSet();
		List<Edge<T>>	edgeSet	= graph.getAllEdges();
		boolean			retval = true;
		
		// For each vertex
		for (int i = 0; i < vertexSet.size(); i++) {
			
			// For each edge
			for (Edge<T> e : edgeSet) {
				
				// Relax based on the edge
				RelaxEdge(nodeMap, e, getWeightOfEdge);
			}
		}
		
		// Check for negative weight cycles
		for (Edge<T> e : edgeSet) {
			AlgoSpecificNode<T> u = nodeMap.get(e.source);
			AlgoSpecificNode<T> v = nodeMap.get(e.target);
			if (v.node.weight > u.node.weight + e.weight)
				retval = false;
		}
		
		// Iterate over vertices and print the shortest path
		if (retval) {
			LinkedList<T> vPath = new LinkedList<T>();
			AlgoSpecificNode<T> v = nodeMap.get(graph.getVertexId(end));
			while (v != null) {
				vPath.addFirst(graph.getVertexById(v.node.node));
				v = v.parent;
			}
			return graph.VertexPathToEdgePath(vPath);
		} else {
			System.out.printf("Negative weight cycle found.\n");
			return new LinkedList<Edge<T>>();
		}
	}

	// Shortest path using Topological Sort
	public List<Edge<T>> ShortestPath_DAG(T start, T end, Function<Edge<T>, Double> getWeightOfEdge) {
		HashMap<Integer, AlgoSpecificNode<T>> nodeMap = graph.InitializeVertexMap(graph.getVertexId(start), Double.MAX_VALUE, 0.0);

		// Topologically sort all vertices into a singly linked list
		SinglyLinkedList<T> sll = new SinglyLinkedList<T>();
		graph.DepthFirstSearch(nodeMap, graph.getVertexSet(), null, 
						(AlgoSpecificNode<T> node)-> { sll.insertHead(node.node.nodeName); return DFSCallbackReturnTypes.Continue; }, false);
		
		// For each vertex in Topologically sorted order
		for (SinglyLinkedList.Node<T> llNode = sll.getHead(); llNode != null; llNode = llNode.next) {

			// Relax every edge that is in the adjacency list of that vertex
			Integer nodeId = graph.getVertexId(llNode.value);
			Collection<Edge<T>> eList = graph.getEdgesByVertexId(nodeId);
			for (Edge<T> e : eList) {
				RelaxEdge(nodeMap, e, getWeightOfEdge);
			}
		}

		// Iterate over vertices and print the shortest path
		// System.out.printf(" Using TopSort: {%s} ", sll);
		LinkedList<T> vPath = new LinkedList<T>();
		AlgoSpecificNode<T> v = nodeMap.get(graph.getVertexId(end));
		while (v != null) {
			vPath.addFirst(graph.getVertexById(v.node.node));
			v = v.parent;
		}
		return graph.VertexPathToEdgePath(vPath);
	}
	
	private static Double DebuggableMaxDouble = 99999.99;
	
	// Shortest path using Dijkstra's algorithm
	public List<Edge<T>> ShortestPath_Dijkstra(T start, T end, Function<Edge<T>, Boolean> qualifies, Function<Edge<T>, Double> getWeightOfEdge) {

		HashMap<Integer, AlgoSpecificNode<T>> nodeMap = graph.InitializeVertexMap(graph.getVertexId(start), DebuggableMaxDouble, 0.0);
		
		// Build a priority queue using vertices of the graph
		PriorityQueue<AlgoSpecificNode<T>>	pqHeap  = new PriorityQueue<AlgoSpecificNode<T>>((a, b)-> a.node.weight > b.node.weight ? 1 : a.node.weight == b.node.weight? 0 : -1);
		for (AlgoSpecificNode<T> node : nodeMap.values()) {
			pqHeap.add(node);
		}
		
		// loop over entries in the priority queue to completion
		//List<AlgoSpecificNode<T>> path = new ArrayList<AlgoSpecificNode<T>>(); 
		while (!pqHeap.isEmpty()) {
			AlgoSpecificNode<T> u = pqHeap.poll();
			
			// Add the node to the list of vertices in the path
			//path.add(u);
			
			// break if we have reached the end node;
			if (u.node.nodeName.equals(end))
				break;
			
			// Relax edges for each element in the adjacency list of u
			Collection<Edge<T>> adjOfu = graph.getEdgesByVertexId(u.node.node);
			for (Edge<T> edge : adjOfu) {

				Boolean considerEdge = qualifies == null ? true : qualifies.apply(edge);
				if (!considerEdge)
					continue;

				// Get the target node for this edge
				AlgoSpecificNode<T> targetNode = nodeMap.get(edge.target); 

				// Remove the vertex from the priority queue
				boolean fRemoved = pqHeap.removeIf((AlgoSpecificNode<T> n)-> { return n.node.node == edge.target; });
				if (fRemoved) {
					
					// Adjust weight
					this.RelaxEdge(nodeMap, edge, getWeightOfEdge);
					
					// Reinsert into priority queue
					pqHeap.add(targetNode);
				}
			}
		}
		
		LinkedList<T> vPath = new LinkedList<T>();
		for (AlgoSpecificNode<T> endNode = nodeMap.get(graph.getVertexId(end)); endNode != null; endNode = endNode.parent) {
			vPath.add(endNode.node.nodeName);
		}
		Collections.reverse(vPath);
		return vPath.size() == 1 ? new ArrayList<Edge<T>>() : graph.VertexPathToEdgePath(vPath);
	}

	// Shortest path using BidirectionalDijkstra's algorithm
	public List<Edge<T>> ShortestPath_BiDijkstra(T start, T end, Function<Edge<T>, Double> getWeightOfEdge) {

		// made to look the code clean
		Graph<T> graphF = graph;
		Graph<T> graphR = graph.transpose();
		
		HashMap<Integer, AlgoSpecificNode<T>> nodeMapF = graph.InitializeVertexMap(graphF.getVertexId(start), Double.MAX_VALUE, 0.0);
		HashMap<Integer, AlgoSpecificNode<T>> nodeMapR = graph.InitializeVertexMap(graphR.getVertexId(end),   Double.MAX_VALUE, 0.0);
		
		// Build priority queues in both directions
		PriorityQueue<AlgoSpecificNode<T>>	pqHeapF  = new PriorityQueue<AlgoSpecificNode<T>>((AlgoSpecificNode<T> a, AlgoSpecificNode<T> b)-> a.node.weight > b.node.weight ? 1 : a.node.weight == b.node.weight? 0 : -1);
		PriorityQueue<AlgoSpecificNode<T>>	pqHeapR  = new PriorityQueue<AlgoSpecificNode<T>>((AlgoSpecificNode<T> a, AlgoSpecificNode<T> b)-> a.node.weight > b.node.weight ? 1 : a.node.weight == b.node.weight? 0 : -1);
		for (AlgoSpecificNode<T> node : nodeMapF.values()) {
			pqHeapF.add(node);
		}
		for (AlgoSpecificNode<T> node : nodeMapR.values()) {
			pqHeapR.add(node);
		}
				
		// loop over entries in the priority queues to completion
		List<AlgoSpecificNode<T>> pathF = new ArrayList<AlgoSpecificNode<T>>(); 
		List<AlgoSpecificNode<T>> pathR = new ArrayList<AlgoSpecificNode<T>>();
		HashSet<Integer> setF = new HashSet<Integer>(), setR = new HashSet<Integer>();
		while (!pqHeapF.isEmpty() && !pqHeapR.isEmpty()) {
			AlgoSpecificNode<T> uF = pqHeapF.poll();
			AlgoSpecificNode<T> uR = pqHeapR.poll();
			
			// Stopping Condition:
			//		(1) The backward pointer has reached the start
			//		(2) The forward  pointer has reached the end
			//		(3) Both queues popped the same node (it does not have to be on the shortest path)
			// There is a better criterion: keep a max distance from both sides and stop if the sum of the 2 max's equal edge 
			if (uF.node.nodeName.compareTo(end) == 0   ||
				uR.node.nodeName.compareTo(start) == 0)
				break;
			if (uF.node.nodeName.compareTo(uR.node.nodeName) == 0) {
				pathF.add(uF); pathR.add(uR);
				break;
			}
			
			// Add the node to the list of vertices in the path
			pathF.add(uF); pathR.add(uR);
			setF.add(uF.node.node); setR.add(uR.node.node);
			
			
			// Relax edges for each element in the adjacency list of uF
			Collection<Edge<T>> adjOfuF = graphF.getEdgesByVertexId(uF.node.node);
			for (Edge<T> edge : adjOfuF) {
				AlgoSpecificNode<T> targetNode = nodeMapF.get(edge.target); 
				boolean fRemoved = pqHeapF.removeIf((AlgoSpecificNode<T> n)-> { return n.node.node == edge.target; });
				if (fRemoved) {
					this.RelaxEdge(nodeMapF, edge, getWeightOfEdge);
					pqHeapF.add(targetNode);
				}
			}
			Collection<Edge<T>> adjOfuR = graphR.getEdgesByVertexId(uR.node.node);
			for (Edge<T> edge : adjOfuR) {
				AlgoSpecificNode<T> targetNode = nodeMapR.get(edge.target); 
				boolean fRemoved = pqHeapR.removeIf((AlgoSpecificNode<T> n)-> { return n.node.node == edge.target; });
				if (fRemoved) {
					this.RelaxEdge(nodeMapR, edge, getWeightOfEdge);
					pqHeapR.add(targetNode);
				}
			}
		}
		
		// print path
		AlgoSpecificNode<T> lastOnF  = pathF.get(pathF.size() - 1);
		AlgoSpecificNode<T> lastOnR  = pathR.get(pathR.size() - 1);
		
		LinkedList<T> vPath = new LinkedList<T>();
		for (AlgoSpecificNode<T> node : pathF) {
			vPath.add(node.node.nodeName);
		}
		Collections.reverse(pathR); boolean f = true;
		for (AlgoSpecificNode<T> node : pathR) {
			if (f) { f = false; continue; }
			vPath.add(node.node.nodeName);
		}
		return graph.VertexPathToEdgePath(vPath);
	}
	
	
	public static void main(String[] args) {
		
		Graph<String> graph5 = GraphFactory.getCLRSPGraph1();
		ShortestPathAlgorithms<String> spAlgos = new ShortestPathAlgorithms<String>(graph5);
		System.out.println(graph5);

		List<Edge<String>> ePath = null;
		
		ePath = spAlgos.ShortestPath_BellmanFord("s", "z", (x)->x.weight);
		System.out.printf("SP-Bellman-Ford on [%s] between [%s] to [%s]: %s with Cost:%4.2f\n", graph5.name, "s", "z", ePath, spAlgos.GetPathCost(ePath));
		
		ePath = spAlgos.ShortestPath_DAG("s", "z", (x)->x.weight);
		System.out.printf("SP-Topological  on [%s] between [%s] to [%s]: %s with Cost:%4.2f\n", graph5.name, "s", "z", ePath, spAlgos.GetPathCost(ePath));

		ePath = spAlgos.ShortestPath_Dijkstra("s", "z", null, (x)->x.weight);
		System.out.printf("SP-Dijkstra     on [%s] between [%s] to [%s]: %s with Cost:%4.2f\n", graph5.name, "s", "z", ePath, spAlgos.GetPathCost(ePath));

		ePath = spAlgos.ShortestPath_BiDijkstra("s", "z", (x)->x.weight);
		System.out.printf("SP-BiDijkstra   on [%s] between [%s] to [%s]: %s with Cost:%4.2f\n", graph5.name, "s", "z", ePath, spAlgos.GetPathCost(ePath));
	}
}
