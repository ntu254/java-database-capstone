package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepository;
    private final TokenService tokenService;

    public PrescriptionController(PrescriptionRepository prescriptionRepository, TokenService tokenService) {
        this.prescriptionRepository = prescriptionRepository;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader(name = "Authorization", required = false) String auth,
            @Valid @RequestBody Prescription prescription
    ) {
        if (auth == null || auth.isBlank()) {
            return ResponseEntity.status(401).body(Map.of("error", "Missing token"));
        }
        try {
            tokenService.parse(auth);
            Prescription saved = prescriptionRepository.save(prescription);
            return ResponseEntity.ok(Map.of("message", "Prescription saved", "id", saved.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}