package lab3;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		try {
			Server s = new Server(2640);
			s.run();
		} catch (IOException e) {
			System.out.println("The port is probably busy");
			e.printStackTrace();
		}

	}
}
