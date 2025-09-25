package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.TokenService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final TokenService tokenService;

    public DoctorController(DoctorService doctorService, TokenService tokenService) {
        this.doctorService = doctorService;
        this.tokenService = tokenService;
    }

    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<?> getAvailability(
            @RequestHeader(name = "Authorization", required = false) String auth,
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (auth == null || auth.isBlank()) {
            return ResponseEntity.status(401).body(Map.of("error", "Missing token"));
        }
        try {
            tokenService.parse(auth);
            List<LocalTime> slots = doctorService.getAvailableTimeSlots(doctorId, date);
            return ResponseEntity.ok(Map.of("doctorId", doctorId, "date", date.toString(), "availableSlots", slots));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAll() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> search(
            @RequestParam String specialty,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time
    ) {
        List<Doctor> result = doctorService.searchBySpecialtyAndTime(specialty, time);
        return ResponseEntity.ok(result);
    }
}