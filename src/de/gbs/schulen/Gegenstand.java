package de.gbs.schulen;

import java.util.Objects;

public class Gegenstand {

    private String bezeichnung;
    private double einzelpreis;
    private int anzahl;

    public Gegenstand(){

    }

    public Gegenstand(String bezeichnung, double einzelpreis, int anzahl) {
        this.bezeichnung = bezeichnung;
        this.einzelpreis = einzelpreis;
        this.anzahl = anzahl;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public double getEinzelpreis() {
        return einzelpreis;
    }

    public void setEinzelpreis(double einzelpreis) {
        this.einzelpreis = einzelpreis;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gegenstand that = (Gegenstand) o;
        return Double.compare(that.einzelpreis, einzelpreis) == 0 &&
                anzahl == that.anzahl &&
                Objects.equals(bezeichnung, that.bezeichnung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bezeichnung, einzelpreis, anzahl);
    }

    @Override
    public String toString() {
        return bezeichnung;
    }
}
