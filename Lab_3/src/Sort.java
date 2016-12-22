import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class Sort {

	public static void main(String args[]) throws IOException {
		String name = "resultAlign";
		String nameSort = "resultSort";
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

		try {
			while ((line = reader.readLine()) != null) {
				Shit = line.trim().split(",");
				if (!Shit[0].startsWith("Id")) {
					pw.append(line);
					pw.append("\n");

				} else {
					sort(Shit);
					for (int i = 0; i < Shit.length; i++) {
						pw.append(Shit[i].split(" ")[3]);

						// pw.append(Shit[i]);
						if (i < Shit.length - 1) {
							pw.append(",");
						} else {
							pw.append("\n");
						}
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.flush();
		pw.close();
		System.out.println("Sorted!");
	}

	public static void sort(String[] s) {

		int low = 0;
		int high = s.length - 1;
		quickSort(s, low, high);

	}

	public static void quickSort(String[] arr, int low, int high) {
		if (arr == null || arr.length == 0)
			return;

		if (low >= high)
			return;

		// pick the pivot
		int middle = low + (high - low) / 2;
		int pivot = Integer.parseInt(arr[middle].split(" ")[1]);

		// make left < pivot and right > pivot
		int i = low, j = high;
		while (i <= j) {
			while (Integer.parseInt(arr[i].split(" ")[1]) < pivot) {
				i++;
			}
			while (Integer.parseInt(arr[j].split(" ")[1]) > pivot) {
				j--;
			}
			if (i <= j) {
				String temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
				i++;
				j--;
			}
		}

		// recursively sort two sub parts
		if (low < j)
			quickSort(arr, low, j);

		if (high > i)
			quickSort(arr, i, high);
	}

}
