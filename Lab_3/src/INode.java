import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

//Lab3
//Interface
public interface INode extends Remote {
	
	 public void registerNode() throws AccessException, RemoteException;
//
	public void receiveMessage(Message message) throws  AccessException, RemoteException, NotBoundException;
//
//	public int getPort() throws RemoteException;
//
//	public int getLevel() throws RemoteException;
//	
//	public int getNodeId() throws RemoteException;
//	
//	public void setNodeId(int id) throws RemoteException;
//	
//	public int getOriginNodeId() throws RemoteException;
//
//	public void receiveMessage(Message m) throws AccessException, RemoteException, NotBoundException;
//
//	public int getStatus() throws RemoteException;

}
