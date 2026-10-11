### Schema Design & Table Creation (DDL & DML Commands)

1. Define a schema for a Library Management System with the following entities:-
    Books- Authors- Members- Borrow_Records

```sql
CREATE DATABASE lib_mgmt;
USE lib_mgmt;
```

2. Write the SQL command to create a table Authors with the following fields:
   - author_id (Primary Key, INT)
   - name (VARCHAR(100))
   - country (VARCHAR(50))

```sql
CREATE TABLE Authors(
    author_id INT PRIMARY KEY,
    name VARCHAR(100) ,
    country VARCHAR(50)
);
```

3. Write the SQL command to create a table Books with the following fields:
   
   - book_id (Primary Key, INT)
   
   - title (VARCHAR(150))
   
   - author_id (Foreign Key referencing Authors)
   
   - published_year (YEAR)
   
   - available_copies (INT)

```sql
CREATE TABLE Books(
    book_id INT PRIMARY KEY,
    title VARCHAR(150),
    author_id INT,
    published_year YEAR,
    available_copies INT,
    FOREIGN KEY (author_id) REFERENCES Authors(author_id)
);
```

4. Write the SQL command to create a table Members with
   
   - member_id (Primary Key, INT)
   
   - name (VARCHAR(100))
   
   - email (VARCHAR(100) unique)
   
   - phone (VARCHAR(15))

```sql
CREATE TABLE Members(
    member_id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15)
);
```

5. Write the SQL command to create a table Borrow_Records with
   
   - record_id (Primary Key, INT)
   
   - member_id (Foreign Key referencing Members)
   
   - book_id (Foreign Key referencing Books)
   
   - borrow_date (DATE)
   
   - return_date (DATE)

```sql
CREATE TABLE Borrow_Records(
    record_id INT PRIMARY KEY,
    member_id INT,
    book_id INT,
    borrow_date DATE,
    return_date DATE,
    FOREIGN KEY member_id REFERENCES Members(member_id),
    FOREIGN KEY book_id REFERENCES Books(book_id)
);
```

6. Modify the Books table to add a column genre of type VARCHAR(50).

```sql
ALTER TABLE Books
ADD genre VARCHAR(50);

```

7. Write the SQL command to drop the Borrow_Recordstable

```sql
DROP TABLE Borrow_Records;
```

8. Insert 3 records into the Authors table

```sql
INSERT INTO Authors (author_id ,name ,country)
VALUES
(101,'PRAJWAL','INDIA'),
(102,'PRANAV','INDIA'),
(103,'RUTUJA','INDIA');
```

9. Insert 5 books into the Books table

```sql
INSERT INTO Books(book_id ,title ,author_id ,published_year
                   ,available_copies ,genre)
VALUES
(01,'MY STANDUP',101,2026,100,'COMICS'),
(02,'MR.BEAN',102,2022,100,'COMICS'),
(03,'INDAIN LIFE',103,2023,100,'LIFE STYLE'),
(04,'Database Systems',102,2021,100,'TECH'),
(05,'Database Systems',101,2023,100,'TECH');
```

10. Insert 3 members into the Members table

```sql
INSERT INTO Members (member_id, name, email, phone)
VALUES
(1, 'Rahul Sharma', 'rahul@gmail.com', '9876543210'),
(2, 'Priya Verma', 'priya@gmail.com', '9876543211'),
(3, 'Amit Patel', 'amit@gmail.com', '9876543212');
```

11. Insert 4 borrow records into the Borrow_Records table

```sql
INSERT INTO Borrow_Records
(record_id, member_id, book_id, borrow_date, return_date)
VALUES
(1, 1, 01, '2026-10-01', '2026-10-08'),
(2, 2, 02, '2026-10-02', NULL),
(3, 3, 03, '2026-10-03', '2026-10-09'),
(4, 1, 02, '2026-10-05', NULL);
```

12. Write an SQL query to select all books where published_year is after 2015

```sql
SELECT * FROM Books
WHERE published_year>2015;
```

13. Write a SQL query to create a foreign key & primary key relationship between two tables.

```sql
-- ALREADY DEFINED DURING THE CREATION SO NO NEED TO DO HERE.
```

14. Write an SQL query to find all members who have borrowed the book with title Database Systems

```sql
SELECT DISTINCT M.*
FROM MEMBERS AS M
--CONNECTS THE MEMBERS ID WITH BORROW RECORDS USING MEMBER_ID
JOIN BORROW_RECORDS AS BR
ON M.MEMBER_ID=BR.MEMBER_ID
--CONNECTS THE BORROW RECORDS WITH BOOKS USING BOOK_ID
JOIN BOOKS AS B
ON BR.BOOK_ID=B.BOOK_ID
WHERE B.TITLE='Database Systems';
```

15. Update the available_copies column of a specific book (choose any book) by reducing it by 1 after it is borrowed

```sql
UPDATE Books
SET available_copies = available_copies - 1
WHERE book_id = 1;
```



16. Delete a record from Members where member_id = 3

```sql
DELETE FROM Members
WHERE member_id = 3;
```

17. Update a Book name record from Book table with id = 1.

```sql
UPDATE Books
SET title = 'JAVA'
WHERE book_id = 1;
```

18. Write an SQL query to list all books along with their authors' names.

```sql
SSELECT B.TITLE, A.NAME
FROM BOOKS AS B
JOIN AUTHORS AS A
    ON A.AUTHOR_ID = B.AUTHOR_ID;
```

19. Write an SQL query to delete all books from the Books table where the published_year is before 2000.

```sql
DELETE FROM BOOKS
WHERE published_year<2000;  
```

20. Write an SQL query to find all books that are never borrowed (i.e., no records in Borrow_Records).

```sql
SELECT B.TITLE
FROM BOOKS AS B
LEFT JOIN BORROW_RECORDS AS BR
    ON B.BOOK_ID = BR.BOOK_ID
WHERE BR.RECORD_ID IS NULL;
```
