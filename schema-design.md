# Smart Clinic MySQL Schema Design

Database: `smart_clinic`

## Tables

1) `doctor`
- `id` BIGINT PK AUTO_INCREMENT
- `name` VARCHAR(100) NOT NULL
- `email` VARCHAR(150) NOT NULL UNIQUE
- `password` VARCHAR(255) NOT NULL
- `specialty` VARCHAR(100) NOT NULL
- `phone_number` VARCHAR(30)

2) `patient`
- `id` BIGINT PK AUTO_INCREMENT
- `name` VARCHAR(100) NOT NULL
- `email` VARCHAR(150) NOT NULL UNIQUE
- `phone_number` VARCHAR(30)
- `password` VARCHAR(255) NOT NULL

3) `appointment`
- `id` BIGINT PK AUTO_INCREMENT
- `doctor_id` BIGINT NOT NULL FK → doctor(id)
- `patient_id` BIGINT NOT NULL FK → patient(id)
- `appointment_time` DATETIME NOT NULL
- `notes` VARCHAR(500)

4) `prescription`
- `id` BIGINT PK AUTO_INCREMENT
- `doctor_id` BIGINT NOT NULL FK → doctor(id)
- `patient_id` BIGINT NOT NULL FK → patient(id)
- `appointment_id` BIGINT NOT NULL
- `description` TEXT NOT NULL

5) `doctor_available_times`
- `doctor_id` BIGINT NOT NULL FK → doctor(id)
- `available_time` TIME NOT NULL
- Composite PK (`doctor_id`, `available_time`)

## Relationships

- A `doctor` has many `appointments`; `appointment.doctor_id` → `doctor.id`
- A `patient` has many `appointments`; `appointment.patient_id` → `patient.id`
- A `doctor` writes many `prescriptions`; `prescription.doctor_id` → `doctor.id`
- A `patient` receives many `prescriptions`; `prescription.patient_id` → `patient.id`

## Indexes

- `doctor.email` UNIQUE
- `patient.email` UNIQUE
- `appointment (doctor_id, appointment_time)` INDEX
- `doctor_available_times (doctor_id, available_time)` PK

## Stored Procedures (examples)

- `GetDailyAppointmentReportByDoctor(in p_date date, in p_doctor_id bigint)`
  - Returns all rows from `appointment` for `doctor_id = p_doctor_id` and `date(appointment_time) = p_date`

- `GetDoctorWithMostPatientsByMonth(in p_year int, in p_month int)`
  - Aggregates distinct patients per doctor for appointments in the given month and returns top 1

- `GetDoctorWithMostPatientsByYear(in p_year int)`
  - Same aggregation for the full year

See `/sql/stored-procedures.sql` for reference implementation.