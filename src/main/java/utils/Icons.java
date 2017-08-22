package utils;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;

public class Icons {
    private static final String WAY_TO_CLOSED_FOLDER_ICON = "icons/closed.png";
    private static final String WAY_TO_OPEN_FOLDER_ICON = "icons/open.png";
    private static final String WAY_TO_FILE_ICON = "icons/file.png";


    private static final double SIZE_OF_ICON = 24;


    public static ImageView closedFolder() {
        return getImageView(WAY_TO_CLOSED_FOLDER_ICON);
    }

    public static ImageView file() {
        return getImageView(WAY_TO_FILE_ICON);
    }

    public static ImageView openFolder() {
        return getImageView(WAY_TO_OPEN_FOLDER_ICON);
    }

    public static ProgressIndicator loading() {
        ProgressIndicator pinMain = new ProgressIndicator();
        pinMain.setPrefSize(SIZE_OF_ICON, SIZE_OF_ICON);
        return pinMain;
    }

    private static ImageView getImageView(String fileWay) {
        ImageView view = new ImageView(fileWay);
        view.setFitWidth(SIZE_OF_ICON);
        view.setFitHeight(SIZE_OF_ICON);
        return view;
    }




}
