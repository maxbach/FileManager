package controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import utils.FileExtension;
import utils.Icons;


import java.io.File;

public class FileTreeItem extends TreeItem<FileExtension> {

    private Boolean childInPlace;

    public FileTreeItem(FileExtension value) {
        super(value);

        childInPlace = false;

    }

    public FileTreeItem(FileExtension value, Node graphic) {
        super(value, graphic);

        childInPlace = false;
    }

    @Override
    public boolean isLeaf() {
        return getValue().isFile();
    }

    @Override
    public ObservableList<TreeItem<FileExtension>> getChildren() {
        if (!childInPlace) {
            childInPlace = true;
            addChildes();
        }
        return super.getChildren();
    }

    public void addNewChild(FileExtension file) {
        if (childInPlace) {
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
