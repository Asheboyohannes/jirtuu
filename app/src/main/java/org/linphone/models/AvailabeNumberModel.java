package org.linphone.models;

public class AvailabeNumberModel {

    String phoneNumber; // : "+13135135553"
    String friendlyName; // : "(313) 513-5553"
    String isoCountry; // : "US"

    String voice; // : true
    String SMS; // : true
    String MMS; // : true

    String latitude; // : null
    String longitude; // : null
    String locality; // : null
    String postalCode; // : null
    String region; // : "MI"

    public void AvailabeNumberModel() {}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getIsoCountry() {
        return isoCountry;
    }

    public void setIsoCountry(String isoCountry) {
        this.isoCountry = isoCountry;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getSMS() {
        return SMS;
    }

    public void setSMS(String SMS) {
        this.SMS = SMS;
    }

    public String getMMS() {
        return MMS;
    }

    public void setMMS(String MMS) {
        this.MMS = MMS;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
