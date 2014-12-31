package com.tlunter.amtrakstatus;

/**
 * Created by toddlunter on 12/11/14.
 */
public class Train {
    public Integer number;
    public String scheduled;
    public String estimated;

    public Train(Integer number) {
        this.number = number;
    }

    public Train(Integer number, String scheduled, String estimated) {
        this.number = number;
        this.scheduled = scheduled;
        this.estimated = estimated;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public String getEstimated() {
        return estimated;
    }

    public void setEstimated(String estimated) {
        this.estimated = estimated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o instanceof Train) {
            Train other = (Train)o;
            return getNumber().equals(other.getNumber());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getNumber();
    }
}
