# BookingBookService

The backend provides a secure REST API for user authentication, book search, and managing personal book lists. It
integrates with external book data sources and stores user information in a database. The system is designed for
scalability, security, and easy future expansion.



Rekomendowane JDK Oracle JDK 24  oracle.com/java/technologies/downloads

I. Baza danych

   psql -U postgres

CREATE DATABASE bookingbook;

\c bookingbook

CREATE TABLE users (
id SERIAL PRIMARY KEY,
username VARCHAR(255) NOT NULL UNIQUE,
email VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL
);


DROP TABLE IF EXISTS users_roles CASCADE;

CREATE TABLE users_roles (
user_id bigint NOT NULL
REFERENCES users(id) ON DELETE CASCADE,
role    varchar(50) NOT NULL
);

GRANT ALL PRIVILEGES ON DATABASE bookingbook TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;
ALTER TABLE users OWNER TO postgres;
ALTER TABLE users_roles OWNER TO postgres;

INSERT INTO users (username, email, password)
VALUES
('ewa', 'ewa@example.com', 'mojeHaslo123'),
('adam', 'adam@example.com', 'mojeHaslo123');


INSERT INTO users_roles (user_id, roles)
VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

SELECT u.id, u.username, u.password, ur.roles
FROM users u
LEFT JOIN users_roles ur ON u.id = ur.user_id;

II.Google Books API

1. Wejdź na Google Cloud Console
   Otwórz stronę: https://console.cloud.google.com/

2. Zaloguj się na swoje konto Google
3. Utwórz nowy projekt (lub wybierz istniejący)
   Kliknij nazwę projektu w górnej belce i wybierz „Nowy projekt”.

Nadaj nazwę i kliknij „Utwórz”.

4. Włącz Google Books API
   W menu po lewej wybierz API i usługi → Biblioteka.

Wyszukaj Books API lub Google Books API.

Kliknij na wynik i wybierz Włącz.

5. Przejdź do zakładki „Dane logowania” (Credentials)
   W menu po lewej kliknij API i usługi → Dane logowania.

6. Utwórz nowy klucz API
   Kliknij + Utwórz dane logowania (Create credentials) → Klucz API (API key).

Klucz zostanie wygenerowany i wyświetlony na ekranie.

