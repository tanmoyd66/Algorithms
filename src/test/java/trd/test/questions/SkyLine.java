package trd.test.questions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class SkyLine {
	public static class Input {
		int s; int e; int h;
		public Input(int s, int e, int h) {
			this.s = s; this.e = e; this.h= h;
		}
	}
	public static class Output implements Comparable<Output> {
		int x; int y; boolean isStart;
		public Output(int x, int y, boolean isStart) {
			this.x = x; this.y = y; this.isStart = isStart;
		}
		public String toString() {
			return x + "," + y + "," + (isStart ? "S" : "E");
		}

		public int compareTo(Output that) {
			if (this.x == that.x) {
				if (this.isStart && that.isStart) {
					return ((Integer)this.y).compareTo(that.y);
				}
				else
					return ((Integer)that.y).compareTo(this.y);
			} else {
				return ((Integer)this.x).compareTo(that.x);
			}
		}
	}
	
	public static List<Output> Find(List<Input> buildings) {
		
		// Convert input into output form
		List<Output> bs = new ArrayList<Output>();
		for (Input building : buildings) {
			bs.add(new Output(building.s, building.h, true));
			bs.add(new Output(building.e, building.h, false));
		}
		
		// Sort with rules
		Collections.sort(bs);
		
		// Create priority queue
		TreeMap<Integer, Integer> pq = new TreeMap<Integer, Integer>();
		pq.put(0, 1);
		Integer prev_max_height = 0, curr_max_height;
		
		// process output records in order
		List<Output> ret = new ArrayList<Output>();
		for (Output op : bs) {
			if (op.isStart) {
				pq.compute(op.y, (k,v)-> { if (v != null) return v + 1; return 1; });
			} else {
				pq.compute(op.y, (k,v)-> { if (v == 1) return null; return v - 1; });
			}
			curr_max_height = pq.lastKey();
			if (curr_max_height != prev_max_height) {
				ret.add(new Output(op.x, curr_max_height, true));
				prev_max_height = curr_max_height;
			}
		}
		return ret;
	}
	
	public static void main(String[] args) {
		List<Input> input = new ArrayList<>();
		input.add(new Input(1, 3, 4));
		input.add(new Input(3, 4, 4));
		input.add(new Input(2, 6, 2));
		input.add(new Input(8,11, 4));
		input.add(new Input(7, 9, 3));
		input.add(new Input(10,11, 2));
		List<Output> result = Find(input);
		System.out.printf("%s\n", result);
	}
}
