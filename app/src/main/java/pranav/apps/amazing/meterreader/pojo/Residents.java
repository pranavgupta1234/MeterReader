package pranav.apps.amazing.meterreader.pojo;

/**
 * Created by Pranav Gupta on 7/4/2017.
 */

public class Residents {
    private String name;
    private String meter_no;
    private String reading;
    private String campus;
    private String building_no;
    private String flat_no;
    private String remarks;

    public Residents(String name, String meter_no,
                   String reading, String campus,
                   String building_no, String flat_no, String remarks) {

        this.name = name;
        this.meter_no = meter_no;
        this.campus = campus;
        this.reading = reading;
        this.building_no = building_no;
        this.flat_no = flat_no;
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeter_no() {
        return meter_no;
    }

    public void setMeter_no(String meter_no) {
        this.meter_no = meter_no;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getBuilding_no() {
        return building_no;
    }

    public void setBuilding_no(String building_no) {
        this.building_no = building_no;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
