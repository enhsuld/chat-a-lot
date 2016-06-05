package com.drestive.chatalot.service.common.dto;

import com.drestive.chatalot.domain.identity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * Created by mustafa on 30/05/2016.
 */
@Component
public class UserMapper extends CustomMapper<User, UserDTO> {

    @Override
    public void mapAtoB(User user, UserDTO userDTO, MappingContext context) {
        super.mapAtoB(user,userDTO,context);
        userDTO.setName(user.getUserProfile().getName());
    }
}
