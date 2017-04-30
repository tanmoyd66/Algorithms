package trd.algorithms.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.concurrent.TimeUnit.*;

public class SleepingBarber {
	
	public static class CustomerGenerator implements Runnable {
		public static final int ARRIVAL_INTERVAL_OFFSET_MILLIS = 10;
		public static final int ARRIVAL_INTERVAL_RANGE_MILLIS = 20;
		private final BarberShop shop;

		public CustomerGenerator(BarberShop shop) {
			this.shop = shop;
		}

		@Override
		public void run() {
			while (shop.isOpen()) {
				try {
					Thread.sleep(nextRandomInterval());
					shop.seatCustomerInWaitingRoom(new Object());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}

		public int nextRandomInterval() {
			return ThreadLocalRandom.current().nextInt(ARRIVAL_INTERVAL_RANGE_MILLIS) + ARRIVAL_INTERVAL_OFFSET_MILLIS;
		}

	}

	// Barber
	public static class Barber implements Runnable {
		private static final int HAIRCUT_TIME_MILLIS = 20;
		private final BarberShop shop;

		public Barber(BarberShop shop) {
			this.shop = shop;
		}

		@Override
		public void run() {
			while (shop.isOpen()) {
				try {
					Object customer = shop.napUntilCustomerArrives();
					cutHair(customer);
					shop.recordHaircut();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}

		private void cutHair(Object customer) throws InterruptedException {
			Thread.sleep(HAIRCUT_TIME_MILLIS);
		}

	}

	// Track and print progress
	public static class ProgressTracker implements Runnable {

		private final BarberShop shop;

		public ProgressTracker(BarberShop shop) {
			this.shop = shop;
		}

		@Override
		public void run() {
			while (shop.isOpen()) {
				try {
					Thread.sleep(100);
					printProgress();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
			printProgress();
			System.out.println();
		}

		private void printProgress() {
			System.out.printf("The shop served %4s customers but turned away %4s.\r", 
					shop.haircuts(), shop.lostCustomers());
		}
	}

	// The Shop
	public static class BarberShop {
		public static final int NUM_WAITING_ROOM_CHAIRS = 3;
		public static final long SHOP_RUNTIME_MILLIS = SECONDS.toMillis(10);
		private final static AtomicBoolean shopOpen = new AtomicBoolean();
		private final static AtomicInteger totalHaircuts = new AtomicInteger();
		private final static AtomicInteger lostCustomers = new AtomicInteger();
		private final BlockingQueue<Object> waitingRoom = new LinkedBlockingQueue<>(NUM_WAITING_ROOM_CHAIRS);

		private void close() {
			shopOpen.set(false);
		}

		private void open() {
			shopOpen.set(true);
		}

		public boolean isOpen() {
			return shopOpen.get();
		}

		public boolean seatCustomerInWaitingRoom(Object customer) {
			boolean customerSeated = waitingRoom.offer(customer);
			if (!customerSeated) {
				lostCustomers.incrementAndGet();
			}
			return customerSeated;
		}

		public Object napUntilCustomerArrives() throws InterruptedException {
			return waitingRoom.take();
		}

		public void recordHaircut() {
			totalHaircuts.incrementAndGet();
		}

		public Object lostCustomers() {
			return lostCustomers.get();
		}

		public Object haircuts() {
			return totalHaircuts.get();
		}

	}

	// Driver
	public static void main(String[] args) throws InterruptedException {

		BarberShop shop = new BarberShop();
		ExecutorService executor = Executors.newFixedThreadPool(4);

		Runnable customerGenerator = new CustomerGenerator(shop);
		Runnable barber1 = new Barber(shop);
		Runnable barber2 = new Barber(shop);
		Runnable progressTracker = new ProgressTracker(shop);

		shop.open();

		executor.execute(progressTracker);
		executor.execute(barber1);
		executor.execute(barber2);
		executor.execute(customerGenerator);
		executor.shutdown();

		Thread.sleep(BarberShop.SHOP_RUNTIME_MILLIS);

		shop.close();
	}
}
