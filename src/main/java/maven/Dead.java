package maven;

public class Dead {
	public static void main(String[] args) throws Exception {
		final Object resource1 = "resource1";
		final Object resource2 = "resource2";

		Thread t1 = new Thread() {
			public void run() {
				synchronized (resource1) {
					System.out.println("Thread1: locked resource1");
					// Pause for a bit, simulating some file IO or something
					// Basically, we just want to give the other thread a chance to run
					try { 
						Thread.sleep(100); 
					} catch (InterruptedException e) {}

					// Now wait till we can get a lock on resource2
					synchronized (resource2) {
						System.out.println("Thread1: locked resource2");
					}
				}
			}
		};

		Thread t2 = new Thread() {
			public void run() {
				synchronized (resource2) {
					System.out.println("Thread2: locked resource2");
					try {
						Thread.sleep(100); 
					} catch (InterruptedException e){}

					// Then it tries to lock resource1.  
					// But wait, thread 1 locked resource1, and 
					// won't release it till it gets a lock on resource2.  
					// This thread holds the lock on resource2 and won't release it till it gets resource1.  
					synchronized (resource1) {
						System.out.println("Thread2: locked resource1");
					}
				}
			}
		};

		t1.start(); 
		t2.start();
	}
}