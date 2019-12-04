package com.tetraval.mochashiadmin.chashimodule.model;

public class InactiveChashiModel {

    String p_uid;
    String p_fname;
    String p_lname;
    String p_imagel;
    String p_address;
    String p_lat;
    String p_long;
    String p_email;
    String p_status;

    public InactiveChashiModel() {
    }

    public InactiveChashiModel(String p_uid, String p_fname, String p_lname, String p_imagel, String p_address, String p_lat, String p_long, String p_email, String p_status) {
        this.p_uid = p_uid;
        this.p_fname = p_fname;
        this.p_lname = p_lname;
        this.p_imagel = p_imagel;
        this.p_address = p_address;
        this.p_lat = p_lat;
        this.p_long = p_long;
        this.p_email = p_email;
        this.p_status = p_status;
    }

    public String getP_uid() {
        return p_uid;
    }

    public void setP_uid(String p_uid) {
        this.p_uid = p_uid;
    }

    public String getP_fname() {
        return p_fname;
    }

    public void setP_fname(String p_fname) {
        this.p_fname = p_fname;
    }

    public String getP_lname() {
        return p_lname;
    }

    public void setP_lname(String p_lname) {
        this.p_lname = p_lname;
    }

    public String getP_imagel() {
        return p_imagel;
    }

    public void setP_imagel(String p_imagel) {
        this.p_imagel = p_imagel;
    }

    public String getP_address() {
        return p_address;
    }

    public void setP_address(String p_address) {
        this.p_address = p_address;
    }

    public String getP_lat() {
        return p_lat;
    }

    public void setP_lat(String p_lat) {
        this.p_lat = p_lat;
    }

    public String getP_long() {
        return p_long;
    }

    public void setP_long(String p_long) {
        this.p_long = p_long;
    }

    public String getP_email() {
        return p_email;
    }

    public void setP_email(String p_email) {
        this.p_email = p_email;
    }

    public String getP_status() {
        return p_status;
    }

    public void setP_status(String p_status) {
        this.p_status = p_status;
    }
}
