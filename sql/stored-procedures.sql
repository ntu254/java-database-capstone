-- Stored procedures for Smart Clinic
DELIMITER $$

DROP PROCEDURE IF EXISTS GetDailyAppointmentReportByDoctor $$
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(IN p_date DATE, IN p_doctor_id BIGINT)
BEGIN
  SELECT a.*
  FROM appointment a
  WHERE a.doctor_id = p_doctor_id
    AND DATE(a.appointment_time) = p_date
  ORDER BY a.appointment_time;
END $$

DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByMonth $$
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(IN p_year INT, IN p_month INT)
BEGIN
  SELECT a.doctor_id, COUNT(DISTINCT a.patient_id) AS patient_count
  FROM appointment a
  WHERE YEAR(a.appointment_time) = p_year
    AND MONTH(a.appointment_time) = p_month
  GROUP BY a.doctor_id
  ORDER BY patient_count DESC
  LIMIT 1;
END $$

DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByYear $$
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(IN p_year INT)
BEGIN
  SELECT a.doctor_id, COUNT(DISTINCT a.patient_id) AS patient_count
  FROM appointment a
  WHERE YEAR(a.appointment_time) = p_year
  GROUP BY a.doctor_id
  ORDER BY patient_count DESC
  LIMIT 1;
END $$

DELIMITER ;