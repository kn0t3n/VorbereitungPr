package de.gbs.schulen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Fenster extends JFrame {

    private JPanel jpNorth, jpSouth;
    private JComboBox<Gegenstand> jComboBox;
    private JTextField jTextField;
    private JButton jbtnEintragen, jbtnLoeschen;
    private JLabel jlGesamtpreis;

    private JMenuBar jMenuBar;
    private JMenu jMenuDatei;
    private JMenuItem jMenuItemNeu, jMenuItemSpeichern, jMenuItemBeenden;

    private JTable jTable;
    private MyTableModel myTableModel;
    private JScrollPane jScrollPane;

    private DAO dao;

    private JFileChooser jFileChooser;

    public Fenster() throws HeadlessException, SQLException {
        super("Einkaufsliste");
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.dao = new DAO();
        this.initMenu();
        this.initComponents();
        this.initEvents();
        this.setSize(700, 400);
        this.setVisible(true);
    }

    private void initEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                beenden();
            }
        });

        jMenuItemBeenden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                beenden();
            }
        });

        jbtnEintragen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eintragen();
            }
        });

        jMenuItemNeu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neu();
            }
        });

        jMenuItemSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speichern();
            }
        });

        jbtnLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loeschen();
            }
        });
    }

    private void loeschen() {
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bezeichnung = (String) myTableModel.getValueAt(selectedRow, 1);
            myTableModel.loeschen(bezeichnung);
            anzeigeAkualisieren();
        }
    }

    private void anzeigeAkualisieren(){
        jlGesamtpreis.setText(String.valueOf(myTableModel.getGesamtpreis()));
    }

    private void speichern() {
        int result = jFileChooser.showSaveDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File file = jFileChooser.getSelectedFile();
        try {
            myTableModel.speichern(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Datei kann nicht gespeichert werden", "Fehler!", JOptionPane.WARNING_MESSAGE);
        }
        JOptionPane.showMessageDialog(this, "Erfolgreich gespeichert", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        this.setTitle("Einkaufliste: " + file.getName());
    }

    private void neu() {
        this.myTableModel = new MyTableModel();
        jTable.setModel(myTableModel);
        anzeigeAkualisieren();
    }

    private void eintragen() {
        if (jComboBox.getSelectedIndex() == 0){
            return;
        }
        String eingabe = jTextField.getText();
        int anzahl = 0;
        try {
            anzahl = Integer.parseInt(eingabe);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Falsche Eingabe", "Fehler!", JOptionPane.WARNING_MESSAGE);
            jComboBox.setSelectedIndex(0);
            jTextField.setText("");
            return;
        }
        Gegenstand gegenstand = (Gegenstand) jComboBox.getSelectedItem();
        Gegenstand neuerGegenstand = new Gegenstand(gegenstand.getBezeichnung(), gegenstand.getEinzelpreis(), gegenstand.getAnzahl());
        if (anzahl > 0){
            neuerGegenstand.setAnzahl(anzahl);
            myTableModel.hinzufuegen(neuerGegenstand);
            jComboBox.setSelectedIndex(0);
            jTextField.setText("");
            anzeigeAkualisieren();
        }
    }

    private void beenden(){
        int result = JOptionPane.showConfirmDialog(this, "Wollen Sie wirklich Beenden?", "Hrnsn", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            System.exit(NORMAL);
        }
    }

    private void initMenu() {
        jMenuBar = new JMenuBar();
        jMenuDatei = new JMenu("Datei");
        jMenuItemNeu = new JMenuItem("Neu");
        jMenuItemSpeichern = new JMenuItem("Speichern");
        jMenuItemBeenden = new JMenuItem("Beenden");

        jMenuDatei.add(jMenuItemNeu);
        jMenuDatei.add(jMenuItemSpeichern);
        jMenuDatei.add(jMenuItemBeenden);

        jMenuBar.add(jMenuDatei);
    }

    private void initComponents() {
        jFileChooser = new JFileChooser();
        jpNorth = new JPanel();
        jComboBox = new JComboBox<>();
        befuelleCombobox();
        jTextField = new JTextField(2);
        jbtnEintragen = new JButton("Eintragen");
        jbtnLoeschen = new JButton("Löschen");

        jpNorth.add(jComboBox);
        jpNorth.add(new JLabel("Anzahl: "));
        jpNorth.add(jTextField);
        jpNorth.add(jbtnEintragen);
        jpNorth.add(jbtnLoeschen);

        myTableModel = new MyTableModel();
        jTable = new JTable(myTableModel);
        jScrollPane = new JScrollPane(jTable);

        jpSouth = new JPanel();
        jpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jpSouth.add(new JLabel("Gesamtpreis: "));
        jlGesamtpreis = new JLabel("0.00");
        jpSouth.add(jlGesamtpreis);

        this.setJMenuBar(jMenuBar);
        this.add(jpNorth, BorderLayout.NORTH);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(jpSouth, BorderLayout.SOUTH);
    }

    private void befuelleCombobox() {
        jComboBox.addItem(new Gegenstand("Bitte auswählen...", 0, 0));
        try {
            dao.findeArtikel("%%");
            for (Gegenstand gegenstand : dao.getGegenstandList()) {
                jComboBox.addItem(gegenstand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dao.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Fenster();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
