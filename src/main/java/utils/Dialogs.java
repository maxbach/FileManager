package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Dialogs {
    public static void errorDialog(String headMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(headMessage);
        alert.setHeaderText("Maybe, you haven't permissions to do that action");
        alert.setContentText("You should a bottle of vodka and it will works");

        alert.showAndWait();
    }

    public static Character choiceIfExistsFile() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Problems with moving file");
        alert.setHeaderText("There is situated file with equal name");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Replace");
        ButtonType buttonTypeTwo = new ButtonType("Rename (add suffix)");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            return 'p';
        } else if (result.get() == buttonTypeTwo) {
            return 'n';
        }  else {
            return 'c';
        }
    }
}
