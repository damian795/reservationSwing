package com.example.buttons.reservationFormView.reservationComponents;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;

public class ReservationFromValidator {
    public static boolean isFromValid(JTextField nameField, JTextField emailField,
                                      JTextField phoneField, JDateChooser dateChooser,
                                      JComboBox<String> timeComboBox, JComboBox<String> guestsComboBox) {

        return !nameField.getText().isEmpty() &&
                !emailField.getText().isEmpty() &&
                !phoneField.getText().isEmpty() &&
                dateChooser.getDate() != null &&
                timeComboBox.getSelectedItem() != null &&
                guestsComboBox.getSelectedItem() != null;
    }
}
