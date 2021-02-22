package org.linphone.models;

public class VoiceMail_MessagesModel {
    String id; // ": 2,
    String msgnum; // ": 0,
    String callerid; // ": "+16312097572",
    String origtime; // ": 1609223904,
    String duration; // ": 14,
    String mailboxuser; // ": "+923331648001",
    String msg_id; // ": "1609223904-00000001",
    String message_recording; // ":
    // "https://2ao1n0pyyg.execute-api.ca-central-1.amazonaws.com/vm_message/2?expires=1609225739&signature=b80b7b360a2184464acf1cbaa1eaae9dc2f654b05c258aff975bb6d0fec312b9"

    public void VoiceMail_MessagesModel() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgnum() {
        return msgnum;
    }

    public void setMsgnum(String msgnum) {
        this.msgnum = msgnum;
    }

    public String getCallerid() {
        return callerid;
    }

    public void setCallerid(String callerid) {
        this.callerid = callerid;
    }

    public String getOrigtime() {
        return origtime;
    }

    public void setOrigtime(String origtime) {
        this.origtime = origtime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMailboxuser() {
        return mailboxuser;
    }

    public void setMailboxuser(String mailboxuser) {
        this.mailboxuser = mailboxuser;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMessage_recording() {
        return message_recording;
    }

    public void setMessage_recording(String message_recording) {
        this.message_recording = message_recording;
    }
}
