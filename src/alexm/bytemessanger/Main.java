package alexm.bytemessanger;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello world");
		Server server = new Server(8080);
		new Thread(server).start();
		Utils utils = new Utils();
	}

}
