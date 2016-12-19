
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
	// the message that send to you.
	ArrayList<Message> messageList;
	//
	ArrayList<Message> messageCountList;
	// For traitors is true
	private boolean traitor;

	// If true, traitor will send random message instead of message with its own
	// value
	boolean traitorRandomMessage;
	// If true, traitor will not send messages at all,
	// If false, traitor will have a 50% chance of sending a message
	boolean traitorDoNotSendMessage;

	// // useless
	// boolean receiveMessage = true;
	// // useless
	// boolean finished = false;

	// delay value set by user
	private int delay;

	private Clock clock;

	// Default constructor
	public Node(int nodeId, int fNumber, int value, boolean traitor, int size, int port, boolean traitorRandomMessage,
			boolean traitorDoNotSendMessage, int delay, int index) throws RemoteException, AlreadyBoundException {
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
		this.traitorRandomMessage = traitorRandomMessage;
		this.traitorDoNotSendMessage = traitorDoNotSendMessage;
		this.delay = delay;
		
		this.traitor = traitor;

		this.links = new ArrayList<>();
		this.messageList = new ArrayList<>();

		// Initialize the clock vector
		this.clock = new Clock(size, index);

	}

	// Create a random delay based on the delay value given.
	public void randomDelay() {
		try {
			Thread.sleep(randomNumber(0, delay));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// RegisterNode only starts when the registry.list.length() are the same.
	// So all threads starts at almost the same time
	public void registerNode() throws AccessException, RemoteException {
		Byzantine byzan;
		Thread thread;
		if (registry.list().length == this.size) {
			byzan = new Byzantine(this);
			thread = new Thread(byzan);
			thread.start();
		}
	}

	// notify the rest about you
	public void notifyOthers() throws AccessException, RemoteException, NotBoundException {
		randomDelay();
		String[] nodes;
		nodes = this.registry.list();
		for (String nodeName : nodes) {
			INode remoteNode = getRemoteNode(nodeName);
			remoteNode.registerNode();
			System.out.println(this.getNodeId() + ":\tNotified node: " + nodeName);
		}

	}

	// get the remote Node based on the nodeId.
	public INode getRemoteNode(String nodeStringId) throws AccessException, RemoteException, NotBoundException {
		INode remoteNode = (INode) this.registry.lookup(nodeStringId);
		return remoteNode;
	}

	// BroadCast one Message to all neighbors
	public void broadCast(Message m) throws AccessException, RemoteException, NotBoundException {
		// If we have enough Nodes in our Links
		if (this.getLinks().size() >= size - 1) {
			for (String node : this.getLinks()) {
				Random randomGenerator = new Random();
				randomDelay();
				// if not a traitor
				if (!this.isTraitor()) {
					getRemoteNode(node).receiveMessage(m);
				} else if (!traitorDoNotSendMessage) {// if a traitor then may
														// not send message,
														// here we
					// use a 50:50 chance
					boolean randomBool = randomGenerator.nextBoolean();
					if (randomBool == true && !traitorRandomMessage) {
						// m sending message of it is given
						getRemoteNode(node).receiveMessage(m);
					} else if (randomBool == true && traitorRandomMessage) {
						// random message
						m.setW(randomNumber(0, 100));
						getRemoteNode(node).receiveMessage(m);

					}
				}

			}
		} else {
			System.out.println("Error: Not engouht Nodes in the Links");
		}
	}

	// Create a random number between [min, max]
	public static int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt(max + 1 - min) + min;
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

	// TODO:
	public boolean waitUntilSameRound() throws AccessException, RemoteException, NotBoundException {
		for (String node : links) {
			getRemoteNode(node);
		}
		return true;
	}

	// public void setReceiveMessageTrue() {
	// this.receiveMessage = true;
	// }
	//
	// public void setReceiveMessageFalse() {
	// this.receiveMessage = false;
	// }

	// Return the number of the larger value
	public synchronized int countMaxMessage(char type, int round) {
		return Math.max(countMessage(type, round, 0), countMessage(type, round, 1));
	}

	// Return the larger value of the two, we don't consider the other value,
	// since n>5f, the majority are correct process
	public synchronized int getMaxMessageValue(char type, int round) {
		return (countMessage(type, round, 0) > countMessage(type, round, 1)) ? 0 : 1;
	}

	// Count the message with a certain round and certain type.
	public synchronized int countMessage(char type, int round) {
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
	public synchronized int countMessage(char type, int round, int value) {
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

	// Print out the decided value in certain round
	public void decideAnounce() {
		randomDelay();
		System.out.format("node %d decided on %d in round %d\n", nodeId, value, round);
	}

	// Add one message into the message List
	public synchronized void receiveMessage(Message m) {
		// if (this.receiveMessage)
		randomDelay();
		this.messageList.add(m);
		// System.out.println(nodeId + "receive Message\t round=" + m.getRound()
		// + "\t type =\t" + m.getType()
		// + "\t value=" + m.getW());
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

	public void increaseRound() throws RemoteException {
		this.round = this.round + 1;
		System.out.println(getNodeId() + ":Round increase, with value"+ getValue());
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
