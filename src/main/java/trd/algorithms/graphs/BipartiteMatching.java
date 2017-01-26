package trd.algorithms.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BipartiteMatching <T extends Comparable<T>> extends NetworkFlow<T> {

	public static class Matching<T> {
		T start;
		T end;
		public Matching(T start, T end) {
			this.start = start; this.end = end;
		}
		public String toString() {
			return "(" + start.toString() + "," + end.toString() + ")";
		}
	}
	
	public BipartiteMatching(String name) {
		super(name);
	}
	public BipartiteMatching(String name, Mode mode) {
		super(name, mode);
	}
	
	private Set<T> U = new HashSet<T>();
	private Set<T> V = new HashSet<T>();
	
	// A graph is bipartite we need to make sure that it is 2-colorable
	// Vertexes are in state {White(0), Gray(1), Black(2)}
	// Can be no less than O(E) as the last edge can break bipartite-ness
	public boolean CheckAndSetPartite(Map<Integer,AlgoSpecificNode<T>> nodeMap) {
		
		Queue<AlgoSpecificNode<T>> bfsQueue = new ConcurrentLinkedQueue<AlgoSpecificNode<T>>();
		
		// BFS on Forest
		for (Map.Entry<Integer,AlgoSpecificNode<T>> nme : nodeMap.entrySet()) {
			
			if (nme.getValue().color != Color.white)
				continue;
			
			// Initialize the start vertex color
			AlgoSpecificNode<T> StartNode = nme.getValue();
			StartNode.color = Color.gray; U.add(StartNode.node.nodeName); bfsQueue.add(StartNode);
			while (!bfsQueue.isEmpty()) {
				AlgoSpecificNode<T>  curr = bfsQueue.poll();
				Collection<Edge<T>> adjOfCurr = this.getEdgesByVertexId(curr.node.node);
				for (Edge<T> outEdge : adjOfCurr) {
					AlgoSpecificNode<T> outTarget = nodeMap.get(outEdge.target);
					if (outTarget.color == curr.color)
						return false;
					else {
						if (outTarget.color == Color.white)
							outTarget.color = curr.color == Color.gray ? Color.black : Color.gray;
						if (outTarget.color == Color.gray)
							U.add(outTarget.node.nodeName);
						else 
							V.add(outTarget.node.nodeName);
						bfsQueue.add(outTarget);
					}
				}
			}
		}
		return true;
	}

	public List<Matching<T>> BipartiteMatchingByNetwokFlow(T start, T end) {
		List<Matching<T>> matchings = new ArrayList<Matching<T>>();
		Map<Integer,AlgoSpecificNode<T>> nodeMap = InitializeVertexMap(1, 0.0, 0.0);

		// Validate that this is a Bipartite Graph
		if (!CheckAndSetPartite(nodeMap))
			return matchings;
		
		// Add start and sink vertices and set all edge-weights to 1
		for (T uVertex : U) {
			this.addEdge(start, nodeMap.get(getVertexId(uVertex)).node.nodeName, 1.0);
		}
		for (T vVertex : V) {
			this.addEdge(nodeMap.get(getVertexId(vVertex)).node.nodeName, end, 1.0);
		}
		for (Edge<T> edge : getAllEdges()) {
			edge.weight = 1.0;
		}
		this.startNode = this.getVertexId(start);
			
		// Call Network Flow
		PushRelabel(start, end);
		
		// Find matchings and add to output
		for (T uVertex : U) {
			Collection<Edge<T>> adjOfU = getEdgesByVertexId(getVertexId(uVertex));
			for (Edge<T> uOut : adjOfU) {
				if (uOut.flow > 0) {
					matchings.add(new Matching<T>(getVertexById(uOut.source), getVertexById(uOut.target)));
					break;
				}
			}
		}
		return matchings;
	}
	
	public void MakeBidirectionalWithLayer() {
		for(Edge<T> e : getAllEdges()) {
			Edge<T> reverseEdge = new Edge<T>(this, e.target, e.source, 0.0, EdgeType.Augmented);
			_addEdgeInternal(e.target, e.source, reverseEdge);
		}
	}

	public List<List<Edge<T>>> GetDisjointAugmentingPaths(Map<Integer,AlgoSpecificNode<T>> nodeMap, Map<Integer, Edge<T>> matchMap) {
		List<List<Edge<T>>>  		augmentingPaths = new ArrayList<List<Edge<T>>>();
		Queue<AlgoSpecificNode<T>> 	bfsQueue 		= new ConcurrentLinkedQueue<AlgoSpecificNode<T>>();
		Graph<T>					bfsGraph		= new Graph<T>("");
		
		// Reset the horizon of the BFS
		for (T u : U) {
			nodeMap.get(getVertexId(u)).label = 0;
		}
		for (T v : V) {
			nodeMap.get(getVertexId(v)).label = 0;
		}
		
		// Start with a free vertex in set U and do a BFS to get a BFS tree (we will encode this as a graph)
		Set<T>  verticesInDFS = new HashSet<T>();
		for (T u : U) {
			Integer uId = getVertexId(u); 
			Collection<Edge<T>> edges = getEdgesByVertexId(uId);
			boolean foundMatchedEdge = false;
			for (Edge<T> outGoingEdge : edges) {
				if (outGoingEdge.label == 1) {
					foundMatchedEdge = true; break;
				}
			}
			if (!foundMatchedEdge) {
				bfsQueue.add(nodeMap.get(uId));
				verticesInDFS.add(u);
			}
		}

		// Now start the BFS search. 
		// We end the search when we have found unmatched vertices at odd levels (BFS proceeds in layers) 
		boolean fNoNeedToExtendSearch = false; 
		int 	frontierOfMatchingVertices = Integer.MAX_VALUE;
		while (!bfsQueue.isEmpty()) {
			
			// Pop from the top of the queue
			AlgoSpecificNode<T> curr = bfsQueue.poll();
			Collection<Edge<T>> edges = getEdgesByVertexId(curr.node.node);			

			// Terminate the search if we have reached a frontier where there are unmatched vertices
			// We have found an augmenting path then
			if (curr.label >= frontierOfMatchingVertices)
				break;
			
			// Go thru all the outgoing edges of curr and add to the BFS graph that meet the criterion
			for (Edge<T> outOfCurr : edges) {
				AlgoSpecificNode<T> targetNode = nodeMap.get(outOfCurr.target);
				
				if (curr.color == Color.gray) {
					// if curr is at even level (gray), we are going to look for edges that are not matched
					if (outOfCurr.label == 0 /*&& !matchMap.containsKey(targetNode.node.node) */) {
						bfsGraph.addEdge(curr.node.nodeName, nodeMap.get(outOfCurr.target).node.nodeName);

						// Check if we have reached an unmatched vertex (target can only be black)
						if (!matchMap.containsKey(targetNode.node.node)) {
							frontierOfMatchingVertices = curr.label + 1;
							fNoNeedToExtendSearch = true;
						}
						if (!fNoNeedToExtendSearch) {
							targetNode.label = curr.label + 1;
							bfsQueue.add(targetNode);
						}
					}
				} else {
					// if curr is at odd level (black), we are going to look for edges that are matched
					if (outOfCurr.label == 1) {
						bfsGraph.addEdge(curr.node.nodeName, nodeMap.get(outOfCurr.target).node.nodeName);
						
						if (!fNoNeedToExtendSearch) {
							targetNode.label = curr.label + 1;
							bfsQueue.add(targetNode);
						}
						// The search cannot end in a gray vertex, so no need to check for frontier
					}
				}
			}
		}

		// The auxiliary graph has all the necessary edges
		if (bfsGraph.getVertexSet().size() > 0) {
			List<List<T>> dfsResultOnAuxiliaryGraph = bfsGraph.DisjointPaths(verticesInDFS);
			for (List<T> vPath : dfsResultOnAuxiliaryGraph) {
				
				// An augmented path is one that starts and ends in a free vertex
				Integer vertexAtTheEndOfThisPath = getVertexId(vPath.get(vPath.size() - 1));
				if (!matchMap.containsKey(vertexAtTheEndOfThisPath))
					augmentingPaths.add(this.VertexPathToEdgePath(vPath));
			}
		}
		return augmentingPaths;
	}
	
 	public List<Matching<T>> BipartiteMatchingByHopcroftKarp() {
		List<Matching<T>> 					matchings = new ArrayList<Matching<T>>();
		Map<Integer, Edge<T>> 				matchMap  = new HashMap<Integer, Edge<T>>(); 
		Map<Integer,AlgoSpecificNode<T>> 	nodeMap   = InitializeVertexMap(1, 0.0, 0.0);

		// Validate that this is a Bipartite Graph and set the vertex colors
		// The U vertices will be colored Gray and V vertices Black
		if (!CheckAndSetPartite(nodeMap))
			return matchings;
		
		// Make the graph bidirectional with label (EdgeType = Augmented) 
		// We will traverse the Graph edges when going from U to V and augmented edges from V to U
		MakeBidirectionalWithLayer();
		
		// Loop until we get more augmenting paths
		while (true) {
			List<List<Edge<T>>> augmentingPaths = GetDisjointAugmentingPaths(nodeMap, matchMap);
			if (augmentingPaths.size() == 0) 
				break;
			
			// For each augmenting path, remove common edges from the match-map
			for (List<Edge<T>> augmentingPath : augmentingPaths) {
				for (Edge<T> edge : augmentingPath) {
					Edge<T> reverseEdge = getEdgeByStartEnd(edge.target, edge.source);
					if (edge.label == 1) {
						edge.label = 0;
						reverseEdge.label = 0;
						matchMap.remove(edge.source);
						matchMap.remove(edge.target);
					} else {
						edge.label = 1;
						reverseEdge.label = 1;
						matchMap.put(edge.source, edge);
						matchMap.put(edge.target, reverseEdge);
					}
				}
			}
		}
		
		// return the matchings
		for (Edge<T> edge : matchMap.values())
			if (U.contains(getVertexById(edge.source))) 
				matchings.add(new Matching<T>(getVertexById(edge.source), getVertexById(edge.target)));
		return matchings;
	}
	
	public static void main(String[] args) {
		if (true) {
			BipartiteMatching<String> graph;
			graph = GraphFactory.getBiPartiteGraph2();
			System.out.printf("Bipartite Matching in %s\n", graph);
		}
		if (true) {
			BipartiteMatching<String> graph;
			String start = "s", end = "t";
			graph = GraphFactory.getBiPartiteGraph2();
			List<Matching<String>> matches = graph.BipartiteMatchingByNetwokFlow(start, end);
			Collections.sort(matches, (a,b) -> a.start.compareTo(b.start));
			System.out.printf("Using Network  Flow: %s\n", matches);
		}
		if (true) {
			BipartiteMatching<String> graph;
			graph = GraphFactory.getBiPartiteGraph2();
			List<Matching<String>> matches = graph.BipartiteMatchingByHopcroftKarp();
			graph.BipartiteMatchingByHopcroftKarp();
			Collections.sort(matches, (a,b) -> a.start.compareTo(b.start));
			System.out.printf("Using Hopcroft Karp: %s\n", matches);
		}
	}
}
