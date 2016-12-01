

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface INode extends Remote{
	 public void registerNode() throws AccessException, RemoteException;

	public void receiveAck(Message message) throws RemoteException;

	public int getPort() throws RemoteException;

	public int getLevel() throws RemoteException;
	
	public int getProcessId() throws RemoteException;
	
	public void setProcessId(int id) throws RemoteException;
	
	public int getOriginProcessId() throws RemoteException;

	public void receiveMessage(Message m) throws AccessException, RemoteException, NotBoundException;
}
