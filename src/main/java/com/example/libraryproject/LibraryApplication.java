package com.example.libraryproject;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
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
        VBox studentsContent = createStudentsContent();
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
        HBox studentsContent = new HBox(30, leftSide, rightSide);
        studentsContent.setAlignment(Pos.CENTER);

        VBox mainStudentsContent = new VBox(10, studentManagementBox, studentsContent);
        mainStudentsContent.setAlignment(Pos.CENTER);

        return mainStudentsContent;
    }

    private VBox createInventoryContent() {
        // Dropdown
        ComboBox<String> inventoryDropdown = new ComboBox<>();
        inventoryDropdown.getItems().addAll("Item Code", "Author/Director", "Title Keyword", "Desc. Keyword");
        inventoryDropdown.setValue("Search By:");

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Enter Value");

        // Search button
        Button searchButton = new Button("Search");
        // Search functionality
        searchButton.setOnAction(event -> {
            String searchText = searchBar.getText();
            // Add search logic here
        });

        // Aligning the dropdown, search bar, and search button horizontally
        HBox searchArea = new HBox(10, inventoryDropdown, searchBar, searchButton);
        searchArea.setAlignment(Pos.CENTER);

        // Left Side
        VBox leftSide = new VBox();
        leftSide.setPadding(new Insets(10));
        leftSide.setSpacing(10);
        leftSide.setStyle("-fx-background-color: lightgray;");
        leftSide.setPrefWidth(300);

        // Right Side
        VBox rightSide = new VBox();
        rightSide.setPadding(new Insets(10));
        rightSide.setSpacing(10);
        rightSide.setStyle("-fx-background-color: lightgray;");
        rightSide.setPrefWidth(600);

        // Title "Inventory"
        Label titleLabel = new Label("Inventory");
        HBox inventoryTitleBox = new HBox((titleLabel));
        inventoryTitleBox.setAlignment(Pos.CENTER);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setFont(new Font("Arial", 20)); // Set the font size

        // Table for Code and Title
        TableView<InventoryItem> table = new TableView<>();
        TableColumn<InventoryItem, String> codeColumn = new TableColumn<>("Code");
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<InventoryItem, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        table.getColumns().setAll(codeColumn, titleColumn);

        // Set the columns to take up equal space
        codeColumn.prefWidthProperty().bind(table.widthProperty().divide(2));
        titleColumn.prefWidthProperty().bind(table.widthProperty().divide(2));

        // Dummy data
        ObservableList<InventoryItem> data = FXCollections.observableArrayList(
                new InventoryItem("C123", "Item 1"),
                new InventoryItem("C456", "Item 2"),
                new InventoryItem("C789", "Item 3")
        );

        // Setting the dummy data to the table
        table.setItems(data);

        // Check boxes with labels
        CheckBox bookCheckBox = new CheckBox("Book");
        CheckBox documentaryCheckBox = new CheckBox("Documentary");
        CheckBox availableCheckBox = new CheckBox("Available");

        // VBox for the left side with the checkboxes
        VBox leftVBox = new VBox(5, bookCheckBox, documentaryCheckBox, availableCheckBox);
        leftVBox.setAlignment(Pos.CENTER_LEFT);

        // Define a specific width for the left VBox
        leftVBox.setPrefWidth(150);

        // Button for the right side
        Button newItemButton = new Button("New Item");
        newItemButton.setPrefHeight(50);
        newItemButton.setPrefWidth(90);

        // HBox for the entire lower part
        HBox lowerHBox = new HBox(10, leftVBox, newItemButton);
       // lowerHBox.setAlignment(Pos.CENTER);

        leftSide.getChildren().setAll(inventoryTitleBox, table, lowerHBox);

        // Main content layout (with equal widths for left and right sides)
        HBox mainContent = new HBox(leftSide, rightSide);
        mainContent.setSpacing(20); // Spacing between left and right sides
        mainContent.setPadding(new Insets(25));

        // Combining the search area with the main content
        VBox fullContent = new VBox(10, searchArea, mainContent);
        fullContent.setPadding(new Insets(20));

        return fullContent;
    }

    public static class InventoryItem {
        private final SimpleStringProperty code;
        private final SimpleStringProperty title;

        public InventoryItem(String code, String title) {
            this.code = new SimpleStringProperty(code);
            this.title = new SimpleStringProperty(title);
        }

        public String getCode() { return code.get(); }
        public String getTitle() { return title.get(); }
    }
}