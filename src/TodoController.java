// TodoController.java
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.util.concurrent.*;

public class TodoController {
    @FXML private TextField taskInput;
    @FXML private VBox taskContainer;
    @FXML private Label progressLabel;
    @FXML private Button addButton;
    
    private final ObservableList<Task<Void>> tasks = FXCollections.observableArrayList();
    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutor;

    public TodoController() {
        executorService = Executors.newFixedThreadPool(2);
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        setupAutoSave();
    }

    @FXML
    public void initialize() {
        loadTasks();
        setupTaskList();
        setupAddButton();
    }

    private void setupAddButton() {
        // Add the plus icon to the button
        ImageView addBtn = new ImageView("add-btn.png");
        addBtn.setFitWidth(20);
        addBtn.setFitHeight(20);
        addBtn.setId("add-icon");

        Button addbtn = new Button();
        addbtn.setId("add-button");
        addbtn.setGraphic(addbtn);
    }

    private void setupAutoSave() {
        scheduledExecutor.scheduleAtFixedRate(this::saveTasksToStorage,10, 10, TimeUnit.SECONDS);
    }

    private void setupTaskList() {
        tasks.addListener((javafx.collections.ListChangeListener<Task<Void>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::createTaskView);
                }
                updateProgress();
            }
        });
    }

    @FXML
    private void handleAddTask() {
        String taskText = taskInput.getText().trim();
        if (!taskText.isEmpty()) {
            Task<Void> newTask = new Task<>(taskText);
            tasks.add(newTask);
            taskInput.clear();
            
            // Run on background thread
            executorService.submit(() -> {
                // Simulate some background processing
                try {
                    Thread.sleep(100);
                    Platform.runLater(this::updateProgress);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    private void createTaskView(Task<Void> task) {
        TaskView taskView = new TaskView(task, this);
        taskView.setOnDelete(() -> {
            tasks.remove(task);
            taskContainer.getChildren().remove(taskView);
            updateProgress();
        });
        taskView.setOnEdit(() -> {
            TextInputDialog dialog = new TextInputDialog(task.getTitle());
            ImageView icon = new ImageView("mainicon.png");
            dialog.setGraphic(icon);
            dialog.setTitle("Edit Task");
            dialog.setHeaderText("Edit your task title");
            dialog.setContentText("Task Title:");

            dialog.showAndWait().ifPresent(newTitle -> {task.setTitle(newTitle);updateProgress();
            });
        });
        taskContainer.getChildren().add(taskView);
    }

    public void updateProgress() {
        int completed = (int) tasks.stream()
            .filter(Task::isCompleted)
            .count();
        Platform.runLater(() -> progressLabel.setText(completed + "/" + tasks.size()));
    }

    private void saveTasksToStorage() {
        // Implement actual storage logic here
        System.out.println("Auto-saving " + tasks.size() + " tasks");
    }

    private void loadTasks() {
        // Implement actual loading logic here
        System.out.println("Loading tasks...");
    }

    public void shutdown() {
        executorService.shutdown();
        scheduledExecutor.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
            if (!scheduledExecutor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                scheduledExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            scheduledExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}