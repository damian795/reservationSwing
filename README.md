# ReservationSwing

## Opis projektu

ReservationSwing to aplikacja napisana w Javie z użyciem Swing, służąca do zarządzania rezerwacjami. Aplikacja zawiera widok dla administratora oraz formularz rezerwacyjny dla użytkowników.

## Technologie

- **Java** (JDK 8+)
- **Swing** (GUI)
- **Maven** (zarządzanie zależnościami)
- **Hibernate** (ORM)
- **MySQL** (baza danych, obsługiwana przez XAMPP)
- **JUnit** (testowanie jednostkowe)
- **AssertJ** (asercje w testach)
- **Swing** (testowanie interfejsu użytkownika)

## Instalacja i uruchomienie

1. **Klonowanie repozytorium:**
   ```sh
   git clone https://github.com/twoj-username/reservationSwing.git
   cd reservationSwing
   ```
2. **Konfiguracja bazy danych:**
   - Uruchom XAMPP i włącz MySQL.
   - Utwórz bazę danych `restaurant_swing`.
   - Zaimportuj strukturę bazy danych (jeśli dostępna).
3. **Kompilacja i uruchomienie:**
   ```sh
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.example.Main"
   ```

## Testy

Projekt zawiera testy jednostkowe umieszczone w katalogu `src/test`. Aby uruchomić testy, użyj polecenia:

```sh
mvn test
```

