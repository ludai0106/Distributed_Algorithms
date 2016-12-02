import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Test {
	static final int processNum = 3;
	static int rPort = 1099;
	public static void main(String args[]) throws RemoteException, AlreadyBoundException {	
		
//		int registryPort = Integer.parseInt(args[0]);
//		int nodePort = Integer.parseInt(args[1]);
//		
//		Process process=new Process(registryPort, nodePort,processNum);
//		process.notifyRemotes();
	
		LocateRegistry.createRegistry(rPort);
		
		for(int i=0;i<processNum;i++){
			int pPort = 1000 + i;
			
			Process process=new Process(rPort, pPort,processNum);
			process.notifyRemotes();
		}
	}
}
