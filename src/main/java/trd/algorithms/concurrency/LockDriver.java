package trd.algorithms.concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class LockDriver<T extends ILocker> {

	@FunctionalInterface
	public static interface Execute {
	    void accept() throws Exception;
	}
	
	public static double TimedExecute(Execute fn) {
		try {
			long startTime = System.nanoTime();
			fn.accept();
			long endTime = System.nanoTime();
			return (endTime - startTime)/1e6;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static class MyRunnable implements Runnable {
		int 	 id;
		Random   rand = new Random();
		int 	 numReads  = 0, numWrites  = 0;
		double 	 timeReads = 0, timeWrites = 0;
		ReadersWritersCondition rwc;
		public MyRunnable(int id, ReadersWritersCondition rwc) {
			this.id = id;
			this.rwc = rwc;
		}
		
		@Override
		public void run() {
			for (;;) {
				try {
					int op = rand.nextInt();
					if (op % 9 == 0) {
						timeWrites += TimedExecute(() -> { rwc.AcquireWrite();});
						Thread.sleep(rand.nextInt(5) + 1);
						numWrites++;
						timeWrites += TimedExecute(() -> { rwc.ReleaseWrite(); });
					} else {
						timeReads += TimedExecute(() -> { rwc.AcquireRead();});
						Thread.sleep(rand.nextInt(5) + 1);
						try { rwc.HasWriters(); } catch (Exception e) { e.printStackTrace(); }
						numReads++;
						timeReads += TimedExecute(() -> {rwc.ReleaseRead(); });
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if ((numReads + numWrites) % 500 == 0) {
					System.out.printf("[%3d] Reads:%8d (%8.2f), Writes:%8d (%8.2f)\n", 
								id, numReads, timeReads/numReads, numWrites, timeWrites/numWrites);
//					timeReads = timeWrites = 0.0;
				} else if ((numReads + numWrites) > 3000)
					break;
			}
		}
	}

	public void PerformTest(String tag) {
		try {
			System.out.printf("%s---------------------------------------\n", tag);
			ExecutorService executorService = Executors.newFixedThreadPool(5);
			ReadersWritersCondition rwc = new ReadersWritersCondition();
			for (int i = 0; i < 5; i++) {
				MyRunnable mr = new MyRunnable(i + 1, rwc);
				executorService.submit(mr);
			}
			executorService.shutdown();
			executorService.awaitTermination(10, TimeUnit.MINUTES);
			System.out.printf("%s---------------------------------------\n", tag);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		(new LockDriver<ReadersWritersCondition>()).PerformTest("ReadersWritersCondition");
		(new LockDriver<ReadersWritersMonitor>()).PerformTest("ReadersWritersMonitor");
	}
}
