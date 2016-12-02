import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.rmi.registry.LocateRegistry;

public class Test{
	
	static final int rPort = 1099;
	static final int processNum = 5;
	
	public static void main(String args[]) throws Exception {
		if(args.length>0){     
			int registryPort = Integer.parseInt(args[0]);
			int processId = Integer.parseInt(args[1]);
			int broadcastRounds = Integer.parseInt(args[2]);
			Process node = new Process(registryPort, processId, processNum, broadcastRounds);
			node.notifyProcess();
			System.out.println("ready...");
		}
		else {
			LocateRegistry.createRegistry(rPort);
			for(int i=0;i<processNum;i++){
				int processId = 1000 + i;
				int broadcastRounds = 3;
				Process process = new Process(rPort, processId, processNum, broadcastRounds);
				process.notifyProcess();
			}

		}
    }

}
