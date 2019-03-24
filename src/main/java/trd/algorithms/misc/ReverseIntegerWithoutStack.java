package trd.algorithms.misc;

public class ReverseIntegerWithoutStack {
	public static int reverse(int val) {
		int rev = 0;
		while (val > 0) {
			int dig = val % 10;
			val = val / 10;
            if (rev > Integer.MAX_VALUE/10 || (rev == Integer.MAX_VALUE / 10 && dig > 7)) 
            	return 0;
            if (rev < Integer.MIN_VALUE/10 || (rev == Integer.MIN_VALUE / 10 && dig < -8)) 
            	return 0;
			rev = rev * 10 + dig;
		}
		return rev;
	}
	
	public static void main(String[] args) {
		int[] nums = {12345, 1230};
		for (int i = 0; i < nums.length; i++) {
			System.out.printf("Reverse of %d is %d\n", nums[i], reverse(nums[i]));
		}
	}
}