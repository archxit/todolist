import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

public class TaskView extends HBox {
    private final ToggleButton statusToggle;
    private final Label titleLabel;
    private final Button editButton;
    private final Button deleteButton;
    private final TodoController controller;
    private Runnable onDelete;
    private Runnable onEdit;

    public TaskView(Task<Void> task, TodoController controller) {
        this.controller = controller;
        setSpacing(10);
        getStyleClass().add("task-view");
        setAlignment(Pos.CENTER_RIGHT);

        // Status toggle with custom graphics
        statusToggle = new ToggleButton();
        statusToggle.getStyleClass().add("status-toggle");
        
        Circle uncheckedCircle = new Circle(8);
        uncheckedCircle.getStyleClass().add("unchecked-circle");
        
        Circle checkedCircle = new Circle(8);
        checkedCircle.getStyleClass().add("checked-circle");
        
        statusToggle.setGraphic(uncheckedCircle);
        statusToggle.selectedProperty().bindBidirectional(task.completedProperty());
        
        statusToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            statusToggle.setGraphic(isSelected ? checkedCircle : uncheckedCircle);
            updateTaskStyle(isSelected);

            controller.updateProgress();
        });

        titleLabel = new Label();
        titleLabel.setId("titleLabel");
        titleLabel.textProperty().bind(task.titleProperty());
        titleLabel.setMaxWidth(200);  // Limit width so long text wraps
        titleLabel.setWrapText(true);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        
        // Spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Edit button
        editButton = createIconButton("edit-btn.png","edit-button");

        // Delete button
        deleteButton = createIconButton("trash.png","delete-button");

        getChildren().addAll(statusToggle, titleLabel, editButton, deleteButton);
        
        updateTaskStyle(task.isCompleted());
    }

    private Button createIconButton(String img, String styleClass) {
        Button button = new Button();
            
        // Load the image and set it to an ImageView
        Image image = new Image(img);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20); // Set width for icon
        imageView.setFitHeight(20); // Set height for icon

        button.setGraphic(imageView);
        button.getStyleClass().add(styleClass);
        button.setFocusTraversable(false);
        return button;
    }

    private void updateTaskStyle(boolean completed) {
        titleLabel.getStyleClass().removeAll("completed-task", "pending-task");
        titleLabel.getStyleClass().add(completed ? "completed-task" : "pending-task");
    }

    public void setOnDelete(Runnable handler) {
        deleteButton.setOnAction(e -> {
            if (handler != null) handler.run();
        });
    }

    public void setOnEdit(Runnable handler) {
        editButton.setOnAction(e -> {
            if (handler != null) handler.run();
        });
    }
}