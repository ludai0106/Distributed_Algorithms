

import java.util.ArrayList;
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
				System.out.println("I'm a candidate process");
			} else {
				System.out.println("I'm an ordinary process");
			}

			// TODO Auto-generated method stub
			String[] remoteIds = currentNode.getRegistry().list();
			for (String id : remoteIds) {
				if (!id.equals(Integer.toString(currentNode.getProcessId())))
					currentNode.getLinks().add(id);
			}
			int k = 0;
			while (!currentNode.finished & currentNode.getStatus() == 1) {
				// Not finished and you are a candidate process
				currentNode.increaseLevel();
				Thread.sleep(200);
				System.out.println();
				System.out.println("New round: " + currentNode.getLevel());
				if (currentNode.getLevel() % 2 == 0) {
					// ackReceived = 0;
					currentNode.ackList.clear();
					if (currentNode.getLinks().size() == 0) {
						currentNode.finished = true;
						System.out.println("Elected, and the winner is: (level, nodeId) = (" + currentNode.getLevel()
								+ ",  " + currentNode.getProcessId() + ")");
					} else {
						int sender = currentNode.getProcessId();
						k = (int) Math.min(Math.pow(2, currentNode.getLevel() / 2), currentNode.getLinks().size());
						for (int i = 0; i < k; i++) {
							int id = randomNumber(0, currentNode.getLinks().size());
							String receiver = currentNode.getLinks().get(id);
							currentNode.getLinks().remove(id);
							INode remoteNode = currentNode.getRemoteNode(receiver);
							int receiverInt = Integer.parseInt(receiver);
							Message m = new Message(false, sender, receiverInt, currentNode.getLevel(),
									remoteNode.getLevel());
							currentNode.broadcastMessage(m);
							remoteNode.receiveMessage(m);
						}
					}
				} else {
					if (currentNode.ackList.size() < k) {
						System.out.println("I kill myself");
						currentNode.setStatus(0);
					} else {
						System.out.println("I stay alive");
					}
				}
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

}
