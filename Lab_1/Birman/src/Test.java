import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

public class Test{
	
	static final int rPort = 1099;
	static final int processNum = 5;
	
	public static void main(String args[]) throws RemoteException {
		if(args.length>0){     
			
			int processId = Integer.parseInt(args[0]) + 1000;
			int broadcastRounds = randomNumber(0,3);
			Process node = new Process(rPort, processId, processNum, broadcastRounds);
			node.notifyProcess();
			System.out.println("ready...");
		}
		else {
			LocateRegistry.createRegistry(rPort);
			for(int i=0;i<processNum;i++){
				int processId = 1000 + i;
				int broadcastRounds = randomNumber(0,3);
				Process process = new Process(rPort, processId, processNum, broadcastRounds);
				process.notifyProcess();
			}

		}
    }

	private static int randomNumber(int min, int max){
		Random r = new Random();
		return (r.nextInt(max-min)+1) + min;
	}

}
