package org.linphone.models;

public class MyNumbersModel {
    String id;
    String friendlyName;
    String phoneNumber;
    String sid;
    String status;
    String endpoint_id;

    String forward_enabled;
    String forward_typ;
    String external_number;

    public void MyNumbersModel() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndpoint_id() {
        return endpoint_id;
    }

    public void setEndpoint_id(String endpoint_id) {
        this.endpoint_id = endpoint_id;
    }

    public String getForward_enabled() {
        return forward_enabled;
    }

    public void setForward_enabled(String forward_enabled) {
        this.forward_enabled = forward_enabled;
    }

    public String getForward_typ() {
        return forward_typ;
    }

    public void setForward_typ(String forward_typ) {
        this.forward_typ = forward_typ;
    }

    public String getExternal_number() {
        return external_number;
    }

    public void setExternal_number(String external_number) {
        this.external_number = external_number;
    }
}
