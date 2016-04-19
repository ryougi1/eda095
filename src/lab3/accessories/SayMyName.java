package lab3.accessories;

public class SayMyName extends Thread {
	private String name;
	private Mailbox mb;

	public SayMyName(String name, Mailbox mb) {
		this.name = name;
		this.mb = mb;
	}

	public void run() {
		while (true) {
			try {
				sleep((long) (Math.random() * 100));
				mb.writeName(name);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Mailbox mb = new Mailbox();

		SayMyName smn1 = new SayMyName("Erica", mb);
		SayMyName smn2 = new SayMyName("James", mb);
		SayMyName smn3 = new SayMyName("Jacob", mb);
		SayMyName smn4 = new SayMyName("Emil", mb);
		SayMyName smn5 = new SayMyName("Anton", mb);
		SayMyName smn6 = new SayMyName("Kevin", mb);
		SayMyName smn7 = new SayMyName("Markus", mb);
		SayMyName smn8 = new SayMyName("Michal", mb);
		SayMyName smn9 = new SayMyName("Lilian", mb);
		SayMyName smn10 = new SayMyName("Ellinor", mb);
		smn1.start();
		smn2.start();
		smn3.start();
		smn4.start();
		smn5.start();
		smn6.start();
		smn7.start();
		smn8.start();
		smn9.start();
		smn10.start();

		while (true) {
			try {
				sleep((long) (Math.random() * 100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(mb.takeName());
		}
	}

}
