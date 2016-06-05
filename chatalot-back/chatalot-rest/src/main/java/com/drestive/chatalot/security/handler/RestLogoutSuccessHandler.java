package com.drestive.chatalot.security.handler;

import com.drestive.chatalot.service.identity.Constants;
import com.drestive.chatalot.service.identity.TokenCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by mustafa on 02/06/2016.
 */
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    protected TokenCacheService tokenCacheService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        Map<String, String> authenticationDetails = (Map<String, String>) authentication.getDetails();
        String token = authenticationDetails.get(Constants.HEADER_X_AUTH_TOKEN);
        tokenCacheService.invalidateUserToken(authentication.getName(), token);
    }
}
