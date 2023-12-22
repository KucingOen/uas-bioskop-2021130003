package controller;

import bioskopreal.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Film;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class AdminViewController {

    @FXML
    private TableView<Film> filmTableView;

    @FXML
    private TableColumn<Film, String> judulColumn;

    @FXML
    private TableColumn<Film, String> genreColumn;

    @FXML
    private TableColumn<Film, Double> hargaColumn;

    @FXML
    private TextField judulField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField hargaField;

    @FXML
    private Button logoutbtn;

    private ObservableList<Film> filmList;

    @FXML
    private void initialize() {
        filmList = FXCollections.observableArrayList();
        initializeTableView();
        loadDataFromDatabase();
        setupTableViewListener();
    }

    private void initializeTableView() {
        judulColumn.setCellValueFactory(cellData -> cellData.getValue().judulProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty().asObject());
        filmTableView.setItems(filmList);
    }

    private void loadDataFromDatabase() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM film";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String judul = resultSet.getString("judul");
                    String genre = resultSet.getString("genre");
                    double harga = resultSet.getDouble("harga");

                    Film film = new Film(id, judul, genre, harga);
                    filmList.add(film);
                }
            }
            filmTableView.setItems(filmList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTableViewListener() {
        filmTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                judulField.setText(newValue.getJudul());
                genreField.setText(newValue.getGenre());
                hargaField.setText(String.valueOf(newValue.getHarga()));
            }
        });
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage adminStage = (Stage) filmTableView.getScene().getWindow();
            adminStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSimpan(ActionEvent event) {
        Film selectedFilm = filmTableView.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            selectedFilm.setJudul(judulField.getText());
            selectedFilm.setGenre(genreField.getText());
            selectedFilm.setHarga(Double.parseDouble(hargaField.getText()));
            updateFilmData(selectedFilm);
        }
    }

    private void updateFilmData(Film film) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE film SET judul=?, genre=?, harga=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, film.getJudul());
                preparedStatement.setString(2, film.getGenre());
                preparedStatement.setDouble(3, film.getHarga());
                preparedStatement.setInt(4, film.getId());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTambah(ActionEvent event) {
        showTambahDialog();
    }

    private void showTambahDialog() {
        Dialog<Film> dialog = new Dialog<>();
        dialog.setTitle("Tambah Film");
        dialog.setHeaderText("Masukkan informasi film baru");

        ButtonType tambahButtonType = new ButtonType("Tambah", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(tambahButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField judulFieldDialog = new TextField();
        judulFieldDialog.setPromptText("Judul");
        TextField genreFieldDialog = new TextField();
        genreFieldDialog.setPromptText("Genre");
        TextField hargaFieldDialog = new TextField();
        hargaFieldDialog.setPromptText("Harga");

        grid.add(new Label("Judul:"), 0, 0);
        grid.add(judulFieldDialog, 1, 0);
        grid.add(new Label("Genre:"), 0, 1);
        grid.add(genreFieldDialog, 1, 1);
        grid.add(new Label("Harga:"), 0, 2);
        grid.add(hargaFieldDialog, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Node tambahButton = dialog.getDialogPane().lookupButton(tambahButtonType);
        tambahButton.setDisable(true);

        judulFieldDialog.textProperty().addListener((observable, oldValue, newValue) -> {
            tambahButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == tambahButtonType) {
                return new Film(0, judulFieldDialog.getText(), genreFieldDialog.getText(), Double.parseDouble(hargaFieldDialog.getText()));
            }
            return null;
        });

        Optional<Film> result = dialog.showAndWait();

        result.ifPresent(newFilm -> {
            filmList.add(newFilm);
            tambahkanKeDatabase(newFilm);
        });
    }

    private void tambahkanKeDatabase(Film film) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO film (judul, genre, harga) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, film.getJudul());
                preparedStatement.setString(2, film.getGenre());
                preparedStatement.setDouble(3, film.getHarga());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Ambil ID yang secara otomatis ditetapkan oleh database
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            film.setId(generatedId);
                        } else {
                            System.out.println("Gagal mendapatkan ID yang dihasilkan oleh database.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHapus(ActionEvent event) {
        Film selectedFilm = filmTableView.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Hapus");
            alert.setHeaderText("Anda yakin ingin menghapus film ini?");
            alert.setContentText(selectedFilm.getJudul());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                filmList.remove(selectedFilm);
                hapusDariDatabase(selectedFilm);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Pilih film yang ingin dihapus.");
            alert.showAndWait();
        }
    }

    private void hapusDariDatabase(Film film) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("ID film sebelum penghapusan: " + film.getId());

            String query = "DELETE FROM film WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, film.getId());
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("ID film setelah penghapusan: " + film.getId());
                    // Tidak perlu menghapus dan memuat ulang semua data, cukup hapus dari filmList
                    filmList.remove(film);
                    System.out.println("Film List setelah penghapusan: " + filmList);
                } else {
                    System.out.println("Penghapusan dari database gagal. Tidak ada baris yang terpengaruh.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Penghapusan dari database gagal. Kesalahan SQL: " + e.getMessage());
        }
    }

    @FXML
    private void handleRiwayatPembelian(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RiwayatPembelian.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
