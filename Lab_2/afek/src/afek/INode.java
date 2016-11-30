package afek;

import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface INode extends Remote{
	 public void registerNode() throws AccessException, RemoteException;

	public void receiveAck(Message message);

	public int getPort();
}
