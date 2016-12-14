//Lab3
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Node {
	
	// 0 represent Ordinary
	// 1 represent Candidate
	// Get killed will make status change from `1` to `0`
	private int status;
	//the size of the network
	private int size;
	private int port;
	//level here represent how many nodes he captured
	private int level;
	//Only for reference
	private int originNodeId;
	private Registry registry;
	
	

	
}
