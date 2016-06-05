package com.drestive.chatalot.security.filter;

import com.drestive.chatalot.rest.utils.URLParameterDecoder;
import com.drestive.chatalot.security.model.RestAuthenticationRequestPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Authentication filter for pre-authenticated requests.
 */
public class TokenPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public TokenPreAuthenticatedProcessingFilter() {
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        log.trace("[AUTHENTICATION] [AUTH_FILTER] [TOKEN_PRE_AUTHENTICATED_PROCESSING_FILTER] [PROCESSING]");
        String token = getTokenFromRequest(request);
        RestAuthenticationRequestPrincipal principal = new RestAuthenticationRequestPrincipal();
        principal.setxAuthToken(token);
        return principal;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return getTokenFromRequest(request);
    }

    protected String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("X-Auth-token");

        if (!StringUtils.hasText(token)) {
            //try to get the token from the url (for websocket requests)
            Map<String, List<String>> params;

            try {
                params = URLParameterDecoder
                        .splitQuery(new URL(request.getRequestURL().toString() + "?" + request.getQueryString()));
            } catch (Exception e) {
                log.warn(e.getMessage());
                return null;
            }

            if (params.get("auth") == null || params.get("auth").size() <= 0) {
                return null;
            }

            return params.get("auth").get(0);
        }

        return token;
    }
}
