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

### How to run (no Maven/Gradle)

From the project root:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out hms.Main
```

