# BookingBookService
The backend provides a secure REST API for user authentication, book search, and managing personal book lists. It integrates with external book data sources and stores user information in a database. The system is designed for scalability, security, and easy future expansion.


1. Baza danych

   psql -U postgres

   CREATE USER viggo WITH PASSWORD 'postgres';

   CREATE DATABASE bookingBook OWNER viggo ;
    