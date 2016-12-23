import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

public class Test {

	static boolean easyMode = false;

	//You can assign initial value by yourSelf
	static boolean forceAssign = false;
	
	static int networkSize = 16;
	static int rPort = 1099;
	static int f = 4;
	static boolean traitorRandomMessage = true;
	static boolean traitorDoNotSendMessage = false;
	static int delay = 100;
	static boolean synchronous = false;

	public static void main(String args[]) throws AlreadyBoundException, NotBoundException, IOException {
		// Configurations for easyMode
		if (easyMode) {
			networkSize = 6;
			f = 1;
			traitorRandomMessage = false;
			traitorDoNotSendMessage = false;
			delay = 0;
		}
		writeCSV(" ", "result", true, true);
		writeCSV("networkSize", "result", true, false);
		writeCSV("f", "result", true, false);
		writeCSV("delay", "result", true, false);
		writeCSV("traitorRandomMessage", "result", true, false);
		writeCSV("traitorDoNotSendMessage", "result", true, false);
		writeCSV("synchronous", "result", true, true);

		writeCSV(Integer.toString(networkSize), "result", true, false);
		writeCSV(Integer.toString(f), "result", true, false);
		writeCSV(Integer.toString(delay), "result", true, false);
		writeCSV(Boolean.toString(traitorRandomMessage), "result", true, false);
		writeCSV(Boolean.toString(traitorDoNotSendMessage), "result", true, false);
		writeCSV(Boolean.toString(synchronous), "result", true, true);

		boolean last = true;

		for (int i = 1; i <= networkSize; i++) {
			// if (i != networkSize) {
			// last = false;
			// } else {
			// last = true;
			// }
			writeOneLine(i, networkSize, Integer.toString(i), "result", true, last);
			// writeCSV(Integer.toString(i), "result", true, last);
		}

		int[] initialValue = new int[networkSize + 1];
		if (args == null || args.length == 0 && !forceAssign) {
			LocateRegistry.createRegistry(rPort);
			for (int i = 1; i <= networkSize; i++) {
				int nodeId = 1000 + i;
				Node node;
				initialValue[i] = 0;
				boolean traitor;
				if (i <= networkSize - f) {
					initialValue[i] = randomNumber(0, 1);
					traitor = true;
					node = new Node(nodeId, f, initialValue[i], traitor, networkSize, rPort, traitorRandomMessage,
							traitorDoNotSendMessage, delay, i, synchronous);

				} else {
					traitor = false;
					node = new Node(nodeId, f, initialValue[i], traitor, networkSize, rPort, traitorRandomMessage,
							traitorDoNotSendMessage, delay, i, synchronous);
				}

				if (i == networkSize)
					last = true;
				else
					last = false;
				writeCSV(Boolean.toString(traitor), "result", true, last);
				node.notifyOthers();
				System.out.println(node.getNodeId() + ":\tWaiting for the incoming messages...");
			}
		} else if (args == null || args.length == 0 && forceAssign) {
			LocateRegistry.createRegistry(rPort);
			for (int i = 1; i <= networkSize; i++) {
				int nodeId = 1000 + i;
				Node node;
				initialValue[i] = 0;
				boolean traitor;
				if (i <= networkSize - f) {
					if (i <= 5 || i >= 9) {
						initialValue[i] = 1;
					}
					traitor = true;
					node = new Node(nodeId, f, initialValue[i], traitor, networkSize, rPort, traitorRandomMessage,
							traitorDoNotSendMessage, delay, i, synchronous);

				} else {
					traitor = false;
					node = new Node(nodeId, f, initialValue[i], traitor, networkSize, rPort, traitorRandomMessage,
							traitorDoNotSendMessage, delay, i, synchronous);
				}

				if (i == networkSize)
					last = true;
				else
					last = false;
				writeCSV(Boolean.toString(traitor), "result", true, last);
				node.notifyOthers();
				System.out.println(node.getNodeId() + ":\tWaiting for the incoming messages...");
			}

		}

		for (int i = 1; i <= networkSize; i++) {
			writeOneLine(i, networkSize, Integer.toString(initialValue[i]), "result", true, last);

			// writeCSV(Integer.toString(initialValue[i]), "result", true,
			// last);
		}

	}

	// Create a random number between min and max. [min,max]
	public static int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt(max + 1 - min) + min;
	}

	public static void writeOneLine(int i, int size, String value, String csvFileName, boolean addMoreLine,
			boolean last) throws IOException {

		if (i != size) {
			last = false;
		} else {
			last = true;
		}

		writeCSV(value, "result", true, last);

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
}
