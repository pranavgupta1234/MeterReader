package pranav.apps.amazing.meterreader.pojo;

/**
 * Created by Pranav Gupta on 6/11/2017.
 */

public class Details {

    private String flat_id;
    private String name;
    private String meter_no;
    private String old_reading;
    private String new_reading;
    private String unit;
    private String current_charges;
    private String fixed_charges;
    private String address;
    private String building_no;
    private String flat_no;
    private String remarks;

    public Details(String flat_id, String name, String meter_no,
                   String old_reading, String new_reading, String unit, String current_charges, String fixed_charges,
                   String address, String building_no, String flat_no, String remarks) {
        this.flat_id = flat_id;
        this.name = name;
        this.meter_no = meter_no;
        this.old_reading = old_reading;
        this.new_reading = new_reading;
        this.unit = unit;
        this.current_charges = current_charges;
        this.fixed_charges = fixed_charges;
        this.address = address;
        this.building_no = building_no;
        this.flat_no = flat_no;
        this.remarks = remarks;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
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

    public String getOld_reading() {
        return old_reading;
    }

    public void setOld_reading(String old_reading) {
        this.old_reading = old_reading;
    }

    public String getNew_reading() {
        return new_reading;
    }

    public void setNew_reading(String new_reading) {
        this.new_reading = new_reading;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCurrent_charges() {
        return current_charges;
    }

    public void setCurrent_charges(String current_charges) {
        this.current_charges = current_charges;
    }

    public String getFixed_charges() {
        return fixed_charges;
    }

    public void setFixed_charges(String fixed_charges) {
        this.fixed_charges = fixed_charges;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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