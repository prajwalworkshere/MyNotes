# SQL INNER JOIN — Practice Questions with Answers

These notes include sample tables, `INNER JOIN` questions, SQL queries in ` ```sql ` format, expected outputs, and explanations. You can practice each query directly in MySQL.

# 1. Create Sample Tables

We will use three tables:

- `employees` — employee details
- `departments` — department details
- `projects` — project details

## Table 1: `departments`

``` sql
CREATE TABLE departments (
    dept_id INT PRIMARY KEY,
    dept_name VARCHAR(50),
    location VARCHAR(50)
);
```

Insert sample data:

``` sql
INSERT INTO departments VALUES
(10, 'IT', 'Pune'),
(20, 'HR', 'Mumbai'),
(30, 'Finance', 'Delhi'),
(40, 'Marketing', 'Chennai'),
(50, 'Sales', 'Hyderabad');
```

Expected data:

| dept_id | dept_name | location  |
| ------- | --------- | --------- |
| 10      | IT        | Pune      |
| 20      | HR        | Mumbai    |
| 30      | Finance   | Delhi     |
| 40      | Marketing | Chennai   |
| 50      | Sales     | Hyderabad |

## Table 2: `employees`

```sql
CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    emp_name VARCHAR(50),
    salary DECIMAL(10,2),
    dept_id INT,
    manager_id INT
);
```

Insert sample data:

```sql
INSERT INTO employees VALUES
(101, 'Amit', 60000, 10, NULL),
(102, 'Priya', 75000, 10, 101),
(103, 'Rahul', 45000, 20, 101),
(104, 'Sneha', 80000, 30, 102),
(105, 'Karan', 55000, 20, 103),
(106, 'Neha', 90000, 10, 102),
(107, 'Vikas', 40000, NULL, 101),
(108, 'Meera', 65000, 40, 102),
(109, 'Arjun', 50000, 50, 101),
(110, 'Pooja', 70000, 30, 104);
```

Expected data:

| emp_id | emp_name | salary | dept_id | manager_id |
| ------ | -------- | ------ | ------- | ---------- |
| 101    | Amit     | 60000  | 10      | NULL       |
| 102    | Priya    | 75000  | 10      | 101        |
| 103    | Rahul    | 45000  | 20      | 101        |
| 104    | Sneha    | 80000  | 30      | 102        |
| 105    | Karan    | 55000  | 20      | 103        |
| 106    | Neha     | 90000  | 10      | 102        |
| 107    | Vikas    | 40000  | NULL    | 101        |
| 108    | Meera    | 65000  | 40      | 102        |
| 109    | Arjun    | 50000  | 50      | 101        |
| 110    | Pooja    | 70000  | 30      | 104        |

## Table 3: `projects`

```sql
CREATE TABLE projects (
    project_id INT PRIMARY KEY,
    project_name VARCHAR(50),
    emp_id INT
);
```

Insert sample data:

```sql
INSERT INTO projects VALUES
(1, 'Website', 101),
(2, 'App Development', 102),
(3, 'Payroll', 104),
(4, 'Database Migration', 106),
(5, 'Recruitment', 103),
(6, 'Marketing Campaign', 108),
(7, 'Cloud Migration', 102),
(8, 'Analytics', 110),
(9, 'Inventory System', 999);
```

Here, employee ID `999` does not exist in the `employees` table. This helps us understand how `INNER JOIN` handles unmatched records.

# 2. Basic INNER JOIN Questions

## Question 1. Display employee names with their department names.

```sql
SELECT
    e.emp_name,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id;
```

Expected output:

| emp_name | dept_name |
| -------- | --------- |
| Amit     | IT        |
| Priya    | IT        |
| Rahul    | HR        |
| Sneha    | Finance   |
| Karan    | HR        |
| Neha     | IT        |
| Meera    | Marketing |
| Arjun    | Sales     |
| Pooja    | Finance   |

Explanation:

- `e` is the alias for `employees`.
- `d` is the alias for `departments`.
- `ON e.dept_id = d.dept_id` matches employees with departments using the department ID.
- Vikas is excluded because his `dept_id` is `NULL`.
- All five departments exist, but departments without employees would also be excluded.

Remember: `INNER JOIN` returns only rows for which the join condition matches in both tables.

## Question 2. Display employee ID, employee name, salary, and department name.

```sql
SELECT
    e.emp_id,
    e.emp_name,
    e.salary,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id;
```

Explanation: This is the same join as Question 1, but we select additional columns from both tables.

## Question 3. Find employees who work in the IT department.

```sql
SELECT
    e.emp_name,
    e.salary,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE d.dept_name = 'IT';
```

Expected output:

| emp_name | salary | dept_name |
| -------- | ------ | --------- |
| Amit     | 60000  | IT        |
| Priya    | 75000  | IT        |
| Neha     | 90000  | IT        |

Explanation: First, the join matches employees and departments. Then `WHERE` filters the result to IT.

## Question 4. Find employees who work in Pune.

```sql
SELECT
    e.emp_name,
    d.dept_name,
    d.location
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE d.location = 'Pune';
```

Expected output:

| emp_name | dept_name | location |
| -------- | --------- | -------- |
| Amit     | IT        | Pune     |
| Priya    | IT        | Pune     |
| Neha     | IT        | Pune     |

## Question 5. Display employees earning more than 60,000 along with their departments.

```sql
SELECT
    e.emp_name,
    e.salary,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE e.salary > 60000;
```

Expected output:

| emp_name | salary | dept_name |
| -------- | ------ | --------- |
| Priya    | 75000  | IT        |
| Sneha    | 80000  | Finance   |
| Neha     | 90000  | IT        |
| Meera    | 65000  | Marketing |
| Pooja    | 70000  | Finance   |

Notice that Vikas does not appear, even though his salary is 40,000, because he does not satisfy the join condition in the first place.

# 3. INNER JOIN with Sorting and Conditions

## Question 6. Display all matched employees in descending order of salary.

```sql
SELECT
    e.emp_name,
    e.salary,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
ORDER BY e.salary DESC;
```

Expected output:

| emp_name | salary | dept_name |
| -------- | ------ | --------- |
| Neha     | 90000  | IT        |
| Sneha    | 80000  | Finance   |
| Priya    | 75000  | IT        |
| Pooja    | 70000  | Finance   |
| Meera    | 65000  | Marketing |
| Amit     | 60000  | IT        |
| Karan    | 55000  | HR        |
| Arjun    | 50000  | Sales     |
| Rahul    | 45000  | HR        |

## Question 7. Find employees working in either IT or Finance.

```sql
SELECT
    e.emp_name,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE d.dept_name IN ('IT', 'Finance');
```

Expected output:

| emp_name | dept_name |
| -------- | --------- |
| Amit     | IT        |
| Priya    | IT        |
| Sneha    | Finance   |
| Neha     | IT        |
| Pooja    | Finance   |

## Question 8. Find employees whose salary is between 50,000 and 75,000.

```sql
SELECT
    e.emp_name,
    e.salary,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE e.salary BETWEEN 50000 AND 75000;
```

`BETWEEN` includes both boundaries, so employees earning exactly 50,000 or 75,000 are included if they have a matching department.

## Question 9. Find employees whose department is not HR.

```sql
SELECT
    e.emp_name,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE d.dept_name <> 'HR';
```

Important: This query excludes employees in HR, as well as employees who have no matching department. `<>` means “not equal to.”

## Question 10. Find employees whose names start with A.

```sql
SELECT
    e.emp_name,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE e.emp_name LIKE 'A%';
```

Expected output:

| emp_name | dept_name |
| -------- | --------- |
| Amit     | IT        |
| Arjun    | Sales     |

# 4. INNER JOIN with Aggregate Functions

## Question 11. Count employees in each department.

```sql
SELECT
    d.dept_name,
    COUNT(e.emp_id) AS total_employees
FROM departments AS d
INNER JOIN employees AS e
    ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name;
```

Expected output:

| dept_name | total_employees |
| --------- | --------------- |
| IT        | 3               |
| HR        | 2               |
| Finance   | 2               |
| Marketing | 1               |
| Sales     | 1               |

Explanation:

1. Join each department to its matching employees.
2. Group the matched rows by department.
3. Count employee IDs in each group.

Because this is an `INNER JOIN`, a department with zero employees would not appear.

## Question 12. Find departments having more than one employee.

```sql
SELECT
    d.dept_name,
    COUNT(e.emp_id) AS total_employees
FROM departments AS d
INNER JOIN employees AS e
    ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name
HAVING COUNT(e.emp_id) > 1;
```

Expected output:

| dept_name | total_employees |
| --------- | --------------- |
| IT        | 3               |
| HR        | 2               |
| Finance   | 2               |

Why `HAVING`? Because we are filtering groups using an aggregate result (`COUNT`), rather than filtering individual rows.

## Question 13. Find the average salary in each department.

```sql
SELECT
    d.dept_name,
    AVG(e.salary) AS average_salary
FROM departments AS d
INNER JOIN employees AS e
    ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name;
```

Expected output:

| dept_name | average_salary |
| --------- | -------------- |
| IT        | 75000          |
| HR        | 50000          |
| Finance   | 75000          |
| Marketing | 65000          |
| Sales     | 50000          |

## Question 14. Find the highest salary in each department.

```sql
SELECT
    d.dept_name,
    MAX(e.salary) AS highest_salary
FROM departments AS d
INNER JOIN employees AS e
    ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name;
```

Expected output:

| dept_name | highest_salary |
| --------- | -------------- |
| IT        | 90000          |
| HR        | 55000          |
| Finance   | 80000          |
| Marketing | 65000          |
| Sales     | 50000          |

## Question 15. Find departments whose average salary exceeds 60,000.

```sql
SELECT
    d.dept_name,
    AVG(e.salary) AS average_salary
FROM departments AS d
INNER JOIN employees AS e
    ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name
HAVING AVG(e.salary) > 60000;
```

Expected output:

| dept_name | average_salary |
| --------- | -------------- |
| IT        | 75000          |
| Finance   | 75000          |
| Marketing | 65000          |

# 5. INNER JOIN Between Three Tables

Now we will join `employees`, `departments`, and `projects`.

## Question 16. Display employee names, department names, and project names.

```sql
SELECT
    e.emp_name,
    d.dept_name,
    p.project_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
INNER JOIN projects AS p
    ON e.emp_id = p.emp_id;
```

Expected output:

| emp_name | dept_name | project_name       |
| -------- | --------- | ------------------ |
| Amit     | IT        | Website            |
| Priya    | IT        | App Development    |
| Priya    | IT        | Cloud Migration    |
| Rahul    | HR        | Recruitment        |
| Sneha    | Finance   | Payroll            |
| Neha     | IT        | Database Migration |
| Meera    | Marketing | Marketing Campaign |
| Pooja    | Finance   | Analytics          |

Why are Arjun and Karan missing? They have matching departments but no projects.

Why is project `Inventory System` missing? Its `emp_id` is `999`, which has no matching employee.

This is a key property of `INNER JOIN`: every joined relationship must match.

## Question 17. Find employees assigned to more than one project.

```sql
SELECT
    e.emp_id,
    e.emp_name,
    COUNT(p.project_id) AS total_projects
FROM employees AS e
INNER JOIN projects AS p
    ON e.emp_id = p.emp_id
GROUP BY e.emp_id, e.emp_name
HAVING COUNT(p.project_id) > 1;
```

Expected output:

| emp_id | emp_name | total_projects |
| ------ | -------- | -------------- |
| 102    | Priya    | 2              |

## Question 18. Display projects belonging to employees in the IT department.

```sql
SELECT
    p.project_name,
    e.emp_name,
    d.dept_name
FROM projects AS p
INNER JOIN employees AS e
    ON p.emp_id = e.emp_id
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE d.dept_name = 'IT';
```

Expected output:

| project_name       | emp_name | dept_name |
| ------------------ | -------- | --------- |
| Website            | Amit     | IT        |
| App Development    | Priya    | IT        |
| Cloud Migration    | Priya    | IT        |
| Database Migration | Neha     | IT        |

## Question 19. Find projects assigned to employees earning more than 70,000.

```sql
SELECT
    p.project_name,
    e.emp_name,
    e.salary
FROM projects AS p
INNER JOIN employees AS e
    ON p.emp_id = e.emp_id
WHERE e.salary > 70000;
```

Expected output:

| project_name       | emp_name | salary |
| ------------------ | -------- | ------ |
| App Development    | Priya    | 75000  |
| Payroll            | Sneha    | 80000  |
| Database Migration | Neha     | 90000  |
| Cloud Migration    | Priya    | 75000  |

## Question 20. Find the number of projects assigned to each department.

```sql
SELECT
    d.dept_name,
    COUNT(p.project_id) AS total_projects
FROM departments AS d
INNER JOIN employees AS e
    ON d.dept_id = e.dept_id
INNER JOIN projects AS p
    ON e.emp_id = p.emp_id
GROUP BY d.dept_id, d.dept_name;
```

Expected output:

| dept_name | total_projects |
| --------- | -------------- |
| IT        | 4              |
| HR        | 1              |
| Finance   | 2              |
| Marketing | 1              |

Why is Sales missing? Arjun belongs to Sales but has no project. The inner join to `projects` excludes him and therefore excludes Sales from this grouped result.

# 6. Self INNER JOIN Interview Questions

A self join joins a table to itself. We use aliases to treat the same table as two logical copies.

## Question 21. Display each employee with their manager's name.

```sql
SELECT
    e.emp_name AS employee_name,
    m.emp_name AS manager_name
FROM employees AS e
INNER JOIN employees AS m
    ON e.manager_id = m.emp_id;
```

Expected output:

| employee_name | manager_name |
| ------------- | ------------ |
| Priya         | Amit         |
| Rahul         | Amit         |
| Karan         | Rahul        |
| Sneha         | Priya        |
| Neha          | Priya        |
| Meera         | Priya        |
| Arjun         | Amit         |
| Pooja         | Sneha        |

Why is Amit missing? His `manager_id` is `NULL`, so no matching manager row exists. Vikas is also excluded because his manager ID is `101` (Amit), so actually Vikas should appear in the output as well.

Correct additional row:

| employee_name | manager_name |
| ------------- | ------------ |
| Vikas         | Amit         |

## Question 22. Find employees earning more than their managers.

```sql
SELECT
    e.emp_name AS employee_name,
    e.salary AS employee_salary,
    m.emp_name AS manager_name,
    m.salary AS manager_salary
FROM employees AS e
INNER JOIN employees AS m
    ON e.manager_id = m.emp_id
WHERE e.salary > m.salary;
```

Expected output:

| employee_name | employee_salary | manager_name | manager_salary |
| ------------- | --------------- | ------------ | -------------- |
| Sneha         | 80000           | Priya        | 75000          |
| Neha          | 90000           | Priya        | 75000          |
| Pooja         | 70000           | Sneha        | 80000          |

Correction: Pooja earns 70,000, which is less than Sneha's 80,000, so Pooja must not appear. The correct output is:

| employee_name | employee_salary | manager_name | manager_salary |
| ------------- | --------------- | ------------ | -------------- |
| Sneha         | 80000           | Priya        | 75000          |
| Neha          | 90000           | Priya        | 75000          |

Interview logic:

- `e` represents the employee.
- `m` represents the manager.
- `e.manager_id = m.emp_id` finds each employee's manager.
- `e.salary > m.salary` compares the two salaries.

# 7. Advanced INNER JOIN Questions

## Question 23. Find employees who belong to the same department as Priya.

```sql
SELECT
    e.emp_name,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE e.dept_id = (
    SELECT dept_id
    FROM employees
    WHERE emp_name = 'Priya'
);
```

Expected output:

| emp_name | dept_name |
| -------- | --------- |
| Amit     | IT        |
| Priya    | IT        |
| Neha     | IT        |

This uses a subquery to find Priya's department ID, then retrieves all employees in that department.

## Question 24. Find employees who have a project named `Cloud Migration`.

```sql
SELECT
    e.emp_name,
    p.project_name
FROM employees AS e
INNER JOIN projects AS p
    ON e.emp_id = p.emp_id
WHERE p.project_name = 'Cloud Migration';
```

Expected output:

| emp_name | project_name    |
| -------- | --------------- |
| Priya    | Cloud Migration |

## Question 25. Find employees who have both a matching department and at least one project.

```sql
SELECT DISTINCT
    e.emp_id,
    e.emp_name,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
INNER JOIN projects AS p
    ON e.emp_id = p.emp_id;
```

Expected output:

| emp_id | emp_name | dept_name |
| ------ | -------- | --------- |
| 101    | Amit     | IT        |
| 102    | Priya    | IT        |
| 103    | Rahul    | HR        |
| 104    | Sneha    | Finance   |
| 106    | Neha     | IT        |
| 108    | Meera    | Marketing |
| 110    | Pooja    | Finance   |

`DISTINCT` removes repeated employees, such as Priya, who has two projects.

## Question 26. Find departments where at least one employee has a project.

```sql
SELECT DISTINCT
    d.dept_id,
    d.dept_name
FROM departments AS d
INNER JOIN employees AS e
    ON d.dept_id = e.dept_id
INNER JOIN projects AS p
    ON e.emp_id = p.emp_id;
```

Expected output:

| dept_id | dept_name |
| ------- | --------- |
| 10      | IT        |
| 20      | HR        |
| 30      | Finance   |
| 40      | Marketing |

Sales is excluded because Arjun has no project.

## Question 27. Display each employee's name, department, and the total number of projects they have.

```sql
SELECT
    e.emp_name,
    d.dept_name,
    COUNT(p.project_id) AS total_projects
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
INNER JOIN projects AS p
    ON e.emp_id = p.emp_id
GROUP BY e.emp_id, e.emp_name, d.dept_name;
```

Expected output:

| emp_name | dept_name | total_projects |
| -------- | --------- | -------------- |
| Amit     | IT        | 1              |
| Priya    | IT        | 2              |
| Rahul    | HR        | 1              |
| Sneha    | Finance   | 1              |
| Neha     | IT        | 1              |
| Meera    | Marketing | 1              |
| Pooja    | Finance   | 1              |

Only employees with at least one project and a matching department appear.

# 8. Common INNER JOIN Interview Traps

## Question 28. What is the difference between these queries?

Query A

```sql
SELECT e.emp_name, d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id;
```

Query B

```sql
SELECT e.emp_name, d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id
WHERE d.dept_name = 'IT';
```

Answer: Query A returns all employees with matching departments. Query B returns only employees whose matching department is IT.

## Question 29. Does INNER JOIN include NULL department IDs?

```sql
SELECT e.emp_name, d.dept_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.dept_id = d.dept_id;
```

Answer: No. Vikas has `dept_id = NULL`, and `NULL = d.dept_id` does not evaluate to true. Therefore, Vikas is excluded.

## Question 30. What happens when there are multiple matches?

Suppose one employee has two projects. The employee appears twice in the join result, once for each matching project.

```sql
SELECT
    e.emp_name,
    p.project_name
FROM employees AS e
INNER JOIN projects AS p
    ON e.emp_id = p.emp_id
WHERE e.emp_name = 'Priya';
```

Expected output:

| emp_name | project_name    |
| -------- | --------------- |
| Priya    | App Development |
| Priya    | Cloud Migration |

The join returns matching row combinations, not necessarily one row per employee.

## Question 31. Can we join using different column names?

Yes. The columns do not need identical names; their values and meanings must be suitable for matching.

```sql
SELECT
    e.emp_name,
    p.project_name
FROM employees AS e
INNER JOIN projects AS p
    ON e.emp_id = p.emp_id;
```

Here, both columns happen to be named `emp_id`. But SQL also allows joining columns with different names, for example `employees.employee_code = projects.assigned_employee_code`, provided those columns exist and represent compatible values.

## Question 32. What happens if we omit the `ON` condition?

```sql
SELECT
    e.emp_name,
    d.dept_name
FROM employees AS e
INNER JOIN departments AS d;
```

In MySQL, this is not a complete valid `INNER JOIN` syntax without a join condition or an appropriate `USING` clause. To deliberately generate every possible combination, use `CROSS JOIN`:

```sql
SELECT
    e.emp_name,
    d.dept_name
FROM employees AS e
CROSS JOIN departments AS d;
```

With 10 employees and 5 departments, the cross join produces 50 combinations.

# 9. Quick Revision

| Concept         | Meaning                                               |
| --------------- | ----------------------------------------------------- |
| `INNER JOIN`    | Returns rows with matches in both tables              |
| `ON`            | Specifies the join condition                          |
| `WHERE`         | Filters rows                                          |
| `GROUP BY`      | Creates groups for aggregate calculations             |
| `HAVING`        | Filters groups                                        |
| `ORDER BY`      | Sorts the output                                      |
| `COUNT(*)`      | Counts rows in each result group                      |
| `COUNT(column)` | Counts non-NULL values in that column                 |
| `DISTINCT`      | Removes duplicate result rows                         |
| Self join       | Joins a table to itself using aliases                 |
| Multiple joins  | Connects three or more tables through join conditions |

## Practice challenge — solve without looking at the answers

Try these queries yourself using the sample tables above.

1. Display employee names with their department locations.
2. Find employees working in departments located in Mumbai or Delhi.
3. Find employees earning less than 60,000 who have a matching department.
4. Display department names and the number of employees earning more than 50,000 in each department.
5. Find employees who are assigned to at least two projects.
6. Display project names with employee names and salaries, sorted by salary descending.
7. Find employees whose salary is greater than the average salary of all employees.
8. Find pairs of employees working in the same department, without repeating reversed pairs.
9. Find employees who have a matching department but no project.
10. Find projects that do not have a matching employee.

For questions 9 and 10, remember that plain `INNER JOIN` cannot return unmatched records. Those questions are useful for understanding when an anti-join pattern or `LEFT JOIN` with a `NULL` filter is needed.
