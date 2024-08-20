# JDBC Utility Functions for Boutique Management System

This repository contains a set of Java-based JDBC utility functions designed to interface with an SQLite database. These functions were developed as part of a boutique management system, and they provide comprehensive capabilities for managing database tables and data.

## Features

The JDBC utility functions included in this project allow you to perform the following operations:

### Table Management
- **Create Table:** Create new tables based on predefined schemas.
- **Delete Table:** Remove tables from the database.
- **Rename Table:** Change the name of existing tables.

### Column Management
- **Select Columns:** Retrieve specific columns from a table.
- **Alter Columns:** 
  - **Rename Column:** Change the name of existing columns.
  - **Add Column:** Add new columns to a table.
  - **Drop Column:** Remove columns from a table.

### Data Operations
- **Insert Data:** Insert new records into a table.
- **Update Data:** Modify existing records based on specific conditions.
- **Delete Data:** Remove records from a table based on conditions.

## Database Schema Overview

Although the Android project is not included, the JDBC functions are designed to work with the following schema:

- **Customer Table (`customer`):** Stores customer details like `cust_id`, `name`, `phone`, and `age`.
- **Cloths Table (`cloths`):** Manages cloth types with fields like `cloth_id` and `name`.
- **Cloth Name Table (`cloth_name`):** Links customers with their cloth measurements.
- **Work Table (`work`):** Tracks work orders, including fashion type, payment, and completion status.

![Database Schema](schema%20graph.png)

## Getting Started

### Prerequisites

- **Java Development Kit (JDK):** Ensure you have JDK installed.
- **SQLite:** This project uses SQLite as the database engine.

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/DevadattaP/jdbc.git
    ```
2. Navigate to the project directory:
    ```sh
    cd jdbc
    ```
3. Compile the java code:
    ```sh
    javac SchemaDB.java
    ```
4. Run the code:
    ```sh
    java SchemaDB
    ```
> [!NOTE]
> The code given is an example of how CRUD utility functions can be used in a `main` method. You can make the code work according to your schema and requirements.

## Contributing
> Feel free to fork the repository and submit pull requests for any enhancements or bug fixes.
