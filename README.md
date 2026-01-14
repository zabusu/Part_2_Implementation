## Healthcare Management System (Swing + MVC + 3-tier, CSV-backed)

This project implements a simple healthcare management system using:

- **Plain Java** (no external libraries)
- **Swing** GUI
- **MVC** structure
- **Three-tier architecture**
  - Presentation: Swing Views + Controllers
  - Business: Services (use-cases)
  - Data: CSV repositories

### Data files

CSV files are expected in:

`data/`

### Features implemented

- Load and display: **Patients, Clinicians, Appointments, Prescriptions, Referrals**
- Patients: **Add / Edit / Delete** (persists to `data/patients.csv`)
- Appointments: **Create / Reschedule / Cancel** (persists to `data/appointments.csv`)
- Prescriptions: **Create** (appends to `data/prescriptions.csv`)
- Referrals: **Create** (persists to `data/referrals.csv`) and **Send** via Singleton referral manager
  - Generates referral text file + audit/EHR logs in `referral_out/`

### How to run (no Maven/Gradle)

From the project root:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out hms.Main
```

