package ke.merlin.models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by mecmurimi on 16/07/2017.
 */

public class RegistrationsApi {
    private String id;
    private String username;
    private String password;
    private String email;
    private String verificationCode;
    private String accessorName;
    private String station;
    private byte deleted;
    private byte isApproved;
    private Timestamp creationDate;
    private Timestamp updatedDate;

    public RegistrationsApi(){

    }

    public RegistrationsApi(String regName, String regEmail, String regUsername, String regStation, String regPassword) {
        this.id = UUID.randomUUID().toString();
        this.username = regUsername;
        this.email = regEmail;
        this.verificationCode = "4G-" + new Date().getTime();
        this.accessorName = regName;
        this.station = regStation;
        this.creationDate = new Timestamp(new Date().getTime());
        this.isApproved = 1;
        this.deleted = 0;
        this.updatedDate = new Timestamp(new Date().getTime());
        this.password = regPassword;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getAccessorName() {
        return accessorName;
    }

    public void setAccessorName(String accessorName) {
        this.accessorName = accessorName;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public byte getDeleted() {
        return deleted;
    }

    public void setDeleted(byte deleted) {
        this.deleted = deleted;
    }

    public byte getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(byte isApproved) {
        this.isApproved = isApproved;
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
