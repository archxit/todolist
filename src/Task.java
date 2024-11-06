import javafx.beans.property.*;

public class Task<T> {
    private final StringProperty title;
    private final BooleanProperty completed;
    private final ObjectProperty<Priority> priority;

    public enum Priority { LOW, MEDIUM, HIGH }

    public Task(String title) {
        this.title = new SimpleStringProperty(title);
        this.completed = new SimpleBooleanProperty(false);
        this.priority = new SimpleObjectProperty<>(Priority.MEDIUM);
    }

    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }
    public StringProperty titleProperty() { return title; }

    public boolean isCompleted() { return completed.get(); }
    public void setCompleted(boolean value) { completed.set(value); }
    public BooleanProperty completedProperty() { return completed; }

    public Priority getPriority() { return priority.get(); }
    public void setPriority(Priority value) { priority.set(value); }
    public ObjectProperty<Priority> priorityProperty() { return priority; }
}
