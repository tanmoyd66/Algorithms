package trd.algorithms.sketching;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import trd.algorithms.utilities.Tuples;

public class CountMinSketch {
	HashFunction hashFunc = Hashing.murmur3_32();
	public int numHashes;
	public int numSlots;

	public final int[] 		randomCoefficients;
	public final int[][] 	slots;
	
	public final int hashSeed1, hashSeed2;
	
	public CountMinSketch(int numHashes, int numSlots) {
		this.numHashes = numHashes;
		this.numSlots = numSlots;
		
		Random rand = new Random();
		randomCoefficients = new int[numHashes];
		for (int i = 0; i < numHashes; i++) {
			randomCoefficients[i] = rand.nextInt();
		}
		
		slots = new int[numHashes][numSlots];
		hashSeed1 = rand.nextInt();
		hashSeed2 = rand.nextInt();
	}

	private Tuples.Pair<Integer, Integer> getHashValues(String word) {
		byte[] _word = word.getBytes();
		
		Hasher hasher1 = hashFunc.newHasher();
		Hasher hasher2 = hashFunc.newHasher();

		hasher1.putInt(hashSeed1); hasher1.putBytes(_word);
		hasher2.putInt(hashSeed2); hasher2.putBytes(_word);
		
		int hashCode1 = hasher1.hash().asInt(), hashCode2 = hasher2.hash().asInt();
		return new Tuples.Pair<>(Math.abs(hashCode1), Math.abs(hashCode2));
	}
	
	public void record(String word) {
		Tuples.Pair<Integer, Integer> hashCodes = getHashValues(word);
		for (int i = 0; i < numHashes; i++) {
			long hash_i = hashCodes.elem1 + randomCoefficients[i] * hashCodes.elem2;
			int slot_i = Math.abs((int) hash_i % numSlots);
			slots[i][slot_i]++;
		}
	}
	
	public int lookup(String word) {
		Tuples.Pair<Integer, Integer> hashCodes = getHashValues(word);
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < numHashes; i++) {
			long hash_i = hashCodes.elem1 + randomCoefficients[i] * hashCodes.elem2;
			int slot_i = Math.abs((int) hash_i % numSlots);
			min = Math.min(min, slots[i][slot_i]);
		}
		return min;
	}
	
	public static void main(String[] args) {
		String text = "Royal Challengers Bangalore's constant chopping and changing - of not only the personnel "
				+ "but also the roles of the players within the XI - has come in sharp focus after the side's "
				+ "four losses in their first four matches in this IPL. Shimron Hetmyer, for example, has had "
				+ "these roles in his first four matches in the IPL: middle order, dropped, opener, middle order. "
				+ "That has an effect on everybody's role in the XI. However, it can also be argued that good results "
				+ "bring about consistency in the XI and the player roles, and not the other way around. "
				+ "Virat Kohli, their captain, was asked after the game against Rajasthan Royals what was more important: "
				+ "a consistent XI or changes until the right combination is found. Kohli didn't commit either way, "
				+ "but hinted he needed to keep making changes to start winning matches.";
		String[] words = text.split(" ");
		HashMap<String, Integer> test = new HashMap<>();
		
		CountMinSketch cms = new CountMinSketch(7, 128);
		for (String word : words) {
			cms.record(word);
			Integer count = test.get(word);
			test.put(word, count == null ? 1 : count + 1);
		}
		
		int errors = 0;
		for (Map.Entry<String, Integer> entry : test.entrySet()) {
			int count = cms.lookup(entry.getKey());
			if (entry.getValue() != count) {
				System.out.printf("[%s]: HashMap:(%d), CMS:(%d)\n", entry.getKey(), entry.getValue(), count);
				errors++;
			}
		}
		System.out.printf("%d errors detected\n", errors);
	}
}
