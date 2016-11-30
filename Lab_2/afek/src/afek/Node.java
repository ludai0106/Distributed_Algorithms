package afek;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Node extends UnicastRemoteObject implements INode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5075309184509243445L;

	// 0 represent Ordinary
	// 1 represent Candidate
	private int status;
	// If it is consider as candidate, then set it true.
	private boolean candidate;
	// size of the complete network
	private int size;
	private int port;
	private int level;
	private int nodeId;
	private int ownerId;
	private int ownerLevel;
	private int processId;
	private Registry registry;
	private ArrayList<Message> buffer;
	private ArrayList<Message> ackList;

	public Node(int port, int nodeId, int processId, boolean candidate, int size)
			throws RemoteException, AlreadyBoundException {
		super();
		this.port = port;
		this.nodeId = nodeId;
		this.ownerId = nodeId;
		this.processId = processId;
		this.registry = LocateRegistry.getRegistry(port);
		this.registry.bind(Integer.toString(processId), this);
		this.candidate = candidate;
		this.size = size;
		this.buffer = new ArrayList<>();
		this.ackList = new ArrayList<>();
	}

	public int getPort() {
		return this.port;
	}

	public int setPort(int port) {
		return this.port = port;

	}

	public int getLevel() {
		return this.level;
	}

	public int getNodeId() {
		return this.nodeId;
	}

	public int getStatus() {
		return this.status;
	}

	public int getOwnerId() {
		return this.ownerId;
	}

	public int getOwnerLevel() {
		return this.ownerLevel;
	}

	public int getProcessId() {
		return this.processId;
	}

	public boolean isCandidate() {
		return this.candidate;
	}

	public Registry getRegistry() {
		return this.registry;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public void setOwnerLevel(int ownerLevel) {
		this.ownerLevel = ownerLevel;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void increaseLevel() {
		this.level = this.level + 1;
	}

	public void increaseOwnerLevel() {
		this.ownerLevel = this.ownerLevel + 1;
	}

	private INode getRemoteNode(String nodeStringId) throws AccessException, RemoteException, NotBoundException {
		INode remoteNode = (INode) this.registry.lookup(nodeStringId);
		return remoteNode;
	}

	public void receiveMessage(Message message) throws AccessException, RemoteException, NotBoundException {
		System.out.println(message.getSender());
		String s = message.toStringReceive();
		System.out.println(s);
		synchronized (this) {
			buffer.add(message);
			if ((message.getLevelSender() >= message.getLevelReceiver() & message.getSender() >= message.getReceiver())
					|| message.getLevelSender() > message.getLevelReceiver()) {
				String sender = Integer.toString(message.getSender());
				INode nodeSender = this.getRemoteNode(sender);
				System.out.println("I send a ack to " + sender);
				nodeSender.receiveAck(new Message(true, message.getReceiver(), message.getSender(),
						message.getLevelReceiver(), message.getLevelSender()));
				this.setLevel(message.getLevelSender());
				this.setOwnerId(message.getSender());
				System.out.println("I changed my level and id to: " + this.getLevel() + " and " + this.getOwnerId());

			} else {
				System.out.println("I'm bigger, so you " + message.getSender() + " don't get a ack");
			}
		}
	}

	public void registerNode() throws AccessException, RemoteException {
		if (registry.list().length == this.size)
			;
	}

	public void broadcastMessage(Message message) {

	}

	public void receiveAck(Message message) {
		if (message.isAck()) {
			ackList.add(message);
			System.out.println(message.toStringAck());
		}
	}

	// notify the rest about you
	public void notifyOthers() throws AccessException, RemoteException, NotBoundException {
		String[] nodes;
		nodes = this.registry.list();
		for (String nodeName : nodes) {
			INode remoteNode = getRemoteNode(nodeName);
			remoteNode.registerNode();
			System.out.println("Notified node: " + nodeName);
		}

	}

}
