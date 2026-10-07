package model;

import exception.InvalidComplaintException;

public class TechnicalComplaint extends Complaint {

    public TechnicalComplaint(String complaintId, String studentId, String title,
                               String description) throws InvalidComplaintException {
        super(complaintId, studentId, title, description);
    }

    @Override
    public String getCategory() {
        return "TECHNICAL";
    }
}
