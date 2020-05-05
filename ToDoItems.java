package ToDoItems;

import java.time.LocalDate;

public class ToDoItems {
    private String shortDescription;
    private String Description;
    private LocalDate deadLine;

    public ToDoItems(String shortDescription, String description, LocalDate deadLine) {
        this.shortDescription = shortDescription;
        Description = description;
        this.deadLine = deadLine;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    @Override
    public String toString() {
        return shortDescription;
    }
}
