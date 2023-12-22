package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RiwayatUser {
    private final SimpleIntegerProperty id;
    private final StringProperty judul;
    private final StringProperty kursi;
    private final SimpleDoubleProperty harga;

    public RiwayatUser(int id, String judul, String kursi, double harga) {
        this.id = new SimpleIntegerProperty(id);
        this.judul = new SimpleStringProperty(judul);
        this.kursi = new SimpleStringProperty(kursi);
        this.harga = new SimpleDoubleProperty(harga);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty getIdProperty() {
        return id;
    }

    public String getJudul() {
        return judul.get();
    }

    public StringProperty getJudulProperty() {
        return judul;
    }

    public String getKursi() {
        return kursi.get();
    }

    public StringProperty getKursiProperty() {
        return kursi;
    }

    public double getHarga() {
        return harga.get();
    }

    public SimpleDoubleProperty getHargaProperty() {
        return harga;
    }
}
