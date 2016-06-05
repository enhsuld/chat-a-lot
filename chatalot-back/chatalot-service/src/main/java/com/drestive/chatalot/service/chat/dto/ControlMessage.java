package com.drestive.chatalot.service.chat.dto;

/**
 * Created by mustafa on 26/05/2016.
 */
public class ControlMessage extends Message {

    public static final String REFRESH_FRIENDS = "refresh-friends";

    public ControlMessage(String command) {
        super(-1, CONTROL_MESSAGE, command);
    }

    public static final ControlMessage REFRESH_FRIENDS_COMMAND = new ControlMessage(REFRESH_FRIENDS);
}
