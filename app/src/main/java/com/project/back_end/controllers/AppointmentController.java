package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;

    public AppointmentController(AppointmentService appointmentService, TokenService tokenService, PatientRepository patientRepository) {
        this.appointmentService = appointmentService;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
    }

    @PostMapping
    public ResponseEntity<?> book(@Valid @RequestBody Appointment appointment) {
        Appointment saved = appointmentService.book(appointment);
        return ResponseEntity.ok(Map.of("message", "Appointment booked", "id", saved.getId()));
    }

    @GetMapping("/my")
    public ResponseEntity<?> myAppointments(@RequestHeader("Authorization") String auth) {
        try {
            Claims claims = tokenService.parse(auth);
            String email = claims.get("email", String.class);
            Patient patient = patientRepository.findByEmail(email).orElseThrow();
            List<Appointment> result = appointmentService.getAppointmentsForPatient(patient.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
    }

    @GetMapping("/doctor/{doctorId}/date")
    public ResponseEntity<?> byDoctorAndDate(
            @PathVariable Long doctorId,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsForDoctorOnDate(doctorId, date));
    }
}