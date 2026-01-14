## Architecture (3-tier + MVC + Singleton)

### Three-tier architecture

- **Presentation tier** (`src/hms/GUI/*`)
  - Swing GUI components: `MainFrame` + tab panels in `GUI/panel/*`
  - Uses `SwingDialogs` for user-facing errors/confirmations.

- **Business tier** (`src/hms/service/*`)
  - Use-cases and rules:
    - `PatientService` (register/update/delete)
    - `AppointmentService` (create/reschedule/cancel)
    - `PrescriptionService` (create; append to CSV)
    - `ReferralService` (create; save to CSV)

- **Data tier** (`src/hms/data/*`)
  - CSV repositories for loading and persistence:
    - `CsvPatientRepository` (rewrite)
    - `CsvAppointmentRepository` (rewrite)
    - `CsvPrescriptionRepository` (append)
    - `CsvReferralRepository` (rewrite)
    - `CsvReferenceDataRepository` (read-only clinicians/facilities/staff)
  - CSV helpers: `CsvTable`, `CsvLoader`, `CsvFileConfig`

### MVC in this codebase

- **Model**: `src/hms/model/*` (domain classes mapped to CSV headers)
- **View**: Swing components in `src/hms/GUI/*`
- **Controller**: action listeners inside each panel (e.g., button handlers) that call `service/*` and refresh tables.

### Singleton pattern (referral management)

`src/hms/service/ReferralManager` is a **Singleton** used for referral processing:

- Maintains a **referral queue**
- Generates **email-style referral text files** (no real emails)
- Writes **audit log** + **EHR updates log**
- Prevents duplicate processing based on the audit trail

Output directory (generated at runtime): `referral_out/`

