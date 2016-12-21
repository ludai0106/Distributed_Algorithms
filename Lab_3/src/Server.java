import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
        
public class Server{
	static int rPort = 1099;
        
    public Server() {}

    public String sayHello() {
        return "Hello, world!";
    }
        
    public static void main(String args[]) throws RemoteException, InterruptedException {
    	LocateRegistry.createRegistry(rPort);
    	System.setProperty("java.rmi.server.hostname","145.94.211.226");
    	System.out.println("Luge ready!");
    	while(true){Thread.sleep(1000);}
    }
}