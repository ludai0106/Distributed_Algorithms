import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IProcess extends Remote{

	 void registerProcess(int length) throws Exception;
	 void receiveMessage(Message m) throws Exception;
	 Clock getClock() throws Exception;
	 void setClock(Clock clockNew) throws Exception;
}
