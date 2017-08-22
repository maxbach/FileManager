package controls;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import utils.FileExtension;
import utils.Icons;

import java.io.File;

public class FileTreeItem extends TreeItem<FileExtension> {

    private Boolean hasLoadedChilds;
    private ProgressIndicator pinMain;

    public FileTreeItem(FileExtension value) {
        super(value);

        hasLoadedChilds = false;
        pinMain = Icons.loading();

    }

    public FileTreeItem(FileExtension value, Node graphic) {
        super(value, graphic);

        hasLoadedChilds = false;
        pinMain = Icons.loading();
    }

    @Override
    public boolean isLeaf() {
        return getValue().isFile();
    }

    @Override
    public ObservableList<TreeItem<FileExtension>> getChildren() {
        if (!hasLoadedChilds) {
            PauseTransition pt = new PauseTransition(javafx.util.Duration.seconds(2));
            pt.setOnFinished(event -> {

                hasLoadedChilds = true;
                addChildes();
                setGraphic(Icons.openFolder());

            });
            setGraphic(pinMain);
            pt.play();
        }

        return super.getChildren();
    }

    public void addNewChild(FileExtension file) {
        if (hasLoadedChilds) {
            FileTreeItem newFolderItem = createItem(file);
            super.getChildren().addAll(newFolderItem);
        }
    }

    public void removeChildItem(FileExtension file) {
        TreeItem<FileExtension> itemToRemove = null;
        for (TreeItem<FileExtension> fileExtensionTreeItem : getChildren()) {
            String fileName = fileExtensionTreeItem.getValue().getFile().getName();
            if (file.getName().equals(fileName)) {
                itemToRemove = fileExtensionTreeItem;
                break;
            }
        }

        if (itemToRemove != null) {
            getChildren().removeAll(itemToRemove);
        }
    }

    public void removeFromParent() {
        getParent().getChildren().removeAll(this);
    }

    private void addChildes() {
        if (!isLeaf()) {
            File[] childs = getValue().getFile().listFiles();
            ObservableList<TreeItem<FileExtension>> childList = FXCollections.observableArrayList();

            if (childs != null) {
                for (File child : childs) {
                    childList.add(createItem(new FileExtension(child)));
                }
            }
            super.getChildren().addAll(childList);
        }


    }

    private FileTreeItem createItem(FileExtension child) {
        ImageView image;
        if (!child.isFile()) {
            image = Icons.closedFolder();
        } else {
            image = Icons.file();
        }

        FileTreeItem item = new FileTreeItem(child, image);

        item.expandedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                item.setGraphic(Icons.openFolder());
            } else {
                item.setGraphic(Icons.closedFolder());
            }
        });

        return item;
    }


}



