module com.example.libraryproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.libraryproject to javafx.fxml;
    exports com.example.libraryproject;
}