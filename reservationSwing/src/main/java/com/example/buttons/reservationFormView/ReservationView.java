package com.example.buttons.reservationFormView;

import com.example.buttons.ReservationDAO;
import com.example.buttons.adminView.adminViewComponents.ReservationTableBuilder;
import com.example.buttons.reservationFormView.reservationComponents.*;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ReservationView extends JPanel{

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JDateChooser dateChooser;
    private JComboBox<String> timeComboBox;
    private JComboBox<String> guestsComboBox;
    private String formattedDate;
    private ReservationTableBuilder tableBuilder;

    public ReservationView(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new GridBagLayout());
        initUI(cardLayout, mainPanel);
    }

    private void initUI(CardLayout cardLayout, JPanel mainPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); //odstępy miedzy komponentami

        // Etykieta reservation form
        JLabel mainLabel = new JLabel("RESERVATION FORM:");
        mainLabel.setFont(new Font("Arial", Font.BOLD,20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        add(mainLabel, gbc);

        // Etykieta: imie i nazwisko
        JLabel nameLabel = new JLabel("Full name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        add(nameLabel, gbc);

        // Pole tekstowe do wpisania imienia i nazwiska
        nameField = new JTextField(20);
        nameField.setDocument(new FullNameDocument());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(nameField, gbc);

        // Etykieta: Email
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        add(emailLabel, gbc);

        // Pole tekstowe do email
        emailField = new JTextField(20);
        emailField.setDocument(new EmailDocument());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(emailField, gbc);

        // Etykieta: Phone number
        JLabel phoneLabel = new JLabel("Phone number:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        add(phoneLabel, gbc);

        // Pole tekstowe do phone number
        phoneField = new JTextField(20);
        phoneField.setDocument(new PhoneDocument());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(phoneField, gbc);

        // Etykieta: Date
        JLabel dateLabel = new JLabel("Date:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        add(dateLabel, gbc);

        // Pole wyboru do Date
        dateChooser = new JDateChooser();
        // Ustawiamy minimalna date na dzisiejszy dzień
        Calendar calendar = Calendar.getInstance();
        dateChooser.setMinSelectableDate(calendar.getTime());
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setEditable(false);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(dateChooser, gbc);

        // Etykieta: Time
        JLabel hourLabel = new JLabel("Time:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        add(hourLabel, gbc);

        // Pole wyboru do Time
        timeComboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(timeComboBox, gbc);

        new HourSelector(timeComboBox, dateChooser);
        timeComboBox.setSelectedIndex(-1);

        // Etykieta: guest
        JLabel guestsLabel = new JLabel("Total guests:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        add(guestsLabel, gbc);

        // Pole wyboru do guests
        String[] guestsNumber = {"1", "2", "3", "4", "5", "6", "7", "8"};
        guestsComboBox = new JComboBox<>(guestsNumber);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(guestsComboBox, gbc);
        guestsComboBox.setSelectedIndex(-1);

        // Pole na Notes
        JLabel notesLabel = new JLabel("Notes:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        add(notesLabel, gbc);

        // Pole na notatki notes
        JTextArea notesArea = new JTextArea(5,20);
        notesArea.setLineWrap(true);//zawijanie tekstu
        notesArea.setWrapStyleWord(true);//zawijanie wyrazow
        JScrollPane scrollPane = new JScrollPane(notesArea);//jscroll aby było możliwe przewijanie
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(scrollPane, gbc);


        // Przycisk: submit
        JButton submitButton = new JButton("Submit Reservation");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ReservationFromValidator.isFromValid(nameField, emailField, phoneField, dateChooser, timeComboBox, guestsComboBox)) {
                    JOptionPane.showMessageDialog(ReservationView.this, "Please fill all required fields", "Error", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                Date selectedDate = dateChooser.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                formattedDate = dateFormat.format(selectedDate);

                ReservationDAO.saveReservation(nameField.getText(),
                        Objects.requireNonNull(guestsComboBox.getSelectedItem()).toString(),
                        Objects.requireNonNull(timeComboBox.getSelectedItem()).toString(),
                        formattedDate,
                        phoneField.getText(),
                        emailField.getText(),
                        notesArea.getText());

                JOptionPane.showMessageDialog(ReservationView.this, "Reservation send", "Success!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);

        // Dodanie pustego komponentu aby dodac pusta przestrzen i dac back na sam dol
        gbc.gridy = 9;
        gbc.weighty = 1; // Pozwalamy na wypełnienie dostępnego miejsca
        add(new JLabel(""), gbc); // Pusty komponent

        // Przycisk: back
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            // Zmieniamy widok na główny
            setSize(400, 500);
            cardLayout.show(mainPanel, "MainView");

            // Wyczyść wszystkie pola formularza
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            dateChooser.setDate(null); // Resetujemy datę
            timeComboBox.setSelectedIndex(-1); // Resetujemy wybór czasu
            notesArea.setText(""); // Wyczyść pole z notatkami
        });
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        add(backButton, gbc);
    }
}
