package com.example.inventorymanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable{
    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<String> role;

    @FXML
    private Button loginbtn;

    @FXML
    private Button close;

    private Connection connect;
    private PreparedStatement prepear;
    private ResultSet result;
    public void addRoles(){
        role.getItems().addAll("admin","salesman");
    }
    public void loginAdmin() {
        // SQL query to check username, password, and role
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ? AND role = ?";
        connect = database.connectDb();
        try {
            prepear = connect.prepareStatement(sql);
            prepear.setString(1, username.getText());
            prepear.setString(2, password.getText());
            prepear.setString(3, role.getValue());  // Retrieve selected role from combo box
            result = prepear.executeQuery();

            Alert alert;

            // Check if any field is empty
            if (username.getText().isEmpty() || password.getText().isEmpty() || role.getValue() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields and select a role.");
                alert.showAndWait();
            } else {
                if (result.next()) {  // If a matching record is found
                    getData.username = username.getText();
                    getData.role = role.getValue();  // Get the selected role
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Logged in as " + getData.role + "!");
                    alert.showAndWait();
                    loginbtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
                    Stage stage = new Stage();
                    stage.getIcons().add(new Image(getClass().getResource("/logo.png").toExternalForm()));
                    Scene scene = new Scene(root);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Username, Password, or Role.");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }

    public void closeDatabaseResources() {
        try {
            if (result != null) {
                result.close();
            }
            if (prepear != null) {
                prepear.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(){
        System.exit(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addRoles();
    }
}