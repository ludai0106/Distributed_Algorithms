
//This is our main function
//If you input arguments then it will create multiple processes
//IF not will create multiple threads
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Test {

	static final int networkSize = 4;
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
			// LocateRegistry.createRegistry(rPort);
			Node node;
			// First input Id
			int Id = Integer.parseInt(args[0]);
			// Second input is the status
			int status = Integer.parseInt(args[1]);
			int processId = 1000 + Id;
			node = new Node(networkSize, rPort, processId, status);
			node.notifyOthers();
			System.out.println(node.getOriginProcessId() + ":\tWaiting for the incoming messages...");
		}
	}
}
