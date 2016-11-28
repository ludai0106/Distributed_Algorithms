import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class Process extends UnicastRemoteObject implements ProcessInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6044439435459038975L;
	private int processId;
    private int processNum;
    private Clock clock;
    private Registry registry;
    private int remotes;
    private int index;
	ArrayList<Message> buffer = new ArrayList<Message>();
    public Process( int port, int processId, int processNum) throws RemoteException, AlreadyBoundException{
        this.processId = processId;
        this.processNum = processNum;
        this.clock = new Clock(processNum);
        this.registry = LocateRegistry.getRegistry(port);
        this.registry.bind(Integer.toString(processId), this);
        this.index = this.registry.list().length-1;
    }

    public int getProcessId(){
        return processId;
    }

    public Clock getClock(){
        return clock;
    }

    public void broadcastMessage(Message message)  throws RemoteException, NotBoundException, InterruptedException{
    	System.out.println(message.toStringBroadcast());
    	String[] remoteIds = this.registry.list();
		String currentId = Integer.toString(processId);
    	for(String remoteId: remoteIds){
			ProcessInterface remoteProcess = (ProcessInterface) this.registry.lookup(remoteId);
			if(!currentId.equals(remoteId)){
				Message m2 = new Message(currentId, remoteId, clock, message.getMessage());
				Thread.sleep(randomNumber(5,350));
				remoteProcess.receiveMessage(m2);
			}	
		}
    }


	public void receiveMessage(Message message) throws RemoteException {
		if(message.getClock().sendCondition(this.clock)){
			System.out.println(message.toStringReceive() +"   "+ processId + " : " + clock);
			//this.setVectorClock(m.getVectorClock());
			this.clock.update(message.getClock());
		}
		else{
			System.out.println(message.toStringReceive() +", own: " + clock + " buffer");
			synchronized(this) {
				for(Message messageBuffer:buffer){
					System.out.println(messageBuffer.getClock() + "sendconditon" + this.getClock());
					if(messageBuffer.getClock().sendCondition(this.clock)){
						System.out.println(messageBuffer.toStringReceive() +", own: " + clock + " retry");
						this.clock.update(messageBuffer.getClock());
						buffer.remove(messageBuffer);
					}
				}
				buffer.add(message);
			}
		}		
	}

    
    public void notifyRemotes(){
    	String[] processes;
		try {
			processes = this.getRegistry().list();
			for(String processName: processes){
				ProcessInterface remoteProcess = (ProcessInterface) this.getRegistry().lookup(processName);
				remoteProcess.registerProcess();
				remotes++;
				System.out.println("Notified node: " + processName + "  JoinedRemotes: " + remotes);
	
			}
		} catch (Exception e) {
			System.out.println("Exception in notifyOthers" + e);
		}
    }
    
	public void registerProcess(){
		System.out.println("In register Process " + processId + "  JoinedRemotes: " + remotes);
		remotes++;
		if(remotes-1 == processNum){

			final Process currentProcess = this;
			final double start = System.currentTimeMillis(); 
		
			Birman birman = new Birman(currentProcess,start,index);
			Thread thread = new Thread(birman);
			thread.start();
		}
			
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public int randomNumber(int low, int high){
		Random r = new Random();
		return r.nextInt(high-low) + low;
	}

}
