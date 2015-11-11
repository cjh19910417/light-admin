package org.lightadmin.core.config.security.authentication.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class LocalLoginUrlAuthenticationEntryPoint extends
		LoginUrlAuthenticationEntryPoint {
	public static final String AUTHENTICATION_TOKEN_KEY = "authToken"; 
	
	public static final String SECURITY_FORM_USERNAME_KEY = "j_username";
    public static final String SECURITY_FORM_PASSWORD_KEY = "j_password";
    public static final String sslServerAddressParameter = "ssl_server_address";
    
	private String usernameParameter = SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SECURITY_FORM_PASSWORD_KEY;
    
    private String sslServerAddress;
	
	protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) {
		if(request.getAttribute(usernameParameter)==null){
		    request.setAttribute(usernameParameter, "");
		}
		if(request.getAttribute(passwordParameter)==null){
		    request.setAttribute(passwordParameter, "");
		}
		request.setAttribute(sslServerAddressParameter, sslServerAddress);
        return super.getLoginFormUrl();
    }
	
	
	public String getUsernameParameter() {
		return usernameParameter;
	}

	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}

	public String getPasswordParameter() {
		return passwordParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}

	public String getSslServerAddress() {
		return sslServerAddress;
	}


	public void setSslServerAddress(String sslServerAddress) {
		this.sslServerAddress = sslServerAddress;
	}

}
