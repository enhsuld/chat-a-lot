package com.drestive.chatalot.rest.register;

import com.drestive.chatalot.rest.common.AbstractController;
import com.drestive.chatalot.service.common.ServiceError;
import com.drestive.chatalot.service.register.RegistrationService;
import com.drestive.chatalot.service.register.dto.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by mustafa on 02/05/2016.
 */

/**
 * Controller that handles user registration. Forwards requests to the corresponding service.
 */
@RestController
@RequestMapping("/register")
public class RegistrationController extends AbstractController {

    @Autowired
    private RegistrationService registrationService;

    /**
     * Receive a registration request.
     *
     * @param registration
     * @throws ServiceError
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void register(@Valid @RequestBody Registration registration) throws ServiceError {
        registrationService.doRegistration(registration);
    }
}
