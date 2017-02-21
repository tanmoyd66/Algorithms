package trd.algorithms.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class ShortestChainXformation {
	
	// Class to abstract priority queue element
	public static class QueueElement {
		String str; Integer DistFromStart;
		QueueElement(String str, Integer DistFromStart) {
			this.str = str; this.DistFromStart = DistFromStart;
		}
		public String toString() {
			return str + "(" + DistFromStart + ")";
		}
	}
	
	// Hash-Table to hold memo
	HashMap<String, Integer> hm = new HashMap<String, Integer>();
	
	// Given two strings find the distance between two
	public int Distance(String s1, String s2) {
		Integer lookupVal = hm.get(s1 + ":" + s2);
		if (lookupVal != null)
			return lookupVal;
		
		int ret = 0;
		if (s1.isEmpty() && !s2.isEmpty())
			ret = s2.length();
		else if (!s1.isEmpty() && s2.isEmpty())
			ret =  s1.length();
		else if (s1.isEmpty() && s2.isEmpty())
			ret = 0;
		else {
			int dist1 = Distance(s1.substring(1, s1.length()), s2.substring(1, s2.length())) +
								(s1.charAt(0) == s2.charAt(0) ? 0 : 1);
			int dist2 = Distance(s1, s2.substring(1, s2.length())) + 1;
			int dist3 = Distance(s1.substring(1, s1.length()), s2) + 1;
			ret = Math.min(Math.min(dist3, dist2), dist1);
		}
		hm.put(s1 + ":" + s2, ret);
		return ret;
	}
	
	// Given a Dictionary, find a shortest chain of words that will
	// transform s1 to s2 using words in dict
	public List<String> Find(String s1, String s2, int acceptable, Set<String> dict) {
		
		List<String> xformList = new ArrayList<String>();
		
		// Create a Priority Queue - we will use shortest path algorithm
		PriorityQueue<QueueElement> pq = new PriorityQueue<QueueElement>(
											(a,b)->a.DistFromStart.compareTo(b.DistFromStart));
		pq.add(new QueueElement(s1, 0));
		
		// Basically running Dijkstra
		while (!pq.isEmpty()) {
			
			// Find the best word
			QueueElement qe = pq.poll();
			xformList.add(qe.str);
			dict.remove(qe.str);

			// If the distance between the current word and the end is acceptable stop
			if (Distance(qe.str, s2) < acceptable) {
				xformList.add(s2);
				break;
			} 
			
			// Find a the distance between two words
			for (String word : dict) {
				int dist = Distance(qe.str, word);
				
				// Update current element in PQ as you do in Dijkstra
				pq.removeIf((QueueElement e)-> e.str.equals(word)); 
				pq.add(new QueueElement(word, dist + qe.DistFromStart));
			}
		}
		return xformList;
	}
	
	public static void main(String[] args) {
		ShortestChainXformation scx = new ShortestChainXformation();
		String[] strDict = new String[] { "poon", "plee", "same", "poie", "plie", "poin", "plea" };
		Set<String> dict = new HashSet<String>();
		for (String str : strDict) dict.add(str);
	    String start = "toon";
	    String end   = "plea";
	    List<String> xForm = scx.Find(start, end, 2, dict);
	    System.out.printf("%s\n", xForm);
	}
}
