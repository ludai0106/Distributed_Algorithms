
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

public class Afek implements Runnable {
	private Node currentNode;

	// Afek is the algorithm that only implement the Runnable.
	// Just to make Node class less complicated
	public Afek(Node currentNode) {
		this.currentNode = currentNode;
	}

	@Override
	public void run() {
		try {
			if (currentNode.getStatus() == 1) {
				System.out.println(currentNode.getOriginProcessId() + ":\tI'm a candidate process");
			} else {
				System.out.println(currentNode.getOriginProcessId() + ":\tI'm an ordinary process");
			}

			String[] allNodes = currentNode.getRegistry().list();
			// Complete graph.
			for (String id : allNodes) {
				if (!id.equals(Integer.toString(currentNode.getProcessId())))
					currentNode.getLinks().add(id);
			}
			int k = 0;
			// Not finished && Still candidate
			while (!currentNode.finished && currentNode.getStatus() == 1) {
				// Maybe can improve that the Nodes can increase level at same
				// time.
				currentNode.increaseLevel();
				Thread.sleep(200);

				System.out.println(currentNode.getOriginProcessId() + "'s New round: " + currentNode.getLevel());
				if (currentNode.getLevel() % 2 == 0) {
					// Empty the ackList each round.
					currentNode.ackList.clear();
					// Well I alive and I finish all the Links in my List,
					// everything done
					if (currentNode.getLinks().size() == 0) {
						currentNode.finished = true;
						System.out.println("Elected, and the leader is: (level, nodeId) = (" + currentNode.getLevel()
								+ ",  " + currentNode.getProcessId() + ")");
						// To change the id of each nodes.
						annouceLeader(allNodes);
					} else {
						// Get my own Id.
						int sender = currentNode.getProcessId();
						// Get the k
						k = (int) Math.min(Math.pow(2, currentNode.getLevel() / 2), currentNode.getLinks().size());
						for (int i = 0; i < k; i++) {
							// Randomly find the next node to send message.
							int id = randomNumber(0, currentNode.getLinks().size());
							String receiver = currentNode.getLinks().get(id);
							// receiver in Links has not change
							currentNode.getLinks().remove(id);
							int receiverInt = Integer.parseInt(receiver);
							INode remoteNode = currentNode.getRemoteNode(receiver);
							Message m = new Message(false, sender, receiverInt, currentNode.getLevel(),
									remoteNode.getLevel());
							currentNode.sendingMessageToLinks(m);
							remoteNode.receiveMessage(m);
						}
						// This is to announce that the round has finished for
						// the candidate.
						currentNode.finishBroadCast.put(currentNode.getLevel(), true);
					}
				} else {
					if (currentNode.ackList.size() < k) {
						System.out.println(
								currentNode.getOriginProcessId() + ":\tI fail the election and make myself ordinary");
						for (Message m : currentNode.ackList) {
							System.out.print(m.toStringAck());
						}
						System.out.print("\n");
						// Now I am just an ordinary.
						currentNode.setStatus(0);
					} else {
						System.out.println(currentNode.getOriginProcessId() + ":\tI am still candidate!");
					}
				}
				Thread.sleep(200);
			} // end of while

		} catch (Exception e) {
			System.out.println("Exception in Afek " + e);
		}
	}

	// Create a random number between min and max.
	public static int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}

	// Now change every Node's processId to my Id
	public void annouceLeader(String[] allNodes) throws AccessException, RemoteException, NotBoundException {
		for (String s : allNodes) {
			INode remoteNode = currentNode.getRemoteNode(s);
			remoteNode.setProcessId(currentNode.getProcessId());
			// System.out.println(s+" "+remoteNode.getOriginProcessId());
		}

	}

}
