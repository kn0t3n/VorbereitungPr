package de.gbs.schulen;

import javax.swing.table.AbstractTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyTableModel extends AbstractTableModel {

    private List<Gegenstand> gegenstandList;
    private String[] columns = {"Anzahl", "Gegenstand", "Einzelpreis", "Preis"};

    public MyTableModel() {
        gegenstandList = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return gegenstandList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Gegenstand gegenstand = gegenstandList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return gegenstand.getAnzahl();
            case 1:
                return gegenstand.getBezeichnung();
            case 2:
                return gegenstand.getEinzelpreis();
            case 3:
                return gegenstand.getAnzahl() * gegenstand.getEinzelpreis();
        }
        return null;
    }

    public void hinzufuegen(Gegenstand gegenstand) {
        Iterator<Gegenstand> iterator = gegenstandList.iterator();
        while (iterator.hasNext()) {
            Gegenstand next = iterator.next();
            if (next.getBezeichnung().equals(gegenstand.getBezeichnung())) {
                next.setAnzahl(next.getAnzahl() + gegenstand.getAnzahl());
                this.fireTableDataChanged();
                return;
            }
        }
        gegenstandList.add(gegenstand);
        this.fireTableDataChanged();
    }

    public void speichern(File file) throws IOException {
        BufferedWriter bufferedWriter = null;
        bufferedWriter = new BufferedWriter(new FileWriter(file));
        for (Gegenstand gegenstand : gegenstandList) {
            bufferedWriter.write(gegenstand.getBezeichnung() + "," + gegenstand.getAnzahl() + "," + gegenstand.getEinzelpreis() + "," + gegenstand.getAnzahl() * gegenstand.getEinzelpreis());
            bufferedWriter.newLine();
        }
        if (bufferedWriter != null) {
            bufferedWriter.close();
        }
    }

    public double getGesamtpreis(){
        double gesamtpreis = 0.0;
        for (Gegenstand gegenstand : gegenstandList) {
            gesamtpreis += gegenstand.getAnzahl() * gegenstand.getEinzelpreis();
        }
        return gesamtpreis;
    }

    public void loeschen(String bezeichnung){
        Iterator<Gegenstand> iterator = gegenstandList.iterator();
        while (iterator.hasNext()) {
            Gegenstand gegenstand = iterator.next();
            if (gegenstand.getBezeichnung().equals(bezeichnung)) {
                iterator.remove();
                fireTableDataChanged();
                return;
            }
        }
    }
}
