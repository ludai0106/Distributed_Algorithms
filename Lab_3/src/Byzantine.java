
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

public class Byzantine implements Runnable {
	private Node currentNode;
	final char N = 'N';
	final char P = 'P';

	// Byzantine is the algorithm that only implement the Runnable.
	// Just to make Node class less complicated
	public Byzantine(Node currentNode) {
		this.currentNode = currentNode;
	}

	@Override
	public void run() {
		try {
			String[] allNodes = currentNode.getRegistry().list();
			// Complete graph.
			for (String id : allNodes) {
				if (!id.equals(Integer.toString(currentNode.getNodeId())))
					currentNode.getLinks().add(id);
			}
			while (true) {
				Message m = new Message(N, currentNode.getRound(), currentNode.getValue());
				currentNode.broadCast(m);

			}

		} catch (Exception e) {
			System.out.println("Exception in Afek " + e);
		}
	}

	// Create a random number between min and max.
	public static int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}

}
