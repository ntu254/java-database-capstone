package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.services.TokenService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final TokenService tokenService;

    public AuthController(DoctorRepository doctorRepository, PatientRepository patientRepository, TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.tokenService = tokenService;
    }

    public record LoginRequest(@Email String email, @NotBlank String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<Doctor> d = doctorRepository.findByEmail(req.email());
        if (d.isPresent() && d.get().getPassword().equals(req.password())) {
            String token = tokenService.generateToken(req.email());
            return ResponseEntity.ok(Map.of("role", "DOCTOR", "token", token));
        }
        Optional<Patient> p = patientRepository.findByEmail(req.email());
        if (p.isPresent() && p.get().getPassword().equals(req.password())) {
            String token = tokenService.generateToken(req.email());
            return ResponseEntity.ok(Map.of("role", "PATIENT", "token", token, "patientId", p.get().getId()));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}