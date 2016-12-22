
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
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
	public void run() {
		try {
			// if (node.getClock().getIndex() + 1 == node.getSize())
			System.out.println(node.getNodeId() + ": I start running");
			String[] allNodes = node.getRegistry().list();
			int pureId = node.getClock().getIndex() + 1;
			// Complete graph.
			for (String id : allNodes) {
				// if (!id.equals(Integer.toString(node.getNodeId())))
				node.getLinks().add(id);
			}

			// Set to true
			node.setStart(true);

			// Change: if two machines
			if (node.getClock().getIndex() + 1 != node.getSize()) {
				while (!node.getRemoteNode(Integer.toString(node.getSize() + 1000)).getStart()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			// do forever
			while (true) {
				// broadcast(N;r,v)

				// if (node.getClock().getIndex() + 1 == node.getSize())
				// System.out.println(node.getNodeId() + ": I enter while");
				int round = node.getRound();
				int value = node.getValue();
				int f = node.getfNumber();
				Message mN = new Message(N, round, value, pureId);
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

					Message mP = new Message(P, round, node.getMaxMessageValue(N, round), pureId);
					node.broadCast(mP);
				} else {
					// else broadcast(P;r,?), broadCast whatEvery, so we choose
					// 1 magic number between 0 and 100

					Message mP = new Message(P, round, randomNumber(0, 100), pureId);
					node.broadCast(mP);
					if (!node.getDecided()) {
						if (countRow("result") == node.getSize() - 1 || countRow("result") > node.getSize())
							writeCSV("Id" + " " + pureId + " " + round + " " + Integer.toString(mP.getW()), "result",
									true, true);
						else
							writeCSV("Id" + " " + pureId + " " + round + " " + Integer.toString(mP.getW()), "result",
									true, false);
					}
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
						if (node.getClock().getIndex() == node.getSize() - 1) {
							writeCSV(" ", "result", true, true);
							writeCSV("Round", Integer.toString(round), "result", true, true);
							writeCSV("FinalValue", Integer.toString(value), "result", true, true);
						}
						node.decideAnounce();
					}
				}
				// else v ← random(0,1)
				else {

					node.setValue(randomNumber(0, 1));
					value = node.getValue();

				}
				// r←r+1
				synchronized (this) {
					node.increaseRound();
					// if (node.getClock().getIndex() + 1 == node.getSize())
					// System.out.println(node.getNodeId() + ": I enter this");
					if (node.synchronous) {
						// First tell others our time
						node.broadcastClock();
						// Wait till every one has their time
						while (node.clockList.size() < node.getSize() - 1) {
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println(node.getNodeId() + " : clockList " + node.clockList.size());
							for (int i = 0; i < node.getSize(); i++) {
								boolean found = false;
								for (Clock c : node.clockList) {
									if (i == c.getIndex()) {
										found = true;
										break;
									}
								}
								if (!found && i != node.getClock().getIndex()) {
									System.out.println(node.getNodeId() + ": Missing " + i);
								} else {
									found = false;
								}

							}
						}
						// Now it's time to break
						while (!node.waitUntilSameRound()) {
							Thread.sleep(200);
						}
					}

					node.clockList.clear();
				}
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

	public static String writeCSV(String value, String csvFileName, boolean addMoreLine, boolean last)
			throws IOException {
		String Path = Paths.get(".").toAbsolutePath().normalize().toString();
		String csv = Path + "/" + csvFileName + ".csv";

		FileWriter pw = new FileWriter(csv, addMoreLine);

		pw.append(value);
		// If not last , if last then we
		if (!last) {
			pw.append(",");
		} else {
			pw.append("\n");
		}
		pw.flush();
		pw.close();

		return csv;

	}

	public static String writeCSV(String value1, String value2, String csvFileName, boolean addMoreLine, boolean last)
			throws IOException {
		String Path = Paths.get(".").toAbsolutePath().normalize().toString();
		String csv = Path + "/" + csvFileName + ".csv";

		FileWriter pw = new FileWriter(csv, addMoreLine);

		pw.append(value1);
		pw.append(",");
		pw.append(value2);
		// If not last , if last then we
		if (!last) {
			pw.append(",");
		} else {
			pw.append("\n");
		}
		pw.flush();
		pw.close();

		return csv;

	}

	public static int countRow(String name) throws FileNotFoundException {
		String Path = Paths.get(".").toAbsolutePath().normalize().toString();
		String csv = Path + "/" + name + ".csv";
		BufferedReader reader = new BufferedReader(new FileReader(csv));
		String line = "";
		String[] Shit = null;
		int count = 0;

		try {
			while ((line = reader.readLine()) != null) {
				Shit = line.trim().split(",");
				// if you want to check either it contains some name
				// index 0 is first name, index 1 is last name, index 2 is ID
				// System.out.println(Shit[0] + "," + Shit[1] + "," + Shit[2] +
				// "," + Shit[3]);
				count = Shit.length;
				// line = reader.readLine();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

}
