package trd.algorithms.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

import trd.algorithms.graphs.Graph.Edge;

public class NetworkFlow<T extends Comparable<T>> extends ShortestPaths<T> {
	public NetworkFlow(String name) {
		super(name);
	}
	public NetworkFlow(String name, Mode mode) {
		super(name, mode);
	}

	public String EdgeType(Edge<T> e) {
		return e.type == EdgeType.Augmented ? "A" : "G";
	}

	boolean fPrintGraphEdgesOnly = false;
	
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
					if (!fPrintGraphEdgesOnly || (fPrintGraphEdgesOnly && meEdge.getValue().type == EdgeType.Graph)) {
						sb.append(String.format("[%2s:%3.0f/%3.0f-%s:%d] ", 
								invVertMap.get(meEdge.getKey()), meEdge.getValue().flow, meEdge.getValue().weight, EdgeType(meEdge.getValue()), meEdge.getValue().label));

					}
				}
			}
		}
		sb.append("\n------------------------------");
		return sb.toString();
	}

	public boolean DFSVisit(AlgoSpecificNode<T> dfsNode, Boolean all, Function<Edge<T>, Boolean> qualifies, Integer endVertexId,
			Map<Integer,AlgoSpecificNode<T>> nodeMap, Stack<AlgoSpecificNode<T>> nodeStack, List<List<Edge<T>>> paths) {
		boolean fContinue = true;
		
		dfsNode.color = Color.gray;
		nodeStack.push(dfsNode);
		
		Collection<Edge<T>> edges = getEdgesByVertexId(dfsNode.node.node);
		for (Edge<T> edge : edges) {
			if (!fContinue)
				return false;
			
			Boolean considerEdge = qualifies == null ? true : qualifies.apply(edge);
			if (!considerEdge)
				continue;
			
			AlgoSpecificNode<T> dfsnTarget = nodeMap.get(edge.target);
			if (dfsnTarget.node.node == endVertexId) {
				
				// We have reached the end vertex
				// We will trace back the pack by iterating over the stack and picking the gray vertices
				List<T> path = new ArrayList<T>();
				path.add(dfsnTarget.node.nodeName);
				for (int i = nodeStack.size() - 1; i >= 0; i--) {
					AlgoSpecificNode<T> stackNode = nodeStack.get(i);
					path.add(stackNode.node.nodeName);
				}
				Collections.reverse(path);
				paths.add(this.VertexPathToEdgePath(path));						
				if (!all)
					return false;
			}

			// Some of the children of the current node might be in the stack. Ignore them.
			if (dfsnTarget.color == Color.white) {
				dfsnTarget.parent = dfsNode;
				fContinue = DFSVisit(dfsnTarget, all, qualifies, endVertexId, nodeMap, nodeStack, paths);
			}
		}
		dfsNode.color = Color.white;
		nodeStack.pop();
		return true;
	}

	public List<List<Edge<T>>> GetPathsDFSRecursive(T start, T end, Boolean all, Function<Edge<T>, Boolean> qualifies) {
		List<List<Edge<T>>> 				paths 		= new ArrayList<List<Edge<T>>>();
		Integer 							startVertex = getVertexId(start), endVertex = getVertexId(end);
		Stack<AlgoSpecificNode<T>>  		nodeStack 	= new Stack<AlgoSpecificNode<T>>();
		Map<Integer,AlgoSpecificNode<T>> 	nodeMap 	= InitializeVertexMap(1, 0.0, 0.0);
		
		DFSVisit(nodeMap.get(startVertex), all, qualifies, endVertex, nodeMap, nodeStack, paths);
		return paths;
	}
	
	public List<List<Edge<T>>> GetPathsDFSStackBased(T start, T end, Boolean all, Function<Edge<T>, Boolean> qualifies) {
		List<List<Edge<T>>> 				pathList 	= new ArrayList<List<Edge<T>>>();
		Integer 							startVertex = getVertexId(start), endVertex = getVertexId(end);
		Stack<AlgoSpecificNode<T>>  		dfsStack 	= new Stack<AlgoSpecificNode<T>>();
		Map<Integer,AlgoSpecificNode<T>> 	nodeMap 	= InitializeVertexMap(1, 0.0, 0.0);
		
		// Do depth first search, but we will not color the end vertex
		int time = 0;
		dfsStack.push(nodeMap.get(startVertex));
		while (!dfsStack.isEmpty()) {

			AlgoSpecificNode<T> dfsNode = dfsStack.peek();
			++time;
			
			if (dfsNode.color == Color.white) {
				
				// This is the first time dfsNode is seen
				// We will:
				//		push all of its children into the stack and mark them white
				//		mark the start time and the color	
				Collection<Edge<T>> edges = getEdgesByVertexId(dfsNode.node.node);
				dfsNode.start = time;
				dfsNode.color = Color.gray;
				
				for (Edge<T> edge : edges) {
					
					Boolean considerEdge = qualifies == null ? true : qualifies.apply(edge);
					if (!considerEdge)
						continue;
					
					AlgoSpecificNode<T> dfsnTarget = nodeMap.get(edge.target);
					if (dfsnTarget.node.node == endVertex) {
						
						// We have reached the end vertex
						// We will trace back the pack by iterating over the stack and picking the gray vertices
						List<T> path = new ArrayList<T>();
						path.add(getVertexById(dfsnTarget.node.node));	
						for (int i = dfsStack.size() - 1; i >= 0; i--) {
							AlgoSpecificNode<T> stackNode = dfsStack.get(i);
							if (stackNode.color == Color.gray && !path.contains(getVertexById(stackNode.node.node)))
								path.add(getVertexById(stackNode.node.node));
						}
						Collections.reverse(path);
						pathList.add(this.VertexPathToEdgePath(path));						
						//System.out.println(path);
					} else {

						// Some of the children of the current node might be in the stack. Ignore them.
						if (dfsnTarget.color == Color.white) {
							dfsnTarget.start = ++time;
							dfsnTarget.parent = dfsNode;
							dfsStack.push(dfsnTarget);
						}
					}
				}
			} else {
				// Pop when all children are done. Mark the node white so that we can backtrack
				dfsNode.color = Color.white;
				dfsNode.end = time;
				dfsStack.pop();
			}
		}
		return pathList;
	}

	// Get Paths by Breadth First Search
	public List<List<Edge<T>>> GetPathsBFS(T start, T end, Boolean all, Function<Edge<T>, Boolean> qualifies) {
		
		int 							 time  		= 1;
		Queue<AlgoSpecificNode<T>> 		 bfsQueue 	= new ConcurrentLinkedQueue<AlgoSpecificNode<T>>();
		Map<Integer,AlgoSpecificNode<T>> nodeMap 	= InitializeVertexMap(1, 0.0, 0.0);
		Integer							 startNode	= getVertexId(start);
		Integer							 endNode	= getVertexId(end);
		AlgoSpecificNode<T> 			 thisNode 	= new AlgoSpecificNode<>(new Node<>(this, startNode), null, time);
		List<List<Edge<T>>>				 paths		= new ArrayList<List<Edge<T>>>();
		
		bfsQueue.add(thisNode); 
		nodeMap.put(thisNode.node.node, thisNode);
		
		boolean fContinue = true;
		while (fContinue && !bfsQueue.isEmpty()) {
			++time;
			AlgoSpecificNode<T> curr = bfsQueue.poll();
			
			// Mark the edge as being processed
			curr.color = Color.gray;
			Collection<Edge<T>> edges = getEdgesByVertexId(curr.node.node);			
			
			for (Edge<T> edge : edges) {

				Boolean considerEdge = qualifies == null ? true : qualifies.apply(edge);
				if (!considerEdge)
					continue;

				if (edge.target == endNode) {
					List<T> path = new ArrayList<T>();
					path.add(end);
					for (AlgoSpecificNode<T> node = curr ; node != null; node = node.parent){
						path.add(getVertexById(node.node.node));
					}
					Collections.reverse(path);
					paths.add(this.VertexPathToEdgePath(path));
					fContinue = all;
				} else {
					
					AlgoSpecificNode<T> target = nodeMap.get(edge.target);
					if (target.color != Color.gray) {
						target.start = time;
						target.parent = curr;
						bfsQueue.add(target);
					}
				}
			}
		}
		return paths;
	}

	public void AddFlow(Edge<T> edge, Double flow) {
		
		// if the edge has capacity
		int cmp = edge.weight.compareTo(flow + edge.flow); 
		if (cmp >= 0) {

			// Add a reverse flow for cancellation at a later time. 
			// If one does not exist, create a new Augmented edge, with capacity equal to 0
			Edge<T> reverseEdge = this.getEdgeByStartEnd(edge.target, edge.source);
			if (reverseEdge == null) {
				reverseEdge = new Edge<>(edge.graph, edge.target, edge.source, 0.0, EdgeType.Augmented);
				reverseEdge.flow = 0.0;
				_addEdgeInternal(edge.target, edge.source, reverseEdge);
			}
			
			// Now add the flow
			if (edge.type == EdgeType.Graph) {
				// When adding flow to a graph edge, increase capacity of the the augmented edge 
				edge.flow += flow;
				reverseEdge.weight += flow;
			} else {
				// When adding flow to a augmented edge, reduce flow thru the reverse graph edge
				edge.flow += flow;
				reverseEdge.flow -= flow;
			}
		}
	}

	// Basic Ford Fulkerson
	public static enum NetworkFlowVariations {FordFulkerson_DFSStackBased, FordFulkerson_DFSRecursive, FordFulkerson_BFS, EdmondsKarp};
	private List<Edge<T>> GetPathByAlgorithm(T start, T end, NetworkFlowVariations nfv) {
		List<List<Edge<T>>> paths;
		switch(nfv) {
			case FordFulkerson_DFSStackBased: { 
				paths = GetPathsDFSStackBased(start, end, false, (x) -> x.flow < x.weight);
				return paths.isEmpty() ? null : paths.get(0);
			}
			case FordFulkerson_DFSRecursive: { 
				paths = GetPathsDFSStackBased(start, end, false, (x) -> x.flow < x.weight);
				return paths.isEmpty() ? null : paths.get(0);
			}
			case FordFulkerson_BFS: { 
				paths = GetPathsBFS(start, end, false, (x) -> x.flow < x.weight);
				return paths.isEmpty() ? null : paths.get(0);
			}
			case EdmondsKarp: { 
				List<Edge<T>> path = ShortestPath_Dijkstra(start, end, (x) -> x.flow < x.weight, (x) -> 1.0);
				return path.isEmpty() ? null : path;
			}
			default:
				return null;
		}
	}
	
	public int FordFulkerson(T start, T end, NetworkFlowVariations algo) {
		int flow  = 0;
		int count = 0;
		
		//System.out.printf("%s\n", this.toString());
		while (true) {
			List<Edge<T>> edges = GetPathByAlgorithm(start, end, algo);
			if (edges == null || edges.isEmpty()) {
				return flow;
			} else {
				// A path from start to end exists
				// Find the minimal flow thru this edge-list
				Double pathFlow = edges.stream().map(x -> (x.weight - x.flow)).reduce(Double.MAX_VALUE, (x, y) -> Math.min(x, y));
				if (pathFlow > 0) {
					
					// For each edge in the path add the flow
					for (Edge<T> edge : edges) {
						AddFlow(edge, pathFlow);
					}

					System.out.printf("[%3d] Augmented %4.0f by path: %s\n", ++count, pathFlow, edges);
				}
				flow += pathFlow;
			}
			//System.out.printf("%s\n", this.toString());
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Push Relabel Algorithm:
	private void InitializePreflow(T start, Map<Integer,AlgoSpecificNode<T>> nodeMap) {
		
		// We will use start as the label to track the vertex height
		// And weight to track the amount of flow in node
		
		// Start vertex has height = |V|
		AlgoSpecificNode<T> startVertex = nodeMap.get(getVertexId(start));
		startVertex.start = nodeMap.size();
		
		// Flood all the vertices adjacent to s with flows from s into them
		Collection<Edge<T>> adjOfStart = this.getEdgesByVertexId(startVertex.node.node);
		for (Edge<T> edge : adjOfStart) {
			AlgoSpecificNode<T> childOfStart = nodeMap.get(edge.target);
			
			// Flood the edge
			AddFlow(edge, edge.weight);
			childOfStart.node.weight = edge.weight;
		}
	}
	
	private AlgoSpecificNode<T> GetOverflowingVertex(T start, T end, Map<Integer,AlgoSpecificNode<T>> nodeMap) {
		for (Map.Entry<Integer,AlgoSpecificNode<T>> me : nodeMap.entrySet()) {
			
			// The start vertex cannot be a overflow vertex 
			if (me.getValue().node.nodeName.compareTo(start) == 0 ||
				me.getValue().node.nodeName.compareTo(end) == 0)
				continue;
			
			// Find the first vertex with an overflow. That should be good enough
			if (me.getValue().node.weight > 0) {
				return me.getValue();
			}
		}
		return null;
	}
	
	private AlgoSpecificNode<T> Push(Map<Integer,AlgoSpecificNode<T>> nodeMap, AlgoSpecificNode<T> node) {
		Collection<Edge<T>> adjOfNode = this.getEdgesByVertexId(node.node.node);
		for (Edge<T> edge : adjOfNode) {
			
			// If the edge is full we cannot add any more
			if (edge.flow.compareTo(edge.weight) >= 0)
				continue;
			
			// Push is only possible downwards 
			AlgoSpecificNode<T> targetNode = nodeMap.get(edge.target);
			if (targetNode.start < node.start) {
				
				// The amount we can push is the minimal of the excess flow in the vertext and that of the edge
				Double flowAmount = Math.min(edge.weight - edge.flow, node.node.weight);
				if (flowAmount > 0) {
					
					// Increase the capacity of the target node and reduce capacity of this node
					targetNode.node.weight += flowAmount;
					node.node.weight -= flowAmount;
					
					// Reduce the flow thru this edge
					AddFlow(edge, flowAmount);
					
					// We are done for now
					return targetNode;
				}
			}
		}
		return null;
	}

	private void Relabel(Map<Integer,AlgoSpecificNode<T>> nodeMap, AlgoSpecificNode<T> node) {
		
		// Find minimal height of all adjacent vertices
		int minHeightOfAdjacent = Integer.MAX_VALUE;
		Collection<Edge<T>> adjOfNode = this.getEdgesByVertexId(node.node.node);
		for (Edge<T> edge : adjOfNode) {
			
			if (edge.flow.compareTo(edge.weight) >= 0)
				continue;
			
			AlgoSpecificNode<T> targetNode = nodeMap.get(edge.target);
			minHeightOfAdjacent = Math.min(minHeightOfAdjacent, targetNode.start);
		}
		
		// Update height of our node to 1 + minimum height (height always increases by 1)
		node.start = minHeightOfAdjacent + 1;
	}
	
	public Double PushRelabel(T start, T end) {
		Map<Integer,AlgoSpecificNode<T>> nodeMap 	= InitializeVertexMap(1, 0.0, 0.0);
		
		// Initialize Pre-flow 
		InitializePreflow(start, nodeMap);
		
		// Loop until there are no overflowing vertex
		while (true) {
			AlgoSpecificNode<T> overflowNode = GetOverflowingVertex(start, end, nodeMap);
			if (overflowNode == null)
				break;
			
			// Either push or relabel, until there is nothing to push
			AlgoSpecificNode<T> pushedToNode = Push(nodeMap, overflowNode);
			if (pushedToNode == null)
				Relabel(nodeMap, overflowNode);
		}
				
		return nodeMap.get(getVertexId(end)).node.weight;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		if (true) {
			NetworkFlow<String> graph5 = GraphFactory.getCLRNetworkFlowGraph2();
			String start = "s", end = "t";
			List<List<Edge<String>>> pathList = graph5.GetPathsDFSRecursive(start, end, true, null);
			System.out.printf("Paths from %s to %s found by DFS-Stack Based are: %s\n", start, end, pathList);
			pathList = graph5.GetPathsDFSStackBased(start, end, true, null);
			System.out.printf("Paths from %s to %s found by DFS Recursive   are: %s\n", start, end, pathList);
			pathList = graph5.GetPathsBFS(start, end, true, null);
			System.out.printf("Paths from %s to %s found by BFS Queue Based are: %s\n", start, end, pathList);
		}
		
		if (true) {
			NetworkFlow<String> graph5;
			String start = "s", end = "t";
			for (NetworkFlowVariations nfv : NetworkFlowVariations.values()) {
				graph5 = GraphFactory.getCLRNetworkFlowGraph1();
				int flow = graph5.FordFulkerson(start, end, nfv);
				graph5.fPrintGraphEdgesOnly = true;
				System.out.printf("Total flow by %s: %d%s\n", nfv.toString(), flow, graph5);
			}
		}
		if (true) {
			NetworkFlow<String> graph6;
			String start = "s", end = "t";
			for (NetworkFlowVariations nfv : NetworkFlowVariations.values()) {
				graph6 = GraphFactory.getCLRNetworkFlowGraph2();
				int flow = graph6.FordFulkerson(start, end, nfv);
				graph6.fPrintGraphEdgesOnly = true;
				System.out.printf("Total flow by %s: %d%s\n", nfv.toString(), flow, graph6);
			}
		}
		if (true) {
			NetworkFlow<String> graph7;
			String start = "s", end = "t";
			graph7 = GraphFactory.getCLRNetworkFlowGraph2();
			graph7.fPrintGraphEdgesOnly = true;
			System.out.printf("Total flow by %s: %f%s\n", "Push Relabel", graph7.PushRelabel(start, end), graph7);
		}
		if (true) {
			NetworkFlow<String> graph8;
			String start = "s", end = "t";
			graph8 = GraphFactory.getCLRNetworkFlowGraph1();
			graph8.fPrintGraphEdgesOnly = true;
			System.out.printf("Total flow by %s: %f%s\n", "Push Relabel", graph8.PushRelabel(start, end), graph8);
		}
	}
}
