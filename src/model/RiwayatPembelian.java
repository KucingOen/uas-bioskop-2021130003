package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RiwayatPembelian {

    private final SimpleIntegerProperty idPembelian;
    private final SimpleStringProperty username;
    private final SimpleStringProperty judulFilm;
    private final SimpleStringProperty kursi;
    private final SimpleDoubleProperty harga;

    public RiwayatPembelian(int idPembelian, String username, String judulFilm, String kursi, double harga) {
        this.idPembelian = new SimpleIntegerProperty(idPembelian);
        this.username = new SimpleStringProperty(username);
        this.judulFilm = new SimpleStringProperty(judulFilm);
        this.kursi = new SimpleStringProperty(kursi);
        this.harga = new SimpleDoubleProperty(harga);
    }

    public int getIdPembelian() {
        return idPembelian.get();
    }

    public SimpleIntegerProperty idPembelianProperty() {
        return idPembelian;
    }

    public void setIdPembelian(int idPembelian) {
        this.idPembelian.set(idPembelian);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getJudulFilm() {
        return judulFilm.get();
    }

    public SimpleStringProperty judulFilmProperty() {
        return judulFilm;
    }

    public void setJudulFilm(String judulFilm) {
        this.judulFilm.set(judulFilm);
    }

    public String getKursi() {
        return kursi.get();
    }

    public SimpleStringProperty kursiProperty() {
        return kursi;
    }

    public void setKursi(String kursi) {
        this.kursi.set(kursi);
    }

    public double getHarga() {
        return harga.get();
    }

    public SimpleDoubleProperty hargaProperty() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga.set(harga);
    }
}
