package trd.algorithms.Arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trd.algorithms.utilities.ArrayPrint;

public class MobileNumberPad {

    static final Map<Integer, List<String>> PAD = new HashMap<>();

    static {
        PAD.put(0, Arrays.asList("".split(" ")));
        PAD.put(1, Arrays.asList("a b c".split(" ")));
        PAD.put(2, Arrays.asList("d e f".split(" ")));
        PAD.put(3, Arrays.asList("g h i".split(" ")));
        PAD.put(4, Arrays.asList("j k l".split(" ")));
        PAD.put(5, Arrays.asList("m n o".split(" ")));
        PAD.put(6, Arrays.asList("p q r".split(" ")));
        PAD.put(7, Arrays.asList("s t u".split(" ")));
        PAD.put(8, Arrays.asList("v w x".split(" ")));
        PAD.put(9, Arrays.asList("y z".split(" ")));
    }

    // Recursive Formulation:
    // Strategy:
    //	 Generate the string for each of A[i+1..N]
    //	 Append all choices of A[i] to the front of that.
    // This is doing the cross product recursively
    private static List<String> formWords(Integer[] arr, int i) {

        if (i == arr.length - 1)
            return new ArrayList<>(PAD.get(arr[i]));

        List<String> l = formWords(arr, i + 1);

        int size = l.size();
        for (int j = 0; j < size; j++) {

            String s = l.remove(0);
            for (String x : PAD.get(arr[i])) {
                l.add(x + s);
            }
        }
        return l;
    }
    
    public static void main(String[] args) {
    	Integer[] A = new Integer[] {1, 2, 3};
    	System.out.printf("%s: %s\n", ArrayPrint.ArrayToString("", A), formWords(A, 0));
    }
}