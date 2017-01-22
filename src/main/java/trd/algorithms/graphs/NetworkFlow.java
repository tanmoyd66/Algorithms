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

public class NetworkFlow<T extends Comparable<T>> extends Graph<T> {
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
			sb.append(String.format("\n[%2s(%d)]: ", invVertMap.get(me.getKey()), me.getKey()));
			HashMap<Integer,Edge<T>> edgeMap = me.getValue();
			if (edgeMap != null) {
				Set<Map.Entry<Integer, Edge<T>>> edgeSet = edgeMap.entrySet();
				for (Map.Entry<Integer, Edge<T>> meEdge : edgeSet) {
					if (!fPrintGraphEdgesOnly || (fPrintGraphEdgesOnly && meEdge.getValue().type == EdgeType.Graph)) {
						sb.append(String.format("[%2s:%3.0f/%3.0f-%s] ", 
								invVertMap.get(meEdge.getKey()), meEdge.getValue().flow, meEdge.getValue().weight, EdgeType(meEdge.getValue())));

					}
				}
			}
		}
		sb.append("\n------------------------------");
		return sb.toString();
	}

	public boolean DFSVisit(AlgoSpecificNode<T> dfsNode, Boolean all, Function<Edge<T>, Boolean> qualifies, Integer endVertexId,
			Map<Integer,AlgoSpecificNode<T>> nodeMap, Stack<AlgoSpecificNode<T>> nodeStack, List<List<T>> paths) {
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
				paths.add(path);						
				//System.out.println(path);
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

	public List<List<T>> GetPathsDFSRecursive(T start, T end, Boolean all, Function<Edge<T>, Boolean> qualifies) {
		List<List<T>> 						paths 		= new ArrayList<List<T>>();
		Integer 							startVertex = getVertexId(start), endVertex = getVertexId(end);
		Stack<AlgoSpecificNode<T>>  		nodeStack 	= new Stack<AlgoSpecificNode<T>>();
		Map<Integer,AlgoSpecificNode<T>> 	nodeMap 	= InitializeVertexMap(1, 0.0, 0.0);
		
		DFSVisit(nodeMap.get(startVertex), all, qualifies, endVertex, nodeMap, nodeStack, paths);
		return paths;
	}
	
	public List<List<T>> GetPathsDFSStackBased(T start, T end, Boolean all, Function<Edge<T>, Boolean> qualifies) {
		List<List<T>> 						pathList 	= new ArrayList<List<T>>();
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
						pathList.add(path);						
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
	public List<List<T>> GetPathsBFS(T start, T end, Boolean all, Function<Edge<T>, Boolean> qualifies) {
		
		int 							 time  		= 1;
		Queue<AlgoSpecificNode<T>> 		 bfsQueue 	= new ConcurrentLinkedQueue<AlgoSpecificNode<T>>();
		Map<Integer,AlgoSpecificNode<T>> nodeMap 	= InitializeVertexMap(1, 0.0, 0.0);
		Integer							 startNode	= getVertexId(start);
		Integer							 endNode	= getVertexId(end);
		AlgoSpecificNode<T> 			 thisNode 	= new AlgoSpecificNode<>(new Node<>(this, startNode), null, time);
		List<List<T>>					 paths		= new ArrayList<List<T>>();
		
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
					paths.add(path);
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

			// Update the flow of the current edge
			edge.flow += flow;
			
			// if it has no more capacity, delete it
			if (cmp == 0) {
				//removeEdge(edge);
			} 
			
			// Add a reverse flow for cancellation at a later time
			Edge<T> reverseEdge = this.getEdgeByStartEnd(edge.target, edge.source);
			if (reverseEdge == null) {
				reverseEdge = new Edge<>(edge.graph, edge.target, edge.source, flow, EdgeType.Augmented);
				reverseEdge.flow = 0.0;
				_addEdgeInternal(edge.target, edge.source, reverseEdge);
			} else {
				reverseEdge.weight += flow;
			}
		}
	}

	// Basic Ford Fulkerson
	public static enum NetworkFlowVariations {FordFulkerson, EdmondsKarp};
	public int FordFulkerson(T start, T end, NetworkFlowVariations algo) {
		int flow  = 0;
		int count = 0;
		
		System.out.printf("%s\n", this.toString());
		while (true) {
			List<List<T>> paths = algo == NetworkFlowVariations.FordFulkerson ?
										GetPathsDFSRecursive(start, end, false, (x) -> x.flow < x.weight):
										GetPathsBFS(start, end, false, (x) -> x.flow < x.weight);
			if (paths.isEmpty()) {
				return flow;
			} else {
				List<Edge<T>> edges = new ArrayList<Edge<T>>();

				// Convert the path into a list of edges
				List<T> path 	= paths.get(0);
				T prev = path.get(0);
				for (int i = 1; i < path.size(); i++) {
					Edge<T> thisEdge = getEdgeByStartEnd(prev, path.get(i));
					edges.add(thisEdge);
					prev = path.get(i);
				}
				
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
	public static void main(String[] args) {
		if (true) {
			NetworkFlow<String> graph5 = GraphFactory.getCLRNetworkFlowGraph2();
			String start = "s", end = "t";
			List<List<String>> pathList = graph5.GetPathsDFSRecursive(start, end, true, null);
			System.out.printf("Paths from %s to %s found by DFS-Stack Based are: %s\n", start, end, pathList);
			pathList = graph5.GetPathsDFSStackBased(start, end, true, null);
			System.out.printf("Paths from %s to %s found by DFS Recursive   are: %s\n", start, end, pathList);
			pathList = graph5.GetPathsBFS(start, end, true, null);
			System.out.printf("Paths from %s to %s found by BFS Queue Based are: %s\n", start, end, pathList);
		}
		
		if (true) {
			NetworkFlow<String> graph5 = GraphFactory.getCLRNetworkFlowGraph1();
			String start = "s", end = "t";
			int flow = graph5.FordFulkerson(start, end, NetworkFlowVariations.FordFulkerson);
			graph5.fPrintGraphEdgesOnly = true;
			System.out.printf("Total flow by Ford-Fulkerson: %d%s\n", flow, graph5);
			graph5 = GraphFactory.getCLRNetworkFlowGraph1();
			flow = graph5.FordFulkerson(start, end, NetworkFlowVariations.EdmondsKarp);
			System.out.printf("Total flow by Edmonds-Karp  : %d%s\n", flow, graph5);
		}
//		if (true) {
//			NetworkFlow<String> graph6 = GraphFactory.getCLRNetworkFlowGraph2();
//			String start = "s", end = "t";
//			int flow = graph6.FordFulkerson(start, end, NetworkFlowVariations.FordFulkerson);
//			graph6.fPrintGraphEdgesOnly = true;
//			System.out.printf("Total flow: %d%s\n", flow, graph6);
//		}
	}
}
