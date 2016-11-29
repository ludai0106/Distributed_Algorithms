import java.io.Serializable;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1729196106619598407L;
	private String sender;
    private String receiver;
    private String message;
    private Clock clock;

    public Message(String sender, String receiver, Clock clock, String message){
        this.sender = sender;
        this.receiver = receiver;
        this.clock = clock;
        this.message = message;
    }
    
	public Message(String sender, String message, Clock clock) {
		this.sender = sender;
		this.message = message;
		this.clock = clock;
	}
	

    public String getSender(){
        return sender;
    }

    public String getReceiver(){
        return receiver;
    }

    public Clock getClock(){
        return clock;
    }
    
    public String getMessage(){
    	return message;
    }
    
    public String toStringBroadcast() {
		String s= "Process " + sender + " broadcast " + message + " " +  clock.toString();
		return s;
	}
    
    public String toStringReceive() {
		String s= "Process " + receiver + " receives from Process " + sender +" "+ message + " " +  clock.toString();
		return s;
	}

}