package com.drestive.chatalot.security;

import com.drestive.chatalot.security.filter.ApiAuthenticationFilter;
import com.drestive.chatalot.security.filter.SimpleCORSFilter;
import com.drestive.chatalot.security.filter.TokenPreAuthenticatedProcessingFilter;
import com.drestive.chatalot.security.handler.*;
import com.drestive.chatalot.security.provider.RestAuthenticationProvider;
import com.drestive.chatalot.security.service.ApiPreAuthUserDetailsService;
import com.drestive.chatalot.security.service.ApiUserDetailsService;
import com.drestive.chatalot.security.service.AuthenticationProviderService;
import com.drestive.chatalot.service.identity.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ApiUserDetailsService userDetailsService;

    @Autowired
    ApiPreAuthUserDetailsService preAuthUserDetailsService;

    @Autowired
    AuthenticationProviderService authenticationProviderService;

    @Autowired
    IdentityService identityService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").csrf().disable()
                // http://spring.io/blog/2013/08/21/spring-security-3-2-0-rc1-highlights-csrf-protection/
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/token").anonymous()
                .antMatchers(HttpMethod.POST, "/register").anonymous()
                .antMatchers("/admin/**").hasAnyRole("ADMIN").antMatchers("/**")
                .hasAnyRole("USER", "ADMIN").and()
                .addFilterAfter(apiKeyAuthFilter(), TokenPreAuthenticatedProcessingFilter.class)
                .addFilterBefore(preAuthFilter(), LogoutFilter.class).exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(logoutSuccessHandler());
    }

    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    public Filter apiKeyAuthFilter() throws Exception {
        RequestMatcher antReqMatch = new AntPathRequestMatcher("/auth/token");

        List<RequestMatcher> reqMatches = new ArrayList<>();
        reqMatches.add(antReqMatch);
        RequestMatcher reqMatch = new AndRequestMatcher(reqMatches);
        ApiAuthenticationFilter filter = new ApiAuthenticationFilter();
        filter.setRequiresAuthenticationRequestMatcher(reqMatch);
        filter.setAuthenticationSuccessHandler(successHandler());
        filter.setAuthenticationFailureHandler(failureHandler());
        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }

    /**
     * We need to configure the CORS Filter to be the first in the chain so that we can
     * allow the OPTIONS requests to go through.
     * @return
     * @throws Exception
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() throws Exception {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SimpleCORSFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

/*    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setEnabled(false);
        filterRegistrationBean.setFilter(restAuthenticationFilter);
        return filterRegistrationBean;
    }*/

    public Filter preAuthFilter() {
        TokenPreAuthenticatedProcessingFilter filter = new TokenPreAuthenticatedProcessingFilter();
        filter.setAuthenticationManager(preAuthAuthenticationManager());
        return filter;
    }

    protected AuthenticationManager preAuthAuthenticationManager() {
        PreAuthenticatedAuthenticationProvider preAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthProvider.setPreAuthenticatedUserDetailsService(preAuthUserDetailsService);

        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(preAuthProvider);

        ProviderManager authMan = new ProviderManager(providers);
        return authMan;
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        RestAuthenticationProvider apiKeyProvider = new RestAuthenticationProvider();
        apiKeyProvider.setUserDetailsService(userDetailsService);
        apiKeyProvider.setIdentityService(identityService);
        apiKeyProvider.setAuthenticationProviderService(authenticationProviderService);

        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(apiKeyProvider);

        ProviderManager authMan = new ProviderManager(providers);
        return authMan;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new RestAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    public LogoutHandler logoutHandler(){
        return new RestLogoutHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new RestLogoutSuccessHandler();
    }
}