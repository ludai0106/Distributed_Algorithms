package afek;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Node extends UnicastRemoteObject implements INode {
	// 0 represent Ordinary
	// 1 represent Candidate
	private int status;
	private boolean candidate;
	private int level;
	private int nodeId;
	private int ownerId;
	private int ownerLevel;
	private int processId;
	private Registry registry;
	private ArrayList<Message> buffer;
	private ArrayList<Message> ackList;

	public Node(int port, int nodeId, int processId, boolean candidate) throws RemoteException, AlreadyBoundException {
		super();

		this.nodeId = nodeId;
		this.ownerId = nodeId;
		this.processId = processId;
		this.registry = LocateRegistry.getRegistry(port);
		this.registry.bind(Integer.toString(processId), this);
		this.candidate = candidate;
		this.buffer = new ArrayList<>();
		this.ackList = new ArrayList<>();
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

	public void receiveMessage(Message message) {
		System.out.println(message.getSender());
	}
	
	public void registerNode(){
		
	}
	
	public void broadcastMessage(Message message) {
		
	}
	
	public void receiveAck(Message message){
		
	}

}
