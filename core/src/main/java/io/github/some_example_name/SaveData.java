package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import Birds.Bird;
import Pigs.Pigs;
import Elements.Structure;

public class SaveData {
    public Array<Bird> birds;
    public Array<Pigs> pigs;
    public Array<Structure> structures;

    // Constructor
    public SaveData() {
        birds = new Array<>();
        pigs = new Array<>();
        structures = new Array<>();
    }

    // Save game data to a file
    public void saveGame(String filename) {
        Json json = new Json();
        String saveData = json.toJson(this);

        FileHandle file = Gdx.files.local(filename);
        file.writeString(saveData, false);
    }

    // Load game data from a file
    public static SaveData loadGame(String filename) {
        try {
            FileHandle file = Gdx.files.local(filename);
            if (!file.exists()) {
                return new SaveData(); // Return empty save data if file doesn't exist
            }

            Json json = new Json();
            return json.fromJson(SaveData.class, file);
        } catch (Exception e) {
            Gdx.app.error("SaveData", "Error loading game: " + e.getMessage());
            return new SaveData();
        }
    }

    // Method to add objects to save data
    public void addBird(Bird bird) {
        birds.add(bird);
    }

    public void addPig(Pigs pig) {
        pigs.add(pig);
    }

    public void addStructure(Structure structure) {
        structures.add(structure);
    }

    // Method to clear all saved data
    public void clearAllData() {
        birds.clear();
        pigs.clear();
        structures.clear();
    }
}
