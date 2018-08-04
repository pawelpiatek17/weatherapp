package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class CSV {
	private static char delimeter = ';';

	public static void create(File file, HashMap<String, String> hashMap) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			for (Entry<String, String> entry : hashMap.entrySet()) {
				bufferedWriter.write(entry.getKey());
				bufferedWriter.write(delimeter);
				bufferedWriter.write(entry.getValue());
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
