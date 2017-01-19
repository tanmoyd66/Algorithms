package trd.algorithms.utilities;

// Knapsack Problem
public class CompositeKey<K1,K2> {
	K1 k1; K2 k2;
	public CompositeKey(K1 k1, K2 k2) {
		this.k1 = k1; this.k2 = k2; 
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		return ((CompositeKey<K1,K2>)other).k1 == k1 && ((CompositeKey<K1,K2>)other).k2 == k2;
	}
	@Override
	public int hashCode() {
		return (k1.toString()+":"+k2.toString()).hashCode();
	}
}
