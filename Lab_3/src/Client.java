import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	static int rPort = 1099;
	private Client(){}
	public static void main(String[] args) throws RemoteException, InterruptedException{
		LocateRegistry.createRegistry(rPort);
		System.setProperty("java.rmi.server.hostname","145.94.183.254");
		System.out.println("Lam gao sou ready!");
		while(true){
			Thread.sleep(1000);
		}
	}
//		String remote = "145.94.211.226";
//		try{
//			Registry registry = LocateRegistry.getRegistry(remote);
//			Hello stub=(Hello) registry.lookup("Hello");
//			String response = stub.sayHello();
//			System.out.println("Lam gao sou : " + response);
//			registry.unbind("Hello");
//			System.setProperty("java.rmi.server.hostname","145.94.224.29");
//				
//		}
//		catch(Exception e){
//			System.err.println("Lam gao sou exception: " + e.toString());
//			e.printStackTrace();
//		}
//	}

}
