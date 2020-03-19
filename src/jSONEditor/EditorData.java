package jSONEditor;

import java.util.ArrayList;

public class EditorData {
    private static EditorData single_instance = null;

    // "Global" variables that should be accessible by ALL classes in the package
    protected ArrayList<Sound> sounds;
    protected ArrayList<Template> templates;

    private EditorData() {
        sounds = new ArrayList<>();
        templates = new ArrayList<>();
    }

    public static EditorData getInstance() {
        if (single_instance == null) {
            single_instance = new EditorData();
        }

        return single_instance;
    }
}
