import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class Align {
	public static void main(String args[]) throws IOException {
		String name = "result";
		String nameSort = name + "Align";
		String Path = Paths.get(".").toAbsolutePath().normalize().toString();
		String csv = Path + "/" + name + ".csv";
		String csvSort = Path + "/" + nameSort + ".csv";
		BufferedReader reader = new BufferedReader(new FileReader(csv));
		BufferedReader readerForward = new BufferedReader(new FileReader(csv));
		FileWriter pw = new FileWriter(csvSort, false);
		String line = "";
		String lineForward = "";
		String[] Shit = null;
		String[] ShitForward = null;

		boolean flag = false;
		if ((lineForward = readerForward.readLine()) != null) {
			flag = true;
		}
		try {
			while ((line = reader.readLine()) != null) {
				if (flag && (lineForward = readerForward.readLine()) != null) {
					ShitForward = lineForward.trim().split(",");

				}
				Shit = line.trim().split(",");
				if (!Shit[0].startsWith("Id")) {
					pw.append(line);
					pw.append("\n");

				} else {
					for (int i = 0; i < Shit.length; i++) {
						pw.append(Shit[i]);
						System.out.print("i = " + i + "\t");

						if (checkSameRound(Shit, ShitForward, i)) {
							pw.append(",");
						} else {
							pw.append("\n");
						}
					}
				}

				System.out.println("Line=" + line);
				System.out.println("Forward=" + lineForward);

				// count = Shit.length;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pw.flush();
		pw.close();

	}

	public static boolean checkSameRound(String[] s1, String[] s2, int i) {
		if (i <= s1.length - 2 && i >= 0) {
			String[] newSi = s1[i].split(" ");
			String[] newSiplus = s1[i + 1].split(" ");
			System.out.println(newSi[2] + " | " + newSiplus[2]);
			return (newSi[2].equals(newSiplus[2])) ? true : false;

		} else if (i == s1.length - 1) {
			String[] newSi = s1[i].split(" ");
			String[] newSiplus = s2[0].split(" ");
			if (newSiplus.length <= 3)
				return false;
			System.out.println("New Line: " + newSi[2] + " | " + newSiplus[2]);
			return (newSi[2].equals(newSiplus[2])) ? true : false;
		}
		return false;
	}

}
