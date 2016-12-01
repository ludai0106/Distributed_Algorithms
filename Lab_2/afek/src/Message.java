

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2710211275054319730L;

	private int senderId;
	private int receiverId;
	private int levelSender;
	private int levelReceiver;
	private boolean isAck;

	public Message(boolean isAck, int sender, int receiver, int levelSender, int levelReceiver) {
		this.isAck = isAck;
		// this.agreeAck = agreeAck;
		this.senderId = sender;
		this.receiverId = receiver;
		this.levelSender = levelSender;
		this.levelReceiver = levelReceiver;
	}

	public boolean isAck() {
		return this.isAck;
	}

	public void setIsAck(boolean isAck) {
		this.isAck = isAck;
	}

	public String toStringReceive() {
		String s = receiverId + " receives message from " + senderId;
		return s;
	}

	public String toStringBroadcast() {
		String s = senderId + " sends a message to " + receiverId;
		return s;
	}

	public String toStringAck() {
		String s = receiverId + " receives a ack from " + senderId;
		// String s= receiver + " sends a ack to " + sender;
		return s;
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

	public int getLevelSender() {
		return levelSender;
	}

	public void setLevelSender(int levelSender) {
		this.levelSender = levelSender;
	}

	public int getLevelReceiver() {
		return levelReceiver;
	}

	public void setLevelReceiver(int levelReceiver) {
		this.levelReceiver = levelReceiver;
	}

	public void setReceiver(int receiver) {
		this.receiverId = receiver;
	}

}
