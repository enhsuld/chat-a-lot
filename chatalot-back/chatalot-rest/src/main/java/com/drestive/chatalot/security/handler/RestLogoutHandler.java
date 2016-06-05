package com.drestive.chatalot.security.handler;

import com.drestive.chatalot.service.identity.TokenCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mustafa on 02/06/2016.
 */
public class RestLogoutHandler implements LogoutHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected TokenCacheService tokenCacheService;

    @Override
    public void logout(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       Authentication authentication) {
        try {
            httpServletRequest.logout();
        }catch (Exception e){
            log.warn("Exception at logout: " + e.getMessage());
        }
    }
}
