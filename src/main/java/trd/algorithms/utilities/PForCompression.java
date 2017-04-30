package trd.algorithms.utilities;

import me.lemire.integercompression.differential.*;

import java.nio.ByteBuffer;
import java.util.*;

import com.avast.Compressor;
import com.avast.jsnappy.SnappyCompressor;


public class PForCompression {
	public static void PForCompress(int[] data) {
		IntegratedIntCompressor iic = new IntegratedIntCompressor();
		System.out.println("Compressing " + data.length + " integers using friendly interface");
		int[] compressed = iic.compress(data);
		int[] recov = iic.uncompress(compressed);
		System.out.println("compressed from " + data.length * 4 / 1024 + "KB to " + compressed.length * 4 / 1024 + "KB");
		if (!Arrays.equals(recov, data))
			throw new RuntimeException("bug");
	}
	
	public static void SnappyCompress(int[] data) {
		System.out.println("Compressing " + data.length + " integers using friendly interface");
		Compressor sc = new SnappyCompressor(0, 0);
		ByteBuffer bb = ByteBuffer.allocate(data.length * 4);
		for (int i = 0; i < data.length; i++)
			bb.putInt(data[i]);
		try {
			ByteBuffer bbOut = sc.compress(bb);
			System.out.println("compressed from " + data.length * 4 / 1024 + "KB to " + bbOut.capacity() * 1 / 1024 + "KB");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int[] data = new int[2342351];
		for (int k = 0; k < data.length; ++k)
			data[k] = k;
		PForCompress(data);
		SnappyCompress(data);
	}
}
