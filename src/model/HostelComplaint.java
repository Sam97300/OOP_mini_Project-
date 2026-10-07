package model;

import exception.InvalidComplaintException;

public class HostelComplaint extends Complaint {

    public HostelComplaint(String complaintId, String studentId, String title,
                           String description) throws InvalidComplaintException {
        super(complaintId, studentId, title, description);
    }

    @Override
    public String getCategory() {
        return "HOSTEL";
    }
}
