
import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2710211275054319730L;

	private int senderId;
	private int receiverId;
	private int senderLevel;
	private int receiverLevel;
	private boolean isAck;

	public Message(boolean isAck, int sender, int receiver, int levelSender, int levelReceiver) {
		this.isAck = isAck;
		this.senderId = sender;
		this.receiverId = receiver;
		this.senderLevel = levelSender;
		this.receiverLevel = levelReceiver;
	}

	public boolean isAck() {
		return this.isAck;
	}

	public void setIsAck(boolean isAck) {
		this.isAck = isAck;
	}

	public int getSender() {
		return senderId;
	}

	public void setSender(int sender) {
		this.senderId = sender;
	}

	public int getReceiver() {
		return receiverId;
	}

	public int getSenderLevel() {
		return senderLevel;
	}

	public void setSenderLevel(int levelSender) {
		this.senderLevel = levelSender;
	}

	public int getReceiverLevel() {
		return receiverLevel;
	}

	public void setReceiverLevel(int levelReceiver) {
		this.receiverLevel = levelReceiver;
	}

	public void setReceiver(int receiver) {
		this.receiverId = receiver;
	}

	public String toStringReceive() {
		return receiverId + " receives message from " + senderId;
	}

	public String toStringSend() {
		return senderId + " sends a message to " + receiverId;
	}

	public String toStringAck() {
		return receiverId + " receives a ack from " + senderId;

	}

}
