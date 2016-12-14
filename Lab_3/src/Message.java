import java.io.Serializable;

public class Message implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4731898095466253355L;
	private int senderId;
	private int receiverId;
	private int senderLevel;
	private int receiverLevel;
	// If true, then means this message is a kill request from child node,
	// Please kill yourself, poor father node.
	private boolean killRequest;
	// If true, that means this is an OK response either for capture request
	// message,
	// or for kill request, true means, your request has been done.
	private boolean okResponse;

	public Message(boolean killRequest, boolean okResponse, int sender, int receiver, int levelSender,
			int levelReceiver) {
		this.killRequest = killRequest;
		this.okResponse = okResponse;
		this.senderId = sender;
		this.receiverId = receiver;
		this.senderLevel = levelSender;
		this.receiverLevel = levelReceiver;
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

	public boolean getOkResponse() {
		return this.okResponse;
	}

	public void setOkResponse(boolean ok) {
		this.okResponse = ok;
	}

	public boolean getKillRequest() {
		return this.killRequest;
	}

	public void setKillRequest(boolean kill) {
		this.killRequest = kill;
	}

}
