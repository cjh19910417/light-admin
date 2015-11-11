package org.lightadmin.core.config.security.authentication.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lightadmin.core.config.security.authentication.AuthenticationInfoExtractor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.util.Map;


public class LocalAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String DEFAULT_LOGIN_KEY = "common_login";
	
    public static final String SECURITY_FORM_LOGINMODE_KEY = "login_mode";
    public static final String SECURITY_LAST_USERNAME_KEY = "SECURITY_LAST_USERNAME";

    public static final String sslServerAddressParameter = "ssl_server_address";
    
    private String sslServerAddress;
    
    private boolean postOnly = true;
    private Map<String,AuthenticationInfoExtractor> authenticationInfoExtractors;


    /**
     * @param defaultFilterProcessesUrl the default value for <tt>filterProcessesUrl</tt>.
     */
    public LocalAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    	request.setAttribute(sslServerAddressParameter, sslServerAddress);
    	
    	if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        
        String loginMode = obtainLoginMode(request);
        if(loginMode==null || "".equals(loginMode)){
        	loginMode = DEFAULT_LOGIN_KEY;
        }
        AuthenticationInfoExtractor authenticationInfoExtractor = authenticationInfoExtractors.get(loginMode);
        Authentication authRequest = authenticationInfoExtractor.extractAuthenticationInfo(request);
        Authentication authentication = super.getAuthenticationManager().authenticate(authRequest);
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
    
    protected String obtainLoginMode(HttpServletRequest request) {
        return request.getParameter(SECURITY_FORM_LOGINMODE_KEY);
    }
    
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public Map<String, AuthenticationInfoExtractor> getAuthenticationInfoExtractors() {
		return authenticationInfoExtractors;
	}

	public void setAuthenticationInfoExtractors(
			Map<String, AuthenticationInfoExtractor> authenticationInfoExtractors) {
		this.authenticationInfoExtractors = authenticationInfoExtractors;
	}

	public String getSslServerAddress() {
		return sslServerAddress;
	}


	public void setSslServerAddress(String sslServerAddress) {
		this.sslServerAddress = sslServerAddress;
	}
}
