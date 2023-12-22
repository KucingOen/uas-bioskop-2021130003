package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.RiwayatUser;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import bioskopreal.DatabaseConnection;

public class RiwayatUserController implements Initializable {

    @FXML
    private TableView<RiwayatUser> riwayatTableView;

    @FXML
    private TableColumn<RiwayatUser, String> judulColumn;

    @FXML
    private TableColumn<RiwayatUser, String> kursiColumn;

    @FXML
    private TableColumn<RiwayatUser, Double> hargaColumn;

    private ObservableList<RiwayatUser> riwayatList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        riwayatList = FXCollections.observableArrayList();
        judulColumn.setCellValueFactory(cellData -> cellData.getValue().getJudulProperty());
        kursiColumn.setCellValueFactory(cellData -> cellData.getValue().getKursiProperty());
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().getHargaProperty().asObject());
        riwayatTableView.setItems(riwayatList);
    }

    public void loadRiwayatUser(int userId) {
        riwayatList.clear();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT r.id, f.judul, r.kursi, r.harga " +
                           "FROM riwayat_pembelian r " +
                           "JOIN film f ON r.film_id = f.id " +
                           "WHERE r.user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String judul = resultSet.getString("judul");
                        String kursi = resultSet.getString("kursi");
                        double harga = resultSet.getDouble("harga");

                        RiwayatUser riwayatUser = new RiwayatUser(id, judul, kursi, harga);
                        riwayatList.add(riwayatUser);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching user's purchase history from the database");
        }
    }
}
