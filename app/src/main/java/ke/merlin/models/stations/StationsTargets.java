package ke.merlin.models.stations;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by mecmurimi on 10/05/2017.
 */
public class StationsTargets implements Serializable {
    private String id;
    private String stationId;
    private double dailyTarget;
    private double weeklyTarget;
    private double monthlyTarget;
    private int monthlyCustomers;
    private byte freeze;
    private byte repeater;
    private byte to_create;
    private byte to_update;
    private String creatorId;
    private byte deleted;
    private Timestamp creationDate;
    private Timestamp updatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public double getDailyTarget() {
        return dailyTarget;
    }

    public void setDailyTarget(double dailyTarget) {
        this.dailyTarget = dailyTarget;
    }

    public double getWeeklyTarget() {
        return weeklyTarget;
    }

    public void setWeeklyTarget(double weeklyTarget) {
        this.weeklyTarget = weeklyTarget;
    }

    public double getMonthlyTarget() {
        return monthlyTarget;
    }

    public void setMonthlyTarget(double monthlyTarget) {
        this.monthlyTarget = monthlyTarget;
    }

    public int getMonthlyCustomers() {
        return monthlyCustomers;
    }

    public void setMonthlyCustomers(int monthlyCustomers) {
        this.monthlyCustomers = monthlyCustomers;
    }

    public byte getFreeze() {
        return freeze;
    }

    public void setFreeze(byte freeze) {
        this.freeze = freeze;
    }

    public byte getRepeater() {
        return repeater;
    }

    public void setRepeater(byte repeater) {
        this.repeater = repeater;
    }

    public byte getTo_create() {
        return to_create;
    }

    public void setTo_create(byte to_create) {
        this.to_create = to_create;
    }

    public byte getTo_update() {
        return to_update;
    }

    public void setTo_update(byte to_update) {
        this.to_update = to_update;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public byte getDeleted() {
        return deleted;
    }

    public void setDeleted(byte deleted) {
        this.deleted = deleted;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
