import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Test {
	static final int networkSize = 15;
	static int rPort = 1099;
	static int f = 1;

	static boolean traitorRandomMessage = true;

	static boolean traitorDoNotSendMessage = false;

	static int delay = 100;

	public static void main(String args[]) throws RemoteException, AlreadyBoundException, NotBoundException {
		if (args == null || args.length == 0) {
			LocateRegistry.createRegistry(rPort);
			for (int i = 1; i <= networkSize; i++) {
				int nodeId = 1000 + i;
				Node node;
				if (i <= networkSize - f)
					node = new Node(nodeId, f, 1, networkSize, rPort, traitorRandomMessage, traitorDoNotSendMessage,
							delay);
				else
					node = new Node(nodeId, f, 0, networkSize, rPort, traitorRandomMessage, traitorDoNotSendMessage,
							delay);
				node.notifyOthers();
				System.out.println(node.getNodeId() + ":\tWaiting for the incoming messages...");
			}

		}
	}
}
