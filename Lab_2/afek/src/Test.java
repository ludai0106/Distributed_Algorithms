

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Test {

	static final int networkSize = 8;
	// static final int processNum = 4;
	static int rPort = 1099;

	public static void main(String args[]) throws RemoteException, AlreadyBoundException, NotBoundException {
		LocateRegistry.createRegistry(rPort);
		for (int i = 0; i < networkSize; i++) {
			int processId = 1000 + i;
			Node node = new Node(networkSize, rPort, processId, 1);
			node.notifyOthers();
			System.out.println("Waiting for the incoming messages...");
		}
	}
}
