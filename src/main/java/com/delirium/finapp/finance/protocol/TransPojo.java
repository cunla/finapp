package com.delirium.finapp.finance.protocol;

import java.util.Date;

/**
 * Created by morand3 on 12/27/2015.
 */
public class TransPojo {
    private Long groupId;
    private Double amount;
    private Date date;
    private Location location;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public class Location {
        public Double latitude;
        public Double longitude;
    }
}
