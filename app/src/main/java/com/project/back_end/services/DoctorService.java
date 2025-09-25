package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository, TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    public Optional<Doctor> validateLogin(String email, String password) {
        return doctorRepository.findByEmail(email)
                .filter(d -> Objects.equals(d.getPassword(), password));
    }

    public List<LocalTime> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        // For demo: return stored availableTimes (no booking conflict logic here)
        return doctor.getAvailableTimes() == null ? List.of() : doctor.getAvailableTimes();
    }

    public List<Doctor> searchBySpecialtyAndTime(String specialty, LocalTime time) {
        // naive: filter in-memory; in real app add query
        List<Doctor> all = doctorRepository.findAll();
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : all) {
            if (d.getSpecialty() != null && d.getSpecialty().equalsIgnoreCase(specialty)) {
                if (time == null || d.getAvailableTimes() == null || d.getAvailableTimes().contains(time)) {
                    result.add(d);
                }
            }
        }
        return result;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}