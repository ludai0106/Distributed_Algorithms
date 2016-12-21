import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class Test {

	static boolean easyMode = false;
	
	static int networkSize = 10;
	static int rPort = 1099;
	static int f = 2;
	static boolean traitorRandomMessage = true;
	static boolean traitorDoNotSendMessage = false;
	static int delay = 0;
	static boolean synchronous = false;
    static ArrayList<String> remoteIps = new ArrayList<>();
    
   

	public static void main(String args[]) throws RemoteException, AlreadyBoundException, NotBoundException, UnknownHostException, InterruptedException {
		// Configurations for easyMode
		if (easyMode) {
			networkSize = 6;
			f = 1;
			traitorRandomMessage = false;
			traitorDoNotSendMessage = false;
			delay = 0;
		}

		if (args == null || args.length == 0) {
		   
		   remoteIps.add("145.94.211.226");
		   remoteIps.add("145.94.183.254");
		  
		    //System.setProperty("java.rmi.server.hostname","145.94.211.226");
		   //System.setProperty("java.rmi.server.hostname","145.94.183.254");

		    
		    //Thread.sleep(5000);
		    
			//LocateRegistry.createRegistry(rPort);
		    	
			for (int i = 1; i <= networkSize/2; i++) {
				int nodeId = 1000 + i;
				Node node;
				if (i <= networkSize/2 - f/2) {
					node = new Node(nodeId, f, randomNumber(0, 1), true, networkSize, rPort, traitorRandomMessage,
							traitorDoNotSendMessage, delay, i, synchronous, remoteIps);
				} else {
					node = new Node(nodeId, f, 0, false, networkSize, rPort, traitorRandomMessage,
							traitorDoNotSendMessage, delay, i, synchronous, remoteIps);
				}
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
