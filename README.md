# BookingBookService
The backend provides a secure REST API for user authentication, book search, and managing personal book lists. It integrates with external book data sources and stores user information in a database. The system is designed for scalability, security, and easy future expansion.


1. Baza danych

   psql -U postgres

   CREATE USER viggo WITH PASSWORD 'postgres';

   CREATE DATABASE bookingBook OWNER viggo ;

   CREATE TABLE users (
   id BIGSERIAL PRIMARY KEY,
   username VARCHAR(255) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   roles VARCHAR(255) NOT NULL
   ); 

   INSERT INTO users (username, password, roles)
   VALUES
   ('john_doe', '$2a$10$AbCdEfGhIjKlMnOpQrStUv', 'ROLE_USER'),
   ('jane_smith', '$2a$10$XyZAbCdEfGhIjKlMnOpQrS', 'ROLE_USER,ROLE_ADMIN');

   SELECT id, username, password, roles FROM users;
