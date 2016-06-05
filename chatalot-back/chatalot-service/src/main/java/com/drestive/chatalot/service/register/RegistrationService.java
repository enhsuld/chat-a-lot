package com.drestive.chatalot.service.register;

import com.drestive.chatalot.domain.identity.Role;
import com.drestive.chatalot.domain.identity.User;
import com.drestive.chatalot.domain.identity.repository.RoleRepository;
import com.drestive.chatalot.domain.identity.repository.UserRepository;
import com.drestive.chatalot.domain.profile.UserProfile;
import com.drestive.chatalot.domain.profile.repository.UserProfileRepository;
import com.drestive.chatalot.service.common.EmailService;
import com.drestive.chatalot.service.common.ServiceError;
import com.drestive.chatalot.service.register.dto.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

/**
 * Created by mustafa on 02/05/2016.
 */
@Service
public class RegistrationService {

    public static final String ERROR_CODE_PREFIX = "registration.error";
    public static final String EMAIL_ALREADY_IN_USE = "email_already_in_use";
    public static final String USERNAME_ALREADY_IN_USE = "username_already_in_use";
    public static final String FAILED_TO_SEND_ACTIVATION_EMAIL = "failed_to_send_activation_email";

    @Value("${chatalot.register.send_notification}")
    private Boolean sendNotification;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    public void doRegistration(Registration registration) throws ServiceError {

        ServiceError error = new ServiceError();

        User checkUser = userRepository.getByUsername(registration.getUsername());
        if (checkUser != null) {
            error.addErrorCode(String.format("%s.%s", ERROR_CODE_PREFIX, USERNAME_ALREADY_IN_USE));
        }

        checkUser = userRepository.getByEmailAddress(registration.getEmailAddress());
        if (checkUser != null) {
            error.addErrorCode(String.format("%s.%s", ERROR_CODE_PREFIX, EMAIL_ALREADY_IN_USE));
        }

        if (error.getErrorCodes().size() > 0) {
            throw error;
        }

        User newUser = new User();
        newUser.setLocked(false);
        newUser.setEnabled(true); //TODO: complete activation logic
        newUser.setExpired(false);
        newUser.setUsername(registration.getUsername());
        newUser.setPassword(registration.getPassword());
        newUser.setEmailAddress(registration.getEmailAddress());

        Role userRole = roleRepository.getById("ROLE_USER");
        newUser.getRoles().add(userRole);
        User user = userRepository.save(newUser);
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setName(registration.getName());
        userProfileRepository.save(userProfile);

        try {
            sendActivationEmail(newUser);
        } catch (Exception exception) {
            exception.printStackTrace();
            ServiceError activationEmailFailed = new ServiceError();
            activationEmailFailed
                    .addErrorCode(String.format("%s.%s", ERROR_CODE_PREFIX, FAILED_TO_SEND_ACTIVATION_EMAIL));
            throw activationEmailFailed;
        }
    }

    private void sendActivationEmail(User user) throws MessagingException {
        if (sendNotification) {
            emailService.send(user, "Your Proxillet Account Activation.", "Activation Link and Code.");
        }
    }
}
