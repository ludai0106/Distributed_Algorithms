import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

public class Test {

	static boolean easyMode = false;

	static int networkSize = 15;
	static int rPort = 1099;
	static int f = 3;
	static boolean traitorRandomMessage = true;
	static boolean traitorDoNotSendMessage = false;
	static int delay = 0;

	public static void main(String args[]) throws RemoteException, AlreadyBoundException, NotBoundException {
		// Configurations for easyMode
		if (easyMode) {
			networkSize = 6;
			f = 1;
			traitorRandomMessage = false;
			traitorDoNotSendMessage = false;
			delay = 0;
		}

		if (args == null || args.length == 0) {
			LocateRegistry.createRegistry(rPort);
			for (int i = 1; i <= networkSize; i++) {
				int nodeId = 1000 + i;
				Node node;
				if (i <= networkSize - f)
					node = new Node(nodeId, f, randomNumber(0, 1), true, networkSize, rPort, traitorRandomMessage,
							traitorDoNotSendMessage, delay, i);
				else
					node = new Node(nodeId, f, 0, false, networkSize, rPort, traitorRandomMessage,
							traitorDoNotSendMessage, delay, i);
				node.notifyOthers();
				System.out.println(node.getNodeId() + ":\tWaiting for the incoming messages...");
			}

		}
	}

	// Create a random number between min and max. [min,max]
	public static int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt(max + 1 - min) + min;
	}
}
