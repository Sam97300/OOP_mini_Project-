package model;

import exception.InvalidComplaintException;
import interfaces.ComplaintOperations;

/**
 * Common abstract class for every type of complaint.
 */
public abstract class Complaint implements ComplaintOperations {
    private String complaintId;
    private String studentId;
    private String title;
    private String description;
    private ComplaintStatus status;

    public Complaint(String complaintId, String studentId, String title,
                     String description) throws InvalidComplaintException {
        if (isEmpty(complaintId)) {
            throw new InvalidComplaintException("Complaint ID cannot be empty.");
        }
        if (isEmpty(studentId)) {
            throw new InvalidComplaintException("Student ID cannot be empty.");
        }
        if (isEmpty(title)) {
            throw new InvalidComplaintException("Complaint title cannot be empty.");
        }
        if (isEmpty(description)) {
            throw new InvalidComplaintException("Complaint description cannot be empty.");
        }

        this.complaintId = complaintId;
        this.studentId = studentId;
        this.title = title;
        this.description = description;

        // Every newly created complaint starts with OPEN status.
        this.status = ComplaintStatus.OPEN;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String getComplaintId() {
        return complaintId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    @Override
    public void updateStatus(ComplaintStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Complaint status cannot be null.");
        }
        this.status = status;
    }

    @Override
    public boolean isResolved() {
        return status == ComplaintStatus.RESOLVED;
    }

    /**
     * Each child class supplies its own complaint category.
     */
    public abstract String getCategory();

    @Override
    public String toString() {
        return complaintId + " | " + studentId + " | " + title
                + " | " + getCategory() + " | " + status;
    }
}
