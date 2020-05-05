package ToDoItems;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class ToDoData {
    private static ToDoData tododata=new ToDoData();
    private static String filename="todolistitem.text";
    private DateTimeFormatter formatter;
    private ObservableList<ToDoItems> todoitems;

    public static ToDoData getInstance(){
        return tododata;
    }
    private ToDoData(){
        formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<ToDoItems> getTodoitems(){
        return todoitems;
    }

    public void addToDOItem(ToDoItems item){
    todoitems.add(item);
    }

    public void setTodoitems(ObservableList<ToDoItems> todoitems){
        this.todoitems=todoitems;
    }

    public void loadTodoItems() throws IOException{
        todoitems= FXCollections.observableArrayList();
        Path path= Paths.get(filename);
        BufferedReader bf= Files.newBufferedReader(path);
        String input;
        try{
            while((input=bf.readLine())!=null){
                String[] itemPieces=input.split("\t");
                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate localdate=LocalDate.parse(dateString,formatter);
                ToDoItems todoitem=new ToDoItems(shortDescription,details,localdate);
                todoitems.add(todoitem);
            }
        }finally {
            if(bf!=null)
                bf.close();
        }
    }

    public void storeToDoItem() throws IOException{
        Path path=Paths.get(filename);
        BufferedWriter bw=Files.newBufferedWriter(path);
        try{
            Iterator<ToDoItems> it=todoitems.iterator();
            while(it.hasNext()){
                ToDoItems item=it.next();
                bw.write(String.format("%s\t%s\t%s",item.getShortDescription(),item.getDescription(),item.getDeadLine().format(formatter)));
                bw.newLine();
            }
        }finally {
            if(bw!=null)
                bw.close();
        }
    }

    public void deleteToDOItem(ToDoItems item){
        todoitems.remove(item);
    }



}
