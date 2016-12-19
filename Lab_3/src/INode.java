import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

//Lab3
//Interface
public interface INode extends Remote {

	public void registerNode() throws AccessException, RemoteException, NotBoundException;

	//
	public void receiveMessage(Message message) throws AccessException, RemoteException, NotBoundException;

	public void receiveClock(Clock c) throws RemoteException;

	public Clock getClock() throws RemoteException;
	
	public boolean getStart() throws RemoteException;
	
	public void setStart(boolean start) throws RemoteException ;
}
