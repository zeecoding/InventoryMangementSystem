module com.example.inventorymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.java;
    requires java.sql;
    requires fontawesomefx;
    requires jasperreports;

    opens com.example.inventorymanagementsystem to javafx.fxml;
    exports com.example.inventorymanagementsystem;
}