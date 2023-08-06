package com.example.libraryproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LibraryApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Library Management Title
        Label titleLabel = new Label("Library Management");
        titleLabel.setStyle("-fx-font-size: 24px;");
        HBox titleBar = new HBox(titleLabel);
        titleBar.setPadding(new Insets(10));
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setStyle("-fx-background-color: #e0e0e0;");

        // 3 Button Menu

        ToggleButton loansCTA = new ToggleButton("Loans");
        ToggleButton peopleCTA = new ToggleButton("People");
        ToggleButton inventoryCTA = new ToggleButton("Inventory");

        ToggleGroup menuGroup = new ToggleGroup();
        loansCTA.setToggleGroup(menuGroup);
        peopleCTA.setToggleGroup(menuGroup);
        inventoryCTA.setToggleGroup(menuGroup);

        HBox buttonBox = new HBox(10, loansCTA, peopleCTA, inventoryCTA); // 10 is the spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);

        // Combine title and buttons into top portion
        VBox titleBox = new VBox(10, titleBar, buttonBox);

        // Listener to handle toggle button changes
        VBox contentArea = new VBox(); // This will hold the content for the selected toggle menu button
        menuGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            contentArea.getChildren().clear();
            if (newValue == loansCTA) {
                contentArea.getChildren().add(createLoansContent());
            } else if (newValue == peopleCTA) {
                contentArea.getChildren().add(createPeopleContent());
            } else if (newValue == inventoryCTA) {
                contentArea.getChildren().add(createInventoryContent());
            }
        });

        loansCTA.setSelected(true);

        // Main layout
        VBox mainLayout = new VBox(10, titleBox, contentArea);

        // Scene
        Scene scene = new Scene(mainLayout, 1000, 800);
        primaryStage.setTitle("Library Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private VBox createLoansContent() {
        // Tabs for Standard and Comprehensive
        TabPane tabPane = new TabPane();
        Tab standardTab = new Tab("Standard");
        Tab comprehensiveTab = new Tab("Comprehensive");
        tabPane.getTabs().addAll(standardTab, comprehensiveTab);

        // Search
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");
        Button searchButton = new Button("Search");

        // Search button functionality:
        searchButton.setOnAction(event -> {
            String searchText = searchBar.getText();
            boolean found = false;
            String line;

            try (BufferedReader br = new BufferedReader(new FileReader("path/to/loans.csv"))) {
                while ((line = br.readLine()) != null) {
                    // Split the line by comma and check if it contains the search text
                    String[] values = line.split(",");
                    for (String value : values) {
                        if (value.equals(searchText)) {
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!found) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alert");
                alert.setHeaderText("Bronco ID not found in database");
                alert.setContentText("Please check to make sure the ID is correct");
                alert.showAndWait();
            }
        });

        HBox searchBox = new HBox(10, searchBar, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        // Main content layout
        VBox mainContent = new VBox(10, tabPane, searchBox);

        return mainContent;
    }

    private VBox createPeopleContent() {
        TabPane subTabs = new TabPane();
        Tab studentsTab = new Tab("Students");
        Tab authorsTab = new Tab("Authors");
        Tab producersTab = new Tab("Producers");

        // Content for the Students tab
        VBox studentsContent = createStudentsContent(); // This method should be defined to create the students' content
        studentsTab.setContent(studentsContent);

        // TODO...
        // authorsTab.setContent(createAuthorsContent());
        // producersTab.setContent(createProducersContent());

        subTabs.getTabs().addAll(studentsTab, authorsTab, producersTab);

        VBox peopleContent = new VBox(subTabs); // Wrap the TabPane in a VBox
        return peopleContent;
    }

    private VBox createStudentsContent() {

        Label studentManagementLabel = new Label("Student Management");
        studentManagementLabel.setStyle("-fx-font-size: 20px;");
        HBox studentManagementBox = new HBox(studentManagementLabel);
        studentManagementBox.setAlignment(Pos.CENTER);
        Insets padding = new Insets(20, 0, 0, 0);
        studentManagementBox.setPadding(padding);

        // Left side content
        VBox leftSide = new VBox(10);
        leftSide.setPadding(new Insets(10));
        leftSide.setStyle("-fx-background-color: lightgray;");
        leftSide.setPrefWidth(400);

        // Student List title
        Label studentListLabel = new Label("Student List");
        studentListLabel.setStyle("-fx-font-size: 16px;");
        HBox studentListTitleBox = new HBox(studentListLabel);
        studentListTitleBox.setAlignment(Pos.CENTER);

        ComboBox<String> filterDropdown = new ComboBox<>();
        filterDropdown.getItems().addAll("BroncoID", "Name", "Course");
        filterDropdown.setValue("Name"); // Set the default value to "Name"

        TextField searchBar = new TextField();
        Button filterButton = new Button("Filter");

        // Horizontally align dropdown, search bar, and filter button
        HBox filterBar = new HBox(10, filterDropdown, searchBar, filterButton);
        filterBar.setAlignment(Pos.CENTER);

        ListView<String> studentList = new ListView<>();
        // Add sample students to list
        // TODO: (replace with actual data)
        studentList.getItems().addAll("Student 1", "Student 2", "Student 3");
        Button addNewStudentButton = new Button("Add New Student");

        leftSide.getChildren().addAll(studentListTitleBox, filterBar, studentList, addNewStudentButton);

        // Right side content
        VBox rightSide = new VBox(10);
        rightSide.setPadding(new Insets(10));
        rightSide.setStyle("-fx-background-color: lightgray;");
        rightSide.setPrefWidth(400);

        // Don't show until a student is selected
        rightSide.setVisible(false);

        // Student  title
        Label studentTitleLabel = new Label("Student");
        studentTitleLabel.setStyle("-fx-font-size: 16px;");
        HBox studentTitleBox = new HBox(studentTitleLabel);
        studentTitleBox.setAlignment(Pos.CENTER);

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField();
        Label broncoIdLabel = new Label("Bronco ID");
        TextField broncoIdField = new TextField();

        Label courseLabel = new Label("Course");
        ComboBox<String> courseDropdown = new ComboBox<>();
        courseDropdown.getItems().addAll("BA", "BS", "CS");
        courseDropdown.setValue("CS"); // Default
        HBox courseBox = new HBox(10, courseLabel, courseDropdown);
        courseBox.setAlignment(Pos.CENTER_LEFT);

        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        updateButton.setMinHeight(45);
        deleteButton.setMinHeight(30);
        VBox buttonBox = new VBox(10, updateButton, deleteButton);
        HBox buttonContainer = new HBox(buttonBox);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(20, 0 ,0, 0));

        rightSide.getChildren().addAll(studentTitleBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                broncoIdLabel, broncoIdField, courseBox, buttonContainer);

        // Add a listener to the student list to show the right side when a student is selected
        studentList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                rightSide.setVisible(true);
                // Populate fields based on the selected student here
            }
        });

        // Main People content
        HBox studentsContent = new HBox(30, leftSide, rightSide); // 10 is the spacing between elements
        studentsContent.setAlignment(Pos.CENTER);

        VBox mainStudentsContent = new VBox(10, studentManagementBox, studentsContent);
        mainStudentsContent.setAlignment(Pos.CENTER);

        return mainStudentsContent;
    }

    private HBox createInventoryContent() {
        // Dropdown
        ComboBox<String> inventoryDropdown = new ComboBox<>();
        inventoryDropdown.getItems().addAll("Item Code", "Author/Director", "Title Keyword", "Desc. Keyword");
        inventoryDropdown.setValue("Search By:");

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search inventory...");

        // Search button
        Button searchButton = new Button("Search");

        // Horizontal container for the dropdown, search bar, and search button
        HBox inventoryContent = new HBox(10, inventoryDropdown, searchBar, searchButton);
        inventoryContent.setAlignment(Pos.CENTER);
        inventoryContent.setPadding(new Insets(10));

        return inventoryContent;
    }
}
