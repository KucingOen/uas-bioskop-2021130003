package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import bioskopreal.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import model.User;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label resultLabel;
    
    private User currentUser; 
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            resultLabel.setText("Isi username dan password terlebih dahulu");
        } else {
            int userId = getUserId(username, password);

            if (userId != -1) {
                String userType = getUserType(username);

                currentUser = new User(userId, username, userType);

                if ("pembeli".equals(userType)) {
                    redirectToUserHome(username, userId);
                } else if ("admin".equals(userType)) {
                    redirectToAdminView();
                }
            } else {
                showMessageBox("Login gagal");
            }
        }
    }

    private void redirectToAdminView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
            Parent root = loader.load();
            AdminViewController adminViewController = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    @FXML
    private void initialize() {
    }

    private int getUserId(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Login failed: An error occurred");
        }
        return -1;
    }

    private void redirectToUserHome(String username, int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHomeView.fxml"));
            Parent root = loader.load();
            UserHomeController userHomeController = loader.getController();
            userHomeController.setUserData(getCurrentUser());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkLogin(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Login failed: An error occurred");
        }
        return false;
    }

    private String getUserType(String username) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT role FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("role");
                    } else {
                        System.out.println("User role not found for username: " + username);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void showMessageBox(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void tutupAplikasi(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
