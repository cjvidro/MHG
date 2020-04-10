package jSONEditor.Controller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SoundIO {
	public static boolean writePlaysounds() {
		if (EditorData.getInstance().currentDirectory == null) {
			// use default directory
			return writePlaysounds(new File(""));
		} else {
			return writePlaysounds(EditorData.getInstance().currentDirectory);
		}
	}

	private static boolean writePlaysounds(File saveDirectory) {
		// the master object that holds everything
		JSONObject master = new JSONObject();

		// create each playsound to add to master
		for (Playsound playsound : EditorData.getInstance().playsounds) {
			// playsound attributes
			JSONObject playsoundDetails = new JSONObject();

			if (playsound.getMin() != null) {
				playsoundDetails.put("min_distance", playsound.getMin());
			}
			if (playsound.getMax() != null) {
				playsoundDetails.put("max_distance", playsound.getMax());
			}
			if (playsound.getCategory() != null) {
				playsoundDetails.put("category", playsound.getCategory().name());
			}

			// populate the sounds array
			JSONArray playsoundSoundsArray = new JSONArray();
			for (Sound sound : playsound.sounds) {
				JSONObject soundObject = new JSONObject();
				soundObject.put("name", "sounds/" + sound.getDirectory());
				soundObject.put("stream", sound.getStream());
				if (sound.getPitch() != null) {
					soundObject.put("pitch", sound.getPitch());
				}
				if (sound.getVolume() != null) {
					soundObject.put("volume", sound.getVolume());
				}
				soundObject.put("load_on_low_memory", sound.getLOLM());

				playsoundSoundsArray.add(soundObject);
			}

			playsoundDetails.put("sounds", playsoundSoundsArray);

			// add to the master object
			master.put(playsound.getName(), playsoundDetails);
		}

		// Write JSON file
		try (FileWriter file = new FileWriter(saveDirectory + "/" + "sound_definitions.json")) {

			file.write(toPrettyFormat(master.toJSONString()));
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private static String toPrettyFormat(String jsonString)
	{
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonString).getAsJsonObject();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		return prettyJson;
	}

	private static File chooseDirectory() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		return directoryChooser.showDialog(new Stage());
	}

	public static boolean saveProjectAs() {
		// get the new file folder
		File selectedDirectory = chooseDirectory();

		// Check if this save already exists
		Set values = EditorData.getInstance().saves.entrySet();
		Iterator iterator = values.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, File> entry = (Map.Entry<String, File>) iterator.next();
			File file = entry.getValue();

			if (selectedDirectory.getName().equals(file.getName())) {
				System.out.println("A project with this name already exists!");
				selectedDirectory = chooseDirectory();
			} else if (selectedDirectory.getAbsoluteFile().equals(file.getAbsoluteFile())) {
				System.out.println("A project with this file path already exists!");
				selectedDirectory = chooseDirectory();
			}
		}

		if (selectedDirectory == null) {
			System.out.println("Failed to save project!");
			return false;
		}

		// create backup folder
		File backupsFolder = new File(selectedDirectory.getAbsolutePath() + "/Backups");
		backupsFolder.mkdir();

		// set the current directory
		EditorData.getInstance().currentDirectory = selectedDirectory;

		// save the save directory
		EditorData.saves.put(selectedDirectory.getName(), selectedDirectory);
		EditorData.serializeSaves();

		// save sound_defintions file
		writePlaysounds(selectedDirectory);

		/*
		Save changelog
		 */

		System.out.println("Saved project as " + selectedDirectory.getName());
		return true;
	}
}
