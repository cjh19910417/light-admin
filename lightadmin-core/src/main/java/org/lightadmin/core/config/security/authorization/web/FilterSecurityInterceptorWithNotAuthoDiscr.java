package org.lightadmin.core.config.security.authorization.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.lightadmin.core.config.security.authorization.NotAuthorizationDiscriminater;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;


public class FilterSecurityInterceptorWithNotAuthoDiscr extends FilterSecurityInterceptor {
	
	NotAuthorizationDiscriminater notAuthorizationDiscriminater;
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
          if(notAuthorizationDiscriminater != null && notAuthorizationDiscriminater.isNotAuthorizationLink(
        		  (HttpServletRequest)request)){
        	  chain.doFilter(request, response);
          }else{
        	  super.doFilter(request, response,chain);
          }
    }
	
	public NotAuthorizationDiscriminater getNotAuthorizationDiscriminater() {
		return notAuthorizationDiscriminater;
	}
	public void setNotAuthorizationDiscriminater(
			NotAuthorizationDiscriminater notAuthorizationDiscriminater) {
		this.notAuthorizationDiscriminater = notAuthorizationDiscriminater;
	}
}
