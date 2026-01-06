# Library Management System - Spring Boot

A comprehensive Library Management System built with Spring Boot that provides RESTful APIs for managing books, book copies, and tracking issue history.

## 🚀 Technologies Used

- **Java 17**
- **Spring Boot 4.0.1**
- **Spring Data JPA**
- **MySQL Database**
- **Maven** (Build Tool)
- **Lombok** (Optional)

## 📋 Features & Functionalities

### 1. Book Management

- **Add New Book**: Create and store new books in the library
- **Get All Books**: Retrieve a list of all books in the library
- **Get Book by ID**: Fetch specific book details by its ID

### 2. Book Copy Management

- **Create Book Copy**: Add physical copies of a book with unique identifiers
- **Get Copies by Book**: Retrieve all copies of a specific book
- **Issue Book Copy**: Issue a book copy to a member (marks as unavailable)
- **Return Book Copy**: Return an issued book copy (marks as available)

### 3. Issue History Tracking

- **Get All Issue History**: View complete transaction history
- **Get History by Book ID**: View all issue/return transactions for a specific book
- **Get History by Copy ID**: View all issue/return transactions for a specific book copy

### 4. Exception Handling

- Global exception handler for consistent error responses
- Proper HTTP status codes for different error scenarios

## 🗄️ Database Schema

### Entities

#### Book

- `id` (Long, Primary Key)
- `title` (String, Required)
- `author` (String)
- `isbn` (String, Required, Unique)
- `publisher` (String)
- `category` (String)

#### BookCopy

- `id` (Long, Primary Key)
- `copyCode` (String, Required, Unique)
- `barcode` (String, Unique)
- `status` (String: AVAILABLE, ISSUED, LOST)
- `rackLocation` (String)
- `available` (Boolean)
- `createdAt` (LocalDateTime)
- `book` (Many-to-One relationship with Book)

#### BookIssueHistory

- `id` (Long, Primary Key)
- `book` (Many-to-One relationship with Book)
- `bookCopy` (Many-to-One relationship with BookCopy)
- `issuedAt` (LocalDateTime)
- `returnedAt` (LocalDateTime)
- `status` (String: ISSUED, RETURNED)

#### Issue

- `id` (Long, Primary Key)
- `issuedTo` (String)
- `issueDate` (LocalDate)
- `bookCopy` (Many-to-One relationship with BookCopy)

## 📡 API Endpoints

### Book Endpoints

#### 1. Create a New Book

```
POST /books
Content-Type: application/json

Request Body:
{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "isbn": "978-0-7432-7356-5",
  "publisher": "Scribner",
  "category": "Fiction"
}

Response: Book object with generated ID
```

#### 2. Get All Books

```
GET /books

Response: List of all books
```

#### 3. Get Book by ID

```
GET /books/{id}

Response: Book object
Error: 404 if book not found
```

### Book Copy Endpoints

#### 4. Create a Book Copy

```
POST /book-copies/{bookId}
Content-Type: application/json

Request Body:
{
  "copyCode": "BC001",
  "barcode": "1234567890",
  "rackLocation": "A-1-2"
}

Response: BookCopyResponse object
Error: 404 if book not found
```

#### 5. Get All Copies of a Book

```
GET /book-copies/book/{bookId}

Response: List of BookCopy objects for the specified book
```

#### 6. Issue a Book Copy

```
POST /book-copies/{copyId}/issue

Response: Updated BookCopy object (status: ISSUED, available: false)
Error:
  - 404 if copy not found
  - 400 if copy is already issued
```

#### 7. Return a Book Copy

```
POST /book-copies/{copyId}/return

Response: "Book returned successfully"
Error:
  - 404 if copy not found
  - 400 if book is already returned
```

### Issue History Endpoints

#### 8. Get All Issue History

```
GET /issue-history

Response: List of all BookIssueHistory records
```

#### 9. Get Issue History by Book ID

```
GET /issue-history/book/{bookId}

Response: List of BookIssueHistory records for the specified book
```

#### 10. Get Issue History by Copy ID

```
GET /issue-history/copy/{copyId}

Response: List of BookIssueHistory records for the specified copy
```

## ⚙️ Configuration

### Database Setup

1. Create a MySQL database:

```sql
CREATE DATABASE library_db;
```

2. Update `application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Application Properties

The application is configured to:

- Run on port **8080**
- Use MySQL database
- Auto-update database schema (`spring.jpa.hibernate.ddl-auto=update`)
- Show SQL queries in console (`spring.jpa.show-sql=true`)

## 🏃 Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Steps to Run

1. **Clone the repository** (if not already done)

2. **Configure database** in `src/main/resources/application.properties`

3. **Build the project**:

```bash
./mvnw clean install
```

4. **Run the application**:

```bash
./mvnw spring-boot:run
```

Or use the Maven wrapper:

```bash
mvnw.cmd spring-boot:run  # Windows
./mvnw spring-boot:run    # Linux/Mac
```

5. **Access the API**:
   - Base URL: `http://localhost:8080`
   - API endpoints are available at the paths mentioned above

## 📝 Example API Usage

### Complete Workflow Example

1. **Create a Book**:

```bash
curl -X POST http://localhost:8080/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot in Action",
    "author": "Craig Walls",
    "isbn": "978-1-61729-254-3",
    "publisher": "Manning",
    "category": "Technology"
  }'
```

2. **Create a Copy for the Book** (assuming book ID is 1):

```bash
curl -X POST http://localhost:8080/book-copies/1 \
  -H "Content-Type: application/json" \
  -d '{
    "copyCode": "SPRING-001",
    "barcode": "9876543210",
    "rackLocation": "Tech-A-5"
  }'
```

3. **Issue the Book Copy** (assuming copy ID is 1):

```bash
curl -X POST http://localhost:8080/book-copies/1/issue
```

4. **Return the Book Copy**:

```bash
curl -X POST http://localhost:8080/book-copies/1/return
```

5. **View Issue History**:

```bash
curl http://localhost:8080/issue-history
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/library/library/
│   │   ├── controller/          # REST Controllers
│   │   │   ├── BookController.java
│   │   │   ├── BookCopyController.java
│   │   │   └── BookIssueHistoryController.java
│   │   ├── entity/              # JPA Entities
│   │   │   ├── Book.java
│   │   │   ├── BookCopy.java
│   │   │   ├── BookIssueHistory.java
│   │   │   └── Issue.java
│   │   ├── repository/          # Data Access Layer
│   │   │   ├── BookRepository.java
│   │   │   ├── BookCopyRepository.java
│   │   │   ├── BookIssueHistoryRepository.java
│   │   │   └── IssueRepository.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── BookCopyResponse.java
│   │   │   └── CreateBookCopyRequest.java
│   │   ├── mapper/              # Entity-DTO Mappers
│   │   │   └── BookCopyMapper.java
│   │   ├── exception/           # Exception Handlers
│   │   │   └── GlobalExceptionHandler.java
│   │   └── LibraryApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── LibraryApplicationTests.java
```

## 🔒 Status Codes

- **200 OK**: Successful GET, PUT, DELETE requests
- **201 Created**: Successful POST request (resource created)
- **400 Bad Request**: Invalid request or business logic violation
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server-side error

## 🎯 Key Features

- ✅ RESTful API design
- ✅ JPA/Hibernate for database operations
- ✅ Automatic database schema updates
- ✅ Transaction history tracking
- ✅ Book copy availability management
- ✅ Global exception handling
- ✅ DTO pattern for API responses
- ✅ Entity relationships (Many-to-One)

## 📌 Notes

- The `Issue` entity exists but is not currently used in the API endpoints. The system uses `BookIssueHistory` for tracking.
- Book copies are automatically set to `AVAILABLE` status when created.
- Issue history is automatically created when a book is issued.
- The system prevents issuing already-issued books and returning already-returned books.

## 🤝 Contributing

Feel free to submit issues and enhancement requests!

## 📄 License

This project is open source and available for educational purposes.
