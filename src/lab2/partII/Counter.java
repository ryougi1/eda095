package lab2.partII;

public class Counter {
	private static int count = 0;
	
	public static synchronized void countUp() {
		count++;
	}
	
	public static synchronized void countDown() {
		count--;
	}
	
	public int getCount() {
		return count;
	}
}
