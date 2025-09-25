package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment book(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsForDoctorOnDate(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return appointmentRepository.findByDoctorAndAppointmentTimeBetween(doctor, start, end);
    }

    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
}