package com.drestive.chatalot.service.profile.dto;

import com.drestive.chatalot.domain.profile.UserProfile;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * Created by mustafa on 24/05/2016.
 */

@Component
public class UserProfileMapper extends CustomMapper<UserProfile, UserProfileDTO> {
    @Override
    public void mapAtoB(UserProfile userProfile, UserProfileDTO userProfileDTO, MappingContext context) {
        super.mapAtoB(userProfile, userProfileDTO, context);
        userProfileDTO.setUsername(userProfile.getUser().getUsername());
        userProfileDTO.setEmailAddress(userProfile.getUser().getEmailAddress());
        userProfileDTO.setOnline(userProfile.getChatStatus().getOnline());
    }
}

