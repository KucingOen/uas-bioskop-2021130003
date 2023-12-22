package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Film {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty judul;
    private final SimpleStringProperty genre;
    private final SimpleDoubleProperty harga;

    public Film(int id, String judul, String genre, double harga) {
        this.id = new SimpleIntegerProperty(id);
        this.judul = new SimpleStringProperty(judul);
        this.genre = new SimpleStringProperty(genre);
        this.harga = new SimpleDoubleProperty(harga);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getJudul() {
        return judul.get();
    }

    public SimpleStringProperty judulProperty() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul.set(judul);
    }

    public String getGenre() {
        return genre.get();
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
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
