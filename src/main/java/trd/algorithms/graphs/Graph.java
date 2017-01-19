package trd.algorithms.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import trd.algorithms.datastructures.DynamicSet;
import trd.algorithms.linkedlists.SinglyLinkedList;

public class Graph<T extends Comparable<T>> {
	public enum Mode 		{ Directed, Undirected };
	public enum EdgeType 	{ Graph, Augmented };
	
	public static class Edge<T extends Comparable<T>> {
		Integer		source;
		Integer 	target;
		Double  	weight;
		EdgeType	type;
		Graph<T>	graph;
		
		public Edge(Graph<T> graph, Integer source, Integer target, Double weight, EdgeType type) {
			this.graph = graph; this.source = source; this.target = target; this.weight = weight; this.type = type;
		}
		public Edge(Graph<T> graph, Integer source, Integer target, Double weight) {
			this.graph = graph; this.source = source; this.target = target; this.weight = weight; this.type = EdgeType.Graph;
		}
		public Edge(Graph<T> graph, Integer source, Integer target) {
			this.graph = graph; this.source = source; this.target = target; this.weight = 0.0; this.type = EdgeType.Graph;
		}
		public String toString() {
			return String.format("(%s,%s:%4.2f) ", graph.getVertexById(source), graph.getVertexById(target), weight);
		}
	}

	// General purpose node structure used by all
	public enum Color { white, gray, black };
	public static class Node<T extends Comparable<T>> {
		Color		color;
		Node<T>		parent;
		int			start;
		int			end;
		Integer		node;
		T			nodeName;
		Graph<T>	graph;
		Double		weight;
		
		public Node(Graph<T> graph, Integer node, int start, Node<T> parent) {
			this.graph = graph; this.node = node; this.start = start; this.parent = parent;
			this.nodeName = graph.getVertexById(node); 
			color = Color.white; end = start;  
		}
		public String getColorString() {
			return color == Color.white ? "-W" : color == Color.gray ? "-G" : "-B";
		}		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("(%s:[%d,%d%s%s])", graph.getVertexById(node), start, end, getColorString(), weight == 0.0 ? "": String.format("-%4.2f", weight)));
			return sb.toString();
		}
	}	

	String										name		= "";
	Mode										mode		= Mode.Directed;
	Integer										startNode	= 1;
	Integer										maxVertex 	= 0;
	HashMap<T,Integer> 							vertexMap 	= new HashMap<T,Integer>();
	HashMap<Integer,T> 							invVertMap 	= new HashMap<Integer,T>();
	HashMap<Integer,HashMap<Integer,Edge<T>>>	adjList		= new HashMap<Integer,HashMap<Integer,Edge<T>>>();
	List<Edge<T>>								edgeList	= new ArrayList<Edge<T>>();
	
	public String printEdge(Edge<T> e) {
		String ret = String.format("(%s,%s)", invVertMap.get(e.source), invVertMap.get(e.target));
		return ret;
	}
	public String printVertex(Integer i) {
		String ret = String.format("%s", invVertMap.get(i));
		return ret;
	}
	
	private Integer getVertexId(T source) {
		Integer sourceId = vertexMap.get(source);
		if (sourceId == null) {
			sourceId = ++maxVertex;
			vertexMap.put(source, sourceId);
			invVertMap.put(sourceId, source);
		}
		return sourceId;
	}
	
	private T getVertexById(Integer id) {
		return invVertMap.get(id);
	}
	
	private Set<Integer> getVertexSet() {
		return invVertMap.keySet();
	}
	
	private Collection<Edge<T>> getEdgesByVertexId(Integer id) {
		HashMap<Integer,Edge<T>> edgeMap = adjList.get(id);
		if (edgeMap != null) {
			return edgeMap.values();
		}
		return Collections.emptyList();
	}

	private List<Edge<T>> getAllEdges() {
		if (mode == Mode.Directed) {
			List<Edge<T>> ret = new ArrayList<Edge<T>>();
			Set<Map.Entry<Integer,HashMap<Integer,Edge<T>>>> meSet = adjList.entrySet();
			for (Map.Entry<Integer,HashMap<Integer,Edge<T>>> me : meSet) {
				HashMap<Integer,Edge<T>> edgeMap = me.getValue();
				if (edgeMap != null) {
					Set<Map.Entry<Integer, Edge<T>>> edgeSet = edgeMap.entrySet();
					for (Map.Entry<Integer, Edge<T>> meEdge : edgeSet) {
						ret.add(meEdge.getValue());
					}
				}
			}
			return ret;
		} else {
			return edgeList;
		}
	}
	
	private void _addEdgeInternal(Integer sourceId, Integer targetId, Edge<T> edge) {
		HashMap<Integer,Edge<T>> edgeList = adjList.get(sourceId);
		if (edgeList == null) {
			edgeList = new HashMap<Integer,Edge<T>>();
			adjList.put(sourceId, edgeList);
		}
		edgeList.put(targetId, edge);
	}

	private void _addEdge(T source, T target, Double weight, EdgeType type) {
		Integer sourceId = getVertexId(source);
		Integer targetId = getVertexId(target);
		Edge<T> edge	 = new Edge<>(this, sourceId, targetId, weight, type);
		_addEdgeInternal(sourceId, targetId, edge);
		
		if (mode == Mode.Undirected) {
			Edge<T> edgeR	 = new Edge<>(this, targetId, sourceId, weight, type);
			_addEdgeInternal(targetId, sourceId, edgeR);
			edgeList.add(edge);
		}
	}
	
	// Adding Edges
	public void addEdge(T source, T target) {
		_addEdge(source, target, 0.0, EdgeType.Graph);
	}
	public void addEdge(T source, T target, Double weight) {
		_addEdge(source, target, weight, EdgeType.Graph);
	}
	public void addEdge(T source, T target, Double weight, EdgeType type) {
		_addEdge(source, target, weight, type);
	}
	
	// Construction
	public Graph(String name) {
		this.name = name;
		this.mode = Mode.Directed;
	}
	public Graph(String name, Mode mode) {
		this.name = name;
		this.mode = mode;
	}
	
	// Transposition
	@SuppressWarnings("unchecked")
	public Graph<T> transpose() {
		if (mode == Mode.Undirected)
			return this;
		
		Graph<T> transpose = new Graph<T>(name + "-transpose");
		transpose.startNode = startNode;
		transpose.vertexMap = (HashMap<T, Integer>)vertexMap.clone();
		transpose.invVertMap = (HashMap<Integer, T>)invVertMap.clone();
		
		Set<Map.Entry<Integer,HashMap<Integer,Edge<T>>>> meSet = adjList.entrySet();
		for (Map.Entry<Integer,HashMap<Integer,Edge<T>>> me : meSet) {
			HashMap<Integer,Edge<T>> edgeMap = me.getValue();
			if (edgeMap != null) {
				Set<Map.Entry<Integer, Edge<T>>> edgeSet = edgeMap.entrySet();
				for (Map.Entry<Integer, Edge<T>> meEdge : edgeSet) {
					Edge<T> edge = new Edge<>(this, meEdge.getValue().target, meEdge.getValue().source, meEdge.getValue().weight, meEdge.getValue().type);
					transpose._addEdgeInternal(meEdge.getValue().target, meEdge.getValue().source, edge);
				}
			}
		}		
		return transpose;
	}
	
	// print 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<Map.Entry<Integer,HashMap<Integer,Edge<T>>>> meSet = adjList.entrySet();
		sb.append("\n------------------------------");
		sb.append("\n"); sb.append(name);
		sb.append("\n------------------------------");
		for (Map.Entry<Integer,HashMap<Integer,Edge<T>>> me : meSet) {
			sb.append(String.format("\n[%s(%d)]: ", invVertMap.get(me.getKey()), me.getKey()));
			HashMap<Integer,Edge<T>> edgeMap = me.getValue();
			if (edgeMap != null) {
				Set<Map.Entry<Integer, Edge<T>>> edgeSet = edgeMap.entrySet();
				for (Map.Entry<Integer, Edge<T>> meEdge : edgeSet) {
					sb.append(String.format("[%s:%4.2f] ", invVertMap.get(meEdge.getKey()), meEdge.getValue().weight));
				}
			}
		}
		sb.append("\n------------------------------");
		return sb.toString();
	}
	
	// General purpose initialization of vertex set
	private HashMap<Integer, Node<T>> InitializeVertexMap(Integer startId, Double defaultWeight, Double startVertexWeight) {
		HashMap<Integer, Node<T>> nodeMap = new HashMap<Integer, Node<T>>();
		Set<Integer> vertexSet = getVertexSet();
		for (Integer v : vertexSet) {
			Node<T> vNode = new Node<>(this, v, 0, null); 
			vNode.weight = defaultWeight;
			nodeMap.put(v, vNode);
		}
		Node<T> s = nodeMap.get(startId);
		s.weight = startVertexWeight;
		return nodeMap;
	}

	// Depth first search of general graphs
	private void DFSVisit(Node<T> dfsNode, AtomicInteger time, Map<Integer,Node<T>> nodeMap, Consumer<Node<T>> processNode, Set<Node<T>> forestNodes) {
		
		// White Node has just been discovered
		time.incrementAndGet();
		//nodeMap.put(dfsNode.node, dfsNode);
		
		// set the color stating that we have started exploring
		dfsNode.color = Color.gray;
		dfsNode.start = time.get();

		// explore children recursively
		Collection<Edge<T>> edges = getEdgesByVertexId(dfsNode.node);
		for (Edge<T> edge : edges) {
			Node<T> dfsnTarget = nodeMap.get(edge.target);
			if (dfsnTarget == null || dfsnTarget.color == Color.white) {
				if (dfsnTarget == null)
					dfsnTarget = new Node<>(this, edge.target, time.get(), dfsNode);
				forestNodes.add(dfsnTarget);
				DFSVisit(dfsnTarget, time, nodeMap, processNode, forestNodes);
			}
		}
		
		// set complete
		dfsNode.color = Color.black;
		dfsNode.end = time.incrementAndGet();

		// process the node
		processNode.accept(dfsNode);
	}

	private void DepthFirstSearch(Map<Integer,Node<T>> nodeMap, Consumer<Node<T>> processNode, boolean fPrint) {
		AtomicInteger time = new AtomicInteger(0);

		// Perform Depth First Search
		int treeId = 0;
		Set<Integer> vertexSet = getVertexSet();
		for (Integer vertex : vertexSet) {
			Node<T> node = nodeMap.get(vertex);
			if (node == null || node.color == Color.white) {
				Node<T> root = node == null ? new Node<T>(this, vertex, time.get(), null) : node;				
				node.start = time.get();
				
				Set<Node<T>> forestNodes = new HashSet<Node<T>>();
				forestNodes.add(root);
				DFSVisit(root, time, nodeMap, processNode, forestNodes);

				if (fPrint) {
					List<Node<T>> _forestNodesList = new ArrayList<>(forestNodes);
					Collections.sort(_forestNodesList, (Node<T> a, Node<T> b) -> a.start - b.start);
					System.out.printf("(%d):{ ", ++treeId);
					for (Node<T> thisNode: _forestNodesList) {
						System.out.printf("%s[%d:%d] ", thisNode.nodeName, thisNode.start, thisNode.end);
					}
					System.out.printf("} ", treeId);
				}
			}
		}
	}	
	
	public void DepthFirstSearch(Consumer<Node<T>> processNode) {
		System.out.printf("DFS on [%s]:", name);
				
		Map<Integer,Node<T>> nodeMap = InitializeVertexMap(1, 0.0, 0.0);
		DepthFirstSearch(nodeMap, processNode, true);

		System.out.println();
	}
	
	public void StronglyConnectedComponents() {
		Map<Integer,Node<T>> nodeMap = InitializeVertexMap(1, 0.0, 0.0);
		AtomicInteger time = new AtomicInteger(0);
		
		System.out.printf("SCC on [%s]:", name);

		// Perform Depth First Search on every vertex
		Set<Integer> vertexSet = getVertexSet();
		for (Integer vertex : vertexSet) {
			Node<T> node = nodeMap.get(vertex);
			if (node == null || node.color == Color.white) {
				Set<Node<T>> forestNodes = new HashSet<Node<T>>();
				forestNodes.add(node);
				DFSVisit(node, time, nodeMap, (a)->{}, forestNodes);
			}
		}
		
		// Sort the nodes of the forests in order of finish time
		List<Node<T>> nodeList = new ArrayList<Node<T>>(nodeMap.values());
		Collections.sort(nodeList, (Node<T> a, Node<T> b) -> b.end - a.end);

		// Get the transpose of the graph and do book-keeping to clear vertices
		int treeId = 0; time.set(0);
		Graph<T> transposed = transpose();
		
		// Reset the node-map for the second DFS run
		nodeMap = InitializeVertexMap(1, 0.0, 0.0);
		
		// Perform DFS on the transposed graph in order of the nodes in the nodeList
		for (Node<T> nodeOther : nodeList) {
			Node<T> node = nodeMap.get(nodeOther.node);
			if (node == null || node.color == Color.white) {
				Node<T> root = node == null ? new Node<T>(this, nodeOther.node, time.get(), null) : node;
				Set<Node<T>> forestNodes = new HashSet<Node<T>>();
				forestNodes.add(root);
				transposed.DFSVisit(root, time, nodeMap, (a)->{}, forestNodes);
				System.out.printf("(%d):{ ", ++treeId);
				for (Node<T> thisNode: forestNodes) {
					System.out.printf("%s[%d:%d] ", thisNode.nodeName, thisNode.start, thisNode.end);
				}
				System.out.printf("} ", treeId);
			}
		}
		System.out.println();
	}	

	// Topological Sorting
	public void TopologicalSort() {
		SinglyLinkedList<T> sll = new SinglyLinkedList<T>();
		DepthFirstSearch((Node<T> node)-> { sll.insertHead(node.nodeName); });
		System.out.printf("TopSort on [%s]:%s\n", name, sll);
	}
	
	// Breadth first search of general graphs
	public void BreadthFirstSearch(Consumer<Node<T>> processNode) {
		int time  = 1;
		Queue<Node<T>> bfsQueue = new ConcurrentLinkedQueue<Node<T>>();
		Map<Integer,Node<T>> nodeMap = new HashMap<Integer,Node<T>>();
		Node<T> thisNode = new Node<>(this, startNode, time, null);
		
		System.out.printf("BFS on [%s]:", name);

		bfsQueue.add(thisNode); nodeMap.put(thisNode.node, thisNode);
		
		while (!bfsQueue.isEmpty()) {
			++time;
			Node<T> curr = bfsQueue.poll();
			
			// Mark the edge as being processed
			curr.color = Color.gray;
			Collection<Edge<T>> edges = getEdgesByVertexId(curr.node);			
			
			// Process the node
			processNode.accept(curr);

			for (Edge<T> edge : edges) {

				Node<T> target = nodeMap.get(edge.target);
				if (target == null) {
					target = new Node<T>(this, edge.target, time, null);
					nodeMap.put(edge.target, target);
					bfsQueue.add(target);
				}
			}
		}
		System.out.println();
	}
	
	// Minimal Spanning Tree using Kruskal's Algorithm
	public void MinimalSpanningTree_Kruskal() {
		System.out.printf("MST-Kruskal on [%s]:", name);
		
		// Get the list of edges and sort them in increasing weight
		List<Edge<T>> edgeList = getAllEdges();
		Collections.sort(edgeList, (Edge<T> a, Edge<T> b)-> a.weight > b.weight ? 1 : a.weight == b.weight? 0 : -1);
		
		// Set the data structures to do the union find and hold the tree-edges
		Set<Edge<T>> treeEdges = new HashSet<Edge<T>>();
		DynamicSet<Integer>  mstSet = new DynamicSet<Integer>();
		Set<Integer> vertexSet = getVertexSet();
		
		// Initialize the DynamicSet data structure with every vertex
		for (Integer v : vertexSet)
			mstSet.MakeSet(v);
		
		// Loop over edges.
		for (Edge<T> e : edgeList) {

			DynamicSet.Node<Integer> sourceSet = mstSet.FindSet(e.source);
			DynamicSet.Node<Integer> targetSet = mstSet.FindSet(e.target);
			
			// if the two sets are equal then you cannot add (source,target) as it will cause a cycle.
			// otherwise 
			if (sourceSet != targetSet) {
				
				// add the edge to the tree
				treeEdges.add(e);
				
				// merge the vertices in each of the two trees
				mstSet.Union(e.source, e.target);
			}
		}
		
		System.out.printf("[ ");
		for (Edge<T> e: treeEdges) {
			System.out.printf("(%s,%s) ", getVertexById(e.source), getVertexById(e.target));
		}
		System.out.printf("]\n");
	}

	// Minimal Spanning Tree using Prim's Algorithm
	public void MinimalSpanningTree_Prim() {
		System.out.printf("MST-Prim on [%s]:", name);
		
		Set<Edge<T>> treeEdges = new HashSet<Edge<T>>();

		HashMap<Integer, Node<T>> 	nodeMap = new HashMap<Integer, Node<T>>();
		PriorityQueue<Node<T>>		pqHeap  = new PriorityQueue<Node<T>>((Node<T> a, Node<T> b)-> a.weight > b.weight ? 1 : a.weight == b.weight? 0 : -1);
		
		// Get a list of all vertices and create a min-priority queue with all vertices with weights set to a large number
		Set<Integer> vertexSet = getVertexSet();
		for (Integer v : vertexSet) {
			Node<T> vNode = new Node<>(this, v, 0, null); 
			vNode.weight = v == startNode ? 0 : Double.MAX_VALUE;
			nodeMap.put(v, vNode);
			pqHeap.add(vNode);
		}
		
		// Loop over the queue, 
		while (!pqHeap.isEmpty()) {
			
			// extracting the vertex with the minimal vertex
			Node<T> curr = pqHeap.poll();
			
			// loop over the adj-list of curr
			Collection<Edge<T>> edges = getEdgesByVertexId(curr.node);
			for (Edge<T> e : edges) {
				Node<T> targetNode = nodeMap.get(e.target); 
				
				// if the vertex is in the queue and its weight is less than that of the edge
				if (pqHeap.contains(targetNode) && e.weight < targetNode.weight) {
					
					// Remove the vertex from the priority queue
					pqHeap.removeIf((Node<T> n)-> { return n.node == e.target; });
					
					// Set its weights
					targetNode.weight = e.weight;
					targetNode.parent = curr;
					
					// Re-insert to queue
					pqHeap.add(targetNode);
				}
			}
		}
		
		// Print the edges. The tree edges correspond to those vertices that have a non-null parent
		System.out.printf("[ ");
		for (HashMap.Entry<Integer, Node<T>> me : nodeMap.entrySet()) {
			Node<T> currVertex = me.getValue();
			if (currVertex.parent != null) {
				System.out.printf("(%s,%s) ", getVertexById(currVertex.parent.node), getVertexById(currVertex.node));
			}
		}
		System.out.printf("]\n");
	}

	private void RelaxEdge(HashMap<Integer, Node<T>> nodeMap, Edge<T> e) {
		Node<T> u = nodeMap.get(e.source);
		Node<T> v = nodeMap.get(e.target);
		if (v.weight > u.weight + e.weight) {
			v.weight = u.weight + e.weight;
			v.parent = u;
		}
	}
	public boolean ShortestPath_BellmanFord(T start, T end) {
		System.out.printf("SP-Bellman-Ford on [%s] between [%s] to [%s]:", name, start, end);

		HashMap<Integer, Node<T>> nodeMap = InitializeVertexMap(getVertexId(start), Double.MAX_VALUE, 0.0);
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
			Node<T> u = nodeMap.get(e.source);
			Node<T> v = nodeMap.get(e.target);
			if (v.weight > u.weight + e.weight)
				retval = false;
		}
		
		// Iterate over vertices and print the shortest path
		if (retval) {
			Node<T> v = nodeMap.get(this.getVertexId(end));
			Stack<Node<T>> path = new Stack<Node<T>>();
			while (v != null) {
				path.push(v);
				v = v.parent;
			}
			
			System.out.printf("[ ");
			Node<T> curr = null;
			while (!path.isEmpty()) {
				curr = path.pop();
				System.out.printf("%s ", getVertexById(curr.node));
			}
			System.out.printf("] Cost: %4.2f\n", curr == null ? 0 : curr.weight);
		} else {
			System.out.printf("Negative weight cycle found.\n");
		}
		return retval;
	}

	// Shortest path using DAG algorithm
	public void ShortestPath_DAG(T start, T end) {
		System.out.printf("SP-DAG on [%s] between [%s] to [%s]:", name, start, end);

		HashMap<Integer, Node<T>> nodeMap = InitializeVertexMap(getVertexId(start), Double.MAX_VALUE, 0.0);

		// Topologically sort all vertices into a singly linked list
		SinglyLinkedList<T> sll = new SinglyLinkedList<T>();
		DepthFirstSearch(nodeMap, (Node<T> node)-> { sll.insertHead(node.nodeName); }, false);
		
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
		Node<T> v = nodeMap.get(this.getVertexId(end));
		Stack<Node<T>> path = new Stack<Node<T>>();
		while (v != null) {
			path.push(v);
			v = v.parent;
		}
		
		System.out.printf("[ ");
		Node<T> curr = null;
		while (!path.isEmpty()) {
			curr = path.pop();
			System.out.printf("%s ", getVertexById(curr.node));
		}
		System.out.printf("] Cost: %4.2f\n", curr == null ? 0 : curr.weight);
	}
	
	// Shortest path using Dijkstra's algorithm
	public void ShortestPath_Dijkstra(T start, T end) {
		System.out.printf("SP-Dijkstra on [%s] between [%s] to [%s]:", name, start, end);

		HashMap<Integer, Node<T>> nodeMap = InitializeVertexMap(getVertexId(start), Double.MAX_VALUE, 0.0);
		
		// Build a priority queue using vertices of the graph
		PriorityQueue<Node<T>>	pqHeap  = new PriorityQueue<Node<T>>((Node<T> a, Node<T> b)-> a.weight > b.weight ? 1 : a.weight == b.weight? 0 : -1);
		for (Node<T> node : nodeMap.values()) {
			pqHeap.add(node);
		}
		
		// loop over entries in the priority queue to completion
		List<Node<T>> path = new ArrayList<Node<T>>(); 
		while (!pqHeap.isEmpty()) {
			Node<T> u = pqHeap.poll();
			
			// Add the node to the list of vertices in the path
			path.add(u);
			
			// break if we have reached the end node;
			if (u.nodeName.equals(end))
				break;
			
			// Relax edges for each element in the adjacency list of u
			Collection<Edge<T>> adjOfu = this.getEdgesByVertexId(u.node);
			for (Edge<T> edge : adjOfu) {
				
				// Get the target node for this edge
				Node<T> targetNode = nodeMap.get(edge.target); 

				// Remove the vertex from the priority queue
				boolean fRemoved = pqHeap.removeIf((Node<T> n)-> { return n.node == edge.target; });
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
		for (Node<T> node : path) {
			System.out.printf("%s ", getVertexById(node.node));
		}
		System.out.printf("] Cost: %4.2f\n", nodeMap.get(getVertexId(end)).weight);
	}

	// Shortest path using BidirectionalDijkstra's algorithm
	public void ShortestPath_BiDijkstra(T start, T end) {
		System.out.printf("SP-BiDijkstra on [%s] between [%s] to [%s]:", name, start, end);

		// made to look the code clean
		Graph<T> graphF = this;
		Graph<T> graphR = this.transpose();
		
		HashMap<Integer, Node<T>> nodeMapF = InitializeVertexMap(graphF.getVertexId(start), Double.MAX_VALUE, 0.0);
		HashMap<Integer, Node<T>> nodeMapR = InitializeVertexMap(graphR.getVertexId(end),   Double.MAX_VALUE, 0.0);
		
		// Build priority queues in both directions
		PriorityQueue<Node<T>>	pqHeapF  = new PriorityQueue<Node<T>>((Node<T> a, Node<T> b)-> a.weight > b.weight ? 1 : a.weight == b.weight? 0 : -1);
		PriorityQueue<Node<T>>	pqHeapR  = new PriorityQueue<Node<T>>((Node<T> a, Node<T> b)-> a.weight > b.weight ? 1 : a.weight == b.weight? 0 : -1);
		for (Node<T> node : nodeMapF.values()) {
			pqHeapF.add(node);
		}
		for (Node<T> node : nodeMapR.values()) {
			pqHeapR.add(node);
		}
				
		// loop over entries in the priority queues to completion
		List<Node<T>> pathF = new ArrayList<Node<T>>(); 
		List<Node<T>> pathR = new ArrayList<Node<T>>();
		Set<Integer> setF = new HashSet<Integer>(), setR = new HashSet<Integer>();
		while (!pqHeapF.isEmpty() && !pqHeapR.isEmpty()) {
			Node<T> uF = pqHeapF.poll();
			Node<T> uR = pqHeapR.poll();
			
			// Add the node to the list of vertices in the path
			pathF.add(uF); pathR.add(uR);
			setF.add(uF.node); setR.add(uR.node);
			
			// break if the forward pointer has reached the end, the reverse the start or a common vertex has been found
			// there is a better criterion: keep a max distance from both sides and stop if the sum of the 2 max's equal edge 
			if (setF.contains(uR.node) || setR.contains(uF.node))
				break;
			
			// Relax edges for each element in the adjacency list of uF
			Collection<Edge<T>> adjOfuF = graphF.getEdgesByVertexId(uF.node);
			for (Edge<T> edge : adjOfuF) {
				Node<T> targetNode = nodeMapF.get(edge.target); 
				boolean fRemoved = pqHeapF.removeIf((Node<T> n)-> { return n.node == edge.target; });
				if (fRemoved) {
					this.RelaxEdge(nodeMapF, edge);
					pqHeapF.add(targetNode);
				}
			}
			Collection<Edge<T>> adjOfuR = graphR.getEdgesByVertexId(uR.node);
			for (Edge<T> edge : adjOfuR) {
				Node<T> targetNode = nodeMapR.get(edge.target); 
				boolean fRemoved = pqHeapR.removeIf((Node<T> n)-> { return n.node == edge.target; });
				if (fRemoved) {
					this.RelaxEdge(nodeMapR, edge);
					pqHeapR.add(targetNode);
				}
			}
		}
		
		// print path
		Node<T> lastOnF  = pathF.get(pathF.size() - 1);
		Node<T> lastOnR  = pathR.get(pathR.size() - 1);
		Double  pathCost = nodeMapF.get(lastOnF.node).weight + nodeMapR.get(lastOnR.node).weight;
		System.out.printf("[ ");
		for (Node<T> node : pathF) {
			System.out.printf("%s ", getVertexById(node.node));
		}
		Collections.reverse(pathR); boolean f = true;
		for (Node<T> node : pathR) {
			if (f) { f = false; continue; }
			System.out.printf("%s ", getVertexById(node.node));
		}
		System.out.printf("] Cost: %4.2f\n", pathCost);
	}
	
	///////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		Graph<String> graph1 = GraphFactory.getNetworkFlowGraph1();
		System.out.println(graph1);
		
		graph1.DepthFirstSearch((Node<String> n) -> { /*System.out.printf("%s[%d:%d] ", n.nodeName, n.start, n.end); */ });	
		graph1.BreadthFirstSearch((Node<String> n) -> { System.out.printf("%s[%d:%d] ", n.nodeName, n.start, n.end); });

		Graph<String> graph2 = GraphFactory.getCLRDFSGraph1();
		graph2.DepthFirstSearch((Node<String> n) -> { /* System.out.printf("%s[%d:%d] ", n.nodeName, n.start, n.end); */ });
		
		Graph<String> graph3a = GraphFactory.getCLRSCCGraph();
		Graph<String> graph3b = graph3a.transpose();
		System.out.println(graph3a);
		System.out.println(graph3b);
		graph3a.DepthFirstSearch((Node<String> n) -> { ; });
		graph3b.DepthFirstSearch((Node<String> n) -> { ; });
		graph3a.StronglyConnectedComponents();
		graph3a.TopologicalSort();
		
		Graph<String> graph4 = GraphFactory.getCLRMSTUGraph();
		System.out.println(graph4);
		graph4.DepthFirstSearch((x)->{});
		graph4.MinimalSpanningTree_Kruskal();
		graph4.MinimalSpanningTree_Prim();
		
		Graph<String> graph5 = GraphFactory.getCLRSPGraph1();
		System.out.println(graph5);
		graph5.ShortestPath_BellmanFord("s", "z");
		graph5.ShortestPath_DAG("s", "z");
		graph5.ShortestPath_Dijkstra("s", "z");
		graph5.ShortestPath_BiDijkstra("s", "z");
	}
}
