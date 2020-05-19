/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RegexValidator;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Roles;
import trip.be.Task;
import trip.be.CountedTime;
import trip.be.Timer;
import trip.gui.TRiP;
import trip.gui.models.ProjectModel;
import trip.gui.models.TaskModel;
import trip.utilities.JFXAlert;
import trip.utilities.TimeConverter;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class MainUserViewController implements Initializable {
    
    private ProjectModel projectModel = new ProjectModel();
    private TaskModel taskModel = new TaskModel();
    
    private Timer timer = new Timer();
    private Employee loggedUser = LoginController.loggedUser;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    @FXML
    private ComboBox<Project> projectComboBox;
    @FXML
    private TableView<Task> taskList;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> timeColumn;
    @FXML
    private TableColumn<CountedTime, String> billableColumn;
    @FXML
    private TableView<CountedTime> taskTimerList;
    @FXML
    private TableColumn<CountedTime, String> durationColumn;
    @FXML
    private TableColumn<CountedTime, String> startColumn;
    @FXML
    private TableColumn<CountedTime, String> endColumn;
    @FXML
    private TextField newTaskTitle;
    @FXML
    private JFXButton startTimer;
    @FXML
    private GridPane addTiimerPane;
    @FXML
    private Label timerLabel;
    @FXML
    private JFXButton stopTimer;
    @FXML
    private JFXButton cancelTimer;
    @FXML
    private ComboBox<Task> tasks;
    @FXML
    private JFXDatePicker dateStart;
    @FXML
    private JFXDatePicker dateEnd;
    @FXML
    private JFXTimePicker timeEnd;
    @FXML
    private JFXTimePicker timeStart;
    @FXML
    private JFXTextField timerField;
    @FXML
    private JFXButton addTime;
    @FXML
    private GridPane startTime;
    @FXML
    private Button startButton;
    @FXML
    private Button addButton;
    @FXML
    private JFXButton addTask;
    @FXML
    private StackPane stackPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        nameColumn.setCellValueFactory((data) -> {
            Task task = data.getValue();
            return new SimpleStringProperty(task.getName());
        });
        
        timeColumn.setCellValueFactory((data) -> {
            Task task = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertSecondsToString(task.getTotalTime()));
        });
        
        durationColumn.setCellValueFactory((data) -> {
            CountedTime countedTime = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertSecondsToString(countedTime.getTime()));
        });
        
        startColumn.setCellValueFactory((data) -> {
            CountedTime countedTime = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertDateToString(countedTime.getStartTime()));
        });
        
        endColumn.setCellValueFactory((data) -> {
            CountedTime countedTime = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertDateToString(countedTime.getStopTime()));
        });
        
        billableColumn.setCellValueFactory((data) -> {
            CountedTime countedTime = data.getValue();
            String isBillable;
            if (countedTime.isBillable()) {
                isBillable = "✓";
            } else {
                isBillable = "✕";
            }
            return new SimpleStringProperty(isBillable);
        });
        
        newTaskTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            decideTimerEnabled();
        });
        
        timeStart.set24HourView(true);
        timeEnd.set24HourView(true);
        
        RegexValidator regex = new RegexValidator();
        regex.setRegexPattern("^([0-9][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$");
        regex.setMessage("Input is not a valid time");
        timerField.getValidators().add(regex);
        
        timerField.textProperty().addListener((Observable, oldValue, newValue) -> {
            changeTime();
            decideAddTimeEnabled();
            timerField.validate();
        });
        
        loadProjects();
        
        final Tooltip tooltipButton = new Tooltip();
        tooltipButton.setText("Add Time");
        addButton.setTooltip(tooltipButton);
        tooltipButton.setStyle("-fx-font-size: 15px; -fx-background-color: rgb(154, 128, 254, .8);");
        
        final Tooltip tooltipButton2 = new Tooltip();
        tooltipButton2.setText("Track Time");
        startButton.setTooltip(tooltipButton2);
        tooltipButton2.setStyle("-fx-font-size: 15px; -fx-background-color: rgb(154, 128, 254, .8);");
        
        executor.submit(update());
    }

    /**
     * This method runs when a project is openened in the MainAdminViewController. This method loads and displays the selected project.
     *
     * @param project The selected project to be loaded.
     */
    public void setAdmin(Project project) {
        try {
            projectComboBox.getSelectionModel().select(project);
            taskList.setItems(taskModel.loadTasks(loggedUser.getId(), project.getId()));
            tasks.setItems(taskModel.loadTasks(loggedUser.getId(), project.getId()));
            tasks.getSelectionModel().select(0);
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error while attempting to set Admin.");
        }
    }

    /**
     * This method runs when a employee is selected from the AdminStatisticsViewController. This method loads the time tracking as the selected user, disabling the timer option. This makes it possible for an admin to check individual users- and the registered time.
     *
     * @param employee The employee the view should be based upon.
     */
    public void setEmployee(Employee employee) {
        loggedUser = employee;
        timerLabel.setVisible(false);
        startTimer.setVisible(false);
        addTask.setVisible(true);
        loadProjects();
    }

    /**
     * Loads all active projects or the projects which the logged user is assigned to, based on the role of the user.
     */
    private void loadProjects() {
        try {
            if (loggedUser.getRole() == Roles.ADMIN) {
                projectComboBox.setItems(projectModel.loadAllActiveProjects());
            } else {
                projectComboBox.setItems(projectModel.loadAllEmployeeProjects(loggedUser.getId()));
            }
            
            projectComboBox.getSelectionModel().select(0);
            if (projectComboBox.getValue() != null) {
                taskList.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getValue().getId()));
                tasks.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getValue().getId()));
                tasks.getSelectionModel().select(0);
            }
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error while attempting to load Projects.");
        }
    }

    /**
     * Displays the tasks of the selected project, displaying the name and the total registered time by the user.
     *
     * @param event
     */
    @FXML
    private void switchProject(ActionEvent event) {
        try {
            if (projectComboBox.getValue() != null) {
                taskList.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
                tasks.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
                tasks.getSelectionModel().select(0);
                decideTimerEnabled();
            }
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error while attempting to switch Project.");
        }
    }

    /**
     * Displays the logged time for the task clicked on.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void showTime(MouseEvent event) {
        
        if (!taskList.getSelectionModel().isEmpty()) {
            taskTimerList.setItems(taskList.getSelectionModel().getSelectedItem().getCountedTime());
            tasks.getSelectionModel().select(taskList.getSelectionModel().getSelectedItem());
            decideTimerEnabled();
            decideAddTimeEnabled();
        }
        
        if (event.getClickCount() > 1 & !taskList.getSelectionModel().isEmpty() & !event.isConsumed()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(TRiP.class.getResource("views/UpdateTaskFormView.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("TRiP");
                stage.getIcons().add(new Image(TRiP.class.getResourceAsStream("images/time.png")));
                stage.setResizable(false);
                UpdateTaskFormController controller = fxmlLoader.getController();
                controller.setTask(updateView(), taskList.getSelectionModel().getSelectedItem());
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                JFXAlert.openError(stackPane, "Error loading update form.");
            }
        }
    }

    /**
     * Adds a task to the selected project and displays it in the list of tasks.
     *
     * @return The id of the newly inserted task.
     */
    private Task addTask() {
        try {
            String taskName = newTaskTitle.getText().trim();
            Task task = taskModel.addTask(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId(), taskName);
            updateView().start();
            return task;
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error while attempting to add Task.");
        }
        return null;
    }

    /**
     * Starts the timer for the selected or just created task.
     *
     * @param event
     */
    @FXML
    private void startTimer(ActionEvent event) {
        
        if (!newTaskTitle.getText().trim().isEmpty()) {
            Task task = addTask();
            newTaskTitle.clear();
            timer.startTimer(task, timerLabel);
        } else {
            timer.startTimer(taskList.getSelectionModel().getSelectedItem(), timerLabel);
        }
        
        startTimer.setVisible(false);
        stopTimer.setVisible(true);
        cancelTimer.setVisible(true);
        
        setupCloseRequest();
    }

    /**
     * Event handler for the stop timr button. Stops the timer and saves the registered time.
     *
     * @param event
     */
    @FXML
    private void stopTimer(ActionEvent event) {
        try {
            timer.stopTimer();
            taskModel.saveTimeForTask(timer.getTask(), timer.getTime(), timer.getStartTime(), timer.getStopTime());
            startTimer.setVisible(true);
            stopTimer.setVisible(false);
            cancelTimer.setVisible(false);
            updateView().start();
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error stopping timer.");
        }
    }

    /**
     * Event handler for the cancel button. Stops the timer without saving the registered time.
     *
     * @param event
     */
    @FXML
    private void cancelTime(ActionEvent event) {
        timer.stopTimer();
        startTimer.setVisible(true);
        stopTimer.setVisible(false);
        cancelTimer.setVisible(false);
    }

    /**
     * Event handler for the add time button. Takes the entered data and saves it to the database.
     *
     * @param event
     */
    @FXML
    private void addTime(ActionEvent event) {
        
        Task selectedTask = tasks.getSelectionModel().getSelectedItem();
        Task task = (selectedTask != null) ? selectedTask : taskList.getSelectionModel().getSelectedItem();
        
        int time = TimeConverter.convertStringToSeconds(timerField.getText());
        LocalDate localStart = dateStart.getValue();
        LocalDate localStop = dateEnd.getValue();
        
        LocalTime start = timeStart.getValue();
        LocalTime stop = timeEnd.getValue();
        
        Instant instantStart = localStart.atTime(start).atZone(ZoneId.systemDefault()).toInstant();
        Instant instantEnd = localStop.atTime(stop).atZone(ZoneId.systemDefault()).toInstant();
        
        Date startDate = Date.from(instantStart);
        Date endDate = Date.from(instantEnd);
        
        try {
            taskModel.saveTimeForTask(task, time, startDate, endDate);
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error saving time for task.");
        }
        
        timerField.setText("00:00:00");
        dateStart.setValue(null);
        dateEnd.setValue(null);
        timeStart.setValue(null);
        timeEnd.setValue(null);
        updateView().start();
    }

    /**
     * Creates a new thread that updates the view based on what is selected and currently displaying.
     *
     * @return the update thread to be executed.
     */
    private Thread updateView() {
        
        Thread updateThread = new Thread(() -> {
            
            Platform.runLater(() -> {
                try {
                    if (!taskList.getSelectionModel().isEmpty()) {
                        
                        Task selectedTask = taskList.getSelectionModel().getSelectedItem();
                        taskList.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
                        taskList.refresh();
                        taskList.getSelectionModel().select(selectedTask);
                        taskTimerList.getItems().clear();
                        
                        if (taskList.getSelectionModel().getSelectedItem() != null) {
                            taskTimerList.setItems(taskList.getSelectionModel().getSelectedItem().getCountedTime());
                        }
                        taskTimerList.refresh();
                    } else {
                        taskList.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
                        taskList.refresh();
                    }
                    Task selectedItem = tasks.getSelectionModel().getSelectedItem();
                    tasks.setItems(taskList.getItems());
                    
                    if (taskList.getItems().contains(selectedItem)) {
                        tasks.getSelectionModel().select(selectedItem);
                    } else {
                        tasks.getSelectionModel().selectLast();
                    }
                } catch (SQLException ex) {
                    JFXAlert.openError(stackPane, "Error updating view.");
                }
            });
        });
        return updateThread;
    }

    /**
     * Disables or enables the start timer button based on what information is entered.
     */
    private void decideTimerEnabled() {
        if (!taskList.getSelectionModel().isEmpty()) {
            startTimer.setDisable(false);
        } else if (!newTaskTitle.getText().trim().isEmpty() && projectComboBox.getSelectionModel().getSelectedItem() != null) {
            startTimer.setDisable(false);
            addTask.setDisable(false);
        } else {
            startTimer.setDisable(true);
            addTask.setDisable(true);
        }
    }

    /**
     * Disables or enables the add time button based on what information is entered.
     */
    private void decideAddTimeEnabled() {
        if ((tasks.getSelectionModel().getSelectedItem() != null || !taskList.getSelectionModel().isEmpty()) && dateStart.getValue() != null && dateEnd.getValue() != null
                && timeStart.getValue() != null && timeEnd.getValue() != null && timerField.validate()) {
            addTime.setDisable(false);
        } else {
            addTime.setDisable(true);
        }
    }

    /**
     * Event handler for the taskTimerList. If a time is selected by double-clicking, the selected time will be opened in the UpdateTaskTimeForm so that the user can change the saved data if changes needs to be made.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void openTaskTime(MouseEvent event) {
        if (event.getClickCount() > 1 & !taskTimerList.getSelectionModel().isEmpty() & !event.isConsumed()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(TRiP.class.getResource("views/UpdateTasktimeFormView.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("TRiP");
                stage.getIcons().add(new Image(TRiP.class.getResourceAsStream("images/time.png")));
                stage.setResizable(false);
                UpdateTasktimeFormController controller = fxmlLoader.getController();
                controller.setCountedTime(updateView(), taskTimerList.getSelectionModel().getSelectedItem());
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                JFXAlert.openError(stackPane, "Error opening update form.");
            }
        }
    }

    /**
     * Validates whether or not the add time button is enabled or disabled when a change happens.
     *
     * @param event
     */
    @FXML
    private void validateAddTask(ActionEvent event) {
        calculateTime();
        decideAddTimeEnabled();
    }

    /**
     * Hides the pane for adding time and displays the pane for starting the timer instead.
     *
     * @param event
     */
    @FXML
    private void switchToStart(MouseEvent event) {
        addTiimerPane.setVisible(false);
        startTime.setVisible(true);
        startButton.setVisible(false);
        addButton.setVisible(true);
        
    }

    /**
     * Hides the pane for starting the timer and displays the pane for adding time instead.
     *
     * @param event
     */
    @FXML
    private void switchToAdd(MouseEvent event) {
        addTiimerPane.setVisible(true);
        startTime.setVisible(false);
        startButton.setVisible(true);
        addButton.setVisible(false);
    }

    /**
     * Event handler for the "add task" buttonn. Gives an admin the ability to add tasks to existing users.
     *
     * @param event
     */
    @FXML
    private void addTaskToUser(ActionEvent event) {
        addTask();
    }

    /**
     * Calculates the time between the selected date and time in order to display this in the timerField for a precise logging time.
     */
    private void calculateTime() {
        if (dateStart.getValue() != null && dateEnd.getValue() != null && timeStart.getValue() != null && timeEnd.getValue() != null) {
            
            LocalDate localStart = dateStart.getValue();
            LocalDate localStop = dateEnd.getValue();
            
            LocalTime start = timeStart.getValue();
            LocalTime stop = timeEnd.getValue();
            
            Instant instantStart = localStart.atTime(start).atZone(ZoneId.systemDefault()).toInstant();
            Instant instantEnd = localStop.atTime(stop).atZone(ZoneId.systemDefault()).toInstant();
            
            Date startDate = Date.from(instantStart);
            Date endDate = Date.from(instantEnd);
            
            int seconds = (int) (endDate.getTime() - startDate.getTime()) / 1000;
            seconds = (seconds > 0) ? seconds : 0;
            
            timerField.clear();
            timerField.setText(TimeConverter.convertSecondsToString(seconds));
        }
    }

    /**
     * Calculates what the date and time fields should be to reflect the change made to the timerField in order to display a precise logging time.
     */
    private void changeTime() {
        if (timerField.validate()) {
            if (dateStart.getValue() == null) {
                dateStart.setValue(LocalDate.now());
            }
            if (timeStart.getValue() == null) {
                timeStart.setValue(LocalTime.now().withSecond(0));
            }
            
            int seconds = TimeConverter.convertStringToSeconds(timerField.getText());
            
            LocalDate localStart = dateStart.getValue();
            LocalTime start = timeStart.getValue();
            
            LocalDateTime dateTime = LocalDateTime.of(localStart, start);
            
            Instant instantEnd = dateTime.atZone(ZoneId.systemDefault()).toInstant().plusSeconds(seconds);
            
            dateEnd.setValue(instantEnd.atZone(ZoneId.systemDefault()).toLocalDate());
            timeEnd.setValue(instantEnd.atZone(ZoneId.systemDefault()).toLocalTime());
        }
    }

    /**
     * Adds a on close request to the stage which will cancel the timer if the stage is closed without disabling the timer. This insures that the timer does not run as a thread in the background when the application is closed.
     */
    public void setupCloseRequest() {
        
        Stage appStage = (Stage) taskList.getScene().getWindow();
        if (appStage.getOnCloseRequest() == null) {
            appStage.setOnCloseRequest((e) -> {
                executor.shutdownNow();
                System.out.println("Closing thread");
                if (timer.isEnabled()) {
                    timer.stopTimer();
                    System.out.println("Closed");
                }
            });
        }
    }

    /**
     * Creates a new thread that automatically updates the view every 15 seconds in case a change should happen.
     *
     * @return The thread to be executed.
     */
    private Thread update() {
        
        Thread thread = new Thread(() -> {
            
            try {
                TimeUnit.SECONDS.sleep(1);
                setupCloseRequest();
                while (taskList.getScene().getWindow() != null) {
                    
                    TimeUnit.SECONDS.sleep(15);
                    
                    ObservableList<Project> projects;
                    if (loggedUser.getRole() == Roles.ADMIN) {
                        projects = projectModel.loadAllActiveProjects();
                    } else {
                        projects = projectModel.loadAllEmployeeProjects(loggedUser.getId());
                    }
                    Project project = projectComboBox.getValue();
                    Task task = tasks.getValue();
                    Platform.runLater(() -> {
                        projectComboBox.setItems(projects);
                    });
                    
                    updateView().start();
                    
                    Platform.runLater(() -> {
                        if (projectComboBox.getItems().contains(project)) {
                            projectComboBox.getSelectionModel().select(project);
                        } else {
                            projectComboBox.getSelectionModel().select(0);
                        }
                        if (tasks.getItems().contains(task)) {
                            tasks.getSelectionModel().select(task);
                        }
                    });
                }
            } catch (SQLException ex) {
                JFXAlert.openError(stackPane, "Error updating view");
            } catch (InterruptedException ex) {
                System.out.println("Update thread cancelled");
            } finally {
                timer.stopTimer();;
            }
        });
        return thread;
    }
    
}
