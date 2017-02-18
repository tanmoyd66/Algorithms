package trd.algorithms.concurrency;

public interface ILocker {	
	public void AcquireRead()  throws InterruptedException;
	public void ReleaseRead();
	public void AcquireWrite() throws InterruptedException;
	public void ReleaseWrite();
	public void HasWriters() throws Exception;
}
