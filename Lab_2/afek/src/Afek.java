
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

public class Afek implements Runnable {
	private Node currentNode;

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

			String[] remoteIds = currentNode.getRegistry().list();
			// Complete graph.
			for (String id : remoteIds) {
				if (!id.equals(Integer.toString(currentNode.getProcessId())))
					currentNode.getLinks().add(id);
			}
			int k = 0;
			while (!currentNode.finished && currentNode.getStatus() == 1) {
				// Not finished && Still candidate
				currentNode.increaseLevel();
				Thread.sleep(200);

				System.out.println();
				System.out.println(currentNode.getOriginProcessId() + "'s New round: " + currentNode.getLevel());
				if (currentNode.getLevel() % 2 == 0) {
					// ackReceived = 0;
					currentNode.ackList.clear();
					if (currentNode.getLinks().size() == 0) {
						currentNode.finished = true;
						System.out.println("Elected, and the leader is: (level, nodeId) = (" + currentNode.getLevel()
								+ ",  " + currentNode.getProcessId() + ")");
						annouceLeader(remoteIds);
					} else {
						int sender = currentNode.getProcessId();
						k = (int) Math.min(Math.pow(2, currentNode.getLevel() / 2), currentNode.getLinks().size());
						for (int i = 0; i < k; i++) {
							int id = randomNumber(0, currentNode.getLinks().size());
							String receiver = currentNode.getLinks().get(id);

							// receiver in Links has not change
							currentNode.getLinks().remove(id);
							INode remoteNode = currentNode.getRemoteNode(receiver);
							int receiverInt = Integer.parseInt(receiver);
							Message m = new Message(false, sender, receiverInt, currentNode.getLevel(),
									remoteNode.getLevel());
							currentNode.broadcastMessage(m);
							remoteNode.receiveMessage(m);
						}
						currentNode.finishBroadCast.put(currentNode.getLevel(), true);
					}
				} else {
					if (currentNode.ackList.size() < k) {
						System.out.println("I kill myself");
						for (Message m : currentNode.ackList) {
							System.out.print(m.toStringAck());
						}
						System.out.print("\n");
						currentNode.setStatus(0);
					} else {
						System.out.println("I stay alive");
					}
				}
				Thread.sleep(200);
			} // end of while

			while (!currentNode.finished && currentNode.getStatus() == 0) {

				Thread.sleep(200);
			}

		} catch (Exception e) {
			System.out.println("Exception in broadCast " + e);
		}
	}

	public static int randomNumber(int low, int high) {
		Random r = new Random();
		return r.nextInt(high - low) + low;
	}
	
	
	//Now change every Node's processId to my Id
	public void annouceLeader(String[] allNodes) throws AccessException, RemoteException, NotBoundException{
		for (String s : allNodes) {
			INode remoteNode = currentNode.getRemoteNode(s);
			remoteNode.setProcessId(currentNode.getProcessId());
//			System.out.println(remoteNode.getProcessId() +"  "+remoteNode.getOriginProcessId());
		}
		
	}

}
