package org.example.ratingsystem.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.enums.ApprovalStatus;
import org.example.ratingsystem.exceptions.NotPendingStatus;
import org.example.ratingsystem.repositories.UserRepository;
import org.example.ratingsystem.repositories.UserStatusRepository;
import org.example.ratingsystem.services.interfaces.AdminService;
import org.example.ratingsystem.services.interfaces.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public void approveSeller(String id) {
        var status = userStatusRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("User status not found"));
        if (status.getStatus() != ApprovalStatus.EMAIL_VERIFIED) {
            throw new NotPendingStatus("User status is not pending");
        }
        status.setStatus(ApprovalStatus.APPROVED);
        userStatusRepository.save(status);

        var user = userRepository.findById(status.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        emailService.sendApprovalEmail(user.getEmail(), user.getFirstName());
    }

    @Override
    @Transactional
    public void rejectSeller(String id) {
        var status = userStatusRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("User status not found"));
        if (status.getStatus() != ApprovalStatus.EMAIL_VERIFIED) {
            throw new NotPendingStatus("User status is not pending");
        }

        status.setStatus(ApprovalStatus.REJECTED);
        userStatusRepository.save(status);

        var user = userRepository.findById(status.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        emailService.sendRejectionEmail(user.getEmail(), user.getFirstName());
    }

    @Override
    public void approveComment(String id) {

    }

    @Override
    public void rejectComment(String id) {

    }
}
