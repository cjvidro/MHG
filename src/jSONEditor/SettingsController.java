package jSONEditor;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.stage.Stage;

        import java.io.IOException;

public class SettingsController {
    /*****************************************************
     * Change Scenes
     *****************************************************/

    protected Stage closeSettings(Stage settingsStage) {
        System.out.println("Close Settings");

        settingsStage.close();
        settingsStage = null;
        return settingsStage;
    }

    @FXML
    private void closeSettings(ActionEvent event) {
        // Calls above method for testing purposes
        closeSettings((Stage) ((Button) event.getSource()).getScene().getWindow());
    }
}
