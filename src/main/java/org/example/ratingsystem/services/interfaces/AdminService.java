package org.example.ratingsystem.services.interfaces;

public interface AdminService {
    void approveSeller(String id);
    void rejectSeller(String id);
    void approveComment(String id);
    void rejectComment(String id);
}
