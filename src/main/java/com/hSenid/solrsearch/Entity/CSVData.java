package com.hSenid.solrsearch.Entity;

public class CSVData {
    private String app_id;
    private String sms;
    private String timestamp;
    private String datetime;
    private String id;
    private String _version_;

    public CSVData(String app_id, String sms, String timestamp, String datetime, String id, String _version_) {
        this.app_id = app_id;
        this.sms = sms;
        this.timestamp = timestamp;
        this.datetime = datetime;
        this.id = id;
        this._version_ = _version_;
    }

    public CSVData() {
    }

    public String toString() {
        return "CSVData{app_id='" + this.app_id + '\'' + ", sms='" + this.sms + '\'' + ", timestamp='" + this.timestamp + '\'' + ", datetime='" + this.datetime + '\'' + ", id='" + this.id + '\'' + ", _version_='" + this._version_ + '\'' + '}';
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_version_() {
        return this._version_;
    }

    public void set_version_(String _version_) {
        this._version_ = _version_;
    }

    public String getApp_id() {
        return this.app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getSms() {
        return this.sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
