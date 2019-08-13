package com.hSenid.solrsearch.Entity;

public class SolrEntity {
    private String app_id;
    private String sms;
    private String timestamp;
    private String datetime;
    private int termCount;
    private String _version_;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getTermCount() {
        return termCount;
    }

    public void setTermCount(int termCount) {
        this.termCount = termCount;
    }

    public String get_version_() {
        return _version_;
    }

    public void set_version_(String _version_) {
        this._version_ = _version_;
    }
}
