package com.drestive.chatalot.rest.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mustafa on 03/05/2016.
 */
public class RestApiError {

    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();
    private List<String> globalErrors = new ArrayList<>();

    public RestApiError() {
    }

    public void addFieldError(String path, String message) {
        FieldErrorDTO error = new FieldErrorDTO(path, message);
        fieldErrors.add(error);
    }

    public void addGlobalError(String message){
        globalErrors.add(message);
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorDTO> fieldErrorDTOs) {
        this.fieldErrors = fieldErrorDTOs;
    }


    public List<String> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(List<String> globalErrors) {
        this.globalErrors = globalErrors;
    }
}