import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IProcess extends Remote{
	 public void registerProcess(int length) throws RemoteException;
	 public void receiveMessage(Message m) throws RemoteException;
	 public Clock getClock() throws RemoteException;
	 public void setClock(Clock clockNew) throws RemoteException;
	 public void WriteSend(String m) throws RemoteException;
	 
	 
}