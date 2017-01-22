package trd.algorithms.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import trd.algorithms.linkedlists.SinglyLinkedList;

public class ShortestPaths<T extends Comparable<T>> extends Graph<T> {

	public ShortestPaths(String name) {
		super(name);
	}
	public ShortestPaths(String name, Mode mode) {
		super(name, mode);
	}
	
	protected void RelaxEdge(HashMap<Integer, AlgoSpecificNode<T>> nodeMap, Edge<T> e) {
		AlgoSpecificNode<T> u = nodeMap.get(e.source);
		AlgoSpecificNode<T> v = nodeMap.get(e.target);
		if (v.node.weight > u.node.weight + e.weight) {
			v.node.weight = u.node.weight + e.weight;
			v.parent = u;
		}
	}
	public boolean ShortestPath_BellmanFord(T start, T end) {
		System.out.printf("SP-Bellman-Ford on [%s] between [%s] to [%s]:", name, start, end);

		HashMap<Integer, AlgoSpecificNode<T>> nodeMap = InitializeVertexMap(getVertexId(start), Double.MAX_VALUE, 0.0);
		Set<Integer> 	vertexSet = getVertexSet();
		List<Edge<T>>	edgeSet	= this.getAllEdges();
		boolean			retval = true;
		
		// For each vertex
		for (int i = 0; i < vertexSet.size(); i++) {
			
			// For each edge
			for (Edge<T> e : edgeSet) {
				
				// Relax based on the edge
				RelaxEdge(nodeMap, e);
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
			AlgoSpecificNode<T> v = nodeMap.get(this.getVertexId(end));
			Stack<AlgoSpecificNode<T>> path = new Stack<AlgoSpecificNode<T>>();
			while (v != null) {
				path.push(v);
				v = v.parent;
			}
			
			System.out.printf("[ ");
			AlgoSpecificNode<T> curr = null;
			while (!path.isEmpty()) {
				curr = path.pop();
				System.out.printf("%s ", getVertexById(curr.node.node));
			}
			System.out.printf("] Cost: %4.2f\n", curr == null ? 0 : curr.node.weight);
		} else {
			System.out.printf("Negative weight cycle found.\n");
		}
		return retval;
	}

	// Shortest path using DAG algorithm
	public void ShortestPath_DAG(T start, T end) {
		System.out.printf("SP-DAG on [%s] between [%s] to [%s]:", name, start, end);

		HashMap<Integer, AlgoSpecificNode<T>> nodeMap = InitializeVertexMap(getVertexId(start), Double.MAX_VALUE, 0.0);

		// Topologically sort all vertices into a singly linked list
		SinglyLinkedList<T> sll = new SinglyLinkedList<T>();
		DepthFirstSearch(nodeMap, getVertexSet(), null, (AlgoSpecificNode<T> node)-> { sll.insertHead(node.node.nodeName); return true; }, false);
		
		// For each vertex in Topologically sorted order
		for (SinglyLinkedList.Node<T> llNode = sll.getHead(); llNode != null; llNode = llNode.next) {

			// Relax every edge that is in the adjacency list of that vertex
			Integer nodeId = getVertexId(llNode.value);
			Collection<Edge<T>> eList = this.getEdgesByVertexId(nodeId);
			for (Edge<T> e : eList) {
				RelaxEdge(nodeMap, e);
			}
		}

		// Iterate over vertices and print the shortest path
		System.out.printf(" Using TopSort: {%s} ", sll);
		AlgoSpecificNode<T> v = nodeMap.get(this.getVertexId(end));
		Stack<AlgoSpecificNode<T>> path = new Stack<AlgoSpecificNode<T>>();
		while (v != null) {
			path.push(v);
			v = v.parent;
		}
		
		System.out.printf("[ ");
		AlgoSpecificNode<T> curr = null;
		while (!path.isEmpty()) {
			curr = path.pop();
			System.out.printf("%s ", getVertexById(curr.node.node));
		}
		System.out.printf("] Cost: %4.2f\n", curr == null ? 0 : curr.node.weight);
	}
	
	// Shortest path using Dijkstra's algorithm
	public List<AlgoSpecificNode<T>> ShortestPath_Dijkstra(T start, T end) {
		System.out.printf("SP-Dijkstra on [%s] between [%s] to [%s]:", name, start, end);

		HashMap<Integer, AlgoSpecificNode<T>> nodeMap = InitializeVertexMap(getVertexId(start), Double.MAX_VALUE, 0.0);
		
		// Build a priority queue using vertices of the graph
		PriorityQueue<AlgoSpecificNode<T>>	pqHeap  = new PriorityQueue<AlgoSpecificNode<T>>((AlgoSpecificNode<T> a, AlgoSpecificNode<T> b)-> a.node.weight > b.node.weight ? 1 : a.node.weight == b.node.weight? 0 : -1);
		for (AlgoSpecificNode<T> node : nodeMap.values()) {
			pqHeap.add(node);
		}
		
		// loop over entries in the priority queue to completion
		List<AlgoSpecificNode<T>> path = new ArrayList<AlgoSpecificNode<T>>(); 
		while (!pqHeap.isEmpty()) {
			AlgoSpecificNode<T> u = pqHeap.poll();
			
			// Add the node to the list of vertices in the path
			path.add(u);
			
			// break if we have reached the end node;
			if (u.node.nodeName.equals(end))
				break;
			
			// Relax edges for each element in the adjacency list of u
			Collection<Edge<T>> adjOfu = this.getEdgesByVertexId(u.node.node);
			for (Edge<T> edge : adjOfu) {
				
				// Get the target node for this edge
				AlgoSpecificNode<T> targetNode = nodeMap.get(edge.target); 

				// Remove the vertex from the priority queue
				boolean fRemoved = pqHeap.removeIf((AlgoSpecificNode<T> n)-> { return n.node.node == edge.target; });
				if (fRemoved) {
					// Adjust weight
					this.RelaxEdge(nodeMap, edge);
					
					// Reinsert into priority queue
					pqHeap.add(targetNode);
				}
			}
		}
		
		// print path
		System.out.printf("[ ");
		for (AlgoSpecificNode<T> node : path) {
			System.out.printf("%s ", getVertexById(node.node.node));
		}
		System.out.printf("] Cost: %4.2f\n", nodeMap.get(getVertexId(end)).node.weight);
		return path;
	}

	// Shortest path using BidirectionalDijkstra's algorithm
	public void ShortestPath_BiDijkstra(T start, T end) {
		System.out.printf("SP-BiDijkstra on [%s] between [%s] to [%s]:", name, start, end);

		// made to look the code clean
		Graph<T> graphF = this;
		Graph<T> graphR = this.transpose();
		
		HashMap<Integer, AlgoSpecificNode<T>> nodeMapF = InitializeVertexMap(graphF.getVertexId(start), Double.MAX_VALUE, 0.0);
		HashMap<Integer, AlgoSpecificNode<T>> nodeMapR = InitializeVertexMap(graphR.getVertexId(end),   Double.MAX_VALUE, 0.0);
		
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
		Set<Integer> setF = new HashSet<Integer>(), setR = new HashSet<Integer>();
		while (!pqHeapF.isEmpty() && !pqHeapR.isEmpty()) {
			AlgoSpecificNode<T> uF = pqHeapF.poll();
			AlgoSpecificNode<T> uR = pqHeapR.poll();
			
			// Add the node to the list of vertices in the path
			pathF.add(uF); pathR.add(uR);
			setF.add(uF.node.node); setR.add(uR.node.node);
			
			// break if the forward pointer has reached the end, the reverse the start or a common vertex has been found
			// there is a better criterion: keep a max distance from both sides and stop if the sum of the 2 max's equal edge 
			if (setF.contains(uR.node) || setR.contains(uF.node))
				break;
			
			// Relax edges for each element in the adjacency list of uF
			Collection<Edge<T>> adjOfuF = graphF.getEdgesByVertexId(uF.node.node);
			for (Edge<T> edge : adjOfuF) {
				AlgoSpecificNode<T> targetNode = nodeMapF.get(edge.target); 
				boolean fRemoved = pqHeapF.removeIf((AlgoSpecificNode<T> n)-> { return n.node.node == edge.target; });
				if (fRemoved) {
					this.RelaxEdge(nodeMapF, edge);
					pqHeapF.add(targetNode);
				}
			}
			Collection<Edge<T>> adjOfuR = graphR.getEdgesByVertexId(uR.node.node);
			for (Edge<T> edge : adjOfuR) {
				AlgoSpecificNode<T> targetNode = nodeMapR.get(edge.target); 
				boolean fRemoved = pqHeapR.removeIf((AlgoSpecificNode<T> n)-> { return n.node.node == edge.target; });
				if (fRemoved) {
					this.RelaxEdge(nodeMapR, edge);
					pqHeapR.add(targetNode);
				}
			}
		}
		
		// print path
		AlgoSpecificNode<T> lastOnF  = pathF.get(pathF.size() - 1);
		AlgoSpecificNode<T> lastOnR  = pathR.get(pathR.size() - 1);
		Double  pathCost = nodeMapF.get(lastOnF.node).node.weight + nodeMapR.get(lastOnR.node).node.weight;
		System.out.printf("[ ");
		for (AlgoSpecificNode<T> node : pathF) {
			System.out.printf("%s ", getVertexById(node.node.node));
		}
		Collections.reverse(pathR); boolean f = true;
		for (AlgoSpecificNode<T> node : pathR) {
			if (f) { f = false; continue; }
			System.out.printf("%s ", getVertexById(node.node.node));
		}
		System.out.printf("] Cost: %4.2f\n", pathCost);
	}
	public static void main(String[] args) {
		ShortestPaths<String> graph5 = GraphFactory.getCLRSPGraph1();
		System.out.println(graph5);
		graph5.ShortestPath_BellmanFord("s", "z");
		graph5.ShortestPath_DAG("s", "z");
		graph5.ShortestPath_Dijkstra("s", "z");
		graph5.ShortestPath_BiDijkstra("s", "z");
	}
}
