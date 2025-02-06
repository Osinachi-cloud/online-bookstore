# Online Bookstore

## Project Description

The Online Bookstore is a Spring Boot application that provides a platform for users to browse, search, and purchase books. The application features an inventory of books, a shopping cart, and a simulated checkout process. Users can view their purchase history and manage their shopping cart effectively.

### Key Features
- **Book Inventory**: Manage a collection of books with attributes such as title, genre, ISBN, author, and year of publication.
- **Search Functionality**: Users can search for books by title, author, year of publication, or genre.
- **Shopping Cart**: Users can add books to their cart and view the contents.
- **Checkout Process**: Simulated payment options including Web, USSD, and Transfer.
- **Purchase History**: Users can view their past purchases.
- **Unit Testing**: Comprehensive unit tests to ensure functionality.
- **High-Level Design**: Scalable and fault-tolerant design.

## Technologies Used
- **Java**: 17
- **Spring Boot**: 3.4.2
- **PostgreSQL**: Database
- **Maven**: Dependency management
- **JUnit & Mockito**: Testing framework

## Setup Instructions

### Prerequisites
- Java 17
- PostgreSQL database
- Maven

### Database Setup
1. Install PostgreSQL and create a database named `bookstore`.
2. Update the `application.properties` file with your PostgreSQL credentials.

### Clone the Repository
```bash
git clone <repository-url>
cd OnlineBookStore
```

### Run the application
```bash
mvn clean install
mvn spring-boot:run
```

### Run the test
```bash
mvn test
```


### Summary of Additions
- **API Endpoints**: A comprehensive list of all the API endpoints, including their methods, request bodies, and expected responses.
- **Port Information**: Clear mention that the application will be accessible at `http://localhost:8090` after running.
- **Endpoint Docs**: http://localhost:8090/api/swagger-ui/index.html

This README provides a complete guide for users to set up, build, run, and interact with the Online Bookstore application.



### Preloaded book details
```bash
1. Title: To Kill a Mockingbird, Genre: FICTION, ISBN: 9780061120084, Author: Harper Lee, Year: 1960, Price: $50.00
2. Title: Gone Girl, Genre: THRILLER, ISBN: 9780307588371, Author: Gillian Flynn, Year: 2012, Price: $60.00
3. Title: The Girl with the Dragon Tattoo, Genre: MYSTERY, ISBN: 9780307949486, Author: Stieg Larsson, Year: 2005, Price: $70.00
4. Title: The Waste Land, Genre: POETRY, ISBN: 9780571202405, Author: T.S. Eliot, Year: 1922, Price: $80.00
5. Title: Dracula, Genre: HORROR, ISBN: 9780486411095, Author: Bram Stoker, Year: 1897, Price: $90.00
6. Title: American Psycho, Genre: SATIRE, ISBN: 9780679735779, Author: Bret Easton Ellis, Year: 1991, Price: $100.00
7. Title: The Shining, Genre: HORROR, ISBN: 9780307743657, Author: Stephen King, Year: 1977, Price: $110.00
8. Title: The Silence of the Lambs, Genre: THRILLER, ISBN: 9780312924584, Author: Thomas Harris, Year: 1988, Price: $120.00
9. Title: And Then There Were None, Genre: MYSTERY, ISBN: 9780062073488, Author: Agatha Christie, Year: 1939, Price: $130.00
10. Title: Catch-22, Genre: SATIRE, ISBN: 9781451626650, Author: Joseph Heller, Year: 1961, Price: $140.00

```

### Preloaded User details
```bash
1. username: username1
2. username: username2

```

### To access the endpoints 
```bash


```




