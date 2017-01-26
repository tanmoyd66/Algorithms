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
import java.util.function.Function;
import java.util.stream.Collectors;

import trd.algorithms.datastructures.DynamicSet;
import trd.algorithms.linkedlists.SinglyLinkedList;

public class Graph<T extends Comparable<T>> {
	public enum Mode 		{ Directed, Undirected };
	public enum EdgeType 	{ Graph, Augmented };
	
	public static class Edge<T extends Comparable<T>> {
		Integer		source;
		Integer 	target;
		Double  	weight;
		Double  	flow;
		EdgeType	type;
		Graph<T>	graph;
		int			label;
		
		public Edge(Graph<T> graph, Integer source, Integer target, Double weight, EdgeType type) {
			this.graph = graph; this.source = source; this.target = target; this.weight = weight; this.type = type; this.flow = 0.0;
		}
		public Edge(Graph<T> graph, Integer source, Integer target, Double weight) {
			this.graph = graph; this.source = source; this.target = target; this.weight = weight; this.type = EdgeType.Graph; this.flow = 0.0;
		}
		public Edge(Graph<T> graph, Integer source, Integer target) {
			this.graph = graph; this.source = source; this.target = target; this.weight = 0.0; this.type = EdgeType.Graph; this.flow = 0.0;
		}
		public String toString() {
			String Flow = this.flow.compareTo(0.0) != 0 || this.flow.compareTo(0.0) != 0 ? String.format("%4.2f/%4.2f", this.flow, this.weight) : "";
			String Label = label != 0 ? String.format("%d", label) : "";
			return String.format("(%s,%s:%s:%s) ", graph.getVertexById(source), graph.getVertexById(target), Flow, Label);
		}		
	}

	// General purpose node structure used by all
	public enum Color { white, gray, black };
	public static class Node<T extends Comparable<T>> {
		Integer		node;
		T			nodeName;
		Graph<T>	graph;
		Double		weight;
		
		public Node(Graph<T> graph, Integer node) {
			this.graph = graph; this.node = node; 
			this.nodeName = graph.getVertexById(node); 
		}
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("(%s%s)", nodeName, weight == 0.0 ? "": String.format(":%3.2f", weight)));
			return sb.toString();
		}
	}
	
	public static class AlgoSpecificNode<T extends Comparable<T>> {
		Color					color;
		Node<T>					node;
		AlgoSpecificNode<T>		parent;
		int						start;
		int						end;
		int						label;
		
		public String getColorString() {
			return color == Color.white ? "W" : color == Color.gray ? "G" : "B";
		}		
		AlgoSpecificNode(Node<T> node, AlgoSpecificNode<T> parent, int start) {
			this.node = node; this.parent = parent; this.start = start; this.end = start;
			color = Color.white;  
		}
		public String toString() {
			StringBuilder sb = new StringBuilder();
			String startEnd = start > 0 || end > 0 ? String.format(":%d-%d", start, end) : "";
			String Parent   = parent != null ? String.format(":%s", parent.node.nodeName) : "";
			sb.append(String.format("[%s%s%s:%s]", node, startEnd, Parent, getColorString()));
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
	
	protected Integer getVertexId(T source) {
		Integer sourceId = vertexMap.get(source);
		if (sourceId == null) {
			sourceId = ++maxVertex;
			vertexMap.put(source, sourceId);
			invVertMap.put(sourceId, source);
		}
		return sourceId;
	}
	
	protected T getVertexById(Integer id) {
		return invVertMap.get(id);
	}
	
	protected Set<Integer> getVertexSet() {
		return invVertMap.keySet();
	}
	
	protected Collection<Edge<T>> getEdgesByVertexId(Integer id) {
		HashMap<Integer,Edge<T>> edgeMap = adjList.get(id);
		if (edgeMap != null) {
			return edgeMap.values();
		}
		return Collections.emptyList();
	}

	protected Edge<T> getEdgeByStartEnd(Integer startId, Integer endId) {
		HashMap<Integer, Edge<T>> adjStart = adjList.get(startId);
		if (adjStart == null)
			return null;
		else {
			Edge<T> thisEdge = adjStart.get(endId);
			return thisEdge;
		}
	}

	public Edge<T> getEdgeByStartEnd(T start, T end) {
		Integer startId = getVertexId(start), endId = getVertexId(end);
		return getEdgeByStartEnd(startId, endId);
	}

	protected void removeEdge(Edge<T> edge) {
		HashMap<Integer, Edge<T>> adjStart = adjList.get(edge.source);
		if (adjStart == null)
			return;
		else {
			adjStart.remove(edge.target);
		}
	}
	
	protected List<Edge<T>> getAllEdges() {
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
	
	protected void _addEdgeInternal(Integer sourceId, Integer targetId, Edge<T> edge) {
		HashMap<Integer,Edge<T>> edgeList = adjList.get(sourceId);
		if (edgeList == null) {
			edgeList = new HashMap<Integer,Edge<T>>();
			adjList.put(sourceId, edgeList);
		}
		edgeList.put(targetId, edge);
	}

	protected void _addEdge(T source, T target, Double weight, EdgeType type) {
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
			sb.append(String.format("\n[%2s(%2d)]: ", invVertMap.get(me.getKey()), me.getKey()));
			HashMap<Integer,Edge<T>> edgeMap = me.getValue();
			if (edgeMap != null) {
				Set<Map.Entry<Integer, Edge<T>>> edgeSet = edgeMap.entrySet();
				for (Map.Entry<Integer, Edge<T>> meEdge : edgeSet) {
					sb.append(String.format("[%2s:%4.2f/%4.2f] ", invVertMap.get(meEdge.getKey()), meEdge.getValue().flow, meEdge.getValue().weight));
				}
			}
		}
		sb.append("\n------------------------------");
		return sb.toString();
	}
	
	// General purpose initialization of vertex set
	protected HashMap<Integer, AlgoSpecificNode<T>> InitializeVertexMap(Integer startId, Double defaultWeight, Double startVertexWeight) {
		HashMap<Integer, AlgoSpecificNode<T>> nodeMap = new HashMap<Integer, AlgoSpecificNode<T>>();
		Set<Integer> vertexSet = getVertexSet();
		for (Integer v : vertexSet) {
			Node<T> vNode = new Node<>(this, v); 
			vNode.weight = defaultWeight;
			nodeMap.put(v, new AlgoSpecificNode<T>(vNode, null, 0));
		}
		Node<T> s = nodeMap.get(startId).node;
		s.weight = startVertexWeight;
		return nodeMap;
	}

	// Depth first search of general graphs
	public enum DFSCallbackReturnTypes { Continue, AbandonSearch, AbandonThisNode } 
	protected void DFSVisit(AlgoSpecificNode<T> dfsNode, AtomicInteger time, 
						Map<Integer,AlgoSpecificNode<T>> nodeMap, 
						Function<AlgoSpecificNode<T>, DFSCallbackReturnTypes> nodeExplored, 
						Function<AlgoSpecificNode<T>, DFSCallbackReturnTypes> nodeComplete, 
						Set<Node<T>> forestNodes) {
		
		// White Node has just been discovered
		time.incrementAndGet();
		
		// set the color stating that we have started exploring
		dfsNode.color = Color.gray;
		dfsNode.start = time.get();

if (dfsNode.node.nodeName.toString().compareTo("5") == 0 ||
dfsNode.node.nodeName.toString().compareTo("T") == 0) {
	int kk = 0;
}
		// explore children recursively
		Collection<Edge<T>> edges = getEdgesByVertexId(dfsNode.node.node);
		for (Edge<T> edge : edges) {
			AlgoSpecificNode<T> dfsnTarget = nodeMap.get(edge.target);
			if (dfsnTarget.color == Color.white) {
				
				dfsnTarget.parent = dfsNode;
				DFSCallbackReturnTypes ret = nodeExplored == null ? DFSCallbackReturnTypes.Continue : nodeExplored.apply(dfsnTarget);
				if (ret == DFSCallbackReturnTypes.AbandonSearch) {
					dfsnTarget.parent = null;
					return;
				} else if (ret == DFSCallbackReturnTypes.AbandonThisNode) {
					dfsnTarget.parent = null;
					continue;
				}
				
				forestNodes.add(dfsnTarget.node);
				DFSVisit(dfsnTarget, time, nodeMap, nodeExplored, nodeComplete, forestNodes);
			}
		}
		
		// process the node
		DFSCallbackReturnTypes ret = nodeComplete == null ? DFSCallbackReturnTypes.Continue : nodeComplete.apply(dfsNode);
		if (ret == DFSCallbackReturnTypes.AbandonSearch)
			return;
		else if (ret == DFSCallbackReturnTypes.AbandonThisNode) {
			// set complete
			dfsNode.color = Color.black;
			dfsNode.end = time.incrementAndGet();
		}

		// set complete
		dfsNode.color = Color.black;
		dfsNode.end = time.incrementAndGet();
	}

	protected void DepthFirstSearch(Map<Integer,AlgoSpecificNode<T>> nodeMap, Set<Integer> vertexSet, 
						Function<AlgoSpecificNode<T>, DFSCallbackReturnTypes> exploreNode, 
						Function<AlgoSpecificNode<T>, DFSCallbackReturnTypes> processNode, 
						boolean fPrint) {
		AtomicInteger time = new AtomicInteger(0);

		// Perform Depth First Search
		int treeId = 0;
		
		// For each vertex, we will go thru the adjecancy list 
		for (Integer vertex : vertexSet) {
			
			AlgoSpecificNode<T> node = nodeMap.get(vertex);
			if (node.color == Color.white) {
				
				AlgoSpecificNode<T> root = node == null ? new AlgoSpecificNode<>(new Node<T>(this, vertex), null, time.get()) : node;				
				node.start = time.get();
				
				Set<Node<T>> forestNodes = new HashSet<Node<T>>();
				forestNodes.add(root.node);
				DFSVisit(root, time, nodeMap, exploreNode, processNode, forestNodes);

				if (fPrint) {
					List<Node<T>> _forestNodesList = new ArrayList<>(forestNodes);
					Collections.sort(_forestNodesList, (Node<T> a, Node<T> b) -> nodeMap.get(a.node).start - nodeMap.get(a.node).start);
					System.out.printf("(%d):{ ", ++treeId);
					for (Node<T> thisNode: _forestNodesList) {
						AlgoSpecificNode<T> aNode = nodeMap.get(thisNode.node);
						System.out.printf("%s[%d:%d] ", thisNode.nodeName, aNode.start, aNode.end);
					}
					System.out.printf("} ", treeId);
				}
			}
		}
	}	
	
	public void DepthFirstSearch(Function<AlgoSpecificNode<T>, DFSCallbackReturnTypes> exploreNode, Function<AlgoSpecificNode<T>, DFSCallbackReturnTypes> processNode) {
		System.out.printf("DFS on [%s]:", name);
				
		Map<Integer,AlgoSpecificNode<T>> nodeMap = InitializeVertexMap(1, 0.0, 0.0);
		DepthFirstSearch(nodeMap, getVertexSet(), exploreNode, processNode, true);

		System.out.println();
	}

	public List<List<T>> DisjointPaths(Set<T> forestVertexNames) {
		Map<Integer,AlgoSpecificNode<T>> nodeMap = InitializeVertexMap(1, 0.0, 0.0);
		List<List<T>> disjointPaths = new ArrayList<List<T>>();
		Set<Integer> vertexSet = forestVertexNames.stream().map(x->getVertexId(x)).collect(Collectors.toSet());
		DepthFirstSearch(nodeMap, vertexSet,
						 (AlgoSpecificNode<T> node)-> {
			
							// Skip processing for non terminal nodes.
							if (getEdgesByVertexId(node.node.node).size() == 0) {
								
								// Walk up the tree and see if we can get a full path to the start
								boolean allVirgin = true;
								for (AlgoSpecificNode<T> nodeInPath = node; allVirgin && nodeInPath != null; nodeInPath = nodeInPath.parent) {
									allVirgin &= (nodeInPath.label == 0);
								}
								
								// If the path is all virgin (aka no node has been seen on a different path) we have a disjoint path
								if (allVirgin) {
									List<T> dfsPath = new ArrayList<T>(); 
									for (AlgoSpecificNode<T> nodeInPath = node; allVirgin && nodeInPath != null; nodeInPath = nodeInPath.parent) {
										dfsPath.add(nodeInPath.node.nodeName);
										nodeInPath.label = 1;
									}
									Collections.reverse(dfsPath);
									disjointPaths.add(dfsPath);
									return DFSCallbackReturnTypes.Continue;
								} else {
									return DFSCallbackReturnTypes.AbandonThisNode;
								}
							} else {
								return DFSCallbackReturnTypes.Continue;
							}
						}, null, false);
		return disjointPaths;
	}
	
	public void StronglyConnectedComponents() {
		Map<Integer,AlgoSpecificNode<T>> nodeMap = InitializeVertexMap(1, 0.0, 0.0);
		AtomicInteger time = new AtomicInteger(0);
		
		System.out.printf("SCC on [%s]:", name);

		// Perform Depth First Search on every vertex
		Set<Integer> vertexSet = getVertexSet();
		for (Integer vertex : vertexSet) {
			AlgoSpecificNode<T> node = nodeMap.get(vertex);
			if (node == null || node.color == Color.white) {
				Set<Node<T>> forestNodes = new HashSet<Node<T>>();
				forestNodes.add(node.node);
				DFSVisit(node, time, nodeMap, null, null, forestNodes);
			}
		}
		
		// Sort the nodes of the forests in order of finish time
		List<AlgoSpecificNode<T>> nodeList = new ArrayList<AlgoSpecificNode<T>>(nodeMap.values());
		Collections.sort(nodeList, (AlgoSpecificNode<T> a, AlgoSpecificNode<T> b) -> b.end - a.end);

		// Get the transpose of the graph and do book-keeping to clear vertices
		int treeId = 0; time.set(0);
		Graph<T> transposed = transpose();
		
		// Reset the node-map for the second DFS run
		nodeMap = InitializeVertexMap(1, 0.0, 0.0);
		
		// Perform DFS on the transposed graph in order of the nodes in the nodeList
		for (AlgoSpecificNode<T> nodeOther : nodeList) {
			AlgoSpecificNode<T> node = nodeMap.get(nodeOther.node);
			if (node == null || node.color == Color.white) {
				AlgoSpecificNode<T> root = node == null ? new AlgoSpecificNode<T>(new Node<T>(this, nodeOther.node.node), null, time.get()) : node;
				Set<Node<T>> forestNodes = new HashSet<Node<T>>();
				forestNodes.add(root.node);
				transposed.DFSVisit(root, time, nodeMap, null, null, forestNodes);
				System.out.printf("(%d):{ ", ++treeId);
				for (Node<T> thisNode: forestNodes) {
					AlgoSpecificNode<T> aNode = nodeMap.get(thisNode.node);
					System.out.printf("%s[%d:%d] ", thisNode.nodeName, aNode.start, aNode.end);
				}
				System.out.printf("} ", treeId);
			}
		}
		System.out.println();
	}	

	// Topological Sorting
	public void TopologicalSort() {
		SinglyLinkedList<T> sll = new SinglyLinkedList<T>();
		DepthFirstSearch(null, (AlgoSpecificNode<T> node)-> { sll.insertHead(node.node.nodeName); return DFSCallbackReturnTypes.Continue;});
		System.out.printf("TopSort on [%s]:%s\n", name, sll);
	}
	
	// Breadth first search of general graphs
	public void BreadthFirstSearch(Consumer<AlgoSpecificNode<T>> processNode) {
		int time  = 1;
		Queue<AlgoSpecificNode<T>> bfsQueue = new ConcurrentLinkedQueue<AlgoSpecificNode<T>>();
		Map<Integer,AlgoSpecificNode<T>> nodeMap = new HashMap<Integer,AlgoSpecificNode<T>>();
		AlgoSpecificNode<T> thisNode = new AlgoSpecificNode<>(new Node<>(this, startNode), null, time);
		
		System.out.printf("BFS on [%s]:", name);

		bfsQueue.add(thisNode); nodeMap.put(thisNode.node.node, thisNode);
		
		while (!bfsQueue.isEmpty()) {
			++time;
			AlgoSpecificNode<T> curr = bfsQueue.poll();
			
			// Mark the edge as being processed
			curr.color = Color.gray;
			Collection<Edge<T>> edges = getEdgesByVertexId(curr.node.node);			
			
			// Process the node
			processNode.accept(curr);

			for (Edge<T> edge : edges) {

				AlgoSpecificNode<T> target = nodeMap.get(edge.target);
				if (target == null) {
					target = new AlgoSpecificNode<>(new Node<T>(this, edge.target), null, time);
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

		HashMap<Integer, AlgoSpecificNode<T>> 	nodeMap = new HashMap<Integer, AlgoSpecificNode<T>>();
		PriorityQueue<AlgoSpecificNode<T>>		pqHeap  = new PriorityQueue<AlgoSpecificNode<T>>((AlgoSpecificNode<T> a, AlgoSpecificNode<T> b)-> a.node.weight > b.node.weight ? 1 : a.node.weight == b.node.weight? 0 : -1);
		
		// Get a list of all vertices and create a min-priority queue with all vertices with weights set to a large number
		Set<Integer> vertexSet = getVertexSet();
		for (Integer v : vertexSet) {
			AlgoSpecificNode<T> vNode = new AlgoSpecificNode<>(new Node<>(this, v), null, 0); 
			vNode.node.weight = v == startNode ? 0 : Double.MAX_VALUE;
			nodeMap.put(v, vNode);
			pqHeap.add(vNode);
		}
		
		// Loop over the queue, 
		while (!pqHeap.isEmpty()) {
			
			// extracting the vertex with the minimal vertex
			AlgoSpecificNode<T> curr = pqHeap.poll();
			
			// loop over the adj-list of curr
			Collection<Edge<T>> edges = getEdgesByVertexId(curr.node.node);
			for (Edge<T> e : edges) {
				AlgoSpecificNode<T> targetNode = nodeMap.get(e.target); 
				
				// if the vertex is in the queue and its weight is less than that of the edge
				if (pqHeap.contains(targetNode) && e.weight < targetNode.node.weight) {
					
					// Remove the vertex from the priority queue
					pqHeap.removeIf((AlgoSpecificNode<T> n)-> { return n.node.node == e.target; });
					
					// Set its weights
					targetNode.node.weight = e.weight;
					targetNode.parent = curr;
					
					// Re-insert to queue
					pqHeap.add(targetNode);
				}
			}
		}
		
		// Print the edges. The tree edges correspond to those vertices that have a non-null parent
		System.out.printf("[ ");
		for (HashMap.Entry<Integer, AlgoSpecificNode<T>> me : nodeMap.entrySet()) {
			AlgoSpecificNode<T> currVertex = me.getValue();
			if (currVertex.parent != null) {
				System.out.printf("(%s,%s) ", getVertexById(currVertex.parent.node.node), getVertexById(currVertex.node.node));
			}
		}
		System.out.printf("]\n");
	}

	
	///////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		Graph<String> graph1 = GraphFactory.getNetworkFlowGraph1();
		System.out.println(graph1);
		
		graph1.DepthFirstSearch(null, null);	
		graph1.BreadthFirstSearch((AlgoSpecificNode<String> n) -> { System.out.printf("%s[%d:%d] ", n.node.nodeName, n.start, n.end); });

		Graph<String> graph2 = GraphFactory.getCLRDFSGraph1();
		graph2.DepthFirstSearch(null, (AlgoSpecificNode<String> n) -> { return DFSCallbackReturnTypes.Continue; });
		
		Graph<String> graph3a = GraphFactory.getCLRSCCGraph();
		Graph<String> graph3b = graph3a.transpose();
		System.out.println(graph3a);
		System.out.println(graph3b);
		graph3a.DepthFirstSearch(null, null);
		graph3b.DepthFirstSearch(null, null);
		graph3a.StronglyConnectedComponents();
		graph3a.TopologicalSort();
		
		Graph<String> graph4 = GraphFactory.getCLRMSTUGraph();
		System.out.println(graph4);
		graph4.DepthFirstSearch(null, null);
		graph4.MinimalSpanningTree_Kruskal();
		graph4.MinimalSpanningTree_Prim();

	}
}
