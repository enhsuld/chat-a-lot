package com.drestive.chatalot.service.chat.dto;

import java.util.Date;

/**
 * Created by mustafa on 26/05/2016.
 */
public class ChatMessage extends Message {

    private Date date;
    private String to;
    private String from;

    public ChatMessage() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
