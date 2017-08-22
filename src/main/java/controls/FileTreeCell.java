package controls;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import utils.Dialogs;
import utils.FileExtension;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileTreeCell extends TreeCell<FileExtension> {

    private static FileTreeItem itemToMove = null;
    private static Boolean isCopy = null;

    private MenuItem pasteItem = new MenuItem("Paste");
    private MenuItem newFolderItem = new MenuItem("New Folder");

    private ContextMenu contextMenu = new ContextMenu();
    private TextField textField;


    public FileTreeCell() {
        super();

        newFolderItem.setOnAction(t -> createNewFolder());

        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setOnAction(event -> setupForMovingFile(true));

        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setOnAction(event -> setupForMovingFile(false));

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> deleteFile());

        pasteItem.setOnAction(event -> moveFile());

        contextMenu.getItems().addAll(copyItem, cutItem, deleteItem);
    }

    private void moveFile() {
        FileExtension fileToMove = itemToMove.getValue();
        FileExtension folderToMove = getTreeItem().getValue();
        FileExtension destinationFile = FileUtils.getResultOfMovement(fileToMove, folderToMove);

        if (destinationFile.isExist()) {
            Character answer = Dialogs.choiceIfExistsFile();
            switch (answer) {
                case 'p': {
                    try {
                        destinationFile.delete();
                        FileTreeItem item = (FileTreeItem) getTreeItem();
                        item.removeChildItem(destinationFile);
                    } catch (IOException e) {
                        error(e, "replacing");
                        return;
                    }
                    break;
                }

                case 'n': {
                    destinationFile = FileUtils.getFileWithSuffix(destinationFile.getAbsolutePath());
                    break;
                }

                default: {
                    return;
                }
            }
        }

        if (isCopy) {
            try {
                fileToMove.copy(destinationFile);
            } catch (IOException e) {
                error(e, "moving");
                return;
            }
        } else {
            itemToMove.removeFromParent();
            try {
                fileToMove.cut(destinationFile);
            } catch (IOException e) {
                error(e, "moving");
                return;
            }
            itemToMove = null;
            isCopy = null;
        }

        FileTreeItem item = (FileTreeItem) getTreeItem();
        item.addNewChild(destinationFile);
        updateItem();
    }

    private void error(IOException e, String operation) {
        Dialogs.errorDialog("Error with "  + operation + " file");
        e.printStackTrace();
    }

    private void deleteFile() {
        FileExtension fileToRemove = getItem();

        if (itemToMove != null && fileToRemove.isEqualOrParent(itemToMove.getValue())) {
            itemToMove = null;
            isCopy = null;
        }

        try {
            fileToRemove.delete();
            FileTreeItem item = (FileTreeItem) getTreeItem();
            item.removeFromParent();
        } catch (IOException e) {
            error(e, "deleting");
        }
    }

    private void createNewFolder() {
        FileExtension currentFolder = getTreeItem().getValue();
        try {
            FileExtension newFolder = currentFolder.newFolder();
            FileTreeItem treeItem = (FileTreeItem) getTreeItem();
            treeItem.addNewChild(newFolder);
            treeItem.setExpanded(true);
        } catch (IOException e) {
            error(e, "creating");
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        } else {
            textField.setText(getText());
        }

        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        updateItem(getItem(), isEmpty());
    }


    private void commitEdit(String newValue) {
        FileExtension fileExtension = getItem();
        if (newValue == null || newValue.isEmpty()) {
            cancelEdit();
        } else {
            FileExtension destinationFile = FileUtils.resultOfRename(fileExtension, newValue);
            if (destinationFile.isExist()) {
                Character answer = Dialogs.choiceIfExistsFile();
                switch (answer) {
                    case 'p': {
                        try {
                            destinationFile.delete();
                        } catch (IOException e) {
                            error(e, "replacing");
                            return;
                        }
                        break;
                    }

                    case 'n': {
                        destinationFile = FileUtils.getFileWithSuffix(destinationFile.getAbsolutePath());
                        break;
                    }

                    default: {
                        return;
                    }

                }
            }

            try {
                fileExtension.rename(destinationFile);
                updateItem();
                super.commitEdit(fileExtension);
            } catch (IOException e) {
                error(e, "renaming");
            }

        }

    }

    @Override
    protected void updateItem(FileExtension item, boolean empty) {

        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getTreeItem().getValue().toString());
            setGraphic(getTreeItem().getGraphic());
            setupMenu();
        }
    }


    private void setupMenu() {
        File file = getTreeItem().getValue().getFile();
        ObservableList<MenuItem> menuItems = contextMenu.getItems();

        if (!file.isFile()) {

            if (menuItems.size() == 3) {
                menuItems.add(newFolderItem);
            }

            if (menuItems.size() == 5) {
                menuItems.removeAll(pasteItem);
            }

            if (itemToMove != null) {
                pasteItem.setText("Paste (" + itemToMove.getValue().toString() + ")");
                menuItems.add(pasteItem);
            }
        } else {
            if (menuItems.size() > 3) {
                menuItems.removeAll(pasteItem, newFolderItem);
            }
        }

        setContextMenu(contextMenu);
    }

    private void updateScreen() {
        updateTreeView(getTreeView());
    }

    private void updateItem() {
        updateTreeItem(getTreeItem());
    }

    private void setupForMovingFile(Boolean isCopy) {
        FileTreeCell.isCopy = isCopy;
        itemToMove = (FileTreeItem) getTreeItem();
        updateScreen();
    }

    private void createTextField() {
        textField = new TextField(getText());
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

}

