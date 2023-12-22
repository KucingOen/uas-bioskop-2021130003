package controller;

import bioskopreal.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;
import model.RiwayatPembelian;

public class RiwayatPembelianController {

    @FXML
    private TableView<RiwayatPembelian> riwayatTableView;

    @FXML
    private TableColumn<RiwayatPembelian, Integer> idPembelianColumn;

    @FXML
    private TableColumn<RiwayatPembelian, String> usernameColumn;

    @FXML
    private TableColumn<RiwayatPembelian, String> judulFilmColumn;

    @FXML
    private TableColumn<RiwayatPembelian, String> kursiColumn;

    @FXML
    private TableColumn<RiwayatPembelian, Double> hargaColumn;

    // Metode untuk menginisialisasi tampilan
    @FXML
    private void initialize() {
        idPembelianColumn.setCellValueFactory(new PropertyValueFactory<>("idPembelian"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        judulFilmColumn.setCellValueFactory(new PropertyValueFactory<>("judulFilm"));
        kursiColumn.setCellValueFactory(new PropertyValueFactory<>("kursi"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<>("harga"));

        loadDataFromDatabase();
    }

    // Metode untuk mengambil data dari database dan menampilkannya di TableView
    private void loadDataFromDatabase() {
        // Lakukan koneksi ke database dan ambil data dari tabel riwayat_pembelian
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT rp.id, u.username, f.judul, rp.kursi, rp.harga " +
                           "FROM riwayat_pembelian rp " +
                           "JOIN users u ON rp.user_id = u.id " +
                           "JOIN film f ON rp.film_id = f.id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                List<RiwayatPembelian> riwayatList = new ArrayList<>();

                while (resultSet.next()) {
                    int idPembelian = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String judulFilm = resultSet.getString("judul");
                    String kursi = resultSet.getString("kursi");
                    double harga = resultSet.getDouble("harga");

                    RiwayatPembelian riwayat = new RiwayatPembelian(idPembelian, username, judulFilm, kursi, harga);
                    riwayatList.add(riwayat);
                }

                // Tambahkan data ke dalam TableView
                riwayatTableView.getItems().addAll(riwayatList);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
