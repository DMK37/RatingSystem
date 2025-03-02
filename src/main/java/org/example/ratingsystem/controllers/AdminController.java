package org.example.ratingsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.services.interfaces.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/seller/{id}/approve")
    public ResponseEntity<String> approveSeller(@PathVariable String id) {
        adminService.approveSeller(id);
        return ResponseEntity.ok("Seller approved");
    }

    @PostMapping("/seller/{id}/reject")
    public ResponseEntity<String> rejectSeller(@PathVariable String id) {
        adminService.rejectSeller(id);
        return ResponseEntity.ok("Seller rejected");
    }

    @PostMapping("/comment")
    public ResponseEntity<String> approveComment() {
        return ResponseEntity.ok("Comment approved");
    }
}
