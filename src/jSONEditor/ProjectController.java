package jSONEditor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller class for the core GUI
 */
public class ProjectController {
    EditorData editorData = EditorData.getInstance();

    /*****************************************************
     * FXML fields
     *****************************************************/
    @FXML private TextField nameField;
    @FXML private TextField minField;
    @FXML private TextField maxField;
    @FXML private ComboBox categoryBox;
    @FXML private TextField minDistanceField;
    @FXML private TextField maxDistanceField;
    @FXML private VBox soundsVBox;

    @FXML
    public void initialize() {
        // populate categories
        if (categoryBox != null) {
            for (Category category : Category.values()) {
                categoryBox.getItems().addAll(category);
            }
        }

        /*
         * INSERT POPULATE TEMPLATES
         */
    }


    /*****************************************************
     * Change Scenes and Button Functionality
     *****************************************************/
    protected boolean quit() throws Exception {
        class ExpectedQuitException extends Exception {
            public ExpectedQuitException(String message) {
                super(message);
            }
        }
        throw new ExpectedQuitException("User exited");
    }

    @FXML
    private void quit(ActionEvent event) throws Exception {
        System.out.println("Quit");

        /*
        INSERT Prompt Save FUNCTIONALITY HERE
         */

        try {
            quit();
        } catch (Exception e) {
            System.exit(0);
        }
    }

    @FXML
    protected Stage showExport(ActionEvent event) throws IOException {
        System.out.println("Show Export");

        // load FXML and set the controller
        ExportController controller = new ExportController(); // the controller for the view project GUI
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../view/export.fxml")));
        loader.setController(controller); // export controller
        Parent root = loader.load();

        // set JavaFX stage details
        Stage exportWindow = new Stage();
        exportWindow.setTitle("JSON Sound Definitions Editor - Export");
        exportWindow.setScene(new Scene(root, 350, 190));
        exportWindow.initModality(Modality.APPLICATION_MODAL);
        exportWindow.setResizable(false);
        exportWindow.show();

        return exportWindow;
    }

    @FXML
    protected Stage showSettings(ActionEvent event) throws IOException {
        System.out.println("Show Settings");

        // load FXML and set the controller
        SettingsController controller = new SettingsController(); // the controller for the view project GUI
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../view/settings.fxml")));
        loader.setController(controller); // export controller
        Parent root = loader.load();

        // set JavaFX stage details
        Stage settingsWindow = new Stage();
        settingsWindow.setTitle("JSON Sound Definitions Editor - Settings");
        settingsWindow.setScene(new Scene(root, 400, 200));
        settingsWindow.initModality(Modality.APPLICATION_MODAL);
        settingsWindow.setResizable(false);
        settingsWindow.show();

        return settingsWindow;
    }

    @FXML
    protected Stage showAddTemplate(ActionEvent event) throws IOException {
        System.out.println("Show Add Template");

        // load FXML and set the controller
        AddTemplateController controller = new AddTemplateController(); // the controller for the view project GUI
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../view/addTemplate.fxml")));
        loader.setController(controller); // addTemplate controller
        Parent root = loader.load();

        // set JavaFX stage details
        Stage addTemplateWindow = new Stage();
        addTemplateWindow.setTitle("JSON Sound Definitions Editor - Add Template");
        addTemplateWindow.setScene(new Scene(root, 325, 400));
        addTemplateWindow.initModality(Modality.APPLICATION_MODAL);
        addTemplateWindow.setResizable(false);
        addTemplateWindow.show();

        return addTemplateWindow;
    }

    @FXML
    protected Stage showEditTemplate(ActionEvent event) throws IOException {
        System.out.println("Show Edit Template");

        // load FXML and set the controller
        EditTemplateController controller = new EditTemplateController(); // the controller for the view project GUI
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../view/editTemplate.fxml")));
        loader.setController(controller); // addTemplate controller
        Parent root = loader.load();

        // set JavaFX stage details
        Stage editTemplateWindow = new Stage();
        editTemplateWindow.setTitle("JSON Sound Definitions Editor - Edit Template");
        editTemplateWindow.setScene(new Scene(root, 325, 400));
        editTemplateWindow.initModality(Modality.APPLICATION_MODAL);
        editTemplateWindow.setResizable(false);
        editTemplateWindow.show();

        return editTemplateWindow;
    }

    protected Stage showAddPlaysound(Stage addPlaysoundWindow) throws IOException {
        System.out.println("Show Add Playsound");

        // load FXML and set the controller
        ProjectController controller = new ProjectController(); // the controller for the view project GUI
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../view/addPlaysound.fxml")));
        loader.setController(controller); // addPlaysound/viewProject controller
        Parent root = loader.load();

        double width =  addPlaysoundWindow.getScene().getWidth();
        double height = addPlaysoundWindow.getScene().getHeight();

        // set JavaFX stage details
        addPlaysoundWindow.setScene(new Scene(root, width, height)); // swap scenes
        addPlaysoundWindow.setTitle("JSON Sound Definitions Editor - Add Playsound");

        return addPlaysoundWindow;
    }

    @FXML
    private void showAddPlaysound(ActionEvent event) throws IOException {
        // Calls the above helper method for testing purposes
        showAddPlaysound((Stage) ((Button) event.getSource()).getScene().getWindow());
    }

    /**
     * @param overlyingBox - The overlying HBox for a sound
     * @return an array of HBoxes corresponding to a sounds directory, stream, volume, pitch, and LOLM
     */
    private HBox[] getSoundHBoxes(HBox overlyingBox) {
        HBox[] soundBoxes = new HBox[5];

        if (overlyingBox != null) {
            VBox containingBox = (VBox) overlyingBox.getChildren().get(1);

            soundBoxes[0] = (HBox) containingBox.getChildren().get(0); // directory box
            soundBoxes[1] = (HBox) containingBox.getChildren().get(1); // stream box
            soundBoxes[2] = (HBox) containingBox.getChildren().get(2); // volume box
            soundBoxes[3] = (HBox) containingBox.getChildren().get(3); // pitch box
            soundBoxes[4] = (HBox) containingBox.getChildren().get(4); // LOLM box
        }

        return soundBoxes;
    }

    /**
     * Checks if the sounds are valid
     * @return true if valid, false if invalid
     */
    protected boolean validateSounds() {
        if (soundsVBox != null) {
            System.out.println("validate " + soundsVBox.getChildren().size());

            for (Node soundNode : soundsVBox.getChildren()) {
                HBox overlyingBox = (HBox) soundNode;

                // check if this is the extra "box" for adding more sounds
                if (overlyingBox != null) {
                    try {
                        overlyingBox.getChildren().get(1);
                    } catch (IndexOutOfBoundsException e) {
                        return true;
                    }
                }

                HBox[] soundBoxes = getSoundHBoxes(overlyingBox);

                String directory = ((TextField) soundBoxes[0].getChildren().get(2)).getText();

                if (directory.equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a playsound is valid
     * @return true if valid, false if invalid
     */
    protected boolean validatePlaysound() {

        // Check if name is null or empty
        if (nameField == null || nameField.getText().equals("")) {
            return false;
        }

        return validateSounds();
    }

    protected boolean createPlaysound() {
        boolean valid = validatePlaysound();

        if (valid) {
            Playsound playsound = new Playsound();

            playsound.setName(nameField.getText());
            playsound.setCategory((Category) categoryBox.getValue());

            // check if min distance is empty
            if (!minDistanceField.getText().equals("")) {
                playsound.setMin(Double.parseDouble(minDistanceField.getText()));
            }

            // check if max distance is empty
            if (!maxDistanceField.getText().equals("")) {
                playsound.setMax(Double.parseDouble(maxDistanceField.getText()));
            }

            // Add all of the individual sounds
            for (Node soundNode : soundsVBox.getChildren()) {
                HBox overlyingBox = (HBox) soundNode;

                // check if this is the extra "box" for adding more sounds
                if (overlyingBox != null) {
                    try {
                        overlyingBox.getChildren().get(1);
                    } catch (IndexOutOfBoundsException e) {
                        break;
                    }
                }

                HBox[] soundBoxes = getSoundHBoxes(overlyingBox);

                String directory = ((TextField) soundBoxes[0].getChildren().get(2)).getText();
                Boolean stream = ((CheckBox) soundBoxes[1].getChildren().get(2)).isSelected();

                // check if volume, pitch, lolm are empty
                Double volume = null;
                Double pitch = null;

                if (!((TextField) soundBoxes[2].getChildren().get(2)).getText().equals("")) {
                    volume = Double.parseDouble(((TextField) soundBoxes[2].getChildren().get(2)).getText());
                }

                if (!((TextField) soundBoxes[3].getChildren().get(2)).getText().equals("")) {
                    pitch = Double.parseDouble(((TextField) soundBoxes[3].getChildren().get(2)).getText());
                }

                Boolean lolm = ((CheckBox) soundBoxes[4].getChildren().get(2)).isSelected();

                playsound.addSound(directory, stream, volume, pitch, lolm);
            }

            // Add the playsound to editorData instance
            editorData.playsounds.add(playsound);

            return true;
        }

        return false;
    }

    @FXML
    private void addSound() throws IOException{
        int sounds = soundsVBox.getChildren().size();

        // load FXML and set the controller
        ProjectController controller = new ProjectController(); // the controller for the view project GUI
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../view/sound.fxml")));
        loader.setController(controller); // view project controller
        Node sound = loader.load();

        soundsVBox.getChildren().add(sounds - 1, sound);

        System.out.println("Add " + soundsVBox.getChildren().size());
    }

    protected Stage saveAddPlaysound(Stage viewProjectWindow) throws IOException {
        System.out.println("Save Add Playsound");

        boolean success = createPlaysound();

        if (success) {
            System.out.print("Added playsound ");
            System.out.println(editorData.playsounds.get(editorData.playsounds.size() - 1).getName());

            return showViewProject(viewProjectWindow);
        }

        System.out.println("Invalid playsound");
        return null;
    }

    @FXML
    private void saveAddPlaysound(ActionEvent event) throws IOException {
        // Calls the above helper method for testing purposes
        saveAddPlaysound((Stage) ((Button) event.getSource()).getScene().getWindow());
    }

    @FXML
    private void cancelAddPlaysound(ActionEvent event) throws IOException {
        // Calls the above helper method for testing purposes
        showViewProject((Stage) ((Button) event.getSource()).getScene().getWindow());
    }

    protected Stage showViewProject(Stage viewProjectWindow) throws IOException {
        // load FXML and set the controller
        ProjectController controller = new ProjectController(); // the controller for the view project GUI
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../view/viewProject.fxml")));
        loader.setController(controller); // addPlaysound/viewProject controller
        Parent root = loader.load();

        double width =  viewProjectWindow.getScene().getWidth();
        double height = viewProjectWindow.getScene().getHeight();

        // set JavaFX stage details
        viewProjectWindow.setScene(new Scene(root, width, height)); // swap scenes
        viewProjectWindow.setTitle("JSON Sound Definitions Editor");

        return viewProjectWindow;
    }
}
