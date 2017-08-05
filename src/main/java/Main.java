import controls.FileTreeCell;
import controls.FileTreeItem;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import utils.FileExtension;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FileExtension root = new FileExtension("/");
        TreeView<FileExtension> fileView = new TreeView<>(
                new FileTreeItem(root));

        fileView.setShowRoot(false);
        fileView.setEditable(true);
        fileView.setCellFactory(param -> new FileTreeCell());

        ScrollPane scrollPane = new ScrollPane(fileView);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 400, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("File manager");

        primaryStage.show();
    }
}
