package sample;

import ToDoItems.ToDoData;
import ToDoItems.ToDoItems;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextField shortDescriptionField;
    @FXML
    private TextArea detailField;
    @FXML
    private DatePicker datePickerField;

    public ToDoItems processResults(){
        String shortDescription=shortDescriptionField.getText().trim();
        String detail=detailField.getText().trim();
        LocalDate deadLine=datePickerField.getValue();
        ToDoItems newItem=new ToDoItems(shortDescription,detail,deadLine);
        ToDoData.getInstance().addToDOItem(newItem);
        return newItem;
    }
}
