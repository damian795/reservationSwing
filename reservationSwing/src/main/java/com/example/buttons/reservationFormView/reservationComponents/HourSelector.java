package com.example.buttons.reservationFormView.reservationComponents;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class HourSelector {
    private JComboBox<String> timeComboBox;
    private JDateChooser dateChooser;

    public HourSelector(JComboBox<String> timeComboBox, JDateChooser dateChooser) {
        this.timeComboBox = timeComboBox;
        this.dateChooser = dateChooser;
        initializeTime();
        setupDateListener();
    }

    private void setupDateListener() {
        dateChooser.getDateEditor().addPropertyChangeListener( e -> {
            if (dateChooser.getDate() != null && isSameDay(dateChooser.getDate())) {
                blockPastHours();
            }
        });
    }

    private void blockPastHours() {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

        for (int i = 0; i <timeComboBox.getItemCount(); i++) {
            String time = timeComboBox.getItemAt(i);
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            if (hour < currentHour || (hour == currentHour && minute <= currentMinute)) {
              timeComboBox.removeItemAt(i);
              i--;
            }
        }
    }

    private boolean isSameDay(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTime(date);

        //jezeli true to wybrana data jest dzisiaj
        return today.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
    }

    private void initializeTime() {
        //godziny od 12:00 do 20:00 co pol godziny
        String[] hours = new String[17];
        int index = 0;
        for (int i = 12; i <= 20; i++){
            hours[index++] = String.format("%02d:00", i); //12:00 itp
            if(i < 20) {
                hours[index++] = String.format("%02d:30", i); //12:30 itp
            }
        }
        timeComboBox.setModel(new DefaultComboBoxModel<>(hours));
    }
}
