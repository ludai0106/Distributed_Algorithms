
//Lab3
import java.net.InetAddress;
import java.net.UnknownHostException;
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
	private ArrayList<Registry> registries;
	// nodes you haven't send a message to
	private ArrayList<String> links;
	// the message that send to you.
	ArrayList<Message> messageList;
	//
	ArrayList<Clock> clockList;
	// For traitors is true
	private boolean traitor;

	// If true, traitor will send random message instead of message with its own
	// value
	boolean traitorRandomMessage;
	// If true, traitor will not send messages at all,
	// If false, traitor will have a 50% chance of sending a message
	boolean traitorDoNotSendMessage;
	// True, we may need to use Clock
	boolean synchronous;

	// // useless
	// boolean receiveMessage = true;
	// // useless
	// boolean finished = false;

	// delay value set by user
	private int delay;

	public boolean start = false;

	private Clock clock;
	
	private ArrayList<String> remoteIps;
	
	private int IpIndex;

	// Default constructor
	public Node(int nodeId, int fNumber, int value, boolean traitor, int size, int port, boolean traitorRandomMessage,
			boolean traitorDoNotSendMessage, int delay, int index, boolean synchronous, ArrayList<String> remoteIps)
			throws RemoteException, AlreadyBoundException, UnknownHostException {
		this.port = port;
		// decided =false at first
		this.decided = false;
		// round equals 1 at first
		this.round = 1;
		this.nodeId = nodeId;
		this.remoteIps = remoteIps;
		this.registries = new ArrayList<>();
		for(String Ip:remoteIps){
			this.registries.add(LocateRegistry.getRegistry(Ip,port));
		}
		System.out.println(InetAddress.getLocalHost().toString());
		String ipaddress = InetAddress.getLocalHost().toString().split("/")[1];
		System.out.println(ipaddress);
		this.IpIndex = this.remoteIps.indexOf(ipaddress);
		System.out.println(IpIndex);

		this.registries.get(IpIndex).bind(Integer.toString(nodeId), this);
		
		this.size = size;
		this.fNumber = fNumber;
		this.value = value;
		this.traitorRandomMessage = traitorRandomMessage;
		this.traitorDoNotSendMessage = traitorDoNotSendMessage;
		this.delay = delay;
		this.traitor = traitor;
		this.links = new ArrayList<>();
		this.messageList = new ArrayList<>();
		this.clockList = new ArrayList<>();
		// Initialize the clock vector
		this.clock = new Clock(size, index - 1);
		this.synchronous = synchronous;

	}

	// Create a random delay based on the delay value given.
	public void randomDelay() {
		try {
			Thread.sleep(randomNumber(0, delay));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// RegisterNode only starts when the registry.list.length() are the same.
	// So all threads starts at almost the same time
	public void registerNode() throws AccessException, RemoteException, NotBoundException {
		Byzantine byzan;
		Thread thread;
		// Change: if two machines
		if (getNodesNum() == this.size) {

			if (this.getClock().getIndex() + 1 == this.getSize())
				System.out.println(this.getNodeId() + ": I start" + getNodesNum());

			byzan = new Byzantine(this);
			thread = new Thread(byzan);
			thread.start();
		}
	}

	// notify the rest about you
	public void notifyOthers() throws AccessException, RemoteException, NotBoundException {
		randomDelay();
		String[] nodes = getallNodes();
		

		for (String nodeName : nodes) {
			INode remoteNode = getRemoteNode(nodeName);
			remoteNode.registerNode();
			System.out.println(this.getNodeId() + ":\tNotified node: " + nodeName);

			// if (this.getClock().getIndex() == this.getSize())
			// System.out.println(this.getNodeId() + ": I register" +
			// registry.list().length);
		}
		
		

	}

	// get the remote Node based on the nodeId.
	public INode getRemoteNode(String nodeStringId) throws AccessException, RemoteException, NotBoundException {
		int remoteIp = nodeStringId.charAt(0)-'0';
		System.out.println("mchine id: " + remoteIp);
		System.out.println("node id: " + nodeStringId.substring(1));
		INode remoteNode = (INode) this.registries.get(remoteIp).lookup(nodeStringId.substring(1));

		return remoteNode;
	}

	// BroadCast one Message to all neighbors
	public void broadCast(Message m) throws AccessException, RemoteException, NotBoundException {
		// If we have enough Nodes in our Links
		if (getNodesNum() == size)
			System.out.println(getNodeId() + ": I broadcast");
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

		// Now we need to make sure we are slower than others
		boolean result = true;
		for (String node : links) {
			result = result && this.getClock().nextRoundCondition(getRemoteNode(node).getClock());
			if (!result)
				return false;
		}
		return result;

	}

	public void broadcastClock() throws AccessException, RemoteException, NotBoundException {
		int flag = 0;
		for (String node : links) {
			if (!node.substring(1).equals(Integer.toString(this.getNodeId()))) {
				getRemoteNode(node).receiveClock(this.clock);
				flag++;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println(this.getNodeId() + ": already sent" + flag);
	}

	public synchronized void receiveClock(Clock c) throws RemoteException {
		clockList.add(c);
		// update our index if we are smaller than theirs
		if (getClock().sameIndexSmaller(c)) {
			getClock().update(c);
		}
	}

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

	public boolean getStart() throws RemoteException {
		return this.start;
	}

	public void setStart(boolean start) throws RemoteException {
		this.start = start;
	}

	public ArrayList<String> getLinks() {
		return this.links;
	}

	public Clock getClock() throws RemoteException {
		return this.clock;
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

	public ArrayList<Registry> getRegistry() {
		return this.registries;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public void increaseRound() throws RemoteException {
		this.round = this.round + 1;
		this.clock.incrementIndex();
		System.out.println(getNodeId() + ":Round increase, with value" + getValue());
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
	
	public int getNodesNum() throws AccessException, RemoteException{
		int num = 0;
		for(int i=0; i<this.remoteIps.size();i++){
			num+=this.registries.get(i).list().length;
		}
		return num;
		
	}
	
	public String[] getallNodes() throws AccessException, RemoteException{
		String[] nodes = new String[getNodesNum()];
		int nodeIndex=0;
		for(int i=0;i<remoteIps.size();i++){
			for(String nodeName:this.registries.get(i).list()){
				nodes[nodeIndex] = Integer.toString(i)+nodeName;
				nodeIndex++;
			}
		}
		return nodes;
	}
}
