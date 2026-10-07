package model;

import exception.InvalidComplaintException;

public class InfrastructureComplaint extends Complaint {

    public InfrastructureComplaint(String complaintId, String studentId, String title,
                                   String description) throws InvalidComplaintException {
        super(complaintId, studentId, title, description);
    }

    @Override
    public String getCategory() {
        return "INFRASTRUCTURE";
    }
}
