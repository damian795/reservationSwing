package com.example.buttons;

import com.example.hibernate.HibernateUtil;
import com.example.hibernate.Reservation;
import org.hibernate.Session;

import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationDAO {

    public static Reservation getReservationById(int reservationId) {
        Reservation reservation = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            reservation = session.get(Reservation.class, reservationId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservation;
    }

    public static void updateReservation(Reservation reservation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Aktualizacja obiektu w bazie danych
            session.update(reservation);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Reservation> getAllReservations() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            List<Reservation> reservations = session.createQuery("FROM Reservation", Reservation.class).getResultList();
            session.getTransaction().commit();
            return reservations;
        }
    }

    public static void saveReservation(String fullName, String guests, String hour, String date, String phone, String email, String notes) {
        Reservation reservation = new Reservation();
        reservation.setFullName(fullName);
        reservation.setGuestsNumber(guests);
        reservation.setPhone(phone);
        reservation.setEmail(email);
        reservation.setHour(hour);
        reservation.setNotes(notes);

        // Konwersja daty
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(date, formatter); // Przekształcenie daty w String na LocalDate
        reservation.setReservationDate(localDate);

        // Zapis rezerwacji do bazy
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(reservation);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteReservation(int reservationId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM Reservation WHERE id = :reservationId";  // Poprawna składnia
            Query query = session.createQuery(hql);
            query.setParameter("reservationId", reservationId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
