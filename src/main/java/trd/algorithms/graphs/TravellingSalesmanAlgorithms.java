package trd.algorithms.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import trd.algorithms.Arrays.PermutationIterator;
import trd.algorithms.graphs.Graph.AlgoSpecificNode;
import trd.algorithms.graphs.Graph.Edge;
import trd.algorithms.graphs.Graph.Node;
import trd.algorithms.graphs.GraphFactory.Point;
import trd.algorithms.utilities.ArrayPrint;

public class TravellingSalesmanAlgorithms<T extends Comparable<T>> {

	private Graph<T>	graph;
	public TravellingSalesmanAlgorithms(Graph<T> graph) {
		this.graph = graph;
	}
	
	public List<Edge<T>> TSP_PermutationGeneration(T start) {
		List<Edge<T>>	bestPath 		= null;
		Double			bestPathCost	= Double.MAX_VALUE;
		
		Integer	startVertex = graph.getVertexId(start);
		
		// Get vertex set
		Set<Integer> vertexSet 	= graph.getVertexSet();
		Integer[] vertexArray	= new Integer[vertexSet.size()];
		vertexSet.toArray(vertexArray);
		
		// Iterate over all permutation of vertices, picking those up that form
		//	a path from start back to start
		PermutationIterator<Integer> pi = new PermutationIterator<Integer>(vertexArray);
		while (pi.hasNext()) {
			Comparable<Integer>[] thisVertexPerm = pi.next();
			if (thisVertexPerm[0].compareTo(startVertex) != 0 ||
				graph.getEdgeByStartEnd((Integer)thisVertexPerm[thisVertexPerm.length - 1], startVertex) == null)
				continue;
			
			Double thisPathCost = 0.0;
			List<Edge<T>> edgeList = new ArrayList<Edge<T>>(); 
			Integer prevVertex = (Integer)thisVertexPerm[0];
			for (int i = 1; i < thisVertexPerm.length; i++) {
				Integer thisVertex = (Integer)thisVertexPerm[i];
				Edge<T> thisEdge = graph.getEdgeByStartEnd(prevVertex, thisVertex);
				if (thisEdge != null) {
					thisPathCost += thisEdge.weight;
					edgeList.add(thisEdge);
				} else {
					// There is an invalid edge in this tour
					continue;
				}
				prevVertex = thisVertex; 
			}
			
			// Check the cycle back to start vertex
			Edge<T> thisEdge = graph.getEdgeByStartEnd((Integer)thisVertexPerm[thisVertexPerm.length - 1], startVertex);
			thisPathCost += thisEdge.weight;
			edgeList.add(thisEdge);
			
			// Set the best path
			if (thisPathCost < bestPathCost) {
				bestPathCost = thisPathCost;
				bestPath = edgeList;
			}
		}
		return bestPath;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// Strategy
	//		C[i, S] = cost of going from start to i (thru S) and back to start.
	//				= min over all sets of the PowerSet of S where S does not include i
	//		We start with S = null set and calculate C[i,null] for all i != start
	//		Then we calculate C[i, V-{i}] for all i
	//		Then we calculate C[i, 2 at a time] etc..
	// This algorithm is exponential in nature but better than factorial as in the permutation generation
	
	public List<Edge<T>> TSP_HeldKarp(T start) {
		List<Edge<T>>	bestPath 		= null;
		Double			bestPathCost	= Double.MAX_VALUE;
		
		
		
		return bestPath;
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// Strategy
	//		O(n^2) works on Euclidean TSP only
	//		Uses notion of Bitonic path: 
	//				A path which strictly goes right and then goes left after reaching the right most point
	//		We need to sort all the points based on their x-coordinate
	//
	@SuppressWarnings("unchecked")
	public List<Edge<T>> TSP_EuclideanBitonic(T start) {
		List<Edge<T>>	bestPath 		= new ArrayList<Edge<T>>();
		Integer			startVertex 	= graph.getVertexId(start);
		Set<Integer>	vertexSet		= graph.getVertexSet();
		int				vertexCount		= vertexSet.size();
		HashMap<Integer, AlgoSpecificNode<T>> nodeMap = graph.InitializeVertexMap(startVertex, 0.0, 0.0);
		Double[][] 		CostMatrix 		= new Double[vertexCount][vertexCount];
		Integer[][] 	Neighbors 		= new Integer[vertexCount][vertexCount];
		

//		Double[][]		AdjMatrix		= graph.GetAdjacencyMatrix();
//		System.out.printf("AdjM:%s\n", ArrayPrint.MatrixToString(AdjMatrix, vertexCount, vertexCount));
		
		// Define comparator to compare points based on their X-Coordinates
		Comparator<AlgoSpecificNode<T>> comp = (AlgoSpecificNode<T> a, AlgoSpecificNode<T> b) -> {
			Point pA = (Point)a.node.garnish;
			Point pB = (Point)b.node.garnish;
			return pA.X.compareTo(pB.X);
		};
		
		// Sort the points based on their X-coordinate
		List<AlgoSpecificNode<T>> points = new ArrayList<AlgoSpecificNode<T>>(nodeMap.values());
		Collections.sort(points, comp);

		// Initialize Cost and Neighbor matrix
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				CostMatrix[i][j] = 95.0;
				Neighbors[i][j] = 0;
			}
		}
		CostMatrix[0][1] = graph.getEdgeByStartEnd(points.get(0).node.node, points.get(1).node.node).weight;
		Neighbors[0][1]  = 0;
		
		for (int j = 2; j < vertexCount; j++) {
			for (int i = 0; i < j; i++) {
				if (j > i + 1) {
					Edge<T> edge = graph.getEdgeByStartEnd(points.get(j).node.node, points.get(j-1).node.node);
					CostMatrix[i][j] = CostMatrix[i][j - 1] + edge.weight;
					Neighbors[i][j]  = j-1;
				} else {
					CostMatrix[i][j] = 99.0;
					for (int k = 0; k < i; k++) {
						Edge<T> edge = graph.getEdgeByStartEnd(points.get(j).node.node, points.get(k).node.node);
						Double thisCost = edge.weight + CostMatrix[k][i];
						if (thisCost < CostMatrix[i][j]) {
							CostMatrix[i][j] = thisCost;
							Neighbors[i][j]  = k;
						}
					}
				}
			}
		}

		// Print the Cost and Neighbor Matrices
//		System.out.printf("Cost:%s\n", ArrayPrint.MatrixToString(CostMatrix, vertexCount, vertexCount));
//		System.out.printf("Neighbors:%s\n", ArrayPrint.MatrixToString(Neighbors, vertexCount, vertexCount));

		// Construct the tour
		Stack<AlgoSpecificNode<T>> S0 = new Stack<AlgoSpecificNode<T>>(); 
		Stack<AlgoSpecificNode<T>> S1 = new Stack<AlgoSpecificNode<T>>(); 
		Object[] S = new Object[] { S0, S1 };
		
		int k = 0, i = vertexCount - 2, j = vertexCount - 1;
		while (j > 0) {
			Stack<AlgoSpecificNode<T>> stack = (Stack<AlgoSpecificNode<T>>)S[k];
			stack.push(points.get(j));
			j = Neighbors[i][j];
			if (j < i) {
				int temp = i; i = j; j = temp;
				k = 1 - k;
			}
		}
		S0.push(points.get(0));
		
		// At this point S0 contains the right-moving vertices and
		// S1 points to the left-moving vertices
		AlgoSpecificNode<T> prev = S0.pop();
		Integer first = prev.node.node;
		AlgoSpecificNode<T> thisNode = null;
		while (!S0.isEmpty()) {
			thisNode = S0.pop();
			Edge<T> edge = graph.getEdgeByStartEnd(prev.node.node, thisNode.node.node);
			bestPath.add(edge);
			prev = thisNode;
		}
		while (!S1.isEmpty())
			S0.push(S1.pop());
		while (!S0.isEmpty()) {
			thisNode = S0.pop();
			Edge<T> edge = graph.getEdgeByStartEnd(prev.node.node, thisNode.node.node);
			bestPath.add(edge);
			prev = thisNode;
		}
		bestPath.add(graph.getEdgeByStartEnd(prev.node.node, first));
		return bestPath;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// Strategy
	//		2-Approximate
	//		Start with a MST from the start vertex
	//		Output is the pre-order traversal of the MST
	public List<Edge<T>> TSP_ApproximateByMST(T start) {
		List<Edge<T>>	bestPath = new ArrayList<Edge<T>>();
		
		// Run Minimal Spanning Tree using Prim's Algorithm 
		List<Edge<T>>	mst	= graph.MinimalSpanningTree_Prim(false, start);
		
		// Create a new Graph (as we will be destructive)
		Graph<T> mstGraph = new Graph<T>("");
		for (Edge<T> edge : mst) {
			mstGraph.addEdge(graph.getVertexById(edge.source), graph.getVertexById(edge.target), edge.weight);
			mstGraph.addEdge(graph.getVertexById(edge.target), graph.getVertexById(edge.source), edge.weight);
		}
		
		// Find an Euler tour on the new graph
		List<Node<T>> eulerTour = mstGraph.EulerPath(start);
		
		// Collapse edges of the Euler Tour to find the final tour
		Collections.reverse(eulerTour);
		for (int i = 1; i < eulerTour.size(); i++) {
			Edge<T> edge = graph.getEdgeByStartEnd(eulerTour.get(i - 1).nodeName, eulerTour.get(i).nodeName);
			bestPath.add(edge);
		}
		bestPath.add(graph.getEdgeByStartEnd(eulerTour.get(eulerTour.size() - 1).nodeName, eulerTour.get(0).nodeName));
		
		return bestPath;
	}

	public static void main(String[] args) {
		if (true) {
			Graph<String> graph5 = GraphFactory.getTSPGraph1();
			TravellingSalesmanAlgorithms<String> tspAlgos = new TravellingSalesmanAlgorithms<String>(graph5);
			List<Edge<String>> edgeList = tspAlgos.TSP_PermutationGeneration("1");
			Double cost = 0.0;
			System.out.printf("By Permutation [");
			for (Edge<String> edge : edgeList) {
				System.out.printf("(%s,%s) ", graph5.getVertexById(edge.source), graph5.getVertexById(edge.target));
				cost += edge.weight;
			}
			System.out.printf("] Total Cost: %4.2f\n", cost);
		}
		if (true) {
			Graph<String> graph5 = GraphFactory.getTSPGraph1();
			TravellingSalesmanAlgorithms<String> tspAlgos = new TravellingSalesmanAlgorithms<String>(graph5);
			List<Edge<String>> edgeList = tspAlgos.TSP_EuclideanBitonic("1");
			Double cost = 0.0;
			System.out.printf("By Bitonic     [");
			for (Edge<String> edge : edgeList) {
				System.out.printf("(%s,%s) ", graph5.getVertexById(edge.source), graph5.getVertexById(edge.target));
				cost += edge.weight;
			}
			System.out.printf("] Total Cost: %4.2f\n", cost);
		}
		if (true) {
			Graph<String> graph5 = GraphFactory.getTSPGraph1();
			TravellingSalesmanAlgorithms<String> tspAlgos = new TravellingSalesmanAlgorithms<String>(graph5);
			List<Edge<String>> edgeList = tspAlgos.TSP_ApproximateByMST("1");
			Double cost = 0.0;
			System.out.printf("By Approx-MST  [");
			for (Edge<String> edge : edgeList) {
				System.out.printf("(%s,%s) ", graph5.getVertexById(edge.source), graph5.getVertexById(edge.target));
				cost += edge.weight;
			}
			System.out.printf("] Total Cost: %4.2f\n", cost);
		}
	}
}
