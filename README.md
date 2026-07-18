# Hospital Management System

[![Java CI](https://github.com/othmanayari049-wq/Hospital-System/actions/workflows/java-ci.yml/badge.svg)](https://github.com/othmanayari049-wq/Hospital-System/actions/workflows/java-ci.yml)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven&logoColor=white)
![Course](https://img.shields.io/badge/Course-Object--Oriented%20Programming-2563EB)

A complete educational hospital-management application built for a Qatar University **Object-Oriented Programming (OOP) course project**.

The system demonstrates Java class design, encapsulation, inheritance, abstraction, interfaces, polymorphism, composition, generics, collections, exceptions, service layers, automated tests, a console interface, and a small JavaScript dashboard.

> This is an independent OOP course project. It is not part of QadamCare AI and does not use QadamCare models, datasets, or medical-image analysis.

> **Educational use only:** the repository uses generated demonstration data and must not be used to store real patient information or support real clinical decisions.

## Features

- patient registration, search, allergy, blood-type, insurance, and emergency-contact records
- doctors, nurses, departments, specializations, shifts, and consultation fees
- appointment scheduling with doctor and patient time-conflict detection
- educational medical records, observations, prescriptions, and prescription items
- hospital rooms, room categories, availability, admissions, and discharge lifecycle
- itemized billing, outstanding balance calculation, partial payments, and full payments
- inventory stock, stock consumption, restocking, and low-stock detection
- seeded fictional data for immediate demonstration
- interactive command-line menu
- built-in Java HTTP API using `HttpServer`
- responsive HTML, CSS, and vanilla JavaScript dashboard
- JUnit tests for scheduling and billing rules
- GitHub Actions Java CI workflow

## OOP concepts demonstrated

| OOP concept | Implementation example |
|---|---|
| Encapsulation | private fields and validated methods such as `Bill.applyPayment()` |
| Inheritance | `Person` → `Staff` → `Doctor` / `Nurse` |
| Abstraction | abstract `Person` and `Staff` base classes |
| Interfaces | `Identifiable`, `Payable`, and `CrudRepository<T>` |
| Polymorphism | generic repository behavior across domain entities |
| Composition | `Bill` contains `BillItem`; `Prescription` contains `PrescriptionItem` |
| Aggregation | `HospitalSystem` coordinates repositories and services |
| Generics | `InMemoryRepository<T extends Identifiable>` |
| Exception handling | entity-not-found and scheduling-conflict exceptions |
| Immutable value objects | Java records for bill and prescription items |

More details are available in [`docs/OOP_CONCEPTS.md`](docs/OOP_CONCEPTS.md).

## Architecture

```text
Console UI / JavaScript Dashboard
               ↓
        Application Services
               ↓
      Generic Repository Layer
               ↓
         Java Domain Model
```

The project intentionally avoids a large application framework so the OOP design remains easy to study. See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).

## Technology

- Java 17
- Maven
- JUnit 5
- Java built-in `HttpServer`
- HTML5
- CSS3
- Vanilla JavaScript

No runtime database or external Java web framework is required.

## Project structure

```text
Hospital-System/
├── .github/workflows/java-ci.yml
├── docs/
│   ├── ARCHITECTURE.md
│   ├── COURSE_REPORT.md
│   └── OOP_CONCEPTS.md
├── src/
│   ├── main/
│   │   ├── java/com/qataruniversity/hms/
│   │   │   ├── cli/
│   │   │   ├── config/
│   │   │   ├── domain/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── util/
│   │   │   ├── web/
│   │   │   └── Application.java
│   │   └── resources/web/
│   └── test/java/com/qataruniversity/hms/
├── pom.xml
├── README.md
└── LICENSE
```

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer

Check your installation:

```bash
java -version
mvn -version
```

## Run the project

Clone the repository and switch to the feature branch while it is under review:

```bash
git clone https://github.com/othmanayari049-wq/Hospital-System.git
cd Hospital-System
git switch feature/full-oop-hospital-system
```

### 1. Run the summary demo

```bash
mvn clean compile exec:java -Dexec.args="demo"
```

### 2. Run the interactive console

```bash
mvn clean compile exec:java -Dexec.args="cli"
```

The console supports dashboard viewing, patient registration, appointment scheduling, room viewing, billing, and inventory viewing.

### 3. Run the web dashboard

```bash
mvn clean compile exec:java -Dexec.args="server"
```

Open:

```text
http://localhost:8080
```

Use another port when needed:

```bash
HMS_PORT=9090 mvn clean compile exec:java -Dexec.args="server"
```

On PowerShell:

```powershell
$env:HMS_PORT="9090"
mvn clean compile exec:java -Dexec.args="server"
```

## HTTP endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/dashboard` | summary counts and outstanding balance |
| GET | `/api/patients` | patient list |
| GET | `/api/doctors` | doctor list |
| GET | `/api/appointments` | appointment list |
| GET | `/api/rooms` | room and availability list |
| GET | `/api/bills` | billing summary |
| GET | `/api/inventory` | inventory and low-stock status |

The API is read-only in this course version. Write operations are demonstrated through Java services and the console interface.

## Tests

Run:

```bash
mvn test
```

Current automated checks cover:

- rejection of overlapping doctor appointments
- partial-payment status
- full-payment status
- outstanding-balance transitions

## Example demo data

The system starts with fictional examples:

- three patients
- two doctors
- one nurse
- three departments
- four rooms
- two appointments
- one medical record and prescription
- one partially paid bill
- three inventory items, including a low-stock item

All names, identifiers, contact details, records, and transactions are generated demonstration data.

## Documentation

- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — layered design and responsibilities
- [`docs/OOP_CONCEPTS.md`](docs/OOP_CONCEPTS.md) — course concepts mapped to code
- [`docs/COURSE_REPORT.md`](docs/COURSE_REPORT.md) — assignment-style project summary
- [`CONTRIBUTING.md`](CONTRIBUTING.md) — contribution rules

## Limitations

This project does not provide:

- authentication or role-based access control
- encrypted storage
- a production database
- audit logging
- backups or disaster recovery
- interoperability with hospital, pharmacy, laboratory, or insurance systems
- clinical decision support
- regulatory or privacy compliance

These are intentionally outside the scope of the OOP course demonstration.

## Academic integrity

The repository is intended to demonstrate and document an educational software project. Students should follow their course rules when reusing code, diagrams, reports, or design ideas.

## Author

**Mohamed Othman Ayari**  
Computer Engineering, Qatar University

## License

The project source code is available under the [MIT License](LICENSE).
