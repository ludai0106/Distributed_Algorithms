import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node extends UnicastRemoteObject implements INode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5075309184509243445L;

	// 0 represent Ordinary
	// 1 represent Candidate
	private int status;
	// If it is consider as candidate, then set it true.
	// private boolean candidate;
	boolean finished = false;

	// size of the complete network
	private int size;
	private int port;
	private int level;
	private int originProcessId;
	private int processId;
	private Registry registry;
	private ArrayList<Message> buffer;
	ArrayList<Message> ackList;
	private ArrayList<String> links; // nodes you haven't send a message to
	Map<Integer, Boolean> finishBroadCast;
	//Round,  finish broadcast?
	public Node(int size, int port, int processId, int status) throws RemoteException, AlreadyBoundException {
//		super();
		this.port = port;
		this.level = -1;
		this.processId = processId;
		this.originProcessId = processId;
		this.registry = LocateRegistry.getRegistry(port);
		this.registry.bind(Integer.toString(processId), this);
		this.status = status;
		this.size = size;
		this.buffer = new ArrayList<>();
		this.ackList = new ArrayList<>();
		this.links = new ArrayList<String>();
		this.finishBroadCast = new HashMap<>();
		for (int i = 0; i < size; i++) {
			finishBroadCast.put(i,false);
		}
	}

	public ArrayList<Message> getBuffer() {
		return this.buffer;
	}

	public ArrayList<String> getLinks() {
		return this.links;
	}

	public int getOriginProcessId() {
		return this.originProcessId;
	}

	public int getPort() throws RemoteException {
		return this.port;
	}

	public int setPort(int port) {
		return this.port = port;

	}

	public int getLevel() throws RemoteException {
		return this.level;
	}

	public int getStatus() {
		return this.status;
	}

	public int setStatus(int status) {
		return this.status = status;
	}

	public int getProcessId() {
		return this.processId;
	}

	public int setProcessId(int id) {
		return this.processId = id;
	}

	public Registry getRegistry() {
		return this.registry;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void increaseLevel() {
		this.level = this.level + 1;
	}

	public INode getRemoteNode(String nodeStringId) throws AccessException, RemoteException, NotBoundException {
		INode remoteNode = (INode) this.registry.lookup(nodeStringId);
		return remoteNode;
	}

	public void receiveMessage(Message message) throws AccessException, RemoteException, NotBoundException {
		System.out.println(message.getSender());
		String s = message.toStringReceive();
		System.out.println(s);
		synchronized (this) {
			buffer.add(message);
			if ((message.getLevelSender() >= message.getLevelReceiver() && message.getSender() >= message.getReceiver())
					|| message.getLevelSender() > message.getLevelReceiver()) {
				String sender = Integer.toString(message.getSender());
				INode nodeSender = this.getRemoteNode(sender);
				System.out.println("I send a ack to " + sender);
				nodeSender.receiveAck(new Message(true, message.getReceiver(), message.getSender(),
						message.getLevelReceiver(), message.getLevelSender()));
				this.setLevel(message.getLevelSender());
				this.setProcessId(message.getSender());
				System.out.println("I changed my level=" + message.getLevelReceiver() + " and id="
						+ message.getReceiver() + " to: " + this.getLevel() + " and " + this.getProcessId());

			} else {
				System.out.println("I'm bigger, so you " + message.getSender() + " don't get a ack");
			}
		}
	}

	public void registerNode() throws AccessException, RemoteException {
		Afek afek;
		Thread thread;
		if (registry.list().length == this.size) {
			afek = new Afek(this);
			thread = new Thread(afek);
			thread.start();
		}
	}

	public void broadcastMessage(Message message) {
		String s = message.toStringBroadcast();
		System.out.println(s);
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
			System.out.println(this.getOriginProcessId() + ":\tNotified node: " + nodeName);
		}

	}

}