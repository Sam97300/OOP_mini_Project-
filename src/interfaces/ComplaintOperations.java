package interfaces;

import model.ComplaintStatus;

public interface ComplaintOperations {

    void updateStatus(ComplaintStatus status);

    boolean isResolved();
}
