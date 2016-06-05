package com.drestive.chatalot.rest.common;

import com.drestive.chatalot.service.common.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;

/**
 * Created by mustafa on 03/05/2016.
 */

@ControllerAdvice
public class ServiceErrorHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ServiceError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError processServiceError(ServiceError ex) {

        return processErrors(ex.getErrorCodes());
    }

    private RestApiError processErrors(List<String> errorCodes) {
        RestApiError restApiError = new RestApiError();
        Locale currentLocale = LocaleContextHolder.getLocale();

        for (String errorCode : errorCodes) {
            String localizedErrorMessage = messageSource
                    .getMessage(errorCode, null, String.format("Service Error: [%s]", errorCode), currentLocale);
            restApiError.addGlobalError(localizedErrorMessage);
        }

        return restApiError;
    }
}
