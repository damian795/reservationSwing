package com.example.buttons.adminView;

import com.example.buttons.ReservationDAO;
import com.example.buttons.adminView.adminViewComponents.ReservationTableBuilder;
import com.example.hibernate.Reservation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminView extends JPanel {

    private JTable table;
    private JTextArea notesArea;
    private JScrollPane tableScrollPane;
    private JTextField fullNameField, guestsField, hourField, dateField, phoneField, emailField;
    private JCheckBox confirmedCheckBox;
    List<Reservation> reservations;

    public AdminView(CardLayout cardLayout, JPanel mainPanel, JFrame mainFrame) {
        setLayout(new BorderLayout());  // Używamy BorderLayout do głównego panelu
        initUI(cardLayout, mainPanel, mainFrame);
    }


    private void initUI(CardLayout cardLayout, JPanel mainPanel, JFrame mainFrame) {
        reservations = ReservationDAO.getAllReservations();

        // Tworzymy panel z tabelą, który będzie w górnej części po lewej
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableScrollPane = ReservationTableBuilder.createTable(reservations);
        table = (JTable) ((JViewport) tableScrollPane.getViewport()).getView();
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Tworzymy panel na notatki, który będzie po prawej stronie tabeli
        JPanel notesPanel = new JPanel(new BorderLayout());
        notesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel notesLabel = new JLabel("Notes:");
        notesArea = new JTextArea(10, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);

        notesPanel.add(notesLabel, BorderLayout.NORTH);
        notesPanel.add(notesScrollPane, BorderLayout.CENTER);

        // Tworzymy panel z formularzem do edytowania rezerwacji
        JPanel editPanel = new JPanel(new GridLayout(7, 2));  // GridLayout dla formularza
        fullNameField = new JTextField();
        guestsField = new JTextField();
        hourField = new JTextField();
        dateField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        confirmedCheckBox = new JCheckBox("Confirmed");

        editPanel.add(new JLabel("  Full Name:"));
        editPanel.add(fullNameField);
        editPanel.add(new JLabel("  Guests:"));
        editPanel.add(guestsField);
        editPanel.add(new JLabel("  Hour:"));
        editPanel.add(hourField);
        editPanel.add(new JLabel("  Date (yyyy-MM-dd):"));
        editPanel.add(dateField);
        editPanel.add(new JLabel("  Phone:"));
        editPanel.add(phoneField);
        editPanel.add(new JLabel("  Email:"));
        editPanel.add(emailField);
        editPanel.add(new JLabel("  Confirmed:"));
        editPanel.add(confirmedCheckBox);

        // Tworzymy przyciski "Save" i "Back"
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUpdatedReservation();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            resetForm();
            resetTableSelection();
            mainFrame.setSize(400, 500);
            mainFrame.setLocationRelativeTo(null);
            cardLayout.show(mainPanel, "MainView");
        });


        JButton deleteButton = new JButton("Delete");
        deleteButton.setName("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteReservation();
            }
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setName("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshReservation();
            }
        });


        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        // Panel główny (layout BorderLayout) z tabelą, notatkami i formularzem
        JPanel mainPanelWrapper = new JPanel(new BorderLayout());
        mainPanelWrapper.add(tablePanel, BorderLayout.CENTER);  // Tabela na górze
        mainPanelWrapper.add(notesPanel, BorderLayout.EAST);    // Notatki po prawej
        mainPanelWrapper.add(editPanel, BorderLayout.SOUTH);    // Formularz na dole

        // Dodanie panelu głównego do głównego panelu
        add(mainPanelWrapper, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH); // Przyciski na dole

        // Nasłuchiwanie zmian w tabeli
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    displaySelectedNotes();
                }
            }
        });
    }

    // Przykładowa metoda do resetowania formularza
    private void resetForm() {
        // Zakładając, że masz pola tekstowe do edycji
        fullNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        guestsField.setText("");
        dateField.setText("");
        hourField.setText("");
        notesArea.setText("");

        // Jeśli masz inne komponenty, np. JComboBox, to je również resetuj:
        confirmedCheckBox.setSelected(false);
    }

    // Przykład metody do resetowania zaznaczenia w tabeli
    private void resetTableSelection() {
        table.clearSelection();
    }

    private void refreshReservation() {
        // 1. Pobierz dane z bazy danych (np. lista rezerwacji)
        List<Reservation> updatedReservations = ReservationDAO.getAllReservations(); // Zakładam, że masz taką metodę w DAO

        // 2. Zaktualizuj model tabeli
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel(); // Zakładając, że masz JTable o nazwie reservationsTable
        tableModel.setRowCount(0); // Najpierw wyczyść tabelę

        // 3. Dodaj zaktualizowane dane do modelu tabeli
        for (Reservation reservation : updatedReservations) {
            Object[] row = new Object[]{
                    reservation.getId(),
                    reservation.getFullName(),
                    reservation.getGuestsNumber(),
                    reservation.getHour(),
                    reservation.getReservationDate(),
                    reservation.getPhone(),
                    reservation.getEmail(),
                    (reservation.isConfirmed() ? "Yes" : "No"),
            };
            tableModel.addRow(row);
        }

        // 4. Odśwież widok tabeli (tabela powinna się odświeżyć automatycznie, ale warto to zrobić ręcznie)
        table.repaint();
    }

    private void deleteReservation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation from the table.");
            return;
        }

        try {
            // Pobranie ID rezerwacji z tabeli
            String reservationIdStr = table.getValueAt(selectedRow, 0).toString();
            int reservationId = Integer.parseInt(reservationIdStr);

            // Usuwanie rezerwacji z bazy danych
            ReservationDAO.deleteReservation(reservationId);

            // Usunięcie wiersza z tabeli
            ((DefaultTableModel) table.getModel()).removeRow(selectedRow);

            JOptionPane.showMessageDialog(this, "Reservation deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting reservation: " + e.getMessage());
        }
    }

    private void saveUpdatedReservation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation from the table.");
            return;
        }

        try {
            // Pobranie ID rezerwacji z tabeli
            String reservationIdStr = table.getValueAt(selectedRow, 0).toString();
            int reservationId = Integer.parseInt(reservationIdStr);

            // Pobranie istniejącej rezerwacji z bazy danych
            Reservation reservation = ReservationDAO.getReservationById(reservationId);
            if (reservation == null) {
                JOptionPane.showMessageDialog(this, "Reservation not found.");
                return;
            }

            // Aktualizacja obiektu Reservation na podstawie danych z formularza
            reservation.setFullName(fullNameField.getText());
            reservation.setGuestsNumber(guestsField.getText());
            reservation.setHour(hourField.getText());
            reservation.setReservationDate(LocalDate.parse(dateField.getText())); // Konwersja na SQL Date
            reservation.setPhone(phoneField.getText());
            reservation.setEmail(emailField.getText());
            reservation.setConfirmed(confirmedCheckBox.isSelected());
            reservation.setNotes(notesArea.getText());

            // Zapisanie zaktualizowanej rezerwacji w bazie danych
            ReservationDAO.updateReservation(reservation);

            JOptionPane.showMessageDialog(this, "Reservation updated successfully.");
            // Odświeżenie danych w tabeli
            reservations = ReservationDAO.getAllReservations();
            updateSingleRow(selectedRow); // zaktualizuj tylko wybrany wiersz
            //updateTableModel();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating reservation: " + e.getMessage());
        }
    }

    private void displaySelectedNotes() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String reservationIdStr = table.getValueAt(selectedRow, 0).toString();
            int reservationId = Integer.parseInt(reservationIdStr);
            Reservation reservation = ReservationDAO.getReservationById(reservationId);

            if (reservation != null) {
                // Wypełnienie pól formularza danymi z obiektu Reservation
                fullNameField.setText(reservation.getFullName());
                guestsField.setText(String.valueOf(reservation.getGuestsNumber()));
                hourField.setText(reservation.getHour());
                dateField.setText(reservation.getReservationDate().toString());
                phoneField.setText(reservation.getPhone());
                emailField.setText(reservation.getEmail());
                confirmedCheckBox.setSelected(reservation.isConfirmed());
                notesArea.setText(reservation.getNotes());
            }

        }
    }


    private void updateSingleRow(int rowIndex) {
        Reservation r = reservations.get(rowIndex);

        table.setValueAt(r.getId(), rowIndex, 0);
        table.setValueAt(r.getFullName(), rowIndex, 1);
        table.setValueAt(r.getGuestsNumber(), rowIndex, 2);
        table.setValueAt(r.getHour(), rowIndex, 3);
        table.setValueAt(r.getReservationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), rowIndex, 4);
        table.setValueAt(r.getPhone(), rowIndex, 5);
        table.setValueAt(r.getEmail(), rowIndex, 6);
        table.setValueAt(r.isConfirmed() ? "Yes" : "No", rowIndex, 7);
    }

}
