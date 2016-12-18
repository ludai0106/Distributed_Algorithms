
//Lab3
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
	private static final long serialVersionUID = 7539504428746918352L;

	// 0 represent undecided
	// 1 represent Candidate
	// Get killed will make status change from `1` to `0`
	private boolean decided;
	// the size of the network
	private int size;
	private int port;
	// level here represent how many nodes he captured
	private int round;
	private int nodeId;
	// Only for reference
	private Registry registry;
	// nodes you haven't send a message to
	private ArrayList<String> links;
	// the OK message that send to you.
	ArrayList<Message> messageList;

	//
	boolean finished = false;

	public Node(int size, int port, int nodeId) throws RemoteException, AlreadyBoundException {
		// super();
		this.port = port;
		this.decided = false;

		this.nodeId = nodeId;
		
		this.registry = LocateRegistry.getRegistry(port);
		this.registry.bind(Integer.toString(nodeId), this);

		this.size = size;
		this.links = new ArrayList<String>();
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

	public int getLevel() throws RemoteException {
		return this.round;
	}

	public boolean getDecided() throws RemoteException {
		return this.decided;
	}

	public void setDecided(boolean decided) {
		this.decided = decided;
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

	public void setLevel(int level) {
		this.round = level;
	}

	public void increaseLevel() {
		this.round = this.round + 1;
	}



}
