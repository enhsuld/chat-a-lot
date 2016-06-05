package com.drestive.chatalot.service.profile.dto;

import com.drestive.chatalot.domain.profile.Invite;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * Created by mustafa on 24/05/2016.
 */

@Component
public class InviteMapper extends CustomMapper<Invite, InviteDTO> {
    @Override
    public void mapAtoB(Invite invite, InviteDTO inviteDTO, MappingContext context) {
        super.mapAtoB(invite, inviteDTO, context);
        inviteDTO.setFromUser(invite.getFrom().getName());
        inviteDTO.setToUser(invite.getTo().getName());
    }
}

