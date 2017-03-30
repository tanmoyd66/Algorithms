package trd.algorithms.datastructures;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import trd.algorithms.utilities.Utilities;

public class SimHash {
	public static interface IWordSeg {
		public List<String> tokens(String doc);
	}

	public static class BinaryWordSeg implements IWordSeg {
		@Override
		public List<String> tokens(String doc) {
			List<String> binaryWords = new LinkedList<String>();
			for (int i = 0; i < doc.length() - 1; i += 1) {
				StringBuilder bui = new StringBuilder();
				bui.append(doc.charAt(i)).append(doc.charAt(i + 1));
				binaryWords.add(bui.toString());
			}
			return binaryWords;
		}
	}

	public static class TextWordSeg implements IWordSeg {
		@Override
		public List<String> tokens(String doc) {
			String[] words = doc.split(" ");
			return Arrays.asList(words);
		}
	}

	public static class Simhash {

		private IWordSeg wordSeg;

		public Simhash(IWordSeg wordSeg) {
			this.wordSeg = wordSeg;
		}

		public int hammingDistance(int hash1, int hash2) {
			int i = hash1 ^ hash2;
			i = i - ((i >>> 1) & 0x55555555);
			i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
			i = (i + (i >>> 4)) & 0x0f0f0f0f;
			i = i + (i >>> 8);
			i = i + (i >>> 16);
			return i & 0x3f;
		}

		public int hammingDistance(long hash1, long hash2) {
			long i = hash1 ^ hash2;
			i = i - ((i >>> 1) & 0x5555555555555555L);
			i = (i & 0x3333333333333333L) + ((i >>> 2) & 0x3333333333333333L);
			i = (i + (i >>> 4)) & 0x0f0f0f0f0f0f0f0fL;
			i = i + (i >>> 8);
			i = i + (i >>> 16);
			i = i + (i >>> 32);
			return (int) i & 0x7f;
		}

		public long simhash64(String doc) {
			int bitLen = 64;
			int[] bits = new int[bitLen];
			List<String> tokens = wordSeg.tokens(doc);
			for (String t : tokens) {
				long v = MurmurHash.hash64(t);
				for (int i = bitLen; i >= 1; --i) {
					if (((v >> (bitLen - i)) & 1) == 1)
						++bits[i - 1];
					else
						--bits[i - 1];
				}
			}
			long hash = 0x0000000000000000;
			long one = 0x0000000000000001;
			for (int i = bitLen; i >= 1; --i) {
				if (bits[i - 1] > 1) {
					hash |= one;
				}
				one = one << 1;
			}
			return hash;
		}

		public long simhash32(String doc) {
			int bitLen = 32;
			int[] bits = new int[bitLen];
			List<String> tokens = wordSeg.tokens(doc);
			for (String t : tokens) {
				int v = MurmurHash.hash32(t);
				for (int i = bitLen; i >= 1; --i) {
					if (((v >> (bitLen - i)) & 1) == 1)
						++bits[i - 1];
					else
						--bits[i - 1];
				}
			}
			int hash = 0x00000000;
			int one = 0x00000001;
			for (int i = bitLen; i >= 1; --i) {
				if (bits[i - 1] > 1) {
					hash |= one;
				}
				one = one << 1;
			}
			return hash;
		}
	}

	public static void main(String[] args) throws Exception {

		args = new String[] { "/home/trd/oss/Algorithms/data_sets/test_in2",
							  "/home/trd/oss/Algorithms/data_sets/test_out" };
		long start = System.nanoTime();

		// Creates SimHash object.
		Simhash simHash = new Simhash(new TextWordSeg());

		// DocHashes is a list that will contain all of the calculated hashes.
		ArrayList<Long> docHashes = Lists.newArrayList();

		// Maps 12-bit key with the documents matching the partial hash
		Map<BitSet, HashSet<Integer>> hashIndex = Maps.newHashMap();

		// Read the documents. (Each line represents a document).
		List<String> docs = readDocs(args);

		int idx = 0;

		System.out.println("Start to build index...");
		for (String doc : docs) {
			// Calculate the document hash.
			long docHash = simHash.simhash64(doc);
			System.out.println("Document=[" + doc + "] Hash=[" + Utilities.BreakInParts(Long.toBinaryString(docHash), 12) + "]");

			// Store the document hash in a list.
			docHashes.add(docHash);

			// StringBuilder keyBuilder = new StringBuilder(12);
			BitSet key = new BitSet(12);

			int step = 0;

			for (int i = 0; i < 64; ++i) {
				key.set(step, ((docHash >> i) & 1) == 1);
				if (++step == 12 || i == 63) {

					// a) Separates the hash in 12-bit keys. 
					// b) This value will be a key in hashIndex. 
					// c) hashIndex will contain sets of documents matching each key (12-bits).
					if (hashIndex.containsKey(key)) {
						hashIndex.get(key).add(idx);
					} else {
						HashSet<Integer> vector = new HashSet<Integer>();
						vector.add(idx);
						hashIndex.put(key, vector);
					}
					step = 0;
					System.out.printf("%s ", key);
					key = new BitSet(12); // reset key holder.
				}
			}
			System.out.println();
			++idx;
		}
		System.out.println("Index has been built.");
		File output = new File(args[1]);
		idx = 0;
		BitSet bits = new BitSet(docs.size());

		for (String doc : docs) {
			// For each document.

			if (bits.get(idx)) {
				++idx;
				continue;
			}

			// Calculates document hash.
			long docHash = simHash.simhash64(doc);
			BitSet key = new BitSet(12);

			int step = 0;
			HashSet<Integer> docSimilarCandidates = Sets.newHashSet();
			for (int i = 0; i < 64; ++i) {
				key.set(step, ((docHash >> i) & 1) == 1);

				if (++step == 12 || i == 63) {

					// a) Separates the hash in 12-bit keys. 
					// b) This value will be a key in hashIndex. 
					// c) hashIndex will contain sets of documents matching each key (12-bits).
					if (hashIndex.containsKey(key)) {
						docSimilarCandidates.addAll(hashIndex.get(key));
					}
					step = 0;
					key = new BitSet(12);
				}
			}
			List<Integer> similarDocs = Lists.newLinkedList();
			Map<Integer, Integer> docDistances = Maps.newHashMap();
			for (Integer i : docSimilarCandidates) {
				int dist = simHash.hammingDistance(docHash, docHashes.get(i));
				if (dist <= 3) {
					similarDocs.add(i);
					bits.set(i);
					docDistances.put(i, dist);
				}
			}
			if (!similarDocs.isEmpty()) {
				Files.append("Documents similar as [" + doc + "]:\n", output, Charsets.UTF_8);
				for (int i : similarDocs) {
					if (i == idx)
						continue;
					Files.append("[" + docs.get(i) + "]\tDistance=[" + docDistances.get(i) + "]\n", output,
							Charsets.UTF_8);
				}
				Files.append("End\n", output, Charsets.UTF_8);
			}
			bits.set(idx);
			++idx;
		}

		System.out.println("Elapsed time: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
	}

	private static List<String> readDocs(String[] args) throws IOException {
		return Files.readLines(new File(args[0]), Charsets.UTF_8);
	}

}
