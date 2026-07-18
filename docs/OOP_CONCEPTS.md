# Object-Oriented Programming Concepts Demonstrated

| Concept | Example |
|---|---|
| Class and object | `Patient`, `Doctor`, `Appointment`, `Bill` |
| Encapsulation | private state with validated methods such as `Bill.applyPayment` |
| Inheritance | `Person` → `Staff` → `Doctor` and `Nurse` |
| Abstraction | abstract `Person` and `Staff` classes |
| Interface | `Identifiable`, `Payable`, `CrudRepository<T>` |
| Polymorphism | generic repository accepts different identifiable entities |
| Composition | `Prescription` owns `PrescriptionItem`; `Bill` owns `BillItem` |
| Aggregation | `HospitalSystem` coordinates repositories and services |
| Enum | appointment, payment, room, staff, and admission states |
| Exception handling | `EntityNotFoundException`, `SchedulingConflictException` |
| Generic programming | `InMemoryRepository<T extends Identifiable>` |
| Method overriding | `Patient.toString`, `Doctor.toString` |
| Immutable value object | Java records `BillItem` and `PrescriptionItem` |

## Course-project learning goals

1. Model a realistic domain with clear classes and responsibilities.
2. Apply SOLID-inspired separation without hiding OOP behind a large framework.
3. Validate state changes through methods instead of exposing fields.
4. Use reusable interfaces and generics.
5. Test important business rules such as schedule conflicts and payment state transitions.
