package trd.algorithms.concurrency;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadersWritersCondition implements ILocker {
	int  numReadWrite 	= 0;
	Lock lock 			= new ReentrantLock();
	Condition cond 		= lock.newCondition();
	
	public void AcquireRead() throws InterruptedException {
		try {
			lock.lock();
			while (numReadWrite < 0) {
				cond.await();
			}
			numReadWrite++;
		} finally {
			lock.unlock();
		}
	}
	
	public void ReleaseRead() {
		try {
			lock.lock();
			if (numReadWrite > 0) {
				numReadWrite--;
				if (numReadWrite == 0)
					cond.signal();
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void AcquireWrite() throws InterruptedException {
		try {
			lock.lock();
			while (numReadWrite != 0) {
				cond.await();
			}
			numReadWrite = -1;
		} finally {
			lock.unlock();
		}
	}
	
	public void ReleaseWrite() {
		try {
			lock.lock();
			if (numReadWrite == -1) {
				numReadWrite = 0;
				cond.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void HasWriters() throws Exception {
		if (numReadWrite == -1)
			throw new InvalidParameterException("");
	}
}
