package trd.algorithms.concurrency;

import java.security.InvalidParameterException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadersWritersMonitor implements ILocker{
	public int numReaders = 0;
	public int numWriters = 0;
	Lock lock = new ReentrantLock();
	
	public void AcquireRead() throws InterruptedException {
		synchronized (this) {
			boolean fLocked = false;
			try {
				while (true) {
					if (numWriters > 0) {
						lock.lock();
						fLocked = true;
					}
					if (numWriters == 0)
						break;
				}
				numReaders++;
			} finally {
				if (fLocked)
					lock.unlock();
			}
		}
	}
	
	public void ReleaseRead() {
		synchronized(this) {
			numReaders--;
		}
	}
	
	public void AcquireWrite() throws InterruptedException {
		synchronized (this) {
			boolean fLocked = false;
			try {
				while (true) {
					if (numReaders > 0 || numWriters > 0) {
						lock.lock();
						fLocked = true;
					}
					if (numWriters == 0 && numReaders == 0)
						break;
				}
				numWriters++;
			} finally {
				if (fLocked)
					lock.unlock();
			}
		}
	}
	
	public void ReleaseWrite() {
		synchronized(this) {
			numWriters--;
		}
	}

	@Override
	public void HasWriters() throws Exception {
		if (numWriters > 0)
			throw new InvalidParameterException("");
	}
}
