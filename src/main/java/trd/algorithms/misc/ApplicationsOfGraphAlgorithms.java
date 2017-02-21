package trd.algorithms.misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import trd.algorithms.graphs.Graph;
import trd.algorithms.linkedlists.SinglyLinkedList;
import trd.algorithms.utilities.Tuples;

public class ApplicationsOfGraphAlgorithms {
	public static List<Integer> CourseOrdering(Integer[] courseIds, 
										List<Tuples.Pair<Integer, Integer>> preReqs) {
		List<Integer> ret = new ArrayList<Integer>();
		
		// Create a set to handle all courses that do not have a pre-req
		Set<Integer> courses = new HashSet<Integer>();
		for (Integer course: courseIds)
			courses.add(course);
		
		// Create a graph
		Graph<Integer> g = new Graph<Integer>("Courses");
		for (Tuples.Pair<Integer, Integer> preReq : preReqs) {
			g.addEdge(preReq.elem1, preReq.elem2);
			if (courses.contains(preReq.elem1))
				courses.remove(preReq.elem1);
			if (courses.contains(preReq.elem2))
				courses.remove(preReq.elem2);
		}
		
		// Do a topological sort on the graph
		SinglyLinkedList<Integer> sll = g.TopologicalSort(false);

		// Add courses to final list
		for (Integer course : courses)
			ret.add(course);
		for (SinglyLinkedList.Node<Integer> p = sll.getHead(); p != null; p = p.next)
			ret.add(p.value);
		
		return ret;
	}
	
	public static void main(String[] args) {
		if (true) {
			Integer[] courses = new Integer[] { 1, 2, 3, 4, 5};
			List<Tuples.Pair<Integer, Integer>> preReqs = new ArrayList<Tuples.Pair<Integer, Integer>>();
			preReqs.add(new Tuples.Pair<Integer, Integer>(1, 3));
			preReqs.add(new Tuples.Pair<Integer, Integer>(1, 2));
			preReqs.add(new Tuples.Pair<Integer, Integer>(2, 4));
			preReqs.add(new Tuples.Pair<Integer, Integer>(2, 3));
			List<Integer> orderedCourses = CourseOrdering(courses, preReqs);
			System.out.printf("Ordering: %s\n", orderedCourses);
		}
	}
}
