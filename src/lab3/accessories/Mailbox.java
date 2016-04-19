package lab3.accessories;

public class Mailbox {
	private String name;

	public synchronized void writeName(String n) {
		while (name != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		name = n;
	}

	public synchronized String takeName() {
		String tempName = name;
		name = null;
		notifyAll();
		return tempName;
	}
}
