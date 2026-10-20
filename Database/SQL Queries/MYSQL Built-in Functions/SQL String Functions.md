# SQL STRING FUNCTIONS — COMPLETE A TO Z NOTES

**MySQL 8.0+ | Theory + Syntax + Examples + Outputs + Practice Questions**

## TABLE OF CONTENTS

1. Introduction to SQL String Functions
2. Demo Database and Sample Data
3. String Functions A–Z
4. How to Use String Functions in Queries
5. Solved SQL Practice Questions
6. Interview Questions and Answers
7. Common Mistakes
8. Quick Revision Cheat Sheet
9. Final Learning Roadmap

---

# 1\. INTRODUCTION TO STRING FUNCTIONS

## What Is a String?

A string is a sequence of characters, such as letters, numbers, spaces, and symbols, stored as text.

Examples:

```
'Hello World'
'SQL123'
'john@gmail.com'
'Mumbai'
' Amit '
```

In SQL, string data is commonly stored in columns with data types such as `CHAR`, `VARCHAR`, and `TEXT`.

## What Are SQL String Functions?

String functions are built-in SQL functions used to manipulate, examine, search, extract, compare, and format text data.

For example:

```sql
SELECT UPPER('hello');
```

**Output:**

| UPPER('hello') |
| --- |
| HELLO |

## Why Do We Use String Functions?

String functions help us:

- Convert text into uppercase or lowercase.
- Remove unnecessary spaces.
- Extract characters or words from a string.
- Search for a word or character.
- Replace one word with another.
- Combine multiple columns.
- Validate email addresses and text formats.
- Extract names, domains, codes, and prefixes.
- Clean inconsistent data.
- Prepare data for reports and analysis.

## General Syntax

```sql
SELECT FUNCTION_NAME(string_value);
```

Example:

```sql
SELECT LENGTH('DATABASE') AS result;
```

**Output:**

| result |
| --- |
| 8 |

**Important:** SQL string functions are not identical across all database systems. These notes use MySQL 8.0+ unless otherwise specified.

---

# 2\. DEMO DATABASE AND SAMPLE DATA

Use the following table throughout these notes.

## Create the Table

```sql
CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    city VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    phone VARCHAR(20)
);
```

## Insert Sample Records

```sql
INSERT INTO employees
(emp_id, first_name, last_name, email, city,
 department, salary, phone)
VALUES
(1, 'Amit', 'Sharma', 'amit.sharma@gmail.com',
 'Mumbai', 'IT', 65000, '9876543210'),

(2, 'priya', 'Patil', 'priya.patil@yahoo.com',
 'Pune', 'HR', 55000, '9876501234'),

(3, 'Rahul', 'Verma', 'rahul.verma@gmail.com',
 'Delhi', 'IT', 72000, '9123456780'),

(4, 'Neha', 'Joshi', 'neha.joshi@outlook.com',
 'Nashik', 'Finance', 60000, '9988776655'),

(5, 'Sneha', 'Kulkarni', 'sneha.k@gmail.com',
 'Mumbai', 'HR', 58000, '9000011111');
```

## Check the Data

```sql
SELECT * FROM employees;
```

**Expected records:**

| emp\_id | first\_name | last\_name | email | city | department | salary |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | Amit | Sharma | amit.sharma@gmail.com | Mumbai | IT | 65000 |
| 2 | priya | Patil | priya.patil@yahoo.com | Pune | HR | 55000 |
| 3 | Rahul | Verma | rahul.verma@gmail.com | Delhi | IT | 72000 |
| 4 | Neha | Joshi | neha.joshi@outlook.com | Nashik | Finance | 60000 |
| 5 | Sneha | Kulkarni | sneha.k@gmail.com | Mumbai | HR | 58000 |

---

# 3\. SQL STRING FUNCTIONS A–Z

## A — ASCII()

### Theory

Returns the numeric code of the first byte of a string in MySQL.

### Syntax

```sql
ASCII(string);
```

### Example

```sql
SELECT ASCII('A') AS result;
```

**Output:**

| result |
| --- |
| 65 |

Another example:

```sql
SELECT ASCII('Apple') AS result;
```

**Output:** `65`

### Real Query

```sql
SELECT
    first_name,
    ASCII(first_name) AS first_char_code
FROM employees;
```

This returns the first character's code for each employee name.

**Use:** Character encoding checks and basic character analysis.

---

## B — BIT\_LENGTH()

### Theory

Returns the number of bits used to represent a string's bytes. In MySQL, the result is the byte length multiplied by eight.

### Syntax

```sql
BIT_LENGTH(string);
```

### Example

```sql
SELECT BIT_LENGTH('ABC') AS result;
```

**Output:**

| result |
| --- |
| 24 |

**Explanation:** Three ASCII characters × eight bits = 24 bits.

### Difference Between LENGTH() and BIT\_LENGTH()

```sql
SELECT
    LENGTH('ABC') AS bytes,
    BIT_LENGTH('ABC') AS bits;
```

**Output:**

| bytes | bits |
| --- | --- |
| 3 | 24 |

**Use:** Low-level data representation and storage analysis.

---

## C — CONCAT()

### Theory

Combines two or more strings into a single string.

### Syntax

```sql
CONCAT(string1, string2, ...);
```

### Example

```sql
SELECT CONCAT('Hello', ' ', 'World') AS result;
```

**Output:** `Hello World`

### Real Query: Combine Employee Names

```sql
SELECT
    CONCAT(first_name, ' ', last_name) AS full_name
FROM employees;
```

**Output:**

| full\_name |
| --- |
| Amit Sharma |
| priya Patil |
| Rahul Verma |
| Neha Joshi |
| Sneha Kulkarni |

### CONCAT() with Multiple Values

```sql
SELECT CONCAT(
    first_name,
    ' works in ',
    department
) AS employee_info
FROM employees;
```

**Example output:** `Amit works in IT`

**Important:** In MySQL, `CONCAT()` returns `NULL` if any argument is `NULL`.

**Use:** Full names, complete addresses, display labels, and messages.

---

## C — CONCAT\_WS()

### Theory

`CONCAT_WS()` means concatenate with separator. It combines strings using a specified separator.

### Syntax

```sql
CONCAT_WS(separator, string1, string2, ...);
```

### Example

```sql
SELECT CONCAT_WS('-', '2026', '10', '20') AS result;
```

**Output:** `2026-10-20`

### Real Query

```sql
SELECT
    CONCAT_WS(' ', first_name, last_name) AS full_name
FROM employees;
```

**Example output:** `Amit Sharma`

### Difference Between CONCAT() and CONCAT\_WS()

```sql
SELECT
    CONCAT('Amit', ' ', 'Sharma') AS result1,
    CONCAT_WS(' ', 'Amit', 'Sharma') AS result2;
```

**Output:**

| result1 | result2 |
| --- | --- |
| Amit Sharma | Amit Sharma |

`CONCAT_WS()` skips `NULL` values among the string arguments after the separator. If the separator itself is `NULL`, the result is `NULL`.

**Use:** Combining names, addresses, and other fields with separators.

---

## C — CHAR\_LENGTH()

### Theory

Returns the number of characters in a string.

### Syntax

```sql
CHAR_LENGTH(string);
```

### Example

```sql
SELECT CHAR_LENGTH('HELLO') AS result;
```

**Output:** `5`

### Unicode Example

```sql
SELECT CHAR_LENGTH('café') AS result;
```

**Output:** `4`

### Real Query

```sql
SELECT
    first_name,
    CHAR_LENGTH(first_name) AS name_length
FROM employees;
```

**Use:** Name-length validation and character-count checks.

---

## C — CHARACTER\_LENGTH()

### Theory

An alternative name for `CHAR_LENGTH()` in MySQL.

### Syntax

```sql
CHARACTER_LENGTH(string);
```

### Example

```sql
SELECT CHARACTER_LENGTH('DATABASE') AS result;
```

**Output:** `8`

**Use:** Character counting. Its behavior is equivalent to `CHAR_LENGTH()`.

---

## C — CHAR()

### Theory

Returns a string made from numeric character codes.

### Syntax

```sql
CHAR(number1, number2, ...);
```

### Example

```sql
SELECT CHAR(65, 66, 67) AS result;
```

**Output:** `ABC`

### Example with ASCII Space

```sql
SELECT CONCAT('Hello', CHAR(32), 'World') AS result;
```

**Output:** `Hello World`

**Use:** Generating characters from numeric codes. Character encoding affects how codes are interpreted.

---

## E — ELT()

### Theory

Returns the string at a specified position in a list of strings. The first string is at position one.

### Syntax

```sql
ELT(index, string1, string2, ...);
```

### Example

```sql
SELECT ELT(2, 'Red', 'Green', 'Blue') AS result;
```

**Output:** `Green`

Another example:

```sql
SELECT ELT(3, 'Small', 'Medium', 'Large') AS size;
```

**Output:** `Large`

If the index is outside the available positions, the result is `NULL`.

**Use:** Selecting a label from a fixed list. For complex mappings, a `CASE` expression or lookup table is usually better.

---

## F — FIELD()

### Theory

Returns the position of a value in a list. It returns zero when there is no match.

### Syntax

```sql
FIELD(value, value1, value2, ...);
```

### Example

```sql
SELECT FIELD('Green', 'Red', 'Green', 'Blue') AS result;
```

**Output:** `2`

### Real Query

```sql
SELECT
    department,
    FIELD(department, 'IT', 'HR', 'Finance') AS dept_position
FROM employees;
```

**Example output:**

| department | dept\_position |
| --- | --- |
| IT | 1 |
| HR | 2 |
| IT | 1 |

**Use:** Ordering values according to a predefined list. Remember that `FIELD()` returns zero for an unmatched value.

---

## F — FIND\_IN\_SET()

### Theory

Searches for a string inside a comma-separated list and returns its position.

### Syntax

```sql
FIND_IN_SET(search_string, comma_separated_list);
```

### Example

```sql
SELECT FIND_IN_SET('SQL', 'Java,Python,SQL,PHP') AS result;
```

**Output:** `3`

If the value is absent, the function returns zero. If either argument is `NULL`, the result is `NULL`.

### Another Example

```sql
SELECT FIND_IN_SET('IT', 'HR,IT,Finance') AS result;
```

**Output:** `2`

**Use:** Working with comma-separated legacy data.

**Best practice:** Avoid storing multiple values in a single comma-separated database column when a normalized related table can represent them properly.

---

## H — HEX()

### Theory

Returns the hexadecimal representation of a string or number.

### Syntax

```sql
HEX(string_or_number);
```

### Example

```sql
SELECT HEX('ABC') AS result;
```

**Output:** `414243`

**Explanation:**

- A = `41`
- B = `42`
- C = `43`

These are hexadecimal byte values in an ASCII-compatible encoding.

**Use:** Inspecting encoded data and debugging byte representations.

---

## I — INSERT()

### Theory

In MySQL, `INSERT()` replaces a section of a string with another string. It does not insert a new row into a table.

### Syntax

```sql
INSERT(original_string, position, length, new_string);
```

Arguments:

- `original_string`: the original text.
- `position`: the starting position, beginning at one.
- `length`: the number of characters to replace.
- `new_string`: the replacement text.

### Example

```sql
SELECT INSERT('Hello World', 7, 5, 'SQL') AS result;
```

**Output:** `Hello SQL`

**Explanation:** Starting at character seven, replace five characters (`World`) with `SQL`.

Another example:

```sql
SELECT INSERT('abcdef', 3, 2, 'XY') AS result;
```

**Output:** `abXYef`

**Use:** Replacing a portion of a string at a known position.

---

## I — INSTR()

### Theory

Returns the position of the first occurrence of a substring within a string.

### Syntax

```sql
INSTR(string, substring);
```

### Example

```sql
SELECT INSTR('DATABASE', 'BASE') AS result;
```

**Output:** `5`

**Explanation:** `BASE` starts at character position five.

### Real Query

```sql
SELECT
    email,
    INSTR(email, '@') AS at_position
FROM employees;
```

**Example output:**

| email | at\_position |
| --- | --- |
| amit.sharma@gmail.com | 12 |

**Use:** Finding character positions and locating separators.

**Note:** In MySQL, `INSTR()` uses the argument order shown above. String comparison behavior can depend on collation.

---

## L — LEFT()

### Theory

Returns a specified number of characters from the left side of a string.

### Syntax

```sql
LEFT(string, number_of_characters);
```

### Example

```sql
SELECT LEFT('DATABASE', 4) AS result;
```

**Output:** `DATA`

### Real Query

```sql
SELECT
    first_name,
    LEFT(first_name, 2) AS first_two_chars
FROM employees;
```

**Example output:**

| first\_name | first\_two\_chars |
| --- | --- |
| Amit | Am |
| priya | pr |
| Rahul | Ra |

**Use:** Extracting prefixes, initials, and the first part of a code.

---

## L — LENGTH()

### Theory

Returns the length of a string in bytes, not necessarily the number of characters.

### Syntax

```sql
LENGTH(string);
```

### Example

```sql
SELECT LENGTH('HELLO') AS result;
```

**Output:** `5`

### Important Difference

```sql
SELECT
    LENGTH('café') AS byte_length,
    CHAR_LENGTH('café') AS character_length;
```

Under a common UTF-8 MySQL encoding, the expected result is:

| byte\_length | character\_length |
| --- | --- |
| 5 | 4 |

The accented character `é` occupies two bytes in UTF-8.

**Use:** Byte-length checks and storage-related analysis.

**Interview tip:** `LENGTH()` counts bytes; `CHAR_LENGTH()` counts characters.

---

## L — LOWER()

### Theory

Converts a string to lowercase.

### Syntax

```sql
LOWER(string);
```

### Example

```sql
SELECT LOWER('HELLO SQL') AS result;
```

**Output:** `hello sql`

### Real Query

```sql
SELECT LOWER(email) AS normalized_email
FROM employees;
```

**Example output:** `amit.sharma@gmail.com`

### Filtering Case-Insensitively

```sql
SELECT *
FROM employees
WHERE LOWER(city) = 'mumbai';
```

This returns employees in Mumbai, even if the stored city capitalization differs.

**Use:** Text normalization and case-insensitive comparisons.

**Performance tip:** Applying a function to a column in a `WHERE` condition may prevent ordinary index usage. Where possible, choose an appropriate collation or use an indexed normalized column.

---

## L — LOCATE()

### Theory

Finds the position of a substring inside another string. It can optionally begin searching from a specified position.

### Syntax

```sql
LOCATE(substring, string);
```

Optional starting position:

```
LOCATE(substring, string, start_position);
```

### Example

```sql
SELECT LOCATE('SQL', 'LEARN SQL TODAY') AS result;
```

**Output:** `7`

### Example with a Starting Position

```sql
SELECT LOCATE('a', 'banana', 3) AS result;
```

**Output:** `4`

### Difference from INSTR()

```sql
SELECT
    LOCATE('SQL', 'LEARN SQL') AS result1,
    INSTR('LEARN SQL', 'SQL') AS result2;
```

**Output:** Both return `7`.

**Use:** Searching for words or characters and finding separators.

---

## L — LPAD()

### Theory

Pads the left side of a string until it reaches the required length.

### Syntax

```sql
LPAD(string, target_length, padding_string);
```

### Example

```sql
SELECT LPAD('25', 5, '0') AS result;
```

**Output:** `00025`

### Real Query

```sql
SELECT
    LPAD(emp_id, 4, '0') AS formatted_emp_id
FROM employees;
```

**Example output:**

| formatted\_emp\_id |
| --- |
| 0001 |
| 0002 |
| 0003 |

**Use:** Formatting employee IDs, invoice numbers, and fixed-width text.

**Important:** If the original string is longer than the target length, MySQL truncates it to the target length.

---

## L — LTRIM()

### Theory

Removes leading ordinary space characters from a string.

### Syntax

```sql
LTRIM(string);
```

### Example

```sql
SELECT LTRIM('   Hello') AS result;
```

**Output:** `Hello`

### Real Query

```sql
SELECT LTRIM(city) AS cleaned_city
FROM employees;
```

**Use:** Cleaning text with unnecessary leading spaces.

---

## M — MID()

### Theory

Extracts part of a string from a specified position. In MySQL, `MID()` is a synonym for `SUBSTRING()`.

### Syntax

```sql
MID(string, start_position, length);
```

### Example

```sql
SELECT MID('DATABASE', 3, 4) AS result;
```

**Output:** `TABA`

**Explanation:** Characters three through six are `T`, `A`, `B`, and `A`.

**Use:** Extracting a known section of a string.

---

## O — OCTET\_LENGTH()

### Theory

Returns the number of bytes in a string. In MySQL, it is equivalent to `LENGTH()`.

### Syntax

```sql
OCTET_LENGTH(string);
```

### Example

```sql
SELECT OCTET_LENGTH('SQL') AS result;
```

**Output:** `3`

### Comparison

```sql
SELECT
    OCTET_LENGTH('ABC') AS bytes,
    CHAR_LENGTH('ABC') AS characters;
```

**Output:**

| bytes | characters |
| --- | --- |
| 3 | 3 |

**Use:** Byte-level string length calculations.

---

## O — ORD()

### Theory

Returns a numeric code based on the first character's bytes. For multibyte characters, it combines the bytes according to MySQL's documented behavior.

### Syntax

```sql
ORD(string);
```

### Example

```sql
SELECT ORD('A') AS result;
```

**Output:** `65`

**Use:** Low-level character encoding analysis.

**Difference from ASCII():** `ASCII()` returns the first byte value; `ORD()` can account for multiple bytes in a multibyte character.

---

## P — POSITION()

### Theory

Finds the position of a substring inside another string. It is similar to `LOCATE()` without a starting-position argument.

### Syntax

```sql
POSITION(substring IN string);
```

### Example

```sql
SELECT POSITION('SQL' IN 'LEARN SQL') AS result;
```

**Output:** `7`

**Use:** Searching within text using SQL-standard-style syntax.

---

## Q — QUOTE()

### Theory

Returns a quoted representation of a string suitable for display or inspection. Special characters are escaped according to MySQL's rules.

### Syntax

```sql
QUOTE(string);
```

### Example

```sql
SELECT QUOTE('Hello') AS result;
```

**Output:** `'Hello'`

### Example with an Apostrophe

```sql
SELECT QUOTE('John''s book') AS result;
```

**Output:** `'John\'s book'`

**Use:** Inspecting string representations and escaping behavior.

**Security warning:** Do not build SQL statements by concatenating user input. Use parameterized queries instead.

---

## R — REPEAT()

### Theory

Repeats a string a specified number of times.

### Syntax

```sql
REPEAT(string, count);
```

### Example

```sql
SELECT REPEAT('SQL ', 3) AS result;
```

**Output:** `SQL SQL SQL`

Another example:

```sql
SELECT REPEAT('*', 5) AS result;
```

**Output:** `*****`

**Use:** Generating separators and formatting text.

---

## R — REPLACE()

### Theory

Replaces every occurrence of a substring with another substring.

### Syntax

```sql
REPLACE(original_string, old_string, new_string);
```

### Example

```sql
SELECT REPLACE('I like Java', 'Java', 'SQL') AS result;
```

**Output:** `I like SQL`

### Replacing Multiple Occurrences

```sql
SELECT REPLACE(
    'apple apple apple',
    'apple',
    'orange'
) AS result;
```

**Output:** `orange orange orange`

### Real Query

```sql
SELECT
    email,
    REPLACE(email, 'gmail.com', 'example.com') AS changed_email
FROM employees;
```

**Use:** Correcting inconsistent text, replacing characters, and transforming strings.

**Important:** `REPLACE()` changes the returned string; it does not update the database column unless used in an `UPDATE` statement.

---

## R — REVERSE()

### Theory

Reverses the order of characters in a string.

### Syntax

```sql
REVERSE(string);
```

### Example

```sql
SELECT REVERSE('SQL') AS result;
```

**Output:** `LQS`

### Real Query

```sql
SELECT
    first_name,
    REVERSE(first_name) AS reversed_name
FROM employees;
```

**Use:** String exercises, demonstrations, and specialized text processing.

---

## R — RIGHT()

### Theory

Returns a specified number of characters from the right side of a string.

### Syntax

```sql
RIGHT(string, number_of_characters);
```

### Example

```sql
SELECT RIGHT('DATABASE', 4) AS result;
```

**Output:** `BASE`

### Real Query

```sql
SELECT
    email,
    RIGHT(email, 10) AS last_ten_chars
FROM employees;
```

**Use:** Extracting suffixes and the final part of a code.

---

## R — RPAD()

### Theory

Adds padding to the right side of a string until it reaches a specified length.

### Syntax

```sql
RPAD(string, target_length, padding_string);
```

### Example

```sql
SELECT RPAD('SQL', 6, '*') AS result;
```

**Output:** `SQL***`

Another example:

```sql
SELECT RPAD('Amit', 10, '.') AS result;
```

**Output:** `Amit......`

**Use:** Fixed-width reports and formatted output.

---

## R — RTRIM()

### Theory

Removes trailing ordinary space characters from a string.

### Syntax

```sql
RTRIM(string);
```

### Example

```sql
SELECT RTRIM('Hello   ') AS result;
```

**Output:** `Hello`

**Use:** Removing unnecessary spaces at the end of stored text.

---

## S — SOUNDEX()

### Theory

Produces a phonetic representation of a string, primarily intended for rough English-language sound matching.

### Syntax

```sql
SOUNDEX(string);
```

### Example

```sql
SELECT SOUNDEX('Smith') AS result;
```

**Output:** `S530`

Another example:

```sql
SELECT
    SOUNDEX('Smith') AS code1,
    SOUNDEX('Smyth') AS code2;
```

These names commonly produce matching Soundex codes.

**Use:** Approximate name matching and basic record-linkage exercises.

**Limitation:** Soundex is not a reliable general-purpose fuzzy-matching system for every language or name.

---

## S — SPACE()

### Theory

Returns a string consisting of a specified number of spaces.

### Syntax

```sql
SPACE(number_of_spaces);
```

### Example

```sql
SELECT CONCAT('Hello', SPACE(3), 'World') AS result;
```

**Output:** `Hello   World`

There are three spaces between the words.

**Use:** Formatting text output and demonstrating whitespace handling.

---

## S — STRCMP()

### Theory

Compares two strings and returns a comparison result.

### Syntax

```sql
STRCMP(string1, string2);
```

### Return Values

- `0`: Strings compare equal.
- `-1`: The first string sorts before the second.
- `1`: The first string sorts after the second.

The exact comparison depends on the collation and string comparison rules.

### Example

```sql
SELECT STRCMP('Apple', 'Apple') AS result;
```

**Output:** `0`

Another example:

```sql
SELECT STRCMP('A', 'B') AS result;
```

Under a typical compatible collation, the result is `-1`.

**Use:** Comparing strings programmatically.

---

## S — SUBSTRING()

### Theory

Extracts a portion of a string beginning at a specified position.

### Syntax

```sql
SUBSTRING(string, start_position);
```

Or:

```
SUBSTRING(string, start_position, length);
```

### Example 1: Starting Position

```sql
SELECT SUBSTRING('DATABASE', 5) AS result;
```

**Output:** `BASE`

### Example 2: Position and Length

```sql
SELECT SUBSTRING('DATABASE', 2, 4) AS result;
```

**Output:** `ATAB`

**Explanation:**

```
D A T A B A S E
1 2 3 4 5 6 7 8
```

Starting at position two and taking four characters gives `ATAB`.

### Negative Starting Position

```sql
SELECT SUBSTRING('DATABASE', -4) AS result;
```

**Output:** `BASE`

A negative starting position counts from the end of the string in MySQL.

### Real Query

```sql
SELECT
    email,
    SUBSTRING(email, 1, 4) AS email_prefix
FROM employees;
```

**Use:** Extracting prefixes, codes, and fixed-position sections.

---

## S — SUBSTRING\_INDEX()

### Theory

Returns the part of a string before or after a specified number of occurrences of a delimiter.

### Syntax

```sql
SUBSTRING_INDEX(string, delimiter, count);
```

- Positive `count`: Returns the portion to the left of the specified delimiter occurrence.
- Negative `count`: Returns the portion to the right.
- `count = 0`: Returns an empty string.

### Example 1: Before the First @

```sql
SELECT SUBSTRING_INDEX(
    'amit@gmail.com',
    '@',
    1
) AS result;
```

**Output:** `amit`

### Example 2: After the Last @

```sql
SELECT SUBSTRING_INDEX(
    'amit@gmail.com',
    '@',
    -1
) AS result;
```

**Output:** `gmail.com`

### Real Query: Extract Email Username

```sql
SELECT
    email,
    SUBSTRING_INDEX(email, '@', 1) AS username
FROM employees;
```

### Real Query: Extract Email Domain

```sql
SELECT
    email,
    SUBSTRING_INDEX(email, '@', -1) AS domain
FROM employees;
```

**Use:** Parsing email addresses, domain names, and delimiter-separated strings.

**Important:** These examples assume the email strings have the expected format. `SUBSTRING_INDEX()` alone does not validate whether an email address is valid.

---

## T — TRIM()

### Theory

Removes unwanted characters from the beginning or end of a string. By default, it removes ordinary spaces from both ends.

### Syntax

```sql
TRIM(string);
```

Or:

```
TRIM(BOTH 'x' FROM string);
TRIM(LEADING 'x' FROM string);
TRIM(TRAILING 'x' FROM string);
```

### Example 1: Remove Spaces

```sql
SELECT TRIM('   Hello World   ') AS result;
```

**Output:** `Hello World`

### Example 2: Remove Leading Characters

```sql
SELECT TRIM(LEADING 'x' FROM 'xxxSQLxxx') AS result;
```

**Output:** `SQLxxx`

### Example 3: Remove Trailing Characters

```sql
SELECT TRIM(TRAILING 'x' FROM 'xxxSQLxxx') AS result;
```

**Output:** `xxxSQL`

### Example 4: Remove Characters from Both Ends

```sql
SELECT TRIM(BOTH 'x' FROM 'xxxSQLxxx') AS result;
```

**Output:** `SQL`

**Important:** The trim specification removes the specified characters from the ends; it does not remove every occurrence of those characters throughout the string.

### Real Query

```sql
SELECT TRIM(first_name) AS cleaned_name
FROM employees;
```

**Use:** Data cleaning, importing files, and correcting whitespace inconsistencies.

---

## U — UPPER()

### Theory

Converts a string to uppercase.

### Syntax

```sql
UPPER(string);
```

### Example

```sql
SELECT UPPER('hello sql') AS result;
```

**Output:** `HELLO SQL`

### Real Query

```sql
SELECT UPPER(first_name) AS capitalized_name
FROM employees;
```

**Example output:**

| capitalized\_name |
| --- |
| AMIT |
| PRIYA |
| RAHUL |
| NEHA |
| SNEHA |

**Use:** Standardizing names, categories, and codes.

---

## U — UCASE()

### Theory

An alias for `UPPER()` in MySQL.

### Syntax

```sql
UCASE(string);
```

### Example

```sql
SELECT UCASE('mysql') AS result;
```

**Output:** `MYSQL`

**Use:** Uppercase conversion. `UPPER()` is generally more familiar in SQL examples.

---

## W — WEIGHT\_STRING()

### Theory

Returns a binary string representing a string's collation weight. This is an advanced MySQL function for examining collation behavior.

### Syntax

```sql
WEIGHT_STRING(string);
```

### Example

```sql
SELECT HEX(WEIGHT_STRING('abc')) AS result;
```

The output is a hexadecimal representation of the weight string. Its exact value depends on the collation and MySQL version.

**Use:** Advanced debugging of string sorting and collation behavior.

**Note:** This function is specialized and generally unnecessary for everyday SQL queries.

---

# 4\. ADDITIONAL IMPORTANT STRING FEATURES

## 4.1 LIKE Operator

`LIKE` is an operator rather than a string function, but it is essential for text searching.

### Syntax

```sqlsql
SELECT *
FROM table_name
WHERE column_name LIKE 'pattern';
```

### Wildcards

| Wildcard | Meaning |
| --- | --- |
| `%` | Zero or more characters |
| `_` | Exactly one character |

### Example 1: Names Beginning with A

```sql
SELECT *
FROM employees
WHERE first_name LIKE 'A%';
```

Expected employee: `Amit`

### Example 2: Names Ending with a

```sql
SELECT *
FROM employees
WHERE LOWER(first_name) LIKE '%a';
```

Expected employees: `priya`, `Neha`, `Sneha`

### Example 3: Names Containing ha

```sql
SELECT *
FROM employees
WHERE LOWER(first_name) LIKE '%ha%';
```

### Example 4: Names with Exactly Four Characters

```sql
SELECT *
FROM employees
WHERE first_name LIKE '____';
```

This matches names with four characters, such as `Amit` and `Neha`.

Whether matching is case-sensitive depends on the collation.

---

## 4.2 REGEXP\_LIKE()

### Theory

Tests whether a string matches a regular expression.

### Syntax

```sql
REGEXP_LIKE(expression, pattern);
```

### Example: Names Beginning with A or S

```sql
SELECT *
FROM employees
WHERE REGEXP_LIKE(first_name, '^[AS]');
```

### Example: Strings Containing Only Digits

```sql
SELECT REGEXP_LIKE('12345', '^[0-9]+$') AS is_numeric;
```

**Output:** `1`

Here, `1` means true and `0` means false.

**Use:** Pattern validation and advanced searching. Regex syntax and options may vary across database systems.

---

## 4.3 REGEXP\_REPLACE()

### Theory

Replaces text matching a regular expression with another string.

### Syntax

```sql
REGEXP_REPLACE(expression, pattern, replacement);
```

### Example: Remove All Spaces

```sql
SELECT REGEXP_REPLACE(
    'SQL String Functions',
    '[[:space:]]+',
    ''
) AS result;
```

**Output:** `SQLStringFunctions`

### Example: Replace Consecutive Digits

```sql
SELECT REGEXP_REPLACE(
    'Order123Number456',
    '[0-9]+',
    '#'
) AS result;
```

**Output:** `Order#Number#`

**Use:** Cleaning data with complex patterns.

---

## 4.4 REGEXP\_SUBSTR()

### Theory

Extracts a substring that matches a regular expression.

### Syntax

```sql
REGEXP_SUBSTR(expression, pattern);
```

### Example: Extract Digits

```sql
SELECT REGEXP_SUBSTR(
    'Order123ABC',
    '[0-9]+'
) AS result;
```

**Output:** `123`

**Use:** Extracting codes, numbers, and structured text segments.

---

## 4.5 REGEXP\_INSTR()

### Theory

Returns the position of a substring matching a regular expression.

### Syntax

```sql
REGEXP_INSTR(expression, pattern);
```

### Example

```sql
SELECT REGEXP_INSTR('ABC123', '[0-9]+') AS result;
```

**Output:** `4`

**Use:** Locating patterns rather than fixed literal substrings.

---

## 4.6 FROM\_BASE64() and TO\_BASE64()

These functions decode and encode Base64 text.

### Encode

```sql
SELECT TO_BASE64('Hello') AS result;
```

**Output:** `SGVsbG8=`

### Decode

```sql
SELECT FROM_BASE64('SGVsbG8=') AS result;
```

The decoded value is `Hello`.

**Use:** Working with Base64-encoded data.

**Security warning:** Base64 is encoding, not encryption.

---

## 4.7 UNHEX()

### Theory

Converts hexadecimal text into a binary string.

### Syntax

```sql
UNHEX(hex_string);
```

### Example

```sql
SELECT CONVERT(UNHEX('414243') USING utf8mb4) AS result;
```

**Output:** `ABC`

**Use:** Decoding hexadecimal byte representations.

---

# 5\. HOW STRING FUNCTIONS ARE USED IN SQL QUERIES

A string function can be used in different parts of a query, depending on the requirement.

## 5.1 Using a String Function in SELECT

**Requirement:** Display employee names in uppercase.

```sql
SELECT UPPER(first_name) AS employee_name
FROM employees;
```

**Explanation:** `UPPER()` transforms the displayed value. It does not permanently change the stored name.

## 5.2 Using a String Function in WHERE

**Requirement:** Find employees whose city is Mumbai, ignoring capitalization.

```sql
SELECT *
FROM employees
WHERE LOWER(city) = 'mumbai';
```

**Explanation:** The city is converted to lowercase before comparison.

## 5.3 Using a String Function in ORDER BY

**Requirement:** Sort employees by lowercase first name.

```sql
SELECT first_name, last_name
FROM employees
ORDER BY LOWER(first_name);
```

## 5.4 Using a String Function in GROUP BY

**Requirement:** Count employees by normalized city.

```sql
SELECT
    LOWER(city) AS normalized_city,
    COUNT(*) AS total_employees
FROM employees
GROUP BY LOWER(city);
```

**Example result:**

| normalized\_city | total\_employees |
| --- | --- |
| mumbai | 2 |
| pune | 1 |
| delhi | 1 |
| nashik | 1 |

## 5.5 Using a String Function in UPDATE

**Requirement:** Convert all employee first names to uppercase in the stored data.

```
UPDATE employees
SET first_name = UPPER(first_name);
```

This changes the actual stored values.

To preview the change first:

```sql
SELECT
    first_name,
    UPPER(first_name) AS proposed_name
FROM employees;
```

**Important:** Use `UPDATE` carefully. A statement without a `WHERE` condition affects every row.

## 5.6 Combining Multiple String Functions

**Requirement:** Extract the email username and convert it to uppercase.

```sql
SELECT
    email,
    UPPER(SUBSTRING_INDEX(email, '@', 1)) AS username
FROM employees;
```

**Example output:**

| email | username |
| --- | --- |
| amit.sharma@gmail.com | AMIT.SHARMA |

**Explanation:**

1. `SUBSTRING_INDEX(email, '@', 1)` extracts the part before `@`.
2. `UPPER()` converts that extracted value to uppercase.

---

# 6\. SOLVED SQL PRACTICE QUESTIONS

Use the `employees` table created earlier.

## Question 1: Display Every Employee's Full Name

```sql
SELECT CONCAT(first_name, ' ', last_name) AS full_name
FROM employees;
```

**Example output:**

| full\_name |
| --- |
| Amit Sharma |
| priya Patil |
| Rahul Verma |
| Neha Joshi |
| Sneha Kulkarni |

## Question 2: Display Every Name in Uppercase

```sql
SELECT UPPER(first_name) AS first_name
FROM employees;
```

**Example output:** `AMIT`, `PRIYA`, `RAHUL`, `NEHA`, `SNEHA`

## Question 3: Display Every Email in Lowercase

```sql
SELECT LOWER(email) AS normalized_email
FROM employees;
```

## Question 4: Find Employees Whose First Name Starts with S

```sql
SELECT *
FROM employees
WHERE first_name LIKE 'S%';
```

**Expected employee:** `Sneha`

## Question 5: Find Employees Whose First Name Ends with a

```sql
SELECT *
FROM employees
WHERE LOWER(first_name) LIKE '%a';
```

**Expected employees:** `priya`, `Neha`, `Sneha`

## Question 6: Find Employees Whose Name Contains ha

```sql
SELECT *
FROM employees
WHERE LOWER(first_name) LIKE '%ha%';
```

**Expected employees:** `Neha`, `Sneha`

## Question 7: Display the First Three Characters of Each First Name

```sql
SELECT
    first_name,
    LEFT(first_name, 3) AS first_three_chars
FROM employees;
```

**Example output:**

| first\_name | first\_three\_chars |
| --- | --- |
| Amit | Ami |
| priya | pri |
| Rahul | Rah |
| Neha | Neh |
| Sneha | Sne |

## Question 8: Display the Last Three Characters of Each First Name

```sql
SELECT
    first_name,
    RIGHT(first_name, 3) AS last_three_chars
FROM employees;
```

**Example output:**

| first\_name | last\_three\_chars |
| --- | --- |
| Amit | mit |
| priya | iya |
| Rahul | hul |
| Neha | eha |
| Sneha | eha |

## Question 9: Count the Number of Characters in Every First Name

```sql
SELECT
    first_name,
    CHAR_LENGTH(first_name) AS name_length
FROM employees;
```

**Example output:**

| first\_name | name\_length |
| --- | --- |
| Amit | 4 |
| priya | 5 |
| Rahul | 5 |
| Neha | 4 |
| Sneha | 5 |

## Question 10: Extract the Username from Each Email Address

```sql
SELECT
    email,
    SUBSTRING_INDEX(email, '@', 1) AS username
FROM employees;
```

**Example output:**

| email | username |
| --- | --- |
| amit.sharma@gmail.com | amit.sharma |
| priya.patil@yahoo.com | priya.patil |
| rahul.verma@gmail.com | rahul.verma |
| neha.joshi@outlook.com | neha.joshi |
| sneha.k@gmail.com | sneha.k |

## Question 11: Extract the Domain from Each Email Address

```sql
SELECT
    email,
    SUBSTRING_INDEX(email, '@', -1) AS domain
FROM employees;
```

**Example output:**

| email | domain |
| --- | --- |
| amit.sharma@gmail.com | gmail.com |
| priya.patil@yahoo.com | yahoo.com |
| rahul.verma@gmail.com | gmail.com |
| neha.joshi@outlook.com | outlook.com |
| sneha.k@gmail.com | gmail.com |

## Question 12: Find Employees Using Gmail

```sql
SELECT *
FROM employees
WHERE LOWER(email) LIKE '%@gmail.com';
```

**Expected employees:** Amit Sharma, Rahul Verma, Sneha Kulkarni

## Question 13: Replace Gmail with an Example Domain in the Output

```sql
SELECT
    email,
    REPLACE(email, 'gmail.com', 'example.com') AS new_email
FROM employees;
```

**Example output:**

| email | new\_email |
| --- | --- |
| amit.sharma@gmail.com | amit.sharma@example.com |

This query changes only the returned result, not the stored email.

## Question 14: Display Employee IDs with Leading Zeros

```sql
SELECT
    emp_id,
    LPAD(emp_id, 5, '0') AS formatted_id
FROM employees;
```

**Example output:**

| emp\_id | formatted\_id |
| --- | --- |
| 1 | 00001 |
| 2 | 00002 |
| 3 | 00003 |
| 4 | 00004 |
| 5 | 00005 |

## Question 15: Count Employees Whose Names Have More Than Four Characters

```sql
SELECT COUNT(*) AS total_employees
FROM employees
WHERE CHAR_LENGTH(first_name) > 4;
```

**Output:**

| total\_employees |
| --- |
| 3 |

**Explanation:** `priya`, `Rahul`, and `Sneha` have more than four characters.

## Question 16: Display Names and Their Reversed Versions

```sql
SELECT
    first_name,
    REVERSE(first_name) AS reversed_name
FROM employees;
```

**Example output:**

| first\_name | reversed\_name |
| --- | --- |
| Amit | timA |
| priya | ayirp |
| Rahul | luhaR |
| Neha | aheN |
| Sneha | ahenS |

## Question 17: Find Names Containing Exactly Four Characters

```sql
SELECT *
FROM employees
WHERE CHAR_LENGTH(first_name) = 4;
```

**Expected employees:** Amit, Neha

## Question 18: Display the First Letter of Each First Name

```sql
SELECT
    first_name,
    LEFT(first_name, 1) AS initial
FROM employees;
```

**Example output:**

| first\_name | initial |
| --- | --- |
| Amit | A |
| priya | p |
| Rahul | R |
| Neha | N |
| Sneha | S |

## Question 19: Display the Full Name with Initials in Uppercase

```sql
SELECT CONCAT(
    UPPER(LEFT(first_name, 1)),
    '. ',
    last_name
) AS name_with_initial
FROM employees;
```

**Example output:**

| name\_with\_initial |
| --- |
| A. Sharma |
| P. Patil |
| R. Verma |
| N. Joshi |
| S. Kulkarni |

## Question 20: Find Employees Whose Email Contains gmail

```sql
SELECT *
FROM employees
WHERE INSTR(LOWER(email), 'gmail') > 0;
```

**Expected employees:** Amit Sharma, Rahul Verma, Sneha Kulkarni

## Question 21: Display Each Employee's First Name Padded to Ten Characters

```sql
SELECT RPAD(first_name, 10, '*') AS padded_name
FROM employees;
```

**Example output:**

| padded\_name |
| --- |
| Amit\*\*\*\*\*\* |
| priya\*\*\*\*\* |
| Rahul\*\*\*\*\* |
| Neha\*\*\*\*\*\* |
| Sneha\*\*\*\*\* |

## Question 22: Remove Spaces from the Beginning and End of a Name

```sql
SELECT TRIM('   Amit Sharma   ') AS cleaned_name;
```

**Output:** `Amit Sharma`

## Question 23: Find Employees Whose First Name Has the Letter a at Position 2

```sql
SELECT *
FROM employees
WHERE SUBSTRING(LOWER(first_name), 2, 1) = 'a';
```

**Expected employee:** Rahul

## Question 24: Display the Domain and Count Employees by Domain

```sql
SELECT
    SUBSTRING_INDEX(LOWER(email), '@', -1) AS email_domain,
    COUNT(*) AS total_employees
FROM employees
GROUP BY SUBSTRING_INDEX(LOWER(email), '@', -1);
```

**Expected output:**

| email\_domain | total\_employees |
| --- | --- |
| gmail.com | 3 |
| yahoo.com | 1 |
| outlook.com | 1 |

## Question 25: Display Employee Names and the Number of Characters in Their Full Names

```sql
SELECT
    CONCAT(first_name, ' ', last_name) AS full_name,
    CHAR_LENGTH(
        CONCAT(first_name, ' ', last_name)
    ) AS full_name_length
FROM employees;
```

**Example output:**

| full\_name | full\_name\_length |
| --- | --- |
| Amit Sharma | 11 |
| priya Patil | 10 |
| Rahul Verma | 11 |
| Neha Joshi | 9 |
| Sneha Kulkarni | 13 |

---

# 7\. COMMON INTERVIEW QUESTIONS AND ANSWERS

## Q1. What Are SQL String Functions?

String functions are built-in SQL functions used to manipulate and process text, including converting case, extracting substrings, searching, trimming spaces, and replacing values.

Examples: `UPPER()`, `LOWER()`, `CONCAT()`, `SUBSTRING()`, and `TRIM()`.

## Q2. What Is the Difference Between LENGTH() and CHAR\_LENGTH()?

- `LENGTH()` returns the number of bytes.
- `CHAR_LENGTH()` returns the number of characters.

Example:

```sql
SELECT
    LENGTH('café') AS bytes,
    CHAR_LENGTH('café') AS characters;
```

Under a common UTF-8 encoding, the results are `5` and `4`, respectively.

## Q3. What Is the Difference Between CONCAT() and CONCAT\_WS()?

`CONCAT()` combines strings directly.

```sql
SELECT CONCAT('A', 'B', 'C');
```

**Output:** `ABC`

`CONCAT_WS()` combines strings using a separator.

```sql
SELECT CONCAT_WS('-', 'A', 'B', 'C');
```

**Output:** `A-B-C`

## Q4. What Is the Difference Between SUBSTRING() and SUBSTRING\_INDEX()?

- `SUBSTRING()` extracts characters by position and length.
- `SUBSTRING_INDEX()` extracts a portion based on a delimiter.

```sql
SELECT SUBSTRING('DATABASE', 2, 4);
```

**Output:** `ATAB`

```sql
SELECT SUBSTRING_INDEX('a@b.com', '@', 1);
```

**Output:** `a`

## Q5. What Is the Difference Between LEFT() and RIGHT()?

`LEFT()` extracts characters from the beginning.

```sql
SELECT LEFT('DATABASE', 3);
```

**Output:** `DAT`

`RIGHT()` extracts characters from the end.

```sql
SELECT RIGHT('DATABASE', 3);
```

**Output:** `ASE`

## Q6. What Is the Difference Between TRIM(), LTRIM(), and RTRIM()?

- `TRIM()` removes spaces from both ends by default.
- `LTRIM()` removes leading spaces.
- `RTRIM()` removes trailing spaces.

## Q7. Does REPLACE() Permanently Modify a Table?

No. `REPLACE()` returns a modified string.

To persist the change, an appropriate `UPDATE` statement is required.

## Q8. What Happens When CONCAT() Receives NULL?

In MySQL, if any argument is `NULL`, `CONCAT()` returns `NULL`.

```sql
SELECT CONCAT('Hello', NULL);
```

**Output:** `NULL`

To skip `NULL` arguments while combining values, consider `CONCAT_WS()` with a non-`NULL` separator.

## Q9. How Do You Extract the Username from an Email?

```sql
SELECT SUBSTRING_INDEX('amit@gmail.com', '@', 1);
```

**Output:** `amit`

## Q10. How Do You Extract the Domain from an Email?

```sql
SELECT SUBSTRING_INDEX('amit@gmail.com', '@', -1);
```

**Output:** `gmail.com`

## Q11. How Do You Find Strings That Start with a Particular Letter?

Use `LIKE`.

```sql
SELECT *
FROM employees
WHERE first_name LIKE 'A%';
```

## Q12. How Do You Find a String Containing a Particular Word?

```sql
SELECT *
FROM employees
WHERE first_name LIKE '%ha%';
```

This finds names containing `ha`, subject to the column's comparison rules.

## Q13. What Is the Difference Between LIKE and REGEXP\_LIKE()?

- `LIKE` supports the simple wildcards `%` and `_`.
- `REGEXP_LIKE()` supports regular-expression patterns, which are more powerful for complex matching.

## Q14. What Does LOCATE() Return If a Substring Is Not Found?

In MySQL, `LOCATE()` returns `0` when the substring is not found.

```sql
SELECT LOCATE('Z', 'DATABASE');
```

**Output:** `0`

## Q15. What Is the Difference Between LOWER() and LCASE()?

Both convert text to lowercase in MySQL. `LCASE()` is a synonym for `LOWER()`.

## Q16. What Is the Difference Between UPPER() and UCASE()?

Both convert text to uppercase in MySQL. `UCASE()` is a synonym for `UPPER()`.

## Q17. How Do You Count the Number of Words in a String?

There is no universal built-in word-count function across SQL databases. For a simple string containing single spaces between words, you can use:

```sql
SELECT
    CHAR_LENGTH('SQL STRING FUNCTIONS')
    - CHAR_LENGTH(REPLACE('SQL STRING FUNCTIONS', ' ', ''))
    + 1 AS word_count;
```

**Output:** `3`

This approach assumes a nonempty string with single spaces between words. It needs additional handling for empty strings, leading or trailing spaces, and repeated whitespace.

## Q18. How Do You Remove Spaces from a String?

To remove leading and trailing spaces:

```sql
SELECT TRIM('   Hello SQL   ');
```

**Output:** `Hello SQL`

To remove every ordinary space:

```sql
SELECT REPLACE('Hello SQL', ' ', '');
```

**Output:** `HelloSQL`

## Q19. How Do You Extract the First Character?

```sql
SELECT LEFT('Database', 1);
```

**Output:** `D`

## Q20. How Do You Replace Multiple Occurrences of a Word?

```sql
SELECT REPLACE(
    'SQL is easy and SQL is useful',
    'SQL',
    'MySQL'
);
```

**Output:** `MySQL is easy and MySQL is useful`

---

# 8\. COMMON MISTAKES TO AVOID

## Mistake 1: Confusing Character Length with Byte Length

```sql
SELECT LENGTH('café');
```

Do not assume the result always equals the number of visible characters. Multibyte character sets can use more than one byte per character.

Use `CHAR_LENGTH()` when you need a character count.

## Mistake 2: Using the Wrong Argument Order

Correct:

```sql
SELECT LOCATE('SQL', 'LEARN SQL');
```

Correct:

```sql
SELECT INSTR('LEARN SQL', 'SQL');
```

The argument order differs between these functions.

## Mistake 3: Expecting REPLACE() to Update Stored Values

This only returns transformed data:

```sql
SELECT REPLACE(email, 'gmail.com', 'example.com')
FROM employees;
```

To update stored values, use an `UPDATE` statement with a suitable `WHERE` condition.

## Mistake 4: Assuming TRIM() Removes All Whitespace

Default `TRIM()` handles ordinary spaces at the ends. It does not necessarily remove tabs, newlines, nonbreaking spaces, or every whitespace character.

For more complex cleaning, consider regular expressions or explicit character replacement.

## Mistake 5: Ignoring NULL

Many string functions return `NULL` when their required string argument is `NULL`.

For example:

```sql
SELECT CONCAT('A', NULL);
```

**Output:** `NULL`

Use `COALESCE()` where an appropriate fallback is needed:

```sql
SELECT CONCAT(
    COALESCE(first_name, ''),
    ' ',
    COALESCE(last_name, '')
) AS full_name
FROM employees;
```

## Mistake 6: Using String Functions Unnecessarily in WHERE

This query can be less index-friendly:

```sql
SELECT *
FROM employees
WHERE LOWER(city) = 'mumbai';
```

If the column's collation already provides the required comparison behavior, a direct comparison may be better:

```sql
SELECT *
FROM employees
WHERE city = 'Mumbai';
```

Check your collation and query plan before choosing an approach.

## Mistake 7: Assuming All SQL Databases Use Identical Syntax

MySQL, PostgreSQL, SQL Server, and Oracle differ in some string functions, argument order, and regular-expression support.

Always check the documentation for the database you're using.

---

# 9\. QUICK REVISION CHEAT SHEET

| Function | Purpose | Example | Output |
| --- | --- | --- | --- |
| `ASCII()` | First character's byte code | `ASCII('A')` | `65` |
| `BIT_LENGTH()` | Length in bits | `BIT_LENGTH('ABC')` | `24` |
| `CHAR()` | Numeric codes to characters | `CHAR(65,66)` | `AB` |
| `CHAR_LENGTH()` | Count characters | `CHAR_LENGTH('Hello')` | `5` |
| `CONCAT()` | Join strings | `CONCAT('A','B')` | `AB` |
| `CONCAT_WS()` | Join with a separator | `CONCAT_WS('-','A','B')` | `A-B` |
| `ELT()` | Select by position | `ELT(2,'A','B')` | `B` |
| `FIELD()` | Find a value's position | `FIELD('B','A','B')` | `2` |
| `FIND_IN_SET()` | Search a comma-separated list | `FIND_IN_SET('B','A,B,C')` | `2` |
| `HEX()` | Convert to hexadecimal | `HEX('ABC')` | `414243` |
| `INSERT()` | Replace by position | `INSERT('abcdef',3,2,'XY')` | `abXYef` |
| `INSTR()` | Find substring position | `INSTR('DATABASE','BASE')` | `5` |
| `LEFT()` | Extract from left | `LEFT('DATABASE',3)` | `DAT` |
| `LENGTH()` | Count bytes | `LENGTH('ABC')` | `3` |
| `LOCATE()` | Find substring position | `LOCATE('SQL','LEARN SQL')` | `7` |
| `LOWER()` | Lowercase | `LOWER('SQL')` | `sql` |
| `LPAD()` | Pad on left | `LPAD('7',3,'0')` | `007` |
| `LTRIM()` | Remove leading spaces | `LTRIM(' SQL')` | `SQL` |
| `MID()` | Extract by position | `MID('DATABASE',3,4)` | `TABA` |
| `OCTET_LENGTH()` | Count bytes | `OCTET_LENGTH('ABC')` | `3` |
| `ORD()` | Character code calculation | `ORD('A')` | `65` |
| `POSITION()` | Find substring position | `POSITION('SQL' IN 'LEARN SQL')` | `7` |
| `QUOTE()` | Return quoted representation | `QUOTE('Hi')` | `'Hi'` |
| `REPEAT()` | Repeat text | `REPEAT('A',3)` | `AAA` |
| `REPLACE()` | Replace occurrences | `REPLACE('a-b','-','_')` | `a_b` |
| `REVERSE()` | Reverse string | `REVERSE('SQL')` | `LQS` |
| `RIGHT()` | Extract from right | `RIGHT('DATABASE',4)` | `BASE` |
| `RPAD()` | Pad on right | `RPAD('A',3,'*')` | `A**` |
| `RTRIM()` | Remove trailing spaces | `RTRIM('SQL ')` | `SQL` |
| `SOUNDEX()` | Phonetic code | `SOUNDEX('Smith')` | `S530` |
| `SPACE()` | Generate spaces | `SPACE(3)` | Three spaces |
| `STRCMP()` | Compare strings | `STRCMP('A','A')` | `0` |
| `SUBSTRING()` | Extract by position | `SUBSTRING('DATABASE',2,4)` | `ATAB` |
| `SUBSTRING_INDEX()` | Extract around delimiter | `SUBSTRING_INDEX('a@b.com','@',1)` | `a` |
| `TRIM()` | Remove spaces at both ends | `TRIM(' SQL ')` | `SQL` |
| `UCASE()` | Uppercase alias | `UCASE('sql')` | `SQL` |
| `UPPER()` | Uppercase | `UPPER('sql')` | `SQL` |
| `WEIGHT_STRING()` | Inspect collation weights | `WEIGHT_STRING('abc')` | Binary weight string |

---

# 10\. FINAL LEARNING ROADMAP

For interviews and practical SQL work, learn the functions in this order.

## Stage 1: Basic Manipulation

- `CONCAT()`
- `CONCAT_WS()`
- `UPPER()`
- `LOWER()`

## Stage 2: Length and Spaces

- `LENGTH()`
- `CHAR_LENGTH()`
- `TRIM()`
- `LTRIM()`
- `RTRIM()`

## Stage 3: Extraction

- `LEFT()`
- `RIGHT()`
- `SUBSTRING()`
- `SUBSTRING_INDEX()`

## Stage 4: Searching

- `LIKE`
- `LOCATE()`
- `INSTR()`
- `REGEXP_LIKE()`

## Stage 5: Replacement and Formatting

- `REPLACE()`
- `INSERT()`
- `LPAD()`
- `RPAD()`

## Stage 6: Advanced Functions

- `REGEXP_REPLACE()`
- `REGEXP_SUBSTR()`
- `HEX()`
- `UNHEX()`
- `SOUNDEX()`

## Most Important Functions to Master First

If you are preparing for SQL interviews, prioritize these ten:

1. `CONCAT()`
2. `UPPER()`
3. `LOWER()`
4. `LENGTH()`
5. `CHAR_LENGTH()`
6. `TRIM()`
7. `SUBSTRING()`
8. `SUBSTRING_INDEX()`
9. `REPLACE()`
10. `LOCATE()`

**Best practice:** Do not memorize functions alone. Practice combining them with `SELECT`, `WHERE`, `GROUP BY`, `ORDER BY`, `CASE`, and aggregate functions to solve realistic data-cleaning and reporting problems.

---

**End of Notes**

To save it as a Markdown file:

1. Copy the content from the document above.
2. Open VS Code, Notepad, or another text editor.
3. Save the file as `SQL_String_Functions_Notes.md`.
4. Use UTF-8 encoding and ensure the extension is `.md`, not `.txt`.
