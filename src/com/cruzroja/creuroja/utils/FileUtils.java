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
	public static final String LOCATIONS_FILENAME = "fijos.json";

	public static void writeToFile(Context context, String data) {
		PrintStream ps = null;
		try {
			ps = new PrintStream(context.openFileOutput(LOCATIONS_FILENAME, Context.MODE_PRIVATE));

			ps.print(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			ps.close();
		}
	}

	public static ArrayList<Location> readFromFiles(Context context) {
		ArrayList<Location> locationList;

		String data = getDataFromFile(context, LOCATIONS_FILENAME);
		if (data.equals("")) {
			return null;
		}

		return JSONParser.parseLocations(data);
	}

	public static boolean removeFiles() {
		return new File(LOCATIONS_FILENAME).delete();
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
