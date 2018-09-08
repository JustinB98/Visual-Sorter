package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import javafx.scene.control.TextField;

public final class FileUtility {

	public final static String MODULAR_FILE_NAME = "sorter-pref.txt";
	public final static boolean STYLE_FILE_EXISTS = new File("style.css").exists();
	public final static String DATA_FILE_NAME = "data.txt";

	private FileUtility() {}

	public static int getFileLen() {
		if (fileExists(MODULAR_FILE_NAME)) {
			try (BufferedReader br = Files.newBufferedReader(Paths.get(MODULAR_FILE_NAME))) {
				String line = br.readLine();
				if (line != null && Utility.isInteger(line)) {
					return Integer.parseInt(line);
				}
			} catch (IOException e) {
				System.err.println("File error: " + e.getMessage());
			}
		}
		return -1;
	}

	public static int[] getFileUserData() {
		if (fileExists(MODULAR_FILE_NAME)) {
			LinkedList<Integer> data = new LinkedList<>();
			try (BufferedReader br = Files.newBufferedReader(Paths.get(MODULAR_FILE_NAME))) {
				String line = br.readLine();
				while (( line = br.readLine() ) != null) {
					if (Utility.isInteger(line)) {
						data.add(Integer.parseInt(line));
					}
				}
				return Utility.convertListToArrayInt(data);
			} catch (IOException e) {
				System.err.println("File error: " + e.getMessage());
			}
		}
		return null;
	}

	public static void setDataFile(TextField[] textFields, boolean loadOnStartUp) {
		try (PrintWriter pw = new PrintWriter(new File(DATA_FILE_NAME))) {
			pw.println(loadOnStartUp);
			for (int i = 0; i < textFields.length; ++i) {
				pw.println(textFields[i].getText());
			}
		} catch (FileNotFoundException e) {
			System.err.println("File error: " + e.getMessage());
		}
	}

	public static void openDataFile() {
		int[] data = getDefaultData();
		try (BufferedReader br = Files.newBufferedReader(Paths.get(DATA_FILE_NAME))) {
			String line = br.readLine();
			boolean loadOnStartUp = false;
			if (line == null) {
				return;
			}
			if (line.toLowerCase().trim().equals("true")) {
				loadOnStartUp = true;
			}
			int i = 0;
			while (( line = br.readLine() ) != null) {
				if (Utility.isInteger(line)) {
					data[i] = Integer.parseInt(line);
				} else {
					// data[i] = -1;
				}
				++i;
			}
			Global.DATA_COLLECTION.setTo(data);
			if (loadOnStartUp) {
				Global.DATA_MANAGER.collectData();
			}
		} catch (IOException e) {
			System.err.println("File error: " + e.getMessage());
		}
	}

	/**
	 * @deprecated although it works, it's pretty useless because we can't write
	 *             to jar files RIP
	 */
	public static void setDataFromFile() {
		int[] data = getDefaultData();
		try (InputStream is = FileUtility.class.getResourceAsStream(DATA_FILE_NAME);
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr)) {
			String line = br.readLine();
			boolean loadOnStartUp = false;
			if (line == null) {
				return;
			}
			if (line.toLowerCase().trim().equals("true")) {
				loadOnStartUp = true;
			}
			int i = 0;
			while (( line = br.readLine() ) != null) {
				if (Utility.isInteger(line)) {
					data[i] = Integer.parseInt(line);
				} else {
					// data[i] = -1;
				}
				++i;
			}
			Global.DATA_COLLECTION.setTo(data);
			if (loadOnStartUp) {
				Global.DATA_MANAGER.collectData();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int[] getDefaultData() {
		int[] data = new int[Global.MAX_LEN];
		for (int i = 0; i < data.length; ++i) {
			data[i] = -1;
		}
		return data;
	}

	private static boolean fileExists(String file) {
		return new File(file).exists();
	}

}
