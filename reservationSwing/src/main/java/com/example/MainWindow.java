package com.example;

import com.example.buttons.adminView.AdminView;
import com.example.buttons.reservationFormView.ReservationView;
import com.example.hibernate.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainWindow() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tworzenie głównego widoku
        JPanel mainView = new JPanel();
        mainView.setLayout(new GridBagLayout());  // Zmieniamy układ na GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Dodanie odstępów między komponentami

        // Tworzenie przycisków
        JButton buttonReservation = new JButton("Book a table");
        JButton buttonAdmin = new JButton("Admin");

        // Zmiana rozmiaru przysickow
        Dimension buttonSize = new Dimension(150,50);
        buttonReservation.setPreferredSize(buttonSize);
        buttonAdmin.setPreferredSize(buttonSize);

        buttonReservation.setFont(new Font("Arial", Font.PLAIN, 20));  // Zwiększamy rozmiar czcionki do 20
        buttonAdmin.setFont(new Font("Arial", Font.PLAIN, 20));  // Zwiększamy rozmiar czcionki do 20

        buttonReservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSize(400, 500);
                setLocationRelativeTo(null);
                cardLayout.show(mainPanel, "ReservationView");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainView.add(buttonReservation,gbc);

        buttonAdmin.setName("Admin");
        buttonAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSize(1000,500);
                setLocationRelativeTo(null);
                cardLayout.show(mainPanel, "AdminView");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainView.add(buttonAdmin,gbc);

        // Tworzenie widoku rezerwacji i dodanie do głównego panela
        ReservationView reservationView = new ReservationView(cardLayout, mainPanel);
        AdminView adminView = new AdminView(cardLayout, mainPanel, this);

        // Dodawanie widoków do głównego panelu
        mainPanel.add(mainView, "MainView");
        mainPanel.add(reservationView, "ReservationView");
        mainPanel.add(adminView, "AdminView");

        // Ustawienie CardLayout na głównym panelu
        cardLayout.show(mainPanel, "MainView");

        // Dodanie głównego panela do okna
        add(mainPanel);
        setTitle("Reservation App");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApp();
            }
        });
        setVisible(true);
    }

    private void closeApp() {
        HibernateUtil.shutdown();
        System.exit(0);
    }
}
