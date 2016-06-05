package com.drestive.chatalot.service.identity;

import com.drestive.chatalot.domain.identity.User;
import com.drestive.chatalot.domain.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mustafa on 22/02/2016.
 */
@Service
public class IdentityService {

    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true)
    public User findUserByUsername(String username){
        return userRepository.getByUsername(username);
    }

}
