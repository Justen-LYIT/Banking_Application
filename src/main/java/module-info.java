module com.example.banking_application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    requires javafx.graphics;
    requires javafx.base;

    opens com.banking_application to javafx.fxml;
    opens com.banking_application.scenes to javafx.fxml;
    exports com.banking_application;
}