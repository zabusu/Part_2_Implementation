## Testing (manual checklist)

This project uses plain Java + Swing; testing is demonstrated via manual end-to-end checks in the GUI.

### Setup

- Ensure CSV files exist in `data/`
- Run:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out hms.Main
```

### Patients tab (CRUD)

- **Read**: table loads existing patients from `data/patients.csv`
- **Create**: click **Add** → fill form → OK
  - Verify new patient row appears and `data/patients.csv` gained a new row
- **Update**: select a patient → click **Edit** → change a field → OK
  - Verify changes persisted to `data/patients.csv`
- **Delete**: select a patient → click **Delete**
  - Verify row removed and persisted to `data/patients.csv`

### Clinicians tab (display)

- Verify clinicians load from `data/clinicians.csv`

### Appointments tab

- **Read**: appointments load from `data/appointments.csv`
- **Create**: click **Create** and use valid IDs (e.g., P001/C001/S001)
  - Verify appointment appears and persisted to `data/appointments.csv`
- **Update**: select appointment → **Reschedule** → change date/time/duration
  - Verify persisted to `data/appointments.csv`
- **Delete (cancel)**: select appointment → **Cancel**
  - Verify status becomes Cancelled and persisted

### Prescriptions tab

- **Read**: prescriptions load from `data/prescriptions.csv`
- **Create**: click **Create**
  - Verify a new row is appended to `data/prescriptions.csv`

### Referrals tab + Singleton outputs

- **Read**: referrals load from `data/referrals.csv`
- **Create**: click **Create**
  - Verify new referral is persisted to `data/referrals.csv`
- **Send Selected**:
  - Select a referral → click **Send Selected**
  - Verify `referral_out/referral_<id>.txt` is generated
  - Verify `referral_out/referral_audit.log` and `referral_out/ehr_updates.log` append new lines

