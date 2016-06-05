package com.drestive.chatalot.service.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mustafa on 03/05/2016.
 */
public class ServiceError extends Exception {
    List<String> errorCodes = new ArrayList<>();

    public void addErrorCode(String errorCode){
        errorCodes.add(errorCode);
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }
}
