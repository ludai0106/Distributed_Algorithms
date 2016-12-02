
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Test {

	static final int networkSize = 2;
	static int rPort = 1099;

	public static void main(String args[]) throws RemoteException, AlreadyBoundException, NotBoundException {
		if (args == null || args.length == 0) {
			LocateRegistry.createRegistry(rPort);
			for (int i = 1; i <= networkSize; i++) {
				int processId = 1000 + i;
				Node node;
				if (i % 2 != 0)
					node = new Node(networkSize, rPort, processId, 1);
				else
					node = new Node(networkSize, rPort, processId, 0);
				node.notifyOthers();
				System.out.println(node.getOriginProcessId() + ":\tWaiting for the incoming messages...");
			}
		} else {
			LocateRegistry.createRegistry(rPort);
			Node node;
			int Id = Integer.parseInt(args[0]);
			int status = Integer.parseInt(args[1]);
			int processId = 1000 + Id;
			node = new Node(networkSize, rPort, processId, 1);
			node.notifyOthers();
			System.out.println(node.getOriginProcessId() + ":\tWaiting for the incoming messages...");
		}
	}
}
