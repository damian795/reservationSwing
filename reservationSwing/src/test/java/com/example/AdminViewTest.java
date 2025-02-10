package com.example;

import com.example.buttons.ReservationDAO;
import com.example.hibernate.Reservation;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import javax.swing.*;

import java.util.List;

import static org.assertj.swing.data.TableCell.row;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AdminViewTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;


    @Override
    protected void onSetUp() {
        // Zapobieganie błędom Swing EDT
        FailOnThreadViolationRepaintManager.install();

        // Uruchomienie aplikacji Swing
        SwingUtilities.invokeLater(MainWindow::new);

        // Znalezienie okna aplikacji
        window = WindowFinder.findFrame(MainWindow.class).using(robot());
    }

    @Test
    public void testTableData() {
        // Kliknięcie przycisku "Admin", aby przejść do AdminView
        window.button("Admin").click();

        // Znalezienie tabeli
        JTableFixture table = window.table();

        // Sprawdzenie, czy tabela ma co najmniej jeden wiersz
        assertTrue(table.rowCount() > 0, "The table should contain at least one row of data.");

        // Pobranie wartości z pierwszej komórki (pierwszy wiersz, pierwsza kolumna)
        String fullName = table.cell(row(0).column(0)).value();
        assertNotNull(fullName, "The 'Full Name' field should not be empty.");
    }


    @Test
    public void testDeleteReservation() {
        // Zapis do bazy rezerwacji
        ReservationDAO.saveReservation("TestName", "2", "12:00", "2025/02/20", "123123123", "test@gmail.com", "test");

        // Pobieramy liczbe rezerwacji przed usunieciem
        List<Reservation> reservationsListBefore = ReservationDAO.getAllReservations();
        int initialRowCount = reservationsListBefore.size();

        // Pobieramy id zapisanego elementu
        Reservation lastReservation = reservationsListBefore.get(reservationsListBefore.size()-1);
        int reservationId = (int) lastReservation.getId();

        // Usuwamy rezerwacje
        ReservationDAO.deleteReservation(reservationId);

        List<Reservation> reservationsListAfter = ReservationDAO.getAllReservations();
        int finalRowCount = reservationsListAfter.size();

        // Sprawdzamy czy liczba rezerwacji zmniejsyzla sie o jeden
        assertEquals(initialRowCount - 1, finalRowCount);
    }

}
