package com.drestive.chatalot.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RestAuthenticationToken extends AbstractAuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	
	private Object credentials;
	private Object principal;
	private String xAuthToken;
	
	
	public RestAuthenticationToken(Object principal) {
		super(null);
		
		this.principal = principal;
		this.credentials = "";
		setAuthenticated(false);
	}
	
	public RestAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		
		this.principal = principal;
		this.credentials = "";
		setAuthenticated(true);
	}
	
	@Override
	public String toString() {
		return "Principal: " + principal;
	}
	
	@Override
	public Object getCredentials() {
		return credentials;
	}
	
	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}
	
	@Override
	public Object getPrincipal() {
		return principal;
	}
	
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
	
	public String getxAuthToken() {
		return xAuthToken;
	}
	
	public void setxAuthToken(String xAuthToken) {
		this.xAuthToken = xAuthToken;
	}
	
}
