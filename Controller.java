package sample;
import ToDoItems.ToDoItems;
import ToDoItems.ToDoData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    private List<ToDoItems> todoitems;
    @FXML
    private ListView<ToDoItems> todolistview;
    @FXML
    private TextArea itemDetailsTextArea;
    @FXML
    private Label deadLineLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private ToggleButton filterButton;

    @FXML
    private FilteredList<ToDoItems> filteredList;
    @FXML
    private Predicate<ToDoItems> wantAllItem;
    private Predicate<ToDoItems> wantTodayItem;

    public void initialize() {

        listContextMenu =new ContextMenu();
        MenuItem deleteMenuItem =new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoItems item=todolistview.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        listContextMenu.getItems().addAll(deleteMenuItem);

        todolistview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItems>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItems> observable, ToDoItems oldValue, ToDoItems newValue) {
                if(newValue !=null){
                    ToDoItems item=todolistview.getSelectionModel().getSelectedItem();
                    itemDetailsTextArea.setText(item.getDescription());
                    DateTimeFormatter df=DateTimeFormatter.ofPattern("MMMM d,yyyy");
                    deadLineLabel.setText(df.format(item.getDeadLine()));
                }
            }

        });
        wantAllItem=new Predicate<ToDoItems>() {
            @Override
            public boolean test(ToDoItems toDoItems) {
                return true;
            }
        };
        wantTodayItem=new Predicate<ToDoItems>() {
            @Override
            public boolean test(ToDoItems toDoItems) {
                return (toDoItems.getDeadLine().equals(LocalDate.now()));
            }
        };
        filteredList=new FilteredList<ToDoItems>(ToDoData.getInstance().getTodoitems(),wantAllItem);

        SortedList<ToDoItems> sortedList=new SortedList<ToDoItems>(filteredList,
                new Comparator<ToDoItems>() {
                    @Override
                    public int compare(ToDoItems o1, ToDoItems o2) {
                        return o1.getDeadLine().compareTo(o2.getDeadLine());
                    }
                });

        todolistview.setItems(sortedList);
       todolistview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
       todolistview.getSelectionModel().selectFirst();


        todolistview.setCellFactory(new Callback<ListView<ToDoItems>, ListCell<ToDoItems>>() {
            @Override
            public ListCell<ToDoItems> call(ListView<ToDoItems> param) {
                ListCell<ToDoItems> cell=new ListCell<ToDoItems>(){
                    @Override
                    protected void updateItem(ToDoItems item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                          setText(null);
                        }else{
                          setText(item.getShortDescription());
                          if(item.getDeadLine().isBefore(LocalDate.now().plusDays(1)))
                              setTextFill(Color.RED);
                          else if(item.getDeadLine().equals(LocalDate.now().plusDays(1)))
                              setTextFill(Color.PINK);
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs,wasEmpty,isNowEmpty)->{
                            if(isNowEmpty)
                                cell.setContextMenu(null);
                            else
                                cell.setContextMenu(listContextMenu);
                        }
                );
                return cell;
            }
        });
       

    }

    public void handleKeyPressed(KeyEvent keyEvent){
        ToDoItems selectedItem=todolistview.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            if(keyEvent.getCode().equals(KeyCode.DELETE))
                deleteItem(selectedItem);
        }
    }

    public void showNewItemDialogue() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("todoitemdialog.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("error in showNewItemDialogue " + e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            ToDoItems newItem=controller.processResults();
            todolistview.getSelectionModel().select(newItem);
        }
    }

    public void deleteItem(ToDoItems item){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete ToDo Item");
        alert.setHeaderText("Delete Items: "+item.getShortDescription());
        alert.setContentText("Are You Sure To Delete Selected Items ? Press OK to Comform or CANCEL to withdraw. ");
        Optional<ButtonType> result=alert.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK)
            ToDoData.getInstance().deleteToDOItem(item);
    }

    public void handleFilterButton(){
        ToDoItems selectedItem=todolistview.getSelectionModel().getSelectedItem();
        if(filterButton.isSelected()){
        filteredList.setPredicate(wantTodayItem);
        if(filteredList.isEmpty()){
            itemDetailsTextArea.clear();
            deadLineLabel.setText("");

        }else if(filteredList.contains(selectedItem)){
            todolistview.getSelectionModel().select(selectedItem);
        }else{
            todolistview.getSelectionModel().selectFirst();
        }
        }else {
         filteredList.setPredicate(wantAllItem);
         todolistview.getSelectionModel().select(selectedItem);
        }
    }
    @FXML
    public void handleExit(){
        Platform.exit();
    }

}
