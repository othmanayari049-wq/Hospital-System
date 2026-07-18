# Architecture

## Overview

The project uses a layered, framework-light Java architecture so the Object-Oriented Programming concepts remain visible.

```text
UI layer
├── ConsoleMenu
└── Browser dashboard (HTML/CSS/JavaScript)
        ↓
Application/service layer
├── PatientService
├── AppointmentService
├── MedicalRecordService
├── AdmissionService
├── BillingService
├── InventoryService
└── DashboardService
        ↓
Repository layer
├── CrudRepository<T>
└── InMemoryRepository<T>
        ↓
Domain layer
├── Person → Patient / Staff → Doctor / Nurse
├── Appointment, MedicalRecord, Prescription
├── Room, Admission
├── Bill, BillItem, Payment
└── InventoryItem
```

## Main design choices

- **Encapsulation:** entity fields are private and changed through validated methods.
- **Inheritance:** `Patient` and `Staff` extend `Person`; `Doctor` and `Nurse` extend `Staff`.
- **Polymorphism:** repository and payable contracts work through interfaces.
- **Composition:** bills contain bill items; prescriptions contain prescription items.
- **Enums:** status and category values avoid unsafe free-text states.
- **Exceptions:** scheduling and lookup failures use meaningful exception types.
- **Separation of concerns:** UI, services, repositories, and domain logic are separated.

## Data policy

The included repository stores only generated demonstration data in memory. It has no production authentication, encryption, database migration, audit logging, or hospital integration. Never use it for real patient information.
