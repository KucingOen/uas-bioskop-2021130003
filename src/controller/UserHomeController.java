package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import model.Film;
import model.User;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import bioskopreal.DatabaseConnection;
import java.io.IOException;
import java.sql.Statement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserHomeController implements Initializable {

    @FXML
    private Label namauser;

    @FXML
    private Button logoutbtn;

    @FXML
    private TableView<Film> tableView;

    @FXML
    private TableColumn<Film, String> judulColumn;

    @FXML
    private TableColumn<Film, String> genreColumn;

    @FXML
    private TableColumn<Film, Double> hargaColumn;

    @FXML
    private TextField tfjudul;

    @FXML
    private ComboBox<String> cbtambahan;

    @FXML
    private TextField tftotal;

    @FXML
    private Button belibtn;

    @FXML
    private ComboBox<String> cbkursi;

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Button riwayatbtn;
    

    private ObservableList<Film> filmList;
    private ObservableList<String> kursiList;
    private User currentUser;

    @FXML
    public void setUserData(User user) {
        this.currentUser = user;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        } else {
            System.out.println("welcomeLabel is null");
        }
        namauser.setText(user.getUsername());
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filmList = FXCollections.observableArrayList();
        kursiList = FXCollections.observableArrayList();

        judulColumn.setCellValueFactory(new PropertyValueFactory<>("judul"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<>("harga"));
        tableView.setItems(filmList);

        cbkursi.setItems(kursiList);

        ObservableList<String> tambahanList = FXCollections.observableArrayList("Tidak ada tambahan", "Popcorn (+15000)", "Es Teh (+5000)", "Paket (+20000)");
        cbtambahan.setItems(tambahanList);
        belibtn.setOnAction(this::belibtnClicked);
        cbtambahan.getSelectionModel().selectedItemProperty().addListener((obs, oldTambahan, newTambahan) -> {
            if (newTambahan != null) {
                hitungTotal();
            }
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldFilm, newFilm) -> {
            if (newFilm != null) {
                tfjudul.setText(newFilm.getJudul());
                tftotal.setEditable(false);
                tftotal.setText("");
                updateKursiComboBox(newFilm.getId());
            }
        });
        filmList.addAll(getAllFilmsFromDatabase());
        riwayatbtn.setOnAction(this::riwayatbtnClicked);
        logoutbtn.setOnAction(this::logoutbtnClicked);
    }
    
    @FXML
    private void logoutbtnClicked(ActionEvent event) {
        try {
            // Tutup halaman UserHome
            Stage userHomeStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            userHomeStage.close();

            // Buka halaman Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Film> getAllFilmsFromDatabase() {
        ObservableList<Film> films = FXCollections.observableArrayList();

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
                    films.add(film);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching films from the database");
        }

        return films;
    }

    private void updateKursiComboBox(int filmId) {
        kursiList.clear();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT nama_kursi FROM kursi WHERE film_id = ? AND tersedia = 1"; 
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, filmId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String namaKursi = resultSet.getString("nama_kursi");
                        kursiList.add(namaKursi);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching available seats from the database");
        }

        cbkursi.setItems(kursiList);
    }

    @FXML
    private void hitungTotal() {
        try {
            Film selectedFilm = tableView.getSelectionModel().getSelectedItem();

            if (selectedFilm != null) {
                double hargaFilm = selectedFilm.getHarga();
                double hargaTambahan = 0.0;
                String selectedTambahan = cbtambahan.getSelectionModel().getSelectedItem();
                if (selectedTambahan != null && !selectedTambahan.equals("Tidak ada tambahan")) {
                    if (selectedTambahan.equals("Popcorn (+15000)")) {
                        hargaTambahan = 15000;
                    } else if (selectedTambahan.equals("Es Teh (+5000)")) {
                        hargaTambahan = 5000;
                    } else if (selectedTambahan.equals("Paket (+20000)")) {
                        hargaTambahan = 20000;
                    }
                }

                double total = hargaFilm + hargaTambahan;
                tftotal.setText(String.valueOf(total));
            } else {
                tftotal.setText("Pilih film terlebih dahulu");
            }
        } catch (NumberFormatException e) {
            tftotal.setText("Invalid Input");
        }
    }

    @FXML
    private void belibtnClicked(ActionEvent event) {
        String selectedTambahan = cbtambahan.getSelectionModel().getSelectedItem();
        String selectedKursi = cbkursi.getSelectionModel().getSelectedItem();

        if (selectedTambahan == null || selectedKursi == null) {
            tampilkanPeringatan("Pilih tambahan dan kursi terlebih dahulu!");
        } else {
            boolean kursiTersedia = cekKetersediaanKursi(selectedKursi);

            if (kursiTersedia) {
                simpanRiwayatPembelian();
                perbaruiStatusKursi(selectedKursi);
                tampilkanRingkasanPembelian();
            } else {
                tampilkanPeringatan("Kursi tidak tersedia. Pilih kursi lain.");
            }
        }
    }

    private boolean cekKetersediaanKursi(String selectedKursi) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String checkQuery = "SELECT tersedia FROM kursi WHERE nama_kursi = ? AND film_id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, selectedKursi);
                checkStatement.setInt(2, tableView.getSelectionModel().getSelectedItem().getId());
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean("tersedia");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking seat availability in the database");
        }
        return false;
    }

    private void simpanRiwayatPembelian() {
        try {
            Film selectedFilm = tableView.getSelectionModel().getSelectedItem();

            if (selectedFilm != null && currentUser != null) {
                int userId = currentUser.getId();
                int filmId = selectedFilm.getId();
                String kursi = cbkursi.getSelectionModel().getSelectedItem();
                double harga = Double.parseDouble(tftotal.getText());

                try (Connection connection = DatabaseConnection.getConnection()) {
                    String query = "INSERT INTO riwayat_pembelian (user_id, film_id, kursi, harga) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                        preparedStatement.setInt(1, userId);
                        preparedStatement.setInt(2, filmId);
                        preparedStatement.setString(3, kursi);
                        preparedStatement.setDouble(4, harga);
                        int affectedRows = preparedStatement.executeUpdate();

                        if (affectedRows > 0) {
                            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                            if (generatedKeys.next()) {
                                int riwayatId = generatedKeys.getInt(1);
                                System.out.println("Riwayat pembelian berhasil ditambahkan dengan ID: " + riwayatId);
                            } else {
                                System.out.println("Gagal mendapatkan ID riwayat pembelian");
                            }
                        } else {
                            System.out.println("Gagal menyimpan riwayat pembelian");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error menyimpan riwayat pembelian ke database");
                }
            }
        } catch (NumberFormatException e) {
            tftotal.setText("Input tidak valid");
        }
    }

    private void perbaruiStatusKursi(String selectedKursi) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE kursi SET tersedia = false WHERE nama_kursi = ? AND film_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, selectedKursi);
                updateStatement.setInt(2, tableView.getSelectionModel().getSelectedItem().getId());
                int affectedRows = updateStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Status kursi berhasil diperbarui: " + selectedKursi);
                } else {
                    System.out.println("Gagal memperbarui status kursi");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating seat status in the database");
        }
    }

    private void tampilkanPeringatan(String pesan) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
    
    @FXML
    private void riwayatbtnClicked(ActionEvent event) {
        int userId = currentUser.getId();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RiwayatUserView.fxml"));
            Parent root = loader.load();
            RiwayatUserController riwayatUserController = loader.getController();
            riwayatUserController.loadRiwayatUser(userId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tampilkanRingkasanPembelian() {
        String judulFilm = tfjudul.getText();
        String kursi = cbkursi.getSelectionModel().getSelectedItem();
        String tambahan = cbtambahan.getSelectionModel().getSelectedItem();
        String hargaTotal = tftotal.getText();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ringkasan Pembelian");
        alert.setHeaderText(null);
        alert.setContentText("Judul Film: " + judulFilm + "\n"
                + "Kursi: " + kursi + "\n"
                + "Tambahan: " + tambahan + "\n"
                + "Harga Total: " + hargaTotal);
        alert.showAndWait();
        updateKursiComboBox(tableView.getSelectionModel().getSelectedItem().getId());
    }
    
}
