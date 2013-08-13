package com.cruzroja.creuroja.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

import com.cruzroja.creuroja.Location;

import android.content.Context;

public class FileUtils {
	public static final String FIJOS_FILENAME = "fijos.json";
	public static final String VARIABLES_FILENAME = "variables.json";

	public static void writeToFile(Context context, String data, boolean isFijo) {
		PrintStream ps = null;
		try {
			if (isFijo) {
				ps = new PrintStream(context.openFileOutput(FIJOS_FILENAME,
						Context.MODE_PRIVATE));
			} else {
				ps = new PrintStream(context.openFileOutput(VARIABLES_FILENAME,
						Context.MODE_PRIVATE));
			}
			ps.print(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			ps.close();
		}
	}

	public static ArrayList<Location> readFromFiles(Context context) {
		ArrayList<Location> locationList;

		String data = getDataFromFile(context, FIJOS_FILENAME);
		if (data.equals("")) {
			return null;
		}
		locationList = JSONParser.parseLocations(data);

		data = getDataFromFile(context, VARIABLES_FILENAME);
		if (data.equals("")) {
			return null;
		}
		locationList.addAll(JSONParser.parseLocations(data));

		return locationList;
	}

	public static boolean removeFiles() {
		return (new File(FIJOS_FILENAME).delete() && new File(
				VARIABLES_FILENAME).delete());
	}

	public static String getDataFromFile(Context context, String fileName) {
		String data = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					context.openFileInput(fileName)));
			String line = "";

			while ((line = reader.readLine()) != null) {
				data = data + line;
			}
		} catch (FileNotFoundException e) {
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return data;
	}
}
