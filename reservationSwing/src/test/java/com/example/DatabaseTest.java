package com.example;

import com.example.buttons.ReservationDAO;
import com.example.hibernate.Reservation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    @After
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }

        if (session != null) {
            session.close();
        }
    }

    @Test
    public void testConnectionToDatabase() {
        try (Session session = sessionFactory.openSession()){
            assertNotNull(session, "Session should be not null");
        }
    }

    @Test
    public void testSaveReservationToDatabase() {
        //Tworzymy rezerwacje
        Reservation reservation = new Reservation();
        reservation.setFullName("Karolina Koralik");
        reservation.setPhone("123123123");
        reservation.setEmail("koralik@gmail.com");
        reservation.setReservationDate(LocalDate.parse("2025-12-02"));
        reservation.setConfirmed(false);

        //Zapisanie obiektu do bazy danych
        Transaction transaction = session.beginTransaction();
        session.save(reservation);
        transaction.commit();

        //Sprawdzanie
        //Po zapisaniu obiektu odczytujemy go z bazy danych aby sprawdzic czy dane zostaly zapisane
        Reservation savedReservation = session.get(Reservation.class, reservation.getId());
        //Sprawdzamy czy obiekt zostal zapisany (nie jest null)
        assertNotNull(savedReservation);
        //Sprawdzamy czy pelne imie zapisane w bazie jest takie samo jak to ktore ustawilismy
        assertEquals(reservation.getFullName(), savedReservation.getFullName());
    }

    @Test
    public void testGetAllReservationsMethodFromReservationDAO() {
        // Pobieramy rezerwacje zakładajac ze juz jakies sa
        List<Reservation> reservations = ReservationDAO.getAllReservations();

        // Upewniamy sie ze lista nie jest pusta
        assertNotNull(reservations,  "Reservations list should not be null.");
        assertTrue(reservations.size() > 0, "Reservations list should contain at least one reservation.");

        // Jezeli lista nie jest pusta to sprawdzamy szczegoly np pierwsze rezerwacji
        Reservation firstReservation = reservations.get(0);
        assertNotNull(firstReservation.getFullName(), "Full name should not be null.");
        assertNotNull(firstReservation.getReservationDate(), "Reservation date should not be null.");
    }
}
