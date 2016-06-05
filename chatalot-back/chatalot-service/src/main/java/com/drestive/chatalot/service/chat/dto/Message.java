package com.drestive.chatalot.service.chat.dto;

/**
 * Created by mustafa on 26/05/2016.
 */
public class Message {

    public static final String CHAT_MESSAGE = "chat-message";
    public static final String CONTROL_MESSAGE = "control-message";

    private int id;
    private String type;
    private String message;

    public Message() {
    }

    public Message(int id, String type, String message) {
        this.id = id;
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
