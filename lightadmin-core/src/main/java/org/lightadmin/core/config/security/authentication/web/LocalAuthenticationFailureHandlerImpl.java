package org.lightadmin.core.config.security.authentication.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LocalAuthenticationFailureHandlerImpl implements
		AuthenticationFailureHandler {
	
    public static final String SECURITY_ERROR_KEY = "securityError";
    
	private String securityErrorParameter = SECURITY_ERROR_KEY;
    
	private String loginFormUrl;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		request.setAttribute(securityErrorParameter, exception.getMessage());
		RequestDispatcher dispatcher = request.getRequestDispatcher(loginFormUrl);
        dispatcher.forward(request, response);
	}

	public String getLoginFormUrl() {
		return loginFormUrl;
	}

	public void setLoginFormUrl(String loginFormUrl) {
		this.loginFormUrl = loginFormUrl;
	}

	public String getSecurityErrorParameter() {
		return securityErrorParameter;
	}

	public void setSecurityErrorParameter(String securityErrorParameter) {
		this.securityErrorParameter = securityErrorParameter;
	}

}
