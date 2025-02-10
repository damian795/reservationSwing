package com.example.buttons.adminView.adminViewComponents;

import com.example.hibernate.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationTableBuilder {
    public static JScrollPane createTable(List<Reservation> reservations) {
        // Nagłówki kolumn tabeli
        String[] columnNames = {"ID", "Full Name", "Guests", "Hour", "Date", "Phone", "Email", "Conf"};

        Object[][] data = new Object[reservations.size()][columnNames.length];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0 ; i < reservations.size(); i++){
            Reservation r = reservations.get(i);
            data[i][0] = r.getId();
            data[i][1] = r.getFullName();
            data[i][2] = r.getGuestsNumber();
            data[i][3] = r.getHour();
            data[i][4] = r.getReservationDate().format(formatter);
            data[i][5] = r.getPhone();
            data[i][6] = r.getEmail();
            data[i][7] = r.isConfirmed() ? "Yes" : "No";
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);

        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(20);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(50);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(30);
        table.setPreferredScrollableViewportSize(new Dimension(700,200));

        return new JScrollPane(table);
    }
}
