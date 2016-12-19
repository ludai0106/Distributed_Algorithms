
import java.util.Random;

public class Byzantine implements Runnable {
	private Node node;
	final char N = 'N';
	final char P = 'P';

	// Byzantine is the algorithm that only implement the Runnable.
	// Just to make Node class less complicated
	public Byzantine(Node currentNode) {
		this.node = currentNode;
	}

	@Override
	public synchronized void run() {
		try {
			String[] allNodes = node.getRegistry().list();
			// Complete graph.
			for (String id : allNodes) {
				// if (!id.equals(Integer.toString(node.getNodeId())))
				node.getLinks().add(id);
			}
			// do forever
			while (true) {
				// broadcast(N;r,v)

				int round = node.getRound();
				int value = node.getValue();
				int f = node.getfNumber();
				Message mN = new Message(N, round, value);
				node.broadCast(mN);

				// System.out.println(node.getNodeId()+":Waiting for N...");

				// await n−f messages of the form (N;r,*)
				node.await(round, N);

				// System.out.println(node.getNodeId()+":Waiting for N
				// over...");

				// if(>(n+f)/2 messages (N;r,w) received with w=0 or 1)then
				if (node.countMaxMessage(N, round) > (node.getSize() + f) / 2.0) {
					// broadcast(P;r,w), w has to be the larger value, either 0
					// or 1

					Message mP = new Message(P, round, node.getMaxMessageValue(N, round));
					node.broadCast(mP);
				} else {
					// else broadcast(P;r,?), broadCast whatEvery, so we choose
					// 1 magic number between 0 and 100

					Message mP = new Message(P, round, randomNumber(0, 100));
					node.broadCast(mP);
				}

				// if decided then STOP
				if (node.getDecided()) {
					break;
				} else {
					// else await n−f messages of the form (P,r,*)
					// System.out.println(node.getNodeId()+":Waiting for P...");

					node.await(round, P);

					// System.out.println(node.getNodeId()+":Waiting for P
					// over...");
				}
				// if (> f messages (P;r,w) received with w=0 or 1) then
				if (node.countMaxMessage(P, round) > f) {
					// v←w

					node.setValue(node.getMaxMessageValue(P, round));
					value = node.getValue();
					// if (> 3f messages (P;r,w)) then
					if (node.countMessage(P, round, value) > 3.0 * f) {
						// decide w; decide true
						node.setDecided(true);
						node.decideAnounce();
					}
				}
				// else v ← random(0,1)
				else {

					node.setValue(randomNumber(0, 1));
					value = node.getValue();

				}
				// r←r+1
				node.increaseRound();
				Thread.sleep(200);

			}

		} catch (Exception e) {
			System.out.println("Exception in Byzantine " + e);
		}
	}

	// Create a random number between min and max. [min,max]
	public static int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt(max + 1 - min) + min;
	}

}
