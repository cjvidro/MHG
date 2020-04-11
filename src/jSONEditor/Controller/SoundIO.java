package jSONEditor.Controller;
import java.io.*;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SoundIO {
	protected void readInPlaySound(String filename) {
		try (FileReader reader = new FileReader(filename)) {

			/*
			READ THE FILE TO A STRING
			 */

			//JSON parser object to parse read file
			JSONParser jsonParser = new JSONParser();

			//Read JSON file
			JSONObject newPlaySound = (JSONObject) jsonParser.parse(""  );  // PASS IN THE READ STRING HERE

			EditorData instance = EditorData.getInstance();

			/*
			THE REST OF THE CODE IS UP TO YOU. . .
			 */

			// get the category from the JSON sound object
			Category category = (Category) newPlaySound.get("category");
			System.out.println("The category is: " + category);

			// get the min_distance from the JSON sound object
			Double min_distance = (Double) newPlaySound.get("min_distance");
			System.out.println("The min_distance is: " + min_distance);

			// get the max_distance from the JSON sound object
			Double max_distance = (Double) newPlaySound.get("max_distance");
			System.out.println("The max_distance is: " + max_distance);

			Playsound playsound = new Playsound();

			playsound.setName(filename);
			playsound.setCategory(category);
			playsound.setMax(max_distance);
			playsound.setMin(min_distance);

			instance.playsounds.add(playsound);

			// get an array from the JSON sound object
			JSONArray JSONplaysounds = (JSONArray) newPlaySound.get("sounds");

			// take the elements of the JSON sound array
			for (int i = 0; i < JSONplaysounds.size(); i++) {

				//temp objects to access functions and store values
				JSONObject tempJSON = (JSONObject) JSONplaysounds.get(i);

				//temp values
				String directory = "";
				Boolean stream = false;
				Double volume = 1.0;
				Double pitch = 1.0;
				Boolean lolm = true;

				//Pulling values from JSONObject to temp values
				directory = (String) tempJSON.get("name");
				stream = (Boolean) tempJSON.get("stream");
				volume = (Double) tempJSON.get("volume");
				pitch = (Double) tempJSON.get("pitch");
				lolm = (Boolean) tempJSON.get("load_on_low_mem");

				//setting temp PlaySound with temp values
				instance.playsounds.get(instance.playsounds.size() - 1).addSound(directory, stream, pitch, volume, lolm);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


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

	public static boolean saveProject() {
		File selectedDirectory = EditorData.getInstance().currentDirectory;

		if (selectedDirectory == null) {
			return saveProjectAs();
		}

		// create / move backups
		File save5 = new File(selectedDirectory.getAbsolutePath() + "/Backups/sound_definitions_5.json");
		save5.delete();
		File save4 = new File(selectedDirectory.getAbsolutePath() + "/Backups/sound_definitions_4.json");
		save4.renameTo(save5);
		File save3 = new File(selectedDirectory.getAbsolutePath() + "/Backups/sound_definitions_3.json");
		save3.renameTo(save4);
		File save2 = new File(selectedDirectory.getAbsolutePath() + "/Backups/sound_definitions_2.json");
		save2.renameTo(save3);
		File save1 = new File(selectedDirectory.getAbsolutePath() + "/Backups/sound_definitions_1.json");
		save1.renameTo(save2);
		File lastSave = new File(selectedDirectory.getAbsolutePath() + "/sound_definitions.json");
		lastSave.renameTo(save1);

		// save sound_defintions file
		writePlaysounds(selectedDirectory);

		/*
		Save changelog
		 */

		System.out.println("Saved project " + selectedDirectory.getName());
		return true;
	}
}
