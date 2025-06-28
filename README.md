# BookingBookService

The backend provides a secure REST API for user authentication, book search, and managing personal book lists. It
integrates with external book data sources and stores user information in a database. The system is designed for
scalability, security, and easy future expansion.

Recommended JDK Oracle JDK 24 oracle.com/java/technologies/downloads

I. Database

psql -U postgres

CREATE DATABASE bookingbook;
DROP DATABASE bookingbook;

II.Google Books API

1. Go to Google Cloud Console
   Open the page: https://console.cloud.google.com/

2. Log in to your Google account
3. Create a new project (or select an existing one)
   Click the project name on the top bar and select “New Project”.

Give it a name and click “Create”.

4. Enable Google Books API
   In the left menu, select APIs & Services → Library.

Search for Books API or Google Books API.

Click on the result and select Enable.

5. Go to the “Credentials” tab
   In the left menu, click APIs & Services → Credentials.

6. Create a new API key
   Click + Create Credentials → API Key.

The key will be generated and displayed on the screen.

III. Security

The application is secured with JWT + apiKey. You need to set your own private applications in the
application.properties file.

#Security
security.api.key=

#JWT Configuration
bookingbook.app.jwtSecret=

#Google
google.books.api.key=