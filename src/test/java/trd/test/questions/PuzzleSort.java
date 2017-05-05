package trd.test.questions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import trd.algorithms.graphs.Graph.Color;

public class PuzzleSort {
	public static class Graph<T extends Comparable<T>> {

		HashMap<T,Integer> 							vertexMap 	= new HashMap<T,Integer>();
		HashMap<Integer,T> 							invVertMap 	= new HashMap<Integer,T>();
		HashMap<Integer,HashMap<Integer,Edge<T>>>	adjList		= new HashMap<Integer,HashMap<Integer,Edge<T>>>();
		List<Edge<T>>								edgeList	= new ArrayList<Edge<T>>();
		Integer										startNode	= 1;
		Integer										maxVertex 	= 0;
		public static class Node<T extends Comparable<T>> {
			Integer		node;
			T			nodeName;
			Color		color;
			Node<T>		parent;
			int			start;
			int			end;
			int			label;
			Graph<T>	graph;
			
			public Node(Graph<T> graph, Integer node) {
				this.graph = graph; this.node = node; this.color = Color.white;
				this.nodeName = graph.getVertexById(node); 
			}
		}
		
		public static class Edge<T extends Comparable<T>> {
			Integer		source;
			Integer 	target;
			Graph<T>	graph;
			
			public Edge(Graph<T> graph, Integer source, Integer target) {
				this.graph = graph; this.source = source; this.target = target;
			}
		}
		protected T getVertexById(Integer id) {
			return invVertMap.get(id);
		}
		public Integer getVertexId(T source) {
			Integer sourceId = vertexMap.get(source);
			if (sourceId == null) {
				sourceId = ++maxVertex;
				vertexMap.put(source, sourceId);
				invVertMap.put(sourceId, source);
			}
			return sourceId;
		}		
		protected void addEdge(T source, T target) {
			Integer sourceId = getVertexId(source);
			Integer targetId = getVertexId(target);
			HashMap<Integer,Edge<T>> edgeList = adjList.get(sourceId);
			if (edgeList == null) {
				edgeList = new HashMap<Integer,Edge<T>>();
				adjList.put(sourceId, edgeList);
			}
			edgeList.put(targetId, new Edge<>(this, sourceId, targetId));
			edgeList = adjList.get(targetId);
			if (edgeList == null) {
				edgeList = new HashMap<Integer,Edge<T>>();
				adjList.put(targetId, edgeList);
			}
		}
		protected Collection<Edge<T>> getEdgesByVertexId(Integer id) {
			HashMap<Integer,Edge<T>> edgeMap = adjList.get(id);
			if (edgeMap != null) {
				return edgeMap.values();
			}
			return Collections.emptyList();
		}
		protected HashMap<Integer, Node<T>> InitializeVertexMap(Integer startId) {
			HashMap<Integer, Node<T>> nodeMap = new HashMap<Integer, Node<T>>();
			Set<Integer> vertexSet = invVertMap.keySet();
			for (Integer v : vertexSet) {
				Node<T> vNode = new Node<>(this, v); 
				nodeMap.put(v, vNode);
			}
			return nodeMap;
		}
		public String toString(Map<Integer,Node<T>> nodeMap) {
			StringBuilder sb = new StringBuilder();
			Set<Map.Entry<Integer,HashMap<Integer,Edge<T>>>> meSet = adjList.entrySet();
			sb.append("\n------------------------------");
			for (Map.Entry<Integer,HashMap<Integer,Edge<T>>> me : meSet) {
				Node<T> node = nodeMap.get(me.getKey());
				sb.append(String.format("\n[%2s(%2d){%s}]: ", 
							invVertMap.get(me.getKey()), me.getKey(),
							String.format("%d-%d", node.start, node.end)));
				HashMap<Integer,Edge<T>> edgeMap = me.getValue();
				if (edgeMap != null) {
					Set<Map.Entry<Integer, Edge<T>>> edgeSet = edgeMap.entrySet();
					for (Map.Entry<Integer, Edge<T>> meEdge : edgeSet) {
						sb.append(String.format("[%2s] ", invVertMap.get(meEdge.getKey())));
					}
				}
			}
			sb.append("\n------------------------------");
			return sb.toString();
		}
		protected void TopSort(Node<T> dfsNode, String label, AtomicInteger time, Map<Integer,Node<T>> nodeMap, List<T> NodeList) {

			// White Node has just been discovered
			time.incrementAndGet();

			// set the color stating that we have started exploring
			dfsNode.color = Color.gray;
			dfsNode.start = time.get();
			
			Float nodeLabel = Float.parseFloat(label);
			NodeList.add(dfsNode.nodeName);

			// explore children recursively
			Collection<Edge<T>> edges = getEdgesByVertexId(dfsNode.node);
			int childNum = 0;
			Integer baseThisLevel = Math.round(nodeLabel) + 1;
			for (Edge<T> edge : edges) {
				Node<T> dfsnTarget = nodeMap.get(edge.target);
				String childLabel = edges.size() == 1 ? baseThisLevel.toString() : baseThisLevel + "." + ++childNum; 
				if (dfsnTarget.color == Color.white) {
					dfsnTarget.parent = dfsNode;
					TopSort(dfsnTarget, childLabel, time, nodeMap, NodeList);
				}
			}
			// set complete
			dfsNode.color = Color.black;
			dfsNode.end = time.incrementAndGet();
		}
		public void TopSort(List<T> NodeList) {
			Map<Integer,Node<T>> nodeMap = InitializeVertexMap(startNode);
			System.out.println(toString(nodeMap));
			AtomicInteger time = new AtomicInteger(0);
			Integer treeNum = 0;
			for (Node<T> node : nodeMap.values())
				if (node.color == Color.white)
					TopSort(node, (++treeNum).toString(), time, nodeMap, NodeList); 
			System.out.println(toString(nodeMap));
		}
	}
	
	public static List<Character> DoPuzzleSort(String[] A) {
		Graph<Character> dependencies 	= new Graph<>();
		
		// Iterate thru A, comparing adjoined entities
		for (int i = 0; i < A.length - 1; i++) {
			String s1 = A[i], s2 = A[i + 1];
			for (int j = 0; j < s1.length(); j++) {
				if (s1.charAt(j) == s2.charAt(j))
					continue;
				dependencies.addEdge(s1.charAt(j), s2.charAt(j));
				break;
			}
		}

		List<Character> vertexList = new ArrayList<Character>();
		dependencies.TopSort(vertexList);
		return vertexList;
	}
	
	public static void main(String[] args) {
		String[] strings01 = new String[] {"a", "b", "c", "d", "e", "f" };
		System.out.println(DoPuzzleSort(strings01));

		String[] strings02 = new String[] {"a", "cb", "bc", "bd", "de", "df" };
		System.out.println(DoPuzzleSort(strings02));
	}
}
