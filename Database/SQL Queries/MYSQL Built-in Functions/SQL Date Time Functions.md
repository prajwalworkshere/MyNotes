# SQL DATE AND TIME FUNCTIONS — COMPLETE A TO Z NOTES

**MySQL 8.0+ | Theory + Syntax + Examples + Outputs + Practice Questions + Interview Q&A**

## TABLE OF CONTENTS

1. Introduction to Date and Time Functions
2. Date and Time Data Types
3. Demo Database and Sample Data
4. SQL Date and Time Functions A–Z
5. Date and Time Arithmetic
6. Date Functions in Real SQL Queries
7. Solved SQL Practice Questions
8. Interview Questions and Answers
9. Common Mistakes
10. Quick Revision Cheat Sheet
11. Final Learning Roadmap

---

# 1\. INTRODUCTION TO DATE AND TIME FUNCTIONS

## What is a Date?

A date represents a calendar day, generally containing a year, month, and day.

Examples:

```
'2026-10-20'
'2025-01-01'
'2000-12-31'
```

## What is Time?

Time represents a time of day, including hours, minutes, seconds, and potentially fractional seconds.

Examples:

```
'10:30:00'
'14:45:30'
'23:59:59'
```

## What are SQL Date and Time Functions?

Date and time functions are built-in SQL functions used to retrieve the current date and time, extract date components, calculate date differences, add or subtract intervals, format dates, and manipulate temporal values.

Example:

```sql
SELECT CURRENT_DATE() AS today;
```

Example output (depends on the date when you run the query):

```
today
2026-10-20
```

## Why do we use Date and Time Functions?

They help us:

- Find the current date and time.
- Extract the year, month, day, hour, minute, and second.
- Calculate employee age or service duration.
- Find the number of days between two dates.
- Add days, months, or years to a date.
- Identify records from a particular month or year.
- Filter orders by date ranges.
- Format dates for reports.
- Find the first or last day of a month.
- Calculate deadlines, delivery dates, and expiry dates.
- Analyze daily, monthly, and yearly sales.

## General Syntax

```sql
SELECT FUNCTION_NAME(date_value);
```

Example:

```sql
SELECT YEAR('2026-10-20') AS result;
```

Output:

```
result
2026
```

**Important:** These notes use MySQL 8.0+. PostgreSQL, SQL Server, Oracle, and other databases have some differences in syntax and function behavior.

---

# 2\. DATE AND TIME DATA TYPES IN MYSQL

Before learning date functions, understand the data types used to store dates and times.

| Data type | Purpose | Example |
| --- | --- | --- |
| `DATE` | Calendar date | `2026-10-20` |
| `TIME` | Time of day or duration | `14:30:00` |
| `DATETIME` | Date and time | `2026-10-20 14:30:00` |
| `TIMESTAMP` | Date and time stored with session time-zone conversion | `2026-10-20 14:30:00` |
| `YEAR` | Year value | `2026` |

## 2.1 DATE

Stores a calendar date without a time component.

```sql
SELECT CAST('2026-10-20' AS DATE) AS result;
```

Output:

```
result
2026-10-20
```

Use it for birth dates, joining dates, order dates, and holidays.

## 2.2 TIME

Stores a time value or duration.

```sql
SELECT CAST('14:30:45' AS TIME) AS result;
```

Output:

```
result
14:30:45
```

Use it for time-of-day values and elapsed durations.

## 2.3 DATETIME

Stores both date and time.

```sql
SELECT CAST('2026-10-20 14:30:45' AS DATETIME) AS result;
```

Output:

```
result
2026-10-20 14:30:45
```

Use it for appointments, order timestamps, and scheduled events when you want a local date-time value without automatic time-zone conversion.

## 2.4 TIMESTAMP

Stores a date-time value that MySQL converts between the session time zone and UTC for storage and retrieval.

```sql
CREATE TABLE events (
    event_id INT PRIMARY KEY,
    event_time TIMESTAMP
);
```

Use it for event timestamps and audit records where time-zone handling matters.

**Important:** `TIMESTAMP` and `DATETIME` are not interchangeable. Choose based on your application's time-zone requirements.

## 2.5 YEAR

Stores a year value.

```sql
CREATE TABLE company_years (
    record_id INT PRIMARY KEY,
    fiscal_year YEAR
);
```

Example:

```sql
SELECT CAST('2026' AS YEAR) AS result;
```

Output:

```
result
2026
```

---

# 3\. DEMO DATABASE AND SAMPLE DATA

We will use an `employees` table to practice date functions.

## Create the table

```sql
CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    joining_date DATE,
    birth_date DATE
);
```

## Insert sample records

```sql
INSERT INTO employees
(emp_id, first_name, department, salary, joining_date, birth_date)
VALUES
(1, 'Amit', 'IT', 65000, '2020-01-15', '1998-05-10'),
(2, 'Priya', 'HR', 55000, '2021-06-20', '1999-08-25'),
(3, 'Rahul', 'IT', 72000, '2018-03-10', '1995-12-05'),
(4, 'Neha', 'Finance', 60000, '2022-11-01', '2000-02-14'),
(5, 'Sneha', 'HR', 58000, '2019-07-12', '1997-09-30');
```

## Check the data

```sql
SELECT * FROM employees;
```

Expected records:

| emp\_id | first\_name | department | salary | joining\_date | birth\_date |
| --- | --- | --- | --- | --- | --- |
| 1 | Amit | IT | 65000.00 | 2020-01-15 | 1998-05-10 |
| 2 | Priya | HR | 55000.00 | 2021-06-20 | 1999-08-25 |
| 3 | Rahul | IT | 72000.00 | 2018-03-10 | 1995-12-05 |
| 4 | Neha | Finance | 60000.00 | 2022-11-01 | 2000-02-14 |
| 5 | Sneha | HR | 58000.00 | 2019-07-12 | 1997-09-30 |

We will also use this orders table.

```sql
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_name VARCHAR(50),
    order_date DATE,
    delivery_date DATE,
    order_amount DECIMAL(10,2)
);
```

```sql
INSERT INTO orders
(order_id, customer_name, order_date, delivery_date, order_amount)
VALUES
(101, 'Amit', '2026-01-05', '2026-01-10', 2500.00),
(102, 'Priya', '2026-01-15', '2026-01-20', 4500.00),
(103, 'Rahul', '2026-02-10', '2026-02-18', 3200.00),
(104, 'Neha', '2026-03-01', '2026-03-04', 1800.00),
(105, 'Sneha', '2026-03-20', '2026-03-28', 5000.00);
```

---

# 4\. SQL DATE AND TIME FUNCTIONS A–Z

## A — ADDDATE()

### Theory

Adds a specified number of days or a specified interval to a date.

`ADDDATE()` is closely related to `DATE_ADD()`.

### Syntax

```sql
ADDDATE(date, days);
```

Or:

```sql
ADDDATE(date, INTERVAL value unit);
```

### Example 1: Add days

```sql
SELECT ADDDATE('2026-10-20', 7) AS result;
```

Output:

```
result
2026-10-27
```

### Example 2: Add months

```sql
SELECT ADDDATE('2026-01-31', INTERVAL 1 MONTH) AS result;
```

Output:

```
result
2026-02-28
```

MySQL adjusts the result to the last valid day of the target month when necessary.

### Real query

```sql
SELECT
    order_id,
    order_date,
    ADDDATE(order_date, 7) AS expected_followup_date
FROM orders;
```

**Use:** Calculating follow-up dates, delivery estimates, and deadlines.

---

## C — CURDATE()

### Theory

Returns the current date according to the MySQL session's time zone.

### Syntax

```sql
CURDATE();
```

### Example

```sql
SELECT CURDATE() AS today;
```

Example output:

```
today
2026-10-20
```

The actual output depends on when you execute the query.

### Real query

```sql
SELECT *
FROM employees
WHERE joining_date <= CURDATE();
```

This returns employees whose joining dates are not in the future.

**Use:** Current-date comparisons and date-based filtering.

---

## C — CURRENT\_DATE()

### Theory

Returns the current date. It is a synonym for `CURDATE()` in MySQL.

### Syntax

```sql
CURRENT_DATE;
```

Or:

```sql
CURRENT_DATE();
```

### Example

```sql
SELECT CURRENT_DATE() AS today;
```

Example output:

```
today
2026-10-20
```

### Difference between CURDATE() and CURRENT\_DATE()

Both return the current date in MySQL.

```sql
SELECT
    CURDATE() AS result1,
    CURRENT_DATE() AS result2;
```

Both columns contain the current date.

**Use:** Standard date retrieval.

---

## C — CURRENT\_TIME()

### Theory

Returns the current time according to the MySQL session's time zone.

### Syntax

```sql
CURRENT_TIME;
```

Or:

```sql
CURRENT_TIME();
```

### Example

```sql
SELECT CURRENT_TIME() AS current_time;
```

Example output:

```
current_time
14:30:45
```

The output above is illustrative, not a live reading.

### Use

- Recording the current time.
- Comparing time values.
- Displaying the current time in reports.

---

## C — CURRENT\_TIMESTAMP()

### Theory

Returns the current date and time according to the MySQL session's time zone.

It is a synonym for `NOW()` in MySQL.

### Syntax

```sql
CURRENT_TIMESTAMP;
```

Or:

```sql
CURRENT_TIMESTAMP();
```

### Example

```sql
SELECT CURRENT_TIMESTAMP() AS current_datetime;
```

Example output:

```
current_datetime
2026-10-20 14:30:45
```

The actual value depends on the execution time and session time zone.

### Real query

```sql
SELECT
    order_id,
    CURRENT_TIMESTAMP() AS report_generated_at
FROM orders;
```

**Use:** Audit reports, event timestamps, and recording when an operation occurs.

---

## D — DATE()

### Theory

Extracts the date portion from a `DATETIME` or timestamp-like expression.

### Syntax

```sql
DATE(datetime_expression);
```

### Example

```sql
SELECT DATE('2026-10-20 14:30:45') AS result;
```

Output:

```
result
2026-10-20
```

### Real query

```sql
SELECT
    DATE('2026-10-20 14:30:45') AS order_day;
```

**Use:** Extracting a calendar date from a date-time value.

---

## D — DATEDIFF()

### Theory

Calculates the difference between two dates in days.

In MySQL:

```
DATEDIFF(date1, date2)
```

The result is the number of days from `date2` to `date1`.

Time portions are ignored.

### Syntax

```sql
DATEDIFF(date1, date2);
```

### Example

```sql
SELECT DATEDIFF('2026-10-20', '2026-10-15') AS result;
```

Output:

```
result
5
```

### Reverse the arguments

```sql
SELECT DATEDIFF('2026-10-15', '2026-10-20') AS result;
```

Output:

```
result
-5
```

### Real query: Delivery duration

```sql
SELECT
    order_id,
    order_date,
    delivery_date,
    DATEDIFF(delivery_date, order_date) AS delivery_days
FROM orders;
```

Example output:

| order\_id | delivery\_days |
| --- | --- |
| 101 | 5 |
| 102 | 5 |
| 103 | 8 |
| 104 | 3 |
| 105 | 8 |

**Use:** Calculating delivery duration, overdue days, and the number of days between events.

**Important:** `DATEDIFF()` returns whole-day differences, not an exact elapsed duration in hours or minutes.

---

## D — DATE\_ADD()

### Theory

Adds a specified interval to a date or date-time value.

### Syntax

```sql
DATE_ADD(date, INTERVAL value unit);
```

Common interval units include:

- `DAY`
- `WEEK`
- `MONTH`
- `QUARTER`
- `YEAR`
- `HOUR`
- `MINUTE`
- `SECOND`
- `MICROSECOND`
- `DAY_HOUR`
- `DAY_MINUTE`

### Example 1: Add days

```sql
SELECT DATE_ADD('2026-10-20', INTERVAL 10 DAY) AS result;
```

Output:

```
result
2026-10-30
```

### Example 2: Add months

```sql
SELECT DATE_ADD('2026-01-15', INTERVAL 2 MONTH) AS result;
```

Output:

```
result
2026-03-15
```

### Example 3: Add years

```sql
SELECT DATE_ADD('2026-10-20', INTERVAL 1 YEAR) AS result;
```

Output:

```
result
2027-10-20
```

### Example 4: Add hours

```sql
SELECT DATE_ADD(
    '2026-10-20 10:00:00',
    INTERVAL 3 HOUR
) AS result;
```

Output:

```
result
2026-10-20 13:00:00
```

### Real query: Calculate a seven-day deadline

```sql
SELECT
    order_id,
    order_date,
    DATE_ADD(order_date, INTERVAL 7 DAY) AS deadline
FROM orders;
```

**Use:** Deadlines, warranties, delivery estimates, subscriptions, and scheduled reminders.

---

## D — DATE\_FORMAT()

### Theory

Formats a date or date-time value as a string using format specifiers.

### Syntax

```sql
DATE_FORMAT(date, format);
```

### Common format specifiers

| Specifier | Meaning | Example |
| --- | --- | --- |
| `%Y` | Four-digit year | `2026` |
| `%y` | Two-digit year | `26` |
| `%m` | Month number, 01–12 | `10` |
| `%c` | Month number, 1–12 | `10` |
| `%M` | Full month name | `October` |
| `%b` | Abbreviated month name | `Oct` |
| `%d` | Day of month, 01–31 | `20` |
| `%e` | Day of month, 1–31 | `20` |
| `%W` | Full weekday name | `Tuesday` |
| `%a` | Abbreviated weekday | `Tue` |
| `%w` | Weekday number, Sunday = 0 | `2` |
| `%H` | Hour, 00–23 | `14` |
| `%h` | Hour, 01–12 | `02` |
| `%i` | Minutes | `30` |
| `%s` | Seconds | `45` |
| `%p` | AM or PM | `PM` |
| `%r` | 12-hour time with AM/PM | `02:30:45 PM` |
| `%T` | 24-hour time | `14:30:45` |
| `%j` | Day of year, 001–366 | `293` |

### Example 1: Display date in DD-MM-YYYY format

```sql
SELECT DATE_FORMAT(
    '2026-10-20',
    '%d-%m-%Y'
) AS result;
```

Output:

```
result
20-10-2026
```

### Example 2: Display month name

```sql
SELECT DATE_FORMAT(
    '2026-10-20',
    '%M'
) AS result;
```

Output:

```
result
October
```

### Example 3: Display complete date

```sql
SELECT DATE_FORMAT(
    '2026-10-20 14:30:45',
    '%W, %d %M %Y'
) AS result;
```

Output:

```
result
Tuesday, 20 October 2026
```

### Example 4: Display time in 12-hour format

```sql
SELECT DATE_FORMAT(
    '2026-10-20 14:30:45',
    '%h:%i:%s %p'
) AS result;
```

Output:

```
result
02:30:45 PM
```

### Real query

```sql
SELECT
    order_id,
    DATE_FORMAT(order_date, '%d-%m-%Y') AS formatted_date
FROM orders;
```

**Use:** Reports, display formatting, and exporting date values as text.

**Important:** `DATE_FORMAT()` returns a string. Use proper date values for date comparisons and sorting.

---

## D — DAY()

### Theory

Returns the day of the month from 1 to 31.

`DAY()` is a synonym for `DAYOFMONTH()` in MySQL.

### Syntax

```sql
DAY(date);
```

### Example

```sql
SELECT DAY('2026-10-20') AS result;
```

Output:

```
result
20
```

### Real query

```sql
SELECT
    order_id,
    DAY(order_date) AS order_day
FROM orders;
```

**Use:** Extracting the day component from a date.

---

## D — DAYNAME()

### Theory

Returns the full weekday name for a date.

### Syntax

```sql
DAYNAME(date);
```

### Example

```sql
SELECT DAYNAME('2026-10-20') AS result;
```

Output:

```
result
Tuesday
```

### Real query

```sql
SELECT
    order_id,
    order_date,
    DAYNAME(order_date) AS weekday_name
FROM orders;
```

**Use:** Identifying weekdays, preparing schedules, and creating calendar reports.

---

## D — DAYOFMONTH()

### Theory

Returns the day of the month as a number from 1 to 31.

### Syntax

```sql
DAYOFMONTH(date);
```

### Example

```sql
SELECT DAYOFMONTH('2026-10-20') AS result;
```

Output:

```
result
20
```

**Use:** Day-based filtering and date analysis.

---

## D — DAYOFWEEK()

### Theory

Returns the weekday number using this MySQL convention:

- Sunday = 1
- Monday = 2
- Tuesday = 3
- Wednesday = 4
- Thursday = 5
- Friday = 6
- Saturday = 7

### Syntax

```sql
DAYOFWEEK(date);
```

### Example

```sql
SELECT DAYOFWEEK('2026-10-20') AS result;
```

Output:

```
result
3
```

October 20, 2026, is a Tuesday.

### Difference from WEEKDAY()

`DAYOFWEEK()` numbers Sunday as 1, while `WEEKDAY()` numbers Monday as 0.

**Use:** Weekday-based reports and schedule calculations.

---

## D — DAYOFYEAR()

### Theory

Returns the day number within the year, from 1 to 366.

### Syntax

```sql
DAYOFYEAR(date);
```

### Example

```sql
SELECT DAYOFYEAR('2026-10-20') AS result;
```

Output:

```
result
293
```

**Use:** Annual reporting and day-of-year calculations.

---

## E — EXTRACT()

### Theory

Extracts a particular component from a date or time expression.

### Syntax

```sql
EXTRACT(unit FROM date_expression);
```

### Common units

`YEAR`, `QUARTER`, `MONTH`, `DAY`, `HOUR`, `MINUTE`, `SECOND`, `WEEK`, `DAY_HOUR`, and others supported by MySQL.

### Example 1: Extract year

```sql
SELECT EXTRACT(
    YEAR FROM '2026-10-20'
) AS result;
```

Output:

```
result
2026
```

### Example 2: Extract month

```sql
SELECT EXTRACT(
    MONTH FROM '2026-10-20'
) AS result;
```

Output:

```
result
10
```

### Example 3: Extract hour

```sql
SELECT EXTRACT(
    HOUR FROM '2026-10-20 14:30:45'
) AS result;
```

Output:

```
result
14
```

### Real query

```sql
SELECT
    order_id,
    EXTRACT(MONTH FROM order_date) AS order_month
FROM orders;
```

**Use:** Extracting components for calculations, filtering, and grouping.

---

## F — FROM\_DAYS()

### Theory

Converts a day number into a date using MySQL's day-number system.

### Syntax

```sql
FROM_DAYS(number_of_days);
```

### Example

```sql
SELECT FROM_DAYS(740000) AS result;
```

Output:

```
result
2026-06-12
```

This example uses MySQL's day-number conversion convention.

**Important:** This is not a general-purpose function for converting an elapsed number of days since an arbitrary starting date. For adding days to an existing date, use `DATE_ADD()`.

**Use:** Specialized day-number conversions.

---

## H — HOUR()

### Theory

Extracts the hour component from a time or date-time value.

### Syntax

```sql
HOUR(time_expression);
```

### Example

```sql
SELECT HOUR('14:35:50') AS result;
```

Output:

```
result
14
```

### Real query

```sql
SELECT
    HOUR('2026-10-20 14:30:45') AS order_hour;
```

**Use:** Hourly reports and time-based analysis.

---

## L — LAST\_DAY()

### Theory

Returns the last calendar day of the month containing the specified date.

### Syntax

```sql
LAST_DAY(date);
```

### Example 1

```sql
SELECT LAST_DAY('2026-02-10') AS result;
```

Output:

```
result
2026-02-28
```

### Example 2

```sql
SELECT LAST_DAY('2024-02-10') AS result;
```

Output:

```
result
2024-02-29
```

2024 is a leap year.

### Real query

```sql
SELECT
    order_id,
    order_date,
    LAST_DAY(order_date) AS month_end
FROM orders;
```

**Use:** Month-end reporting, billing cycles, and financial calculations.

---

## L — LOCALTIME()

### Theory

Returns the current date and time in the MySQL session's time zone.

It is a synonym for `NOW()` in MySQL.

### Syntax

```sql
LOCALTIME;
```

Or:

```sql
LOCALTIME();
```

### Example

```sql
SELECT LOCALTIME() AS current_datetime;
```

Example output:

```
current_datetime
2026-10-20 14:30:45
```

The displayed time is illustrative.

**Use:** Retrieving the current local date and time.

---

## L — LOCALTIMESTAMP()

### Theory

Another synonym for `NOW()` in MySQL.

### Syntax

```sql
LOCALTIMESTAMP;
```

Or:

```sql
LOCALTIMESTAMP();
```

### Example

```sql
SELECT LOCALTIMESTAMP() AS current_datetime;
```

Example output:

```
current_datetime
2026-10-20 14:30:45
```

**Use:** Current date-time retrieval. In MySQL, its behavior is equivalent to `NOW()`.

---

## M — MAKEDATE()

### Theory

Creates a date from a year and a day-of-year value.

### Syntax

```sql
MAKEDATE(year, day_of_year);
```

### Example

```sql
SELECT MAKEDATE(2026, 293) AS result;
```

Output:

```
result
2026-10-20
```

### Another example

```sql
SELECT MAKEDATE(2026, 1) AS result;
```

Output:

```
result
2026-01-01
```

**Use:** Converting a year and day number into a calendar date.

---

## M — MAKETIME()

### Theory

Creates a time value from hours, minutes, and seconds.

### Syntax

```sql
MAKETIME(hour, minute, second);
```

### Example

```sql
SELECT MAKETIME(14, 30, 45) AS result;
```

Output:

```
result
14:30:45
```

### Example with fractional seconds

```sql
SELECT MAKETIME(14, 30, 45.5) AS result;
```

Output:

```
result
14:30:45.500000
```

**Use:** Building time values from separate components.

---

## M — MICROSECOND()

### Theory

Returns the microsecond component of a time or date-time value.

A second can contain up to six fractional decimal digits in MySQL's supported temporal precision.

### Syntax

```sql
MICROSECOND(time_expression);
```

### Example

```sql
SELECT MICROSECOND(
    '2026-10-20 14:30:45.123456'
) AS result;
```

Output:

```
result
123456
```

**Use:** High-precision timing and timestamp analysis.

---

## M — MINUTE()

### Theory

Returns the minute component of a time or date-time value.

### Syntax

```sql
MINUTE(time_expression);
```

### Example

```sql
SELECT MINUTE('14:35:50') AS result;
```

Output:

```
result
35
```

**Use:** Time-based calculations and reporting.

---

## M — MONTH()

### Theory

Returns the month number from 1 to 12.

### Syntax

```sql
MONTH(date);
```

### Example

```sql
SELECT MONTH('2026-10-20') AS result;
```

Output:

```
result
10
```

### Real query

```sql
SELECT
    order_id,
    MONTH(order_date) AS order_month
FROM orders;
```

**Use:** Monthly reporting and date-component extraction.

---

## M — MONTHNAME()

### Theory

Returns the full month name.

### Syntax

```sql
MONTHNAME(date);
```

### Example

```sql
SELECT MONTHNAME('2026-10-20') AS result;
```

Output:

```
result
October
```

**Use:** Reports with readable month names.

---

## N — NOW()

### Theory

Returns the current date and time according to the MySQL session's time zone.

### Syntax

```sql
NOW();
```

### Example

```sql
SELECT NOW() AS current_datetime;
```

Example output:

```
current_datetime
2026-10-20 14:30:45
```

The actual value depends on execution time and session time zone.

### Real query

```sql
SELECT
    order_id,
    customer_name,
    NOW() AS query_time
FROM orders;
```

**Important:** `NOW()` is evaluated using the statement's start time in MySQL. Multiple calls within the same statement return the same current date-time value.

**Use:** Logging, auditing, reports, and current date-time comparisons.

---

## P — PERIOD\_ADD()

### Theory

Adds a number of months to a period represented in `YYMM` or `YYYYMM` numeric format.

The function returns a numeric period, not a full date.

### Syntax

```sql
PERIOD_ADD(period, number_of_months);
```

### Example

```sql
SELECT PERIOD_ADD(202610, 3) AS result;
```

Output:

```
result
202701
```

October 2026 plus three months is January 2027.

### Another example

```sql
SELECT PERIOD_ADD(202601, 2) AS result;
```

Output:

```
result
202603
```

**Use:** Month-period calculations in legacy financial or accounting data.

**Note:** For ordinary calendar-date arithmetic, `DATE_ADD()` is generally clearer.

---

## P — PERIOD\_DIFF()

### Theory

Returns the number of months between two periods represented in `YYMM` or `YYYYMM` numeric format.

### Syntax

```sql
PERIOD_DIFF(period1, period2);
```

### Example

```sql
SELECT PERIOD_DIFF(202610, 202601) AS result;
```

Output:

```
result
9
```

October 2026 is nine months after January 2026.

**Use:** Calculating month differences between accounting periods.

---

## Q — QUARTER()

### Theory

Returns the quarter of the year:

- Quarter 1: January–March
- Quarter 2: April–June
- Quarter 3: July–September
- Quarter 4: October–December

### Syntax

```sql
QUARTER(date);
```

### Example

```sql
SELECT QUARTER('2026-10-20') AS result;
```

Output:

```
result
4
```

### Real query

```sql
SELECT
    order_id,
    QUARTER(order_date) AS order_quarter
FROM orders;
```

**Use:** Quarterly sales reports and financial analysis.

---

## S — SEC\_TO\_TIME()

### Theory

Converts a number of seconds into a time value.

### Syntax

```sql
SEC_TO_TIME(seconds);
```

### Example

```sql
SELECT SEC_TO_TIME(3661) AS result;
```

Output:

```
result
01:01:01
```

Explanation:

- 3600 seconds = 1 hour
- 60 seconds = 1 minute
- 1 second remains

### Another example

```sql
SELECT SEC_TO_TIME(90) AS result;
```

Output:

```
result
00:01:30
```

**Use:** Converting elapsed seconds into a readable duration.

**Important:** MySQL time values can represent durations beyond a normal 24-hour clock, within the supported `TIME` range.

---

## S — SECOND()

### Theory

Returns the seconds component of a time or date-time value.

### Syntax

```sql
SECOND(time_expression);
```

### Example

```sql
SELECT SECOND('14:30:45') AS result;
```

Output:

```
result
45
```

**Use:** Extracting seconds for timing calculations.

---

## S — STR\_TO\_DATE()

### Theory

Converts a string into a date or date-time value using a specified format.

### Syntax

```sql
STR_TO_DATE(string, format);
```

### Example 1: Convert DD-MM-YYYY into a date

```sql
SELECT STR_TO_DATE(
    '20-10-2026',
    '%d-%m-%Y'
) AS result;
```

Output:

```
result
2026-10-20
```

### Example 2: Convert a date-time string

```sql
SELECT STR_TO_DATE(
    '20/10/2026 14:30:45',
    '%d/%m/%Y %H:%i:%s'
) AS result;
```

Output:

```
result
2026-10-20 14:30:45
```

### Real query

Suppose an imported text file contains dates in `DD-MM-YYYY` format.

```sql
SELECT STR_TO_DATE(
    '15-01-2026',
    '%d-%m-%Y'
) AS converted_date;
```

Output:

```
converted_date
2026-01-15
```

**Use:** Converting imported date strings into proper temporal values.

**Important:** Input strings must follow the expected format. Invalid or incomplete values can produce `NULL` or warnings depending on the input and SQL mode.

---

## S — SUBDATE()

### Theory

Subtracts a specified number of days or an interval from a date.

### Syntax

```sql
SUBDATE(date, days);
```

Or:

```sql
SUBDATE(date, INTERVAL value unit);
```

### Example 1: Subtract days

```sql
SELECT SUBDATE('2026-10-20', 5) AS result;
```

Output:

```
result
2026-10-15
```

### Example 2: Subtract months

```sql
SELECT SUBDATE(
    '2026-10-20',
    INTERVAL 2 MONTH
) AS result;
```

Output:

```
result
2026-08-20
```

**Use:** Calculating previous dates, lookback periods, and historical windows.

---

## S — SUBTIME()

### Theory

Subtracts a time interval from a time or date-time expression.

### Syntax

```sql
SUBTIME(expression1, expression2);
```

### Example

```sql
SELECT SUBTIME(
    '2026-10-20 14:30:45',
    '02:00:00'
) AS result;
```

Output:

```
result
2026-10-20 12:30:45
```

### Another example

```sql
SELECT SUBTIME('10:00:00', '00:30:00') AS result;
```

Output:

```
result
09:30:00
```

**Use:** Subtracting elapsed time from a date-time or time value.

---

## T — TIME()

### Theory

Extracts the time portion from a date-time expression.

### Syntax

```sql
TIME(expression);
```

### Example

```sql
SELECT TIME('2026-10-20 14:30:45') AS result;
```

Output:

```
result
14:30:45
```

**Use:** Extracting the time portion for reporting or comparison.

---

## T — TIME\_FORMAT()

### Theory

Formats a time value using time format specifiers.

### Syntax

```sql
TIME_FORMAT(time, format);
```

### Example

```sql
SELECT TIME_FORMAT(
    '14:30:45',
    '%h:%i %p'
) AS result;
```

Output:

```
result
02:30 PM
```

### Another example

```sql
SELECT TIME_FORMAT(
    '14:30:45',
    '%H hours and %i minutes'
) AS result;
```

Output:

```
result
14 hours and 30 minutes
```

**Use:** Formatting times for reports and display.

---

## T — TIMEDIFF()

### Theory

Calculates the difference between two time or date-time expressions and returns a time value.

### Syntax

```sql
TIMEDIFF(expression1, expression2);
```

The result is the first expression minus the second expression.

### Example

```sql
SELECT TIMEDIFF(
    '14:30:00',
    '12:00:00'
) AS result;
```

Output:

```
result
02:30:00
```

### Date-time example

```sql
SELECT TIMEDIFF(
    '2026-10-20 14:00:00',
    '2026-10-20 09:30:00'
) AS result;
```

Output:

```
result
04:30:00
```

### Real query

```sql
SELECT TIMEDIFF(
    '2026-10-20 18:00:00',
    '2026-10-20 09:00:00'
) AS working_duration;
```

Output:

```
working_duration
09:00:00
```

**Use:** Calculating shift duration, elapsed time, and time differences.

**Important:** `TIMEDIFF()` returns a `TIME`-style result and has range limitations. For some long-duration calculations, use timestamps and calculate seconds instead.

---

## T — TIMESTAMP()

### Theory

Converts a date-time expression into a timestamp-like date-time value. MySQL also supports combining a date and a time expression.

### Syntax

```sql
TIMESTAMP(date_expression);
```

Or:

```sql
TIMESTAMP(date_expression, time_expression);
```

### Example 1

```sql
SELECT TIMESTAMP(
    '2026-10-20 14:30:45'
) AS result;
```

Output:

```
result
2026-10-20 14:30:45
```

### Example 2: Combine a date and a time

```sql
SELECT TIMESTAMP(
    '2026-10-20',
    '14:30:45'
) AS result;
```

Output:

```
result
2026-10-20 14:30:45
```

**Use:** Constructing date-time values from date and time components.

**Note:** The `TIMESTAMP()` function should not be confused with the `TIMESTAMP` column data type.

---

## T — TO\_DAYS()

### Theory

Returns a day number representing the number of days from year 0 to a given date according to MySQL's calendar convention.

### Syntax

```sql
TO_DAYS(date);
```

### Example

```sql
SELECT TO_DAYS('2026-10-20') AS result;
```

Output:

```
result
740243
```

### Calculate the difference in days

```sql
SELECT
    TO_DAYS('2026-10-20') -
    TO_DAYS('2026-10-15') AS result;
```

Output:

```
result
5
```

For ordinary date differences, `DATEDIFF()` is generally simpler.

**Use:** Specialized date calculations and converting dates to day numbers.

---

## T — TO\_SECONDS()

### Theory

Returns the number of seconds from year 0 to the specified date or date-time according to MySQL's internal date-time calculation convention.

### Syntax

```sql
TO_SECONDS(date_expression);
```

### Example

```sql
SELECT TO_SECONDS(
    '2026-10-20 14:30:45'
) AS result;
```

The output is a large integer representing the corresponding second count.

**Use:** Specialized temporal calculations. For normal elapsed-time comparisons, Unix timestamps or direct date-time arithmetic may be more appropriate.

---

## U — UNIX\_TIMESTAMP()

### Theory

Converts a date-time value into Unix time: the number of seconds since `1970-01-01 00:00:00 UTC`.

Without an argument, it returns the current Unix timestamp.

### Syntax

```sql
UNIX_TIMESTAMP();
```

Or:

```sql
UNIX_TIMESTAMP(date_expression);
```

### Example

```sql
SELECT UNIX_TIMESTAMP(
    '1970-01-01 00:00:00'
) AS result;
```

In a UTC session, the output is:

```
result
0
```

### Another example

```sql
SELECT UNIX_TIMESTAMP() AS current_unix_time;
```

The result changes over time.

**Important:** Converting a date-time string to Unix time depends on MySQL's time-zone interpretation. Use an explicit and consistent time-zone strategy when handling timestamps across regions.

**Use:** Epoch timestamps, time-based integrations, and certain elapsed-time calculations.

---

## U — UTC\_DATE()

### Theory

Returns the current date in UTC.

### Syntax

```sql
UTC_DATE();
```

### Example

```sql
SELECT UTC_DATE() AS utc_today;
```

Example output:

```
utc_today
2026-10-20
```

The date depends on the current UTC date.

**Use:** Applications and reports that require a UTC calendar date.

---

## U — UTC\_TIME()

### Theory

Returns the current time in UTC.

### Syntax

```sql
UTC_TIME();
```

### Example

```sql
SELECT UTC_TIME() AS utc_time;
```

Example output:

```
utc_time
09:00:00
```

The time above is illustrative.

**Use:** UTC-based time reporting and integration.

---

## U — UTC\_TIMESTAMP()

### Theory

Returns the current date and time in UTC.

### Syntax

```sql
UTC_TIMESTAMP();
```

### Example

```sql
SELECT UTC_TIMESTAMP() AS utc_datetime;
```

Example output:

```
utc_datetime
2026-10-20 09:00:00
```

The value is illustrative.

### Difference from NOW()

- `NOW()` returns the current date-time in the MySQL session time zone.
- `UTC_TIMESTAMP()` returns the current date-time in UTC.

**Use:** Consistent UTC timestamps across applications and regions.

---

## W — WEEK()

### Theory

Returns the week number of a date. The result depends on the selected week mode, which controls how weeks start and how the first week of the year is defined.

### Syntax

```sql
WEEK(date);
```

Or:

```sql
WEEK(date, mode);
```

### Example

```sql
SELECT WEEK('2026-10-20') AS result;
```

The result depends on the default week mode.

### Specify the mode

```sql
SELECT WEEK('2026-10-20', 3) AS result;
```

Output:

```
result
43
```

Mode `3` uses Monday as the first day of the week and follows ISO-style week numbering rules.

**Use:** Weekly reports and week-based analysis.

**Important:** For consistent reports, explicitly select the week mode rather than relying on defaults.

---

## W — WEEKDAY()

### Theory

Returns the weekday number using this MySQL convention:

- Monday = 0
- Tuesday = 1
- Wednesday = 2
- Thursday = 3
- Friday = 4
- Saturday = 5
- Sunday = 6

### Syntax

```sql
WEEKDAY(date);
```

### Example

```sql
SELECT WEEKDAY('2026-10-20') AS result;
```

Output:

```
result
1
```

October 20, 2026, is Tuesday.

### Difference from DAYOFWEEK()

```sql
SELECT
    DAYOFWEEK('2026-10-20') AS dayofweek_result,
    WEEKDAY('2026-10-20') AS weekday_result;
```

Output:

| dayofweek\_result | weekday\_result |
| --- | --- |
| 3 | 1 |

**Use:** Weekday filtering and scheduling.

---

## W — WEEKOFYEAR()

### Theory

Returns the week number of the year using Monday as the first day of the week and week numbering from 1 to 53.

### Syntax

```sql
WEEKOFYEAR(date);
```

### Example

```sql
SELECT WEEKOFYEAR('2026-10-20') AS result;
```

Output:

```
result
43
```

**Use:** Weekly reports and week-based grouping.

---

## Y — YEAR()

### Theory

Extracts the year component from a date.

### Syntax

```sql
YEAR(date);
```

### Example

```sql
SELECT YEAR('2026-10-20') AS result;
```

Output:

```
result
2026
```

### Real query

```sql
SELECT
    order_id,
    YEAR(order_date) AS order_year
FROM orders;
```

**Use:** Annual reports, year-based filtering, and grouping.

---

## Y — YEARWEEK()

### Theory

Returns a combined year and week number.

The year associated with the week may differ from the calendar year near New Year's Day, depending on the selected mode.

### Syntax

```sql
YEARWEEK(date);
```

Or:

```sql
YEARWEEK(date, mode);
```

### Example

```sql
SELECT YEARWEEK('2026-10-20', 3) AS result;
```

Output:

```
result
202643
```

This represents ISO-style week 43 of 2026.

**Use:** Weekly reporting across year boundaries.

---

# 5\. DATE AND TIME ARITHMETIC

Date arithmetic means calculating a new date or time by adding or subtracting an interval.

## 5.1 Add days

```sql
SELECT DATE_ADD(
    '2026-10-20',
    INTERVAL 15 DAY
) AS result;
```

Output:

```
result
2026-11-04
```

## 5.2 Subtract days

```sql
SELECT DATE_SUB(
    '2026-10-20',
    INTERVAL 10 DAY
) AS result;
```

Output:

```
result
2026-10-10
```

## 5.3 Add months

```sql
SELECT DATE_ADD(
    '2026-01-15',
    INTERVAL 3 MONTH
) AS result;
```

Output:

```
result
2026-04-15
```

## 5.4 Subtract years

```sql
SELECT DATE_SUB(
    '2026-10-20',
    INTERVAL 2 YEAR
) AS result;
```

Output:

```
result
2024-10-20
```

## 5.5 Add hours and minutes

```sql
SELECT DATE_ADD(
    '2026-10-20 10:00:00',
    INTERVAL 90 MINUTE
) AS result;
```

Output:

```
result
2026-10-20 11:30:00
```

## 5.6 Calculate a date difference

```sql
SELECT DATEDIFF(
    '2026-10-20',
    '2026-10-01'
) AS total_days;
```

Output:

```
total_days
19
```

## 5.7 Calculate elapsed seconds

For date-time values representing instants on a consistent time basis:

```sql
SELECT TIMESTAMPDIFF(
    SECOND,
    '2026-10-20 10:00:00',
    '2026-10-20 11:30:00'
) AS elapsed_seconds;
```

Output:

```
elapsed_seconds
5400
```

---

# 6\. IMPORTANT ADDITIONAL DATE FUNCTIONS

## 6.1 DATE\_SUB()

Subtracts an interval from a date or date-time value.

```sql
SELECT DATE_SUB(
    '2026-10-20',
    INTERVAL 1 MONTH
) AS result;
```

Output:

```
result
2026-09-20
```

## 6.2 TIMESTAMPADD()

Adds an interval to a date-time expression.

```sql
SELECT TIMESTAMPADD(
    DAY,
    5,
    '2026-10-20'
) AS result;
```

Output:

```
result
2026-10-25
```

## 6.3 TIMESTAMPDIFF()

Calculates the difference between two temporal expressions in a specified unit.

### Syntax

```sql
TIMESTAMPDIFF(unit, start_datetime, end_datetime);
```

### Example: Difference in days

```sql
SELECT TIMESTAMPDIFF(
    DAY,
    '2026-10-01',
    '2026-10-20'
) AS result;
```

Output:

```
result
19
```

### Example: Difference in months

```sql
SELECT TIMESTAMPDIFF(
    MONTH,
    '2026-01-15',
    '2026-04-15'
) AS result;
```

Output:

```
result
3
```

### Example: Difference in years

```sql
SELECT TIMESTAMPDIFF(
    YEAR,
    '2000-05-10',
    '2026-10-20'
) AS result;
```

Output:

```
result
26
```

**Important:** `TIMESTAMPDIFF()` returns completed units according to the function's calculation rules. It is not necessarily a fractional duration.

## 6.4 CONVERT\_TZ()

Converts a date-time value from one time zone to another.

### Syntax

```sql
CONVERT_TZ(datetime, from_timezone, to_timezone);
```

### Example

```sql
SELECT CONVERT_TZ(
    '2026-10-20 12:00:00',
    '+00:00',
    '+05:30'
) AS result;
```

Output:

```
result
2026-10-20 17:30:00
```

**Use:** Converting UTC timestamps into local time.

Named time zones such as `Asia/Kolkata` require MySQL's time-zone tables to be loaded and available.

---

# 7\. HOW DATE AND TIME FUNCTIONS ARE USED IN SQL QUERIES

## 7.1 Use a date function in SELECT

Requirement: Display each employee's joining year.

```sql
SELECT
    first_name,
    joining_date,
    YEAR(joining_date) AS joining_year
FROM employees;
```

Explanation: `YEAR()` extracts the year from the joining date.

## 7.2 Use a date function in WHERE

Requirement: Find employees who joined during 2020.

```sql
SELECT *
FROM employees
WHERE joining_date >= '2020-01-01'
  AND joining_date < '2021-01-01';
```

This date-range approach is generally preferable to:

```
WHERE YEAR(joining_date) = 2020
```

because it can make better use of an index on `joining_date`.

## 7.3 Use a date function in ORDER BY

Requirement: Sort employees by joining date, newest first.

```sql
SELECT
    first_name,
    joining_date
FROM employees
ORDER BY joining_date DESC;
```

## 7.4 Use a date function in GROUP BY

Requirement: Count orders per month.

```sql
SELECT
    YEAR(order_date) AS order_year,
    MONTH(order_date) AS order_month,
    COUNT(*) AS total_orders
FROM orders
GROUP BY
    YEAR(order_date),
    MONTH(order_date)
ORDER BY
    order_year,
    order_month;
```

Expected output:

| order\_year | order\_month | total\_orders |
| --- | --- | --- |
| 2026 | 1 | 2 |
| 2026 | 2 | 1 |
| 2026 | 3 | 2 |

## 7.5 Use date arithmetic in SELECT

Requirement: Calculate the expected delivery date as seven days after an order.

```sql
SELECT
    order_id,
    order_date,
    DATE_ADD(order_date, INTERVAL 7 DAY) AS expected_delivery
FROM orders;
```

## 7.6 Use date functions with CASE

Requirement: Classify orders by delivery duration.

```sql
SELECT
    order_id,
    DATEDIFF(delivery_date, order_date) AS delivery_days,
    CASE
        WHEN DATEDIFF(delivery_date, order_date) <= 3
            THEN 'Fast'
        WHEN DATEDIFF(delivery_date, order_date) <= 7
            THEN 'Normal'
        ELSE 'Delayed'
    END AS delivery_status
FROM orders;
```

Expected output:

| order\_id | delivery\_days | delivery\_status |
| --- | --- | --- |
| 101 | 5 | Normal |
| 102 | 5 | Normal |
| 103 | 8 | Delayed |
| 104 | 3 | Fast |
| 105 | 8 | Delayed |

---

# 8\. SOLVED SQL PRACTICE QUESTIONS

Use the `employees` and `orders` tables created earlier.

## Question 1: Display the current date.

```sql
SELECT CURDATE() AS today;
```

The result is the current date when the query runs.

## Question 2: Display the current date and time.

```sql
SELECT NOW() AS current_datetime;
```

The result depends on execution time and session time zone.

## Question 3: Display each employee's joining year.

```sql
SELECT
    first_name,
    YEAR(joining_date) AS joining_year
FROM employees;
```

Expected output:

| first\_name | joining\_year |
| --- | --- |
| Amit | 2020 |
| Priya | 2021 |
| Rahul | 2018 |
| Neha | 2022 |
| Sneha | 2019 |

## Question 4: Display each employee's joining month.

```sql
SELECT
    first_name,
    MONTH(joining_date) AS joining_month
FROM employees;
```

Expected output:

| first\_name | joining\_month |
| --- | --- |
| Amit | 1 |
| Priya | 6 |
| Rahul | 3 |
| Neha | 11 |
| Sneha | 7 |

## Question 5: Display the full month name of each joining date.

```sql
SELECT
    first_name,
    MONTHNAME(joining_date) AS joining_month
FROM employees;
```

Expected output:

| first\_name | joining\_month |
| --- | --- |
| Amit | January |
| Priya | June |
| Rahul | March |
| Neha | November |
| Sneha | July |

## Question 6: Find employees who joined in 2020 or later.

```sql
SELECT *
FROM employees
WHERE joining_date >= '2020-01-01';
```

Expected employees: Amit, Priya, and Neha.

## Question 7: Find employees who joined before 2020.

```sql
SELECT *
FROM employees
WHERE joining_date < '2020-01-01';
```

Expected employees: Rahul and Sneha.

## Question 8: Find the number of days between two dates.

```sql
SELECT DATEDIFF(
    '2026-12-31',
    '2026-01-01'
) AS total_days;
```

Output:

```
total_days
364
```

## Question 9: Calculate delivery duration for every order.

```sql
SELECT
    order_id,
    DATEDIFF(delivery_date, order_date) AS delivery_days
FROM orders;
```

Expected output:

| order\_id | delivery\_days |
| --- | --- |
| 101 | 5 |
| 102 | 5 |
| 103 | 8 |
| 104 | 3 |
| 105 | 8 |

## Question 10: Find orders placed in January 2026.

```sql
SELECT *
FROM orders
WHERE order_date >= '2026-01-01'
  AND order_date < '2026-02-01';
```

Expected order IDs: 101 and 102.

## Question 11: Find orders placed in March 2026.

```sql
SELECT *
FROM orders
WHERE order_date >= '2026-03-01'
  AND order_date < '2026-04-01';
```

Expected order IDs: 104 and 105.

## Question 12: Display a date in DD-MM-YYYY format.

```sql
SELECT DATE_FORMAT(
    '2026-10-20',
    '%d-%m-%Y'
) AS formatted_date;
```

Output:

```
formatted_date
20-10-2026
```

## Question 13: Find the last day of each order's month.

```sql
SELECT
    order_id,
    order_date,
    LAST_DAY(order_date) AS month_end
FROM orders;
```

Expected output:

| order\_id | month\_end |
| --- | --- |
| 101 | 2026-01-31 |
| 102 | 2026-01-31 |
| 103 | 2026-02-28 |
| 104 | 2026-03-31 |
| 105 | 2026-03-31 |

## Question 14: Find the weekday name for every order.

```sql
SELECT
    order_id,
    order_date,
    DAYNAME(order_date) AS weekday_name
FROM orders;
```

Expected output:

| order\_id | weekday\_name |
| --- | --- |
| 101 | Monday |
| 102 | Thursday |
| 103 | Tuesday |
| 104 | Sunday |
| 105 | Friday |

## Question 15: Find orders whose delivery took more than five days.

```sql
SELECT *
FROM orders
WHERE DATEDIFF(delivery_date, order_date) > 5;
```

Expected order IDs: 103 and 105.

## Question 16: Find employees who joined in the first quarter.

```sql
SELECT *
FROM employees
WHERE QUARTER(joining_date) = 1;
```

Expected employee: Rahul.

## Question 17: Add 30 days to each joining date.

```sql
SELECT
    first_name,
    joining_date,
    DATE_ADD(joining_date, INTERVAL 30 DAY) AS after_30_days
FROM employees;
```

This returns each joining date plus 30 days.

## Question 18: Find the number of orders placed in each month.

```sql
SELECT
    MONTH(order_date) AS order_month,
    COUNT(*) AS total_orders
FROM orders
GROUP BY MONTH(order_date)
ORDER BY order_month;
```

Expected output:

| order\_month | total\_orders |
| --- | --- |
| 1 | 2 |
| 2 | 1 |
| 3 | 2 |

## Question 19: Calculate total order amount by month.

```sql
SELECT
    YEAR(order_date) AS order_year,
    MONTH(order_date) AS order_month,
    SUM(order_amount) AS total_amount
FROM orders
GROUP BY
    YEAR(order_date),
    MONTH(order_date)
ORDER BY
    order_year,
    order_month;
```

Expected output:

| order\_year | order\_month | total\_amount |
| --- | --- | --- |
| 2026 | 1 | 7000.00 |
| 2026 | 2 | 3200.00 |
| 2026 | 3 | 6800.00 |

## Question 20: Find employees with birthdays in August.

```sql
SELECT *
FROM employees
WHERE MONTH(birth_date) = 8;
```

Expected employee: Priya.

## Question 21: Find employees born before 2000.

```sql
SELECT *
FROM employees
WHERE birth_date < '2000-01-01';
```

Expected employees: Amit, Priya, Rahul, and Sneha.

## Question 22: Calculate age in completed years as of a specified date.

```sql
SELECT
    first_name,
    TIMESTAMPDIFF(
        YEAR,
        birth_date,
        '2026-10-20'
    ) AS age
FROM employees;
```

Expected output:

| first\_name | age |
| --- | --- |
| Amit | 28 |
| Priya | 27 |
| Rahul | 30 |
| Neha | 26 |
| Sneha | 29 |

These are completed years as of October 20, 2026.

## Question 23: Find employees with at least five years of service as of October 20, 2026.

```sql
SELECT
    first_name,
    joining_date,
    TIMESTAMPDIFF(
        YEAR,
        joining_date,
        '2026-10-20'
    ) AS service_years
FROM employees
WHERE joining_date <= DATE_SUB(
    '2026-10-20',
    INTERVAL 5 YEAR
);
```

Expected employees: Amit, Rahul, and Sneha.

## Question 24: Find the number of days remaining until a fixed deadline.

```sql
SELECT DATEDIFF(
    '2026-12-31',
    CURDATE()
) AS days_remaining;
```

The result depends on the current date when the query runs. A negative result means the deadline has passed.

## Question 25: Find the earliest and latest order dates.

```sql
SELECT
    MIN(order_date) AS earliest_order,
    MAX(order_date) AS latest_order
FROM orders;
```

Output:

| earliest\_order | latest\_order |
| --- | --- |
| 2026-01-05 | 2026-03-20 |

`MIN()` and `MAX()` are aggregate functions, but they are particularly useful with dates.

## Question 26: Find orders placed during the current month.

```sql
SELECT *
FROM orders
WHERE order_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01')
  AND order_date < DATE_ADD(
      DATE_FORMAT(CURDATE(), '%Y-%m-01'),
      INTERVAL 1 MONTH
  );
```

This query identifies records in the current calendar month. It returns rows only if the `orders` table contains dates in that month.

## Question 27: Find orders delivered earlier than the expected seven-day deadline.

```sql
SELECT
    order_id,
    order_date,
    delivery_date
FROM orders
WHERE delivery_date < DATE_ADD(
    order_date,
    INTERVAL 7 DAY
);
```

Expected order IDs: 101, 102, and 104.

## Question 28: Find the average delivery time.

```sql
SELECT AVG(
    DATEDIFF(delivery_date, order_date)
) AS average_delivery_days
FROM orders;
```

Output:

```
average_delivery_days
5.8000
```

## Question 29: Find the total orders placed on each weekday.

```sql
SELECT
    DAYNAME(order_date) AS weekday_name,
    COUNT(*) AS total_orders
FROM orders
GROUP BY DAYNAME(order_date)
ORDER BY total_orders DESC;
```

This groups orders by weekday name.

For a stable Monday-to-Sunday order, group or sort using `WEEKDAY(order_date)` instead of sorting alphabetically by weekday name.

## Question 30: Display the first day and last day of each order's month.

```sql
SELECT
    order_id,
    order_date,
    DATE_SUB(
        order_date,
        INTERVAL DAYOFMONTH(order_date) - 1 DAY
    ) AS month_start,
    LAST_DAY(order_date) AS month_end
FROM orders;
```

Example output:

| order\_id | month\_start | month\_end |
| --- | --- | --- |
| 101 | 2026-01-01 | 2026-01-31 |
| 102 | 2026-01-01 | 2026-01-31 |
| 103 | 2026-02-01 | 2026-02-28 |
| 104 | 2026-03-01 | 2026-03-31 |
| 105 | 2026-03-01 | 2026-03-31 |

---

# 9\. INTERVIEW QUESTIONS AND ANSWERS

## Q1. What are SQL date and time functions?

They are built-in functions for retrieving, extracting, formatting, comparing, and calculating dates and times.

Examples include `NOW()`, `CURDATE()`, `DATEDIFF()`, `DATE_ADD()`, and `DATE_FORMAT()`.

## Q2. What is the difference between NOW() and CURDATE()?

- `NOW()` returns the current date and time.
- `CURDATE()` returns only the current date.

```sql
SELECT NOW(), CURDATE();
```

Example output:

```
NOW()                 CURDATE()
2026-10-20 14:30:45   2026-10-20
```

## Q3. What is the difference between DATE and DATETIME?

- `DATE` stores a calendar date.
- `DATETIME` stores both a date and a time.

Examples:

```
DATE:     2026-10-20
DATETIME: 2026-10-20 14:30:45
```

## Q4. What is the difference between TIMESTAMP and DATETIME?

`DATETIME` stores the specified date-time value without automatic time-zone conversion.

`TIMESTAMP` is converted between the session time zone and UTC for storage and retrieval in MySQL.

Choose according to the application's time-zone requirements.

## Q5. What is the difference between DATEDIFF() and TIMESTAMPDIFF()?

`DATEDIFF()` returns a difference in days and ignores time portions.

`TIMESTAMPDIFF()` returns the difference in a specified unit, such as seconds, minutes, hours, days, months, or years.

```sql
SELECT DATEDIFF(
    '2026-10-20',
    '2026-10-15'
);
```

Output: `5`

```sql
SELECT TIMESTAMPDIFF(
    HOUR,
    '2026-10-20 10:00:00',
    '2026-10-20 15:00:00'
);
```

Output: `5`

## Q6. What is the difference between DATE\_ADD() and ADDDATE()?

Both can add days or intervals to dates in MySQL.

```sql
SELECT DATE_ADD('2026-10-20', INTERVAL 5 DAY);
```

```sql
SELECT ADDDATE('2026-10-20', 5);
```

Both return `2026-10-25`.

## Q7. What is the difference between DATE\_SUB() and SUBDATE()?

Both subtract days or intervals from dates in MySQL.

```sql
SELECT DATE_SUB('2026-10-20', INTERVAL 5 DAY);
```

```sql
SELECT SUBDATE('2026-10-20', 5);
```

Both return `2026-10-15`.

## Q8. How do you extract the year from a date?

```sql
SELECT YEAR('2026-10-20');
```

Output: `2026`

You can also use:

```sql
SELECT EXTRACT(YEAR FROM '2026-10-20');
```

## Q9. How do you extract the month from a date?

```sql
SELECT MONTH('2026-10-20');
```

Output: `10`

## Q10. How do you find the last day of a month?

```sql
SELECT LAST_DAY('2026-02-10');
```

Output: `2026-02-28`

## Q11. How do you calculate age in completed years?

```sql
SELECT TIMESTAMPDIFF(
    YEAR,
    '2000-05-10',
    '2026-10-20'
) AS age;
```

Output: `26`

Use the person's birth date and the appropriate reference date.

## Q12. How do you format a date as DD-MM-YYYY?

```sql
SELECT DATE_FORMAT(
    '2026-10-20',
    '%d-%m-%Y'
);
```

Output: `20-10-2026`

## Q13. What does STR\_TO\_DATE() do?

It converts a formatted text value into a date or date-time value.

```sql
SELECT STR_TO_DATE(
    '20-10-2026',
    '%d-%m-%Y'
);
```

Output: `2026-10-20`

## Q14. What is the difference between WEEKDAY() and DAYOFWEEK()?

`WEEKDAY()` returns Monday = 0 through Sunday = 6.

`DAYOFWEEK()` returns Sunday = 1 through Saturday = 7.

## Q15. What is a leap year?

A leap year has 366 days instead of 365, with February containing 29 days.

For example:

```sql
SELECT LAST_DAY('2024-02-01');
```

Output: `2024-02-29`

## Q16. How do you find records from a particular month?

Use a date range.

```sql
SELECT *
FROM orders
WHERE order_date >= '2026-03-01'
  AND order_date < '2026-04-01';
```

This approach is generally preferable to extracting the month with `MONTH()` in the `WHERE` condition when you want efficient use of an index on the date column.

## Q17. How do you calculate the difference in hours?

```sql
SELECT TIMESTAMPDIFF(
    HOUR,
    '2026-10-20 09:00:00',
    '2026-10-20 17:00:00'
) AS total_hours;
```

Output: `8`

## Q18. What does UNIX\_TIMESTAMP() return?

It returns Unix time, measured in seconds from the Unix epoch, `1970-01-01 00:00:00 UTC`.

```sql
SELECT UNIX_TIMESTAMP(
    '1970-01-01 00:00:00'
);
```

In a UTC session, output: `0`.

## Q19. What is the difference between NOW() and UTC\_TIMESTAMP()?

`NOW()` returns the current date-time in the session time zone.

`UTC_TIMESTAMP()` returns the current date-time in UTC.

## Q20. How do you group sales by month?

```sql
SELECT
    YEAR(order_date) AS order_year,
    MONTH(order_date) AS order_month,
    SUM(order_amount) AS total_sales
FROM orders
GROUP BY
    YEAR(order_date),
    MONTH(order_date)
ORDER BY
    order_year,
    order_month;
```

Grouping by both year and month prevents January from different years from being combined into one group.

---

# 10\. COMMON MISTAKES TO AVOID

## Mistake 1: Using the wrong date format

MySQL commonly represents dates as:

```
YYYY-MM-DD
```

For example:

```sql
SELECT DATE('2026-10-20');
```

For a string formatted as `20-10-2026`, use `STR_TO_DATE()` with the matching format.

```sql
SELECT STR_TO_DATE('20-10-2026', '%d-%m-%Y');
```

## Mistake 2: Confusing format specifiers

In MySQL:

- `%m` means month.
- `%i` means minutes.
- `%M` means full month name.
- `%s` means seconds.

Correct example:

```sql
SELECT DATE_FORMAT(
    '2026-10-20 14:30:45',
    '%d-%m-%Y %H:%i:%s'
);
```

Output:

```
20-10-2026 14:30:45
```

## Mistake 3: Forgetting the argument order of DATEDIFF()

Correct:

```sql
SELECT DATEDIFF('2026-10-20', '2026-10-15');
```

Output: `5`

Reversing the arguments returns `-5`.

## Mistake 4: Using MONTH() alone to filter a year-specific month

This query can combine March from multiple years:

```sql
SELECT *
FROM orders
WHERE MONTH(order_date) = 3;
```

For March 2026 specifically, use:

```sql
SELECT *
FROM orders
WHERE order_date >= '2026-03-01'
  AND order_date < '2026-04-01';
```

## Mistake 5: Treating formatted dates as dates

`DATE_FORMAT()` returns a string.

Use actual date values for comparisons, calculations, and date sorting.

## Mistake 6: Ignoring time portions

`DATEDIFF()` ignores the time portion.

For example, it calculates whole-day differences rather than exact elapsed hours.

Use `TIMESTAMPDIFF()` or another appropriate method for time-based calculations.

## Mistake 7: Confusing session time with UTC

`NOW()` and `UTC_TIMESTAMP()` can return different values.

Be consistent about time zones when saving events, comparing timestamps, and generating reports.

## Mistake 8: Assuming every date calculation is a simple number of days

Adding one month is not always equivalent to adding 30 days.

For example:

```sql
SELECT DATE_ADD(
    '2026-01-31',
    INTERVAL 1 MONTH
);
```

Output:

```
2026-02-28
```

Calendar intervals follow calendar rules.

## Mistake 9: Ignoring NULL values

If a date column contains `NULL`, date functions may return `NULL`.

Example:

```sql
SELECT DATEDIFF(NULL, '2026-10-20') AS result;
```

Output:

```
result
NULL
```

Use `COALESCE()` only when an appropriate fallback value makes sense.

## Mistake 10: Calculating age by subtracting years alone

This approach can overstate age if the birthday has not occurred yet in the current year:

```
YEAR(CURDATE()) - YEAR(birth_date)
```

Prefer:

```
TIMESTAMPDIFF(
    YEAR,
    birth_date,
    CURDATE()
)
```

This calculates completed years.

---

# 11\. QUICK REVISION CHEAT SHEET

| Function | Purpose | Example |
| --- | --- | --- |
| `ADDDATE()` | Add days or an interval | `ADDDATE('2026-10-20', 5)` |
| `CURDATE()` | Current date | `CURDATE()` |
| `CURRENT_DATE()` | Current date | `CURRENT_DATE()` |
| `CURRENT_TIME()` | Current time | `CURRENT_TIME()` |
| `CURRENT_TIMESTAMP()` | Current date and time | `CURRENT_TIMESTAMP()` |
| `DATE()` | Extract date portion | `DATE('2026-10-20 14:30:00')` |
| `DATE_ADD()` | Add an interval | `DATE_ADD('2026-10-20', INTERVAL 5 DAY)` |
| `DATE_FORMAT()` | Format a date | `DATE_FORMAT('2026-10-20', '%d-%m-%Y')` |
| `DATE_SUB()` | Subtract an interval | `DATE_SUB('2026-10-20', INTERVAL 5 DAY)` |
| `DATEDIFF()` | Difference in days | `DATEDIFF('2026-10-20', '2026-10-15')` |
| `DAY()` | Day of month | `DAY('2026-10-20')` |
| `DAYNAME()` | Weekday name | `DAYNAME('2026-10-20')` |
| `DAYOFMONTH()` | Day number in month | `DAYOFMONTH('2026-10-20')` |
| `DAYOFWEEK()` | Weekday number, Sunday = 1 | `DAYOFWEEK('2026-10-20')` |
| `DAYOFYEAR()` | Day number in year | `DAYOFYEAR('2026-10-20')` |
| `EXTRACT()` | Extract a date component | `EXTRACT(YEAR FROM '2026-10-20')` |
| `FROM_DAYS()` | Convert day number to date | `FROM_DAYS(740000)` |
| `HOUR()` | Extract hour | `HOUR('14:30:00')` |
| `LAST_DAY()` | Last day of month | `LAST_DAY('2026-02-10')` |
| `MAKEDATE()` | Build date from year and day number | `MAKEDATE(2026, 293)` |
| `MAKETIME()` | Build time from components | `MAKETIME(14, 30, 45)` |
| `MICROSECOND()` | Extract microseconds | `MICROSECOND('14:30:45.123456')` |
| `MINUTE()` | Extract minute | `MINUTE('14:30:00')` |
| `MONTH()` | Extract month number | `MONTH('2026-10-20')` |
| `MONTHNAME()` | Extract month name | `MONTHNAME('2026-10-20')` |
| `NOW()` | Current date and time | `NOW()` |
| `PERIOD_ADD()` | Add months to a period | `PERIOD_ADD(202610, 3)` |
| `PERIOD_DIFF()` | Difference between periods in months | `PERIOD_DIFF(202610, 202601)` |
| `QUARTER()` | Extract quarter | `QUARTER('2026-10-20')` |
| `SEC_TO_TIME()` | Convert seconds to time | `SEC_TO_TIME(3661)` |
| `SECOND()` | Extract seconds | `SECOND('14:30:45')` |
| `STR_TO_DATE()` | Convert string to date | `STR_TO_DATE('20-10-2026', '%d-%m-%Y')` |
| `SUBDATE()` | Subtract days or an interval | `SUBDATE('2026-10-20', 5)` |
| `SUBTIME()` | Subtract time | `SUBTIME('10:00:00', '01:00:00')` |
| `TIME()` | Extract time portion | `TIME('2026-10-20 14:30:45')` |
| `TIME_FORMAT()` | Format a time value | `TIME_FORMAT('14:30:00', '%h:%i %p')` |
| `TIMEDIFF()` | Difference between time values | `TIMEDIFF('14:00:00', '09:00:00')` |
| `TIMESTAMP()` | Construct a date-time value | `TIMESTAMP('2026-10-20', '14:30:00')` |
| `TIMESTAMPADD()` | Add an interval | `TIMESTAMPADD(DAY, 5, '2026-10-20')` |
| `TIMESTAMPDIFF()` | Difference in a specified unit | `TIMESTAMPDIFF(DAY, '2026-10-01', '2026-10-20')` |
| `TO_DAYS()` | Convert date to day number | `TO_DAYS('2026-10-20')` |
| `TO_SECONDS()` | Convert date-time to second count | `TO_SECONDS('2026-10-20 14:30:00')` |
| `UNIX_TIMESTAMP()` | Convert date-time to Unix time | `UNIX_TIMESTAMP()` |
| `UTC_DATE()` | Current UTC date | `UTC_DATE()` |
| `UTC_TIME()` | Current UTC time | `UTC_TIME()` |
| `UTC_TIMESTAMP()` | Current UTC date-time | `UTC_TIMESTAMP()` |
| `WEEK()` | Week number | `WEEK('2026-10-20', 3)` |
| `WEEKDAY()` | Weekday, Monday = 0 | `WEEKDAY('2026-10-20')` |
| `WEEKOFYEAR()` | Week number with Monday-based convention | `WEEKOFYEAR('2026-10-20')` |
| `YEAR()` | Extract year | `YEAR('2026-10-20')` |
| `YEARWEEK()` | Year and week number | `YEARWEEK('2026-10-20', 3)` |
| `CONVERT_TZ()` | Convert between time zones | `CONVERT_TZ('2026-10-20 12:00:00', '+00:00', '+05:30')` |

---

# 12\. FINAL LEARNING ROADMAP

For SQL interviews and practical work, learn the functions in this order.

## Level 1: Basic date retrieval and extraction

Master:

- `CURDATE()`
- `NOW()`
- `DATE()`
- `YEAR()`
- `MONTH()`
- `DAY()`
- `HOUR()`
- `MINUTE()`
- `SECOND()`

## Level 2: Date formatting and parsing

Master:

- `DATE_FORMAT()`
- `STR_TO_DATE()`
- `TIME_FORMAT()`
- `MONTHNAME()`
- `DAYNAME()`

## Level 3: Date arithmetic

Master:

- `DATE_ADD()`
- `DATE_SUB()`
- `ADDDATE()`
- `SUBDATE()`
- `DATEDIFF()`
- `TIMESTAMPDIFF()`
- `TIMEDIFF()`

## Level 4: Reporting and business analysis

Master:

- `LAST_DAY()`
- `QUARTER()`
- `WEEK()`
- `WEEKDAY()`
- `YEARWEEK()`
- `TIMESTAMPADD()`

Practice combining them with:

- `SELECT`
- `WHERE`
- `GROUP BY`
- `ORDER BY`
- `CASE`
- `COUNT()`
- `SUM()`
- `AVG()`
- `MIN()`
- `MAX()`

## Most important functions for interviews

If you are short on time, prioritize these ten:

1. `NOW()`
2. `CURDATE()`
3. `YEAR()`
4. `MONTH()`
5. `DATE_FORMAT()`
6. `DATEDIFF()`
7. `DATE_ADD()`
8. `DATE_SUB()`
9. `TIMESTAMPDIFF()`
10. `LAST_DAY()`

**Best practice:** Do not memorize functions in isolation. Practice combining date and time functions with filtering, grouping, aggregation, and real business requirements such as employee service duration, monthly sales, delivery times, and overdue orders.

**End of SQL Date and Time Functions notes.**
