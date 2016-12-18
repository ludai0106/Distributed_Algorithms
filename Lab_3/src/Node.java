
//Lab3
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class Node extends UnicastRemoteObject implements INode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7539504428746918352L;

	// 0 represent undecided
	// 1 represent Candidate
	// Get killed will make status change from `1` to `0`
	private boolean decided;
	// the size of the network
	private final int size;
	// Number of traitors
	private final int fNumber;
	// Value given initially, for traitor it was 0, others 1
	private int value;
	// port number
	private int port;
	// level here represent how many nodes he captured
	private int round;
	// the nodeId
	private int nodeId;
	// Only for reference
	private Registry registry;
	// nodes you haven't send a message to
	private ArrayList<String> links;
	// the OK message that send to you.
	ArrayList<Message> messageList;
	// Fort traitors is true
	private boolean traitor;

	boolean finished = false;

	public Node(int nodeId, int fNumber, int value, int size, int port) throws RemoteException, AlreadyBoundException {
		this.port = port;
		// decided =false at first
		this.decided = false;
		// round equals 1 at first
		this.round = 1;
		this.nodeId = nodeId;
		this.registry = LocateRegistry.getRegistry(port);
		this.registry.bind(Integer.toString(nodeId), this);
		this.size = size;
		this.fNumber = fNumber;
		this.value = value;

		if (value == 0)
			this.traitor = false;
		else
			this.traitor = true;

		this.links = new ArrayList<>();
		this.messageList = new ArrayList<>();
	}

	// RegisterNode only starts when the registry.list.length() are the same.
	// So all threads starts at almost the same time
	public void registerNode() throws AccessException, RemoteException {
		Byzantine afek;
		Thread thread;
		if (registry.list().length == this.size) {
			afek = new Byzantine(this);
			thread = new Thread(afek);
			thread.start();
		}
	}

	// notify the rest about you
	public void notifyOthers() throws AccessException, RemoteException, NotBoundException {
		String[] nodes;
		nodes = this.registry.list();
		for (String nodeName : nodes) {
			INode remoteNode = getRemoteNode(nodeName);
			remoteNode.registerNode();
			System.out.println(this.getNodeId() + ":\tNotified node: " + nodeName);
		}

	}

	public INode getRemoteNode(String nodeStringId) throws AccessException, RemoteException, NotBoundException {
		INode remoteNode = (INode) this.registry.lookup(nodeStringId);
		return remoteNode;
	}

	// BroadCast one Message to all neighbors
	public void broadCast(Message m) throws AccessException, RemoteException, NotBoundException {
		// If we have enough Nodes in our Links
		if (this.getLinks().size() == size - 1) {
			for (String node : this.getLinks()) {
				Random randomGenerator = new Random();
				// if not a traitor
				if (!this.isTraitor()) {
					getRemoteNode(node).receiveMessage(m);
				} else {// if a traitor then may not send message, here we
						// use a 50:50 chance
					boolean randomBool = randomGenerator.nextBoolean();
					if (randomBool == true) {
						getRemoteNode(node).receiveMessage(m);
					}
				}

			}
		} else {
			System.out.println("Error: Not engouht Nodes in the Links");
		}
	}

	// Make Node sleep until we got the number of message we want
	public void await(int round, char type) throws RemoteException, InterruptedException {
		while (true) {
			// await : n-f message
			if (countMessage(type, round) >= this.getSize() - this.getfNumber()) {
				break;
			} else {
				Thread.sleep(200);
			}
		}

	}

	public int countMaxMessage(char type, int round) {
		return Math.max(countMessage(type, round, 0), countMessage(type, round, 1));
	}

	public int getMaxMessageValue(char type, int round) {
		return (countMessage(type, round, 0) > countMessage(type, round, 1)) ? 0 : 1;
	}

	// Count the message with a certain round and certain type.
	public int countMessage(char type, int round) {
		int count = 0;
		if (messageList.size() != 0) {
			for (Message m : messageList) {
				if (m.getType() == type && m.getRound() == round) {
					count++;
				}
			}
		}
		return count;
	}

	// Count the message with a certain round, certain type and certain value.
	public int countMessage(char type, int round, int value) {
		int count = 0;
		if (messageList.size() != 0) {
			for (Message m : messageList) {
				if (m.getType() == type && m.getRound() == round && m.getW() == value) {
					count++;
				}
			}
		}
		return count;
	}

	public void receiveMessage(Message m) {
		this.messageList.add(m);
	}

	public ArrayList<String> getLinks() {
		return this.links;
	}

	public int getSize() {
		return this.size;
	}

	public int getPort() throws RemoteException {
		return this.port;
	}

	public int setPort(int port) {
		return this.port = port;

	}

	public int getRound() throws RemoteException {
		return this.round;
	}

	public boolean getDecided() throws RemoteException {
		return this.decided;
	}

	public void setDecided(boolean decided) {
		this.decided = decided;
	}

	public void nodeDecided() {
		setDecided(true);
	}

	public int getNodeId() throws RemoteException {
		return this.nodeId;
	}

	public void setNodeId(int id) {
		this.nodeId = id;
	}

	public Registry getRegistry() {
		return this.registry;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public void increaseRound() {
		this.round = this.round + 1;
	}

	public int getfNumber() {
		return this.fNumber;
	}


	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;

	}

	public boolean isTraitor() {
		return !(this.traitor);

	}
}
