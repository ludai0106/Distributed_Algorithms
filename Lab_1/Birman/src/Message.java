import java.io.Serializable;

public class Message implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2366875620178315816L;
	private String sender;
	private String receiver;
	private String message;
	private Clock clock;
	
	public Message(String sender, String receiver, String message, Clock vectorClock) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.clock = vectorClock;
	}
	
	public Message(String sender, String message, Clock vectorClock) {
		this.sender = sender;
		this.message = message;
		this.clock = vectorClock;
	}
	
	public String toStringReceive() {
		String s = receiver + " <-- " + sender + ": " + message;
		return s;
	}
	
	public String toStringBroadcast() {
		String s= "Broadcast: " + sender + " broadcast " + message + ", " +  clock.toString();
		return s;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Clock getClock() {
		return clock;
	}

	public void setClock(Clock vectorClock) {
		this.clock = vectorClock;
	}

	
	
	
	

}
