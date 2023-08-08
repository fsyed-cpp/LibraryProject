package com.example.libraryproject;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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

        HBox buttonBox = new HBox(10, loansCTA, peopleCTA, inventoryCTA);
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
        searchBar.setPromptText("Bronco ID");
        Button searchButton = new Button("Search");

        HBox searchBox = new HBox(10, searchBar, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        // Main content layout
        VBox mainContent = new VBox(10, tabPane, searchBox);

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

            // TESTING !!!! REMOVE WHEN WE ADD CSV DATA FOR LOANS !!
            found = true;

            if (!found) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alert");
                alert.setHeaderText("Bronco ID not found in database");
                alert.setContentText("Please check to make sure the ID is correct");
                alert.showAndWait();
            } else {
                // Show loan content
                VBox loanDetailContent = createLoanDetailContent();
                mainContent.getChildren().add(loanDetailContent);
            }
        });

        return mainContent;
    }

    private VBox createLoanDetailContent() {
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

        // Title "<Person Name?"
        Label titleLabel = new Label("John Smith"); // TODO: Change to actual name
        HBox loanTitleBox = new HBox((titleLabel));
        loanTitleBox.setAlignment(Pos.CENTER);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setFont(new Font("Arial", 20)); // Set the font size

        // Table for Code and Title
        TableView<LoanItem> table = new TableView<>();
        TableColumn<LoanItem, String> loanColumn = new TableColumn<>("Loan #");
        loanColumn.setCellValueFactory(new PropertyValueFactory<>("loan"));
        TableColumn<LoanItem, String> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        table.getColumns().setAll(loanColumn, dueDateColumn);

        // Set the columns to take up equal space
        loanColumn.prefWidthProperty().bind(table.widthProperty().divide(2));
        dueDateColumn.prefWidthProperty().bind(table.widthProperty().divide(2));

        // Dummy data
        ObservableList<LoanItem> data = FXCollections.observableArrayList(
                new LoanItem("0244L", "02/27/23"),
                new LoanItem("2389L", "03/25/23")
        );

        // Setting the dummy data to the table
        table.setItems(data);

        // Button to add new loan
        Button addNewLoanButton = new Button("New Loan");
        addNewLoanButton.prefWidth(150);
        addNewLoanButton.prefHeight(80);
        addNewLoanButton.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox(5, addNewLoanButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPrefWidth(200);
        buttonBox.setPrefHeight(100);

        leftSide.getChildren().setAll(loanTitleBox, table, buttonBox);

        // RIGHT SIDE !!

        Label buttonTitleLabel = new Label("Book");
        HBox buttonTitleBox = new HBox((buttonTitleLabel));
        buttonTitleBox.setAlignment(Pos.CENTER);
        buttonTitleLabel.setAlignment(Pos.CENTER);
        buttonTitleLabel.setFont(new Font("Arial", 20));

        // Create a GridPane for the fields
        GridPane fieldsGrid = new GridPane();
        fieldsGrid.setHgap(5);
        fieldsGrid.setVgap(10);

        // Code field
        TextField codeField = new TextField();
        fieldsGrid.addRow(0, new Label("Code"), codeField);

        // Title field
        TextField titleField = new TextField();
        fieldsGrid.addRow(1, new Label("Title"), titleField);

        // Location field
        TextField locationField = new TextField();
        fieldsGrid.addRow(2, new Label("Location"), locationField);

        // Price/D field with dropdown
        ComboBox<String> priceDropdown = new ComboBox<>();
        priceDropdown.setValue("$1.20");
        // You can add items to the dropdown here
        fieldsGrid.addRow(3, new Label("Price/D"), priceDropdown);

        // Left VBox containing the fields
        VBox rightSideLeftVBox = new VBox(fieldsGrid);

        // Authors list
        ListView<String> authorsList = new ListView<>();
        authorsList.setPrefHeight(100);
        // add authors here...

        // + and - buttons
        Button addButton = new Button("+");
        Button removeButton = new Button("-");
        // HBox for + and - buttons
        HBox authorButtonsHBox = new HBox(5, addButton, removeButton);
        authorButtonsHBox.setAlignment(Pos.BOTTOM_RIGHT);

        // VBox for authors list and buttons
        VBox authorsVBox = new VBox(5, new Label("Authors"), authorsList, authorButtonsHBox);
        authorsVBox.setPadding(new Insets(-5));

        // GridPane containing left and right sections
        GridPane mainGrid = new GridPane();
        mainGrid.add(rightSideLeftVBox, 0, 0);
        mainGrid.add(authorsVBox, 1, 0);
        mainGrid.setHgap(110);

        // Create The Book Info section
        Label pagesLabel = new Label("Pages");
        TextField pagesTextField = new TextField();
        pagesTextField.setPrefWidth(100);

        Label pubDateLabel = new Label("Pub Date");
        DatePicker pubDateField = new DatePicker();
        pubDateField.setPrefWidth(120);

        Label publisherLabel = new Label("Publisher");
        TextField publisherTextField = new TextField();

        HBox firstHBox = new HBox(10, pagesLabel, pagesTextField, pubDateLabel, pubDateField, publisherLabel, publisherTextField);
        firstHBox.setAlignment(Pos.CENTER_LEFT);

        // Second HBox
        Label descriptionLabel = new Label("Description");
        TextField descriptionTextField = new TextField();
        descriptionTextField.setMaxWidth(Double.MAX_VALUE);

        HBox secondHBox = new HBox(10, descriptionLabel, descriptionTextField);
        secondHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(descriptionTextField, Priority.ALWAYS);

        // Main VBox
        VBox additionalBookInfo = new VBox(10, firstHBox, secondHBox);
        additionalBookInfo.setPadding(new Insets(50, 0, 0 ,0));

        // Copies
        Label copiesLabel = new Label("Copies");
        ComboBox<String> copiesDropdown = new ComboBox<>();
        copiesDropdown.getItems().addAll("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        copiesDropdown.setValue("3");
        VBox copies = new VBox(10, copiesLabel, copiesDropdown);

        // Borrowed / Overdue
        // TODO: Insert actual borrowed/overdue items from DB
        HBox borrowedBox = new HBox(10, new Label("Borrowed"), new Label("3"));
        HBox overdueBox = new HBox(10, new Label("Overdue"), new Label("  2"));
        VBox borrowedOverdueBox = new VBox(15, borrowedBox, overdueBox);

        // Waitlisted
        Label waitListedLabel = new Label("Waitlisted");
        ToggleButton yesButton = new ToggleButton("Yes");
        ToggleButton noButton = new ToggleButton("No");
        ToggleGroup waitlistedGroup = new ToggleGroup();
        yesButton.setToggleGroup(waitlistedGroup);
        noButton.setToggleGroup(waitlistedGroup);
        HBox waitlistedButtonBox = new HBox(0, yesButton, noButton);
        VBox waitlistedBox = new VBox(10.0, waitListedLabel, waitlistedButtonBox);

        // Combine copies, borrowed/overdue, and waitlisted into hbox
        HBox detailedBookInfo = new HBox(20, copies, borrowedOverdueBox, waitlistedBox);
        detailedBookInfo.setPadding(new Insets(50, 0, 0 ,0));

        // Update/Delete Buttons
        Button updateButton = new Button("Update");
        updateButton.setPrefWidth(150);
        updateButton.setPrefHeight(80);

        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(70);
        deleteButton.setPrefHeight(80);

        HBox updateDelBox = new HBox(10, updateButton, deleteButton);
        updateDelBox.setAlignment(Pos.BOTTOM_RIGHT);

        // Main VBox containing the title and the main HBox
        VBox mainRightContent = new VBox(10, buttonTitleBox, mainGrid, additionalBookInfo, detailedBookInfo, updateDelBox);
        rightSide.getChildren().setAll(mainRightContent);

        // Main content layout (with equal widths for left and right sides)
        HBox mainContent = new HBox(leftSide, rightSide);
        mainContent.setSpacing(20); // Spacing between left and right sides
        mainContent.setPadding(new Insets(25));

        // Combining the search area with the main content
        VBox fullContent = new VBox(10, mainContent);

        return fullContent;
    }

    private VBox createPeopleContent() {
        TabPane subTabs = new TabPane();
        Tab studentsTab = new Tab("Students");
        Tab authorsTab = new Tab("Authors");
        Tab producersTab = new Tab("Producers");

        // Content for the People tab
        VBox studentsContent = createStudentsContent();
        VBox authorsContent = createAuthorsContent();
        VBox producersContent = createProducersContent();

        studentsTab.setContent(studentsContent);
        authorsTab.setContent(authorsContent);
        producersTab.setContent(producersContent);


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

        // Add a listener to the author list to show the right side when an author is selected
        studentList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                rightSide.setVisible(true);
                rightSide.getChildren().clear();
                rightSide.getChildren().addAll(studentTitleBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                        broncoIdLabel, broncoIdField, courseBox, buttonContainer);
                // Populate fields based on the selected student here
            }
        });

        // Add new author should clear right side if it's shown and show add author UI
        addNewStudentButton.setOnAction(event -> {
            studentList.getSelectionModel().clearSelection();
            rightSide.setVisible(true);
            rightSide.getChildren().clear();
            VBox addStudentRightSide = createAddStudentRightSideContent();
            rightSide.getChildren().setAll(addStudentRightSide);
        });

        // Main People content
        HBox studentsContent = new HBox(30, leftSide, rightSide);
        studentsContent.setAlignment(Pos.CENTER);

        VBox mainStudentsContent = new VBox(10, studentManagementBox, studentsContent);
        mainStudentsContent.setAlignment(Pos.CENTER);

        return mainStudentsContent;
    }

    private VBox createAddStudentRightSideContent() {

        // Right side content
        VBox rightSide = new VBox(10);
        rightSide.setPadding(new Insets(10));
        rightSide.setStyle("-fx-background-color: lightgray;");
        rightSide.setPrefWidth(400);

        // Author  title
        Label studentTitleLabel = new Label("Add Student");
        studentTitleLabel.setStyle("-fx-font-size: 16px;");
        HBox studentTitleBox = new HBox(studentTitleLabel);
        studentTitleBox.setAlignment(Pos.CENTER);

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField();
        Label broncoIdLabel = new Label("Nationality");
        TextField broncoIdField = new TextField();
        Label courseLabel = new Label("Course");
        ComboBox<String> courseDropdown = new ComboBox<>();
        courseDropdown.getItems().addAll("BA", "BS", "CS");
        courseDropdown.setValue("CS"); // Default
        HBox courseBox = new HBox(10, courseLabel, courseDropdown);
        courseBox.setAlignment(Pos.CENTER_LEFT);


        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        addButton.setMinHeight(45);
        addButton.setPrefWidth(90);
        cancelButton.setMinHeight(30);
        cancelButton.setPrefWidth(90);
        VBox buttonBox = new VBox(10, addButton, cancelButton);
        HBox buttonContainer = new HBox(buttonBox);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(20, 0 ,0, 0));

        cancelButton.setOnAction(event -> {
            rightSide.getChildren().clear();
        });

        rightSide.getChildren().addAll(studentTitleBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                broncoIdLabel, broncoIdField, courseBox, buttonContainer);

        return rightSide;
    }

    private VBox createAuthorsContent() {

        Label authorManagementLabel = new Label("Author Management");
        authorManagementLabel.setStyle("-fx-font-size: 20px;");
        HBox authorManagementBox = new HBox(authorManagementLabel);
        authorManagementBox.setAlignment(Pos.CENTER);
        Insets padding = new Insets(20, 0, 0, 0);
        authorManagementBox.setPadding(padding);

        // Left side content
        VBox leftSide = new VBox(10);
        leftSide.setPadding(new Insets(10));
        leftSide.setStyle("-fx-background-color: lightgray;");
        leftSide.setPrefWidth(400);

        // Author List title
        Label authorListLabel = new Label("Author List");
        authorListLabel.setStyle("-fx-font-size: 16px;");
        HBox authorListTitleBox = new HBox(authorListLabel);
        authorListTitleBox.setAlignment(Pos.CENTER);

        ComboBox<String> filterDropdown = new ComboBox<>();
        filterDropdown.getItems().addAll("Name", "Item ID");
        filterDropdown.setValue("Filter by:"); // Set the default value to "Name"

        TextField searchBar = new TextField();
        Button filterButton = new Button("Filter");

        // Horizontally align dropdown, search bar, and filter button
        HBox filterBar = new HBox(10, filterDropdown, searchBar, filterButton);
        filterBar.setAlignment(Pos.CENTER);

        ListView<String> authorList = new ListView<>();
        // Add sample authors to list
        // TODO: (replace with actual data)
        authorList.getItems().addAll("Ernest Hemingway");
        Button addNewAuthorButton = new Button("Add New Author");

        leftSide.getChildren().addAll(authorListTitleBox, filterBar, authorList, addNewAuthorButton);

        // Right side content
        VBox rightSide = new VBox(10);
        rightSide.setPadding(new Insets(10));
        rightSide.setStyle("-fx-background-color: lightgray;");
        rightSide.setPrefWidth(400);

        // Don't show until a student is selected
        rightSide.setVisible(false);

        // Author  title
        Label authorTitleLabel = new Label("Author");
        authorTitleLabel.setStyle("-fx-font-size: 16px;");
        HBox authorTitleBox = new HBox(authorTitleLabel);
        authorTitleBox.setAlignment(Pos.CENTER);

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField();
        Label nationalityLabel = new Label("Nationality");
        TextField nationalityField = new TextField();
        Label subjectLabel = new Label("Subject");
        TextField subjectField = new TextField();

        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        updateButton.setMinHeight(45);
        deleteButton.setMinHeight(30);
        VBox buttonBox = new VBox(10, updateButton, deleteButton);
        HBox buttonContainer = new HBox(buttonBox);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(20, 0 ,0, 0));

        rightSide.getChildren().addAll(authorTitleBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                nationalityLabel, nationalityField, subjectLabel, subjectField, buttonContainer);

        // Add a listener to the author list to show the right side when an author is selected
        authorList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                rightSide.setVisible(true);
                rightSide.getChildren().clear();
                rightSide.getChildren().addAll(authorTitleBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                        nationalityLabel, nationalityField, subjectLabel, subjectField, buttonContainer);
                // Populate fields based on the selected student here
            }
        });

        // Add new author should clear right side if it's shown and show add author UI
        addNewAuthorButton.setOnAction(event -> {
            authorList.getSelectionModel().clearSelection();
            rightSide.setVisible(true);
            rightSide.getChildren().clear();
            VBox addAuthorRightSide = createAddAuthorRightSideContent();
            rightSide.getChildren().setAll(addAuthorRightSide);
        });

        // Main People content
        HBox authorsContent = new HBox(30, leftSide, rightSide);
        authorsContent.setAlignment(Pos.CENTER);

        VBox mainStudentsContent = new VBox(10, authorManagementBox, authorsContent);
        mainStudentsContent.setAlignment(Pos.CENTER);

        return mainStudentsContent;
    }

    private VBox createAddAuthorRightSideContent() {

        // Right side content
        VBox rightSide = new VBox(10);
        rightSide.setPadding(new Insets(10));
        rightSide.setStyle("-fx-background-color: lightgray;");
        rightSide.setPrefWidth(400);

        // Author  title
        Label authorTitleLabel = new Label("Add Author");
        authorTitleLabel.setStyle("-fx-font-size: 16px;");
        HBox authorTitleBox = new HBox(authorTitleLabel);
        authorTitleBox.setAlignment(Pos.CENTER);

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField();
        Label nationalityLabel = new Label("Nationality");
        TextField nationalityField = new TextField();
        Label subjectLabel = new Label("Subject");
        TextField subjectField = new TextField();

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        addButton.setMinHeight(45);
        addButton.setPrefWidth(90);
        cancelButton.setMinHeight(30);
        cancelButton.setPrefWidth(90);
        VBox buttonBox = new VBox(10, addButton, cancelButton);
        HBox buttonContainer = new HBox(buttonBox);

        cancelButton.setOnAction(event -> {
            rightSide.getChildren().clear();
        });

        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(20, 0 ,0, 0));

        rightSide.getChildren().addAll(authorTitleBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                nationalityLabel, nationalityField, subjectLabel, subjectField, buttonContainer);

        return rightSide;
    }

    private VBox createProducersContent() {

        Label producerManagementLabel = new Label("Producer Management");
        producerManagementLabel.setStyle("-fx-font-size: 20px;");
        HBox producerManagementBox = new HBox(producerManagementLabel);
        producerManagementBox.setAlignment(Pos.CENTER);
        Insets padding = new Insets(20, 0, 0, 0);
        producerManagementBox.setPadding(padding);

        // Left side content
        VBox leftSide = new VBox(10);
        leftSide.setPadding(new Insets(10));
        leftSide.setStyle("-fx-background-color: lightgray;");
        leftSide.setPrefWidth(400);

        // Producer List title
        Label producerListLabel = new Label("Producer List");
        producerListLabel.setStyle("-fx-font-size: 16px;");
        HBox producerListTitleBox = new HBox(producerListLabel);
        producerListTitleBox.setAlignment(Pos.CENTER);

        ComboBox<String> filterDropdown = new ComboBox<>();
        filterDropdown.getItems().addAll("Name", "Item ID");
        filterDropdown.setValue("Filter by:"); // Set the default value to "Name"

        TextField searchBar = new TextField();
        Button filterButton = new Button("Filter");

        // Horizontally align dropdown, search bar, and filter button
        HBox filterBar = new HBox(10, filterDropdown, searchBar, filterButton);
        filterBar.setAlignment(Pos.CENTER);

        ListView<String> producerList = new ListView<>();
        // Add sample producers to list
        // TODO: (replace with actual data)
        producerList.getItems().addAll("Ernest Hemingway");
        Button addNewAuthorButton = new Button("Add New Producer");

        leftSide.getChildren().addAll(producerListTitleBox, filterBar, producerList, addNewAuthorButton);

        // Right side content
        VBox rightSide = new VBox(10);
        rightSide.setPadding(new Insets(10));
        rightSide.setStyle("-fx-background-color: lightgray;");
        rightSide.setPrefWidth(400);

        // Don't show until a student is selected
        rightSide.setVisible(false);

        // Author  title
        Label producerTitleLabel = new Label("Producer");
        producerTitleLabel.setStyle("-fx-font-size: 16px;");
        HBox producerTitleBox = new HBox(producerTitleLabel);
        producerTitleBox.setAlignment(Pos.CENTER);

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField();
        Label nationalityLabel = new Label("Nationality");
        TextField nationalityField = new TextField();
        Label styleLabel = new Label("Style");
        TextField styleField = new TextField();

        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        updateButton.setMinHeight(45);
        deleteButton.setMinHeight(30);
        VBox buttonBox = new VBox(10, updateButton, deleteButton);
        HBox buttonContainer = new HBox(buttonBox);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(20, 0 ,0, 0));

        rightSide.getChildren().addAll(producerTitleBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                nationalityLabel, nationalityField, styleLabel, styleField, buttonContainer);

        // Add a listener to the author list to show the right side when an author is selected
        producerList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                rightSide.setVisible(true);
                rightSide.getChildren().clear();
                rightSide.getChildren().addAll(producerTitleBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                        nationalityLabel, nationalityField, styleLabel, styleField, buttonContainer);
                // Populate fields based on the selected student here
            }
        });

        // Add new author should clear right side if it's shown and show add author UI
        addNewAuthorButton.setOnAction(event -> {
            producerList.getSelectionModel().clearSelection();
            rightSide.setVisible(true);
            rightSide.getChildren().clear();
            VBox addProducerRightSide = createAddProducerRightSideContent();
            rightSide.getChildren().setAll(addProducerRightSide);
        });

        // Main People content
        HBox producerContent = new HBox(30, leftSide, rightSide);
        producerContent.setAlignment(Pos.CENTER);

        VBox mainStudentsContent = new VBox(10, producerManagementBox, producerContent);
        mainStudentsContent.setAlignment(Pos.CENTER);

        return mainStudentsContent;
    }

    private VBox createAddProducerRightSideContent() {

        // Right side content
        VBox rightSide = new VBox(10);
        rightSide.setPadding(new Insets(10));
        rightSide.setStyle("-fx-background-color: lightgray;");
        rightSide.setPrefWidth(400);

        // Author  title
        Label producerTitleLabel = new Label("Add Producer");
        producerTitleLabel.setStyle("-fx-font-size: 16px;");
        HBox producerTitleBox = new HBox(producerTitleLabel);
        producerTitleBox.setAlignment(Pos.CENTER);

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField();
        Label nationalityLabel = new Label("Nationality");
        TextField nationalityField = new TextField();
        Label styleLabel = new Label("Subject");
        TextField styleField = new TextField();

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        addButton.setMinHeight(45);
        addButton.setPrefWidth(90);
        cancelButton.setMinHeight(30);
        cancelButton.setPrefWidth(90);
        VBox buttonBox = new VBox(10, addButton, cancelButton);
        HBox buttonContainer = new HBox(buttonBox);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(20, 0 ,0, 0));

        cancelButton.setOnAction(event -> {
            rightSide.getChildren().clear();
        });

        rightSide.getChildren().addAll(producerTitleBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                nationalityLabel, nationalityField, styleLabel, styleField, buttonContainer);

        return rightSide;
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

        leftSide.getChildren().setAll(inventoryTitleBox, table, lowerHBox);

        // RIGHT SIDE !!

        Label buttonTitleLabel = new Label("Book");
        HBox buttonTitleBox = new HBox((buttonTitleLabel));
        buttonTitleBox.setAlignment(Pos.CENTER);
        buttonTitleLabel.setAlignment(Pos.CENTER);
        buttonTitleLabel.setFont(new Font("Arial", 20));

        // Create a GridPane for the fields
        GridPane fieldsGrid = new GridPane();
        fieldsGrid.setHgap(5);
        fieldsGrid.setVgap(10);

        // Code field
        TextField codeField = new TextField();
        fieldsGrid.addRow(0, new Label("Code"), codeField);

        // Title field
        TextField titleField = new TextField();
        fieldsGrid.addRow(1, new Label("Title"), titleField);

        // Location field
        TextField locationField = new TextField();
        fieldsGrid.addRow(2, new Label("Location"), locationField);

        // Price/D field with dropdown
        ComboBox<String> priceDropdown = new ComboBox<>();
        priceDropdown.setValue("$1.20");
        // You can add items to the dropdown here
        fieldsGrid.addRow(3, new Label("Price/D"), priceDropdown);

        // Left VBox containing the fields
        VBox rightSideLeftVBox = new VBox(fieldsGrid);

        // Authors list
        ListView<String> authorsList = new ListView<>();
        authorsList.setPrefHeight(100);
        // add authors here...

        // + and - buttons
        Button addButton = new Button("+");
        Button removeButton = new Button("-");
        // HBox for + and - buttons
        HBox authorButtonsHBox = new HBox(5, addButton, removeButton);
        authorButtonsHBox.setAlignment(Pos.BOTTOM_RIGHT);

        // VBox for authors list and buttons
        VBox authorsVBox = new VBox(5, new Label("Authors"), authorsList, authorButtonsHBox);
        authorsVBox.setPadding(new Insets(-5));

        // GridPane containing left and right sections
        GridPane mainGrid = new GridPane();
        mainGrid.add(rightSideLeftVBox, 0, 0);
        mainGrid.add(authorsVBox, 1, 0);
        mainGrid.setHgap(110);

        // Create The Book Info section
        Label pagesLabel = new Label("Pages");
        TextField pagesTextField = new TextField();
        pagesTextField.setPrefWidth(100);

        Label pubDateLabel = new Label("Pub Date");
        DatePicker pubDateField = new DatePicker();
        pubDateField.setPrefWidth(120);

        Label publisherLabel = new Label("Publisher");
        TextField publisherTextField = new TextField();

        HBox firstHBox = new HBox(10, pagesLabel, pagesTextField, pubDateLabel, pubDateField, publisherLabel, publisherTextField);
        firstHBox.setAlignment(Pos.CENTER_LEFT);

        // Second HBox
        Label descriptionLabel = new Label("Description");
        TextField descriptionTextField = new TextField();
        descriptionTextField.setMaxWidth(Double.MAX_VALUE);

        HBox secondHBox = new HBox(10, descriptionLabel, descriptionTextField);
        secondHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(descriptionTextField, Priority.ALWAYS);

        // Main VBox
        VBox additionalBookInfo = new VBox(10, firstHBox, secondHBox);
        additionalBookInfo.setPadding(new Insets(50, 0, 0 ,0));

        // Copies
        Label copiesLabel = new Label("Copies");
        ComboBox<String> copiesDropdown = new ComboBox<>();
        copiesDropdown.getItems().addAll("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        copiesDropdown.setValue("3");
        VBox copies = new VBox(10, copiesLabel, copiesDropdown);

        // Borrowed / Overdue
        // TODO: Insert actual borrowed/overdue items from DB
        HBox borrowedBox = new HBox(10, new Label("Borrowed"), new Label("3"));
        HBox overdueBox = new HBox(10, new Label("Overdue"), new Label("  2"));
        VBox borrowedOverdueBox = new VBox(15, borrowedBox, overdueBox);

        // Waitlisted
        Label waitListedLabel = new Label("Waitlisted");
        ToggleButton yesButton = new ToggleButton("Yes");
        ToggleButton noButton = new ToggleButton("No");
        ToggleGroup waitlistedGroup = new ToggleGroup();
        yesButton.setToggleGroup(waitlistedGroup);
        noButton.setToggleGroup(waitlistedGroup);
        HBox waitlistedButtonBox = new HBox(0, yesButton, noButton);
        VBox waitlistedBox = new VBox(10.0, waitListedLabel, waitlistedButtonBox);

        // Combine copies, borrowed/overdue, and waitlisted into hbox
        HBox detailedBookInfo = new HBox(20, copies, borrowedOverdueBox, waitlistedBox);
        detailedBookInfo.setPadding(new Insets(50, 0, 0 ,0));

        // Update/Delete Buttons
        Button updateButton = new Button("Update");
        updateButton.setPrefWidth(150);
        updateButton.setPrefHeight(80);

        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(70);
        deleteButton.setPrefHeight(80);

        HBox updateDelBox = new HBox(10, updateButton, deleteButton);
        updateDelBox.setAlignment(Pos.BOTTOM_RIGHT);

        // Main VBox containing the title and the main HBox
        VBox mainRightContent = new VBox(10, buttonTitleBox, mainGrid, additionalBookInfo, detailedBookInfo, updateDelBox);
        rightSide.getChildren().setAll(mainRightContent);

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

    public static class LoanItem {
        private final SimpleStringProperty loan;
        private final SimpleStringProperty dueDate;

        public LoanItem(String loan, String dueDate) {
            this.loan = new SimpleStringProperty(loan);
            this.dueDate = new SimpleStringProperty(dueDate);
        }

        public String getLoan() {
            return loan.get();
        }

        public String getDueDate() {
            return dueDate.get();
        }
    }
}