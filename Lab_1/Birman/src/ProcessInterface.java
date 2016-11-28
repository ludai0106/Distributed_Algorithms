import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessInterface extends Remote{

    public void broadcastMessage(Message message) throws RemoteException, NotBoundException, InterruptedException;
    
    public void receiveMessage(Message message) throws RemoteException;
	
	public void registerProcess() throws RemoteException;

}
