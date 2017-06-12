package pranav.apps.amazing.meterreader.pojo;

import java.util.Date;

/**
 * Created by Pranav Gupta on 6/11/2017.
 */

public class Reading {

    private String  flat_id;
    private String newReading;
    private String takenOn;
    private String remarks;
    private String takenBy;
    private String status;

    public Reading(String flat_id, String newReading, String takenOn, String remarks, String takenBy,String status) {
        this.flat_id = flat_id;
        this.newReading = newReading;
        this.takenOn = takenOn;
        this.remarks = remarks;
        this.takenBy = takenBy;
        this.status = status;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getNewReading() {
        return newReading;
    }

    public void setNewReading(String newReading) {
        this.newReading = newReading;
    }

    public String getTakenOn() {
        return takenOn;
    }

    public void setTakenOn(String takenOn) {
        this.takenOn = takenOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }
}
