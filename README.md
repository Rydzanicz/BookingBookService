# BookingBookService

The backend provides a secure REST API for user authentication, book search, and managing personal book lists. It
integrates with external book data sources and stores user information in a database. The system is designed for
scalability, security, and easy future expansion.

1. Baza danych

   psql -U postgres

CREATE DATABASE bookingbook;

\c bookingbook

CREATE TABLE users (
id SERIAL PRIMARY KEY,        
username VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL         
);

CREATE TABLE users_roles (
user_id BIGINT NOT NULL,      
roles VARCHAR(100) NOT NULL,  
PRIMARY KEY (user_id, roles),
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

GRANT ALL PRIVILEGES ON DATABASE bookingbook TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;
ALTER TABLE users OWNER TO postgres;
ALTER TABLE users_roles OWNER TO postgres;

INSERT INTO users (username, password)
VALUES
('ewa', '$2a$10$XMcRhkMPWboQvXAlJKyCa.RLVY0kzKfIUNJDcjFgTnz5EHzDY6YmO'),
('adam', '$2a$10$XMcRhkMPWboQvXAlJKyCa.RLVY0kzKfIUNJDcjFgTnz5EHzDY6YmO');

INSERT INTO users_roles (user_id, roles)
VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

SELECT u.id, u.username, u.password, ur.roles
FROM users u
LEFT JOIN users_roles ur ON u.id = ur.user_id;